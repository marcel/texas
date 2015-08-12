package com.andbutso.poker.cards

import com.andbutso.poker.{cards, Board}
import com.andbutso.poker.cards.HandRank.HandRank

case class HandValue(handRank: HandRank, value: Double, cards: Cards, hand: Hand) {
  def magnitude = {
    Math.round(Math.log10(value))
  }
}

object HandRankEvaluator {
  val All = Seq(
    HighCardEvaluator,
    OnePairEvaluator,
    TwoPairEvaluator,
    ThreeOfAKindEvaluator,
    StraightEvaluator,
    FlushEvaluator,
    FullHouseEvaluator,
    FourOfAKindEvaluator,
    StraightFlushEvaluator
  )

  def apply(hand: Hand) = {
    All.foldLeft(Seq.empty[HandValue]) { case (handValues, evaluator) =>
      handValues ++ evaluator(hand)
    } sortBy { - _.value } head // N.B. Assumed invariant: A hand will always produce at least 1 HandValue
  }
}

abstract class HandRankEvaluator(val rank: com.andbutso.poker.cards.HandRank.HandRank) {
  def apply(hand: Hand): Option[HandValue]
  def strength = Math.pow(10, rank.id)

  protected def handValue = HandValue(rank, _: Double, _: Cards, _: Hand)
}

object StraightDrawEvaluator extends HandRankEvaluator(HandRank.StraightDraw) {
  override def apply(hand: Hand) = {
    val possibleStraights = hand.outs filter { _.value.handRank == HandRank.Straight }

    if (possibleStraights.nonEmpty) {
      val highestStraight = possibleStraights.maxBy { _.value.value }
      val straightCards   = hand.cards & highestStraight.value.cards
      val highCard        = HighCardEvaluator(Hand(straightCards)).get

      Some(handValue(strength + highCard.value, straightCards, hand))
    } else {
      None
    }
  }
}

object FlushDrawEvaluator extends HandRankEvaluator(HandRank.FlushDraw) {
  override def apply(hand: Hand) = {
    FlushEvaluator(hand, cardsToAFlush = 4) collect {
      case flush if FlushEvaluator(hand).isEmpty =>
        val highCard = HighCardEvaluator(Hand(flush.cards)).get // N.B. HighCard will always return a HandValue
        handValue(strength + highCard.value, flush.cards, hand)
    }
  }
}

object HighCardEvaluator extends HandRankEvaluator(HandRank.HighCard) {
  override def apply(hand: Hand) = {
    val maxCard = hand.cards.maxBy {
      _.rank.id
    }

    Some(handValue(strength + maxCard.rank.id, Set(maxCard), hand))
  }
}

object OnePairEvaluator extends HandRankEvaluator(HandRank.OnePair) {
  override def apply(hand: Hand) = {
    hand.byRank.find { case (_, cards) =>
      cards.size == 2
    } map { case (_, cards) =>
      handValue(strength + cards.head.rank.id, cards, hand)
    }
  }
}

object TwoPairEvaluator extends HandRankEvaluator(HandRank.TwoPair) {
  override def apply(hand: Hand) = {
    val pairs = hand.byRank collect { case (_, cards) if cards.size == 2 =>
      cards
    }

    if (pairs.size >= 2) {
      val pairsSorted = pairs.toSeq.sortBy { _.head.rank }
      val value       = strength + pairsSorted.last.head.rank.id
      val topTwoPair  = pairsSorted.takeRight(2).toSet.flatten

      Some(handValue(value, topTwoPair, hand))
    } else {
      None
    }
  }
}

object ThreeOfAKindEvaluator extends HandRankEvaluator(HandRank.ThreeOfAKind) {
  override def apply(hand: Hand) = {
    val trips = hand.byRank.filter { case (_, cards) =>
      cards.size == 3
    }

    if (trips.nonEmpty) {
      trips maxBy {
        case (rank, _) => rank
      } match {
        case (_, cards) =>
          Some(handValue(strength + cards.head.rank.id, cards, hand))
        case _ =>
          None
      }
    } else {
      None
    }
  }
}

object StraightEvaluator extends HandRankEvaluator(HandRank.Straight) {
  val WheelRanks = Set(Rank.Ace, Rank.Two, Rank.Three, Rank.Four, Rank.Five)

  override def apply(hand: Hand): Option[HandValue] = {
    apply(hand, cardsToAStraight = 5)
  }

  def apply(hand: Hand, cardsToAStraight: Int) = {
    val reverseSortedJustRank = hand.cards.toSeq.map { _.rank }.sortBy { -_.id }.distinct

    if (reverseSortedJustRank.size < cardsToAStraight) {
      None
    } else {
      if (WheelRanks.subsetOf(reverseSortedJustRank.toSet)) {
        // N.B. Special case where Ace is low card
        val wheel = hand.cards.filter { card => WheelRanks.contains(card.rank) }
        Some(handValue(strength + Rank.Five.id, wheel.toSet, hand))
      } else {
        reverseSortedJustRank.sliding(cardsToAStraight).find { cards =>
          cards.sliding(2).forall { case Seq(lhs, rhs) =>
            lhs.id - 1 == rhs.id
          }
        } map { straight =>
          val reconstituted = hand.cards.filter { card => straight.contains(card.rank) }
          val highCard = reconstituted.maxBy { _.rank }
          handValue(strength + highCard.rank.id, reconstituted, hand)
        }
      }
    }
  }
}

object FlushEvaluator extends HandRankEvaluator(HandRank.Flush) {
  override def apply(hand: Hand): Option[HandValue] = {
    apply(hand, cardsToAFlush = 5)
  }

  def apply(hand: Hand, cardsToAFlush: Int) = {
    if (hand.cards.size < cardsToAFlush) {
      None
    } else {
      hand.bySuit.find { case (_, cards) =>
        cards.size >= cardsToAFlush // N.B. >= not == b/c hole cards + board could have 6 or 7 card flush
      } map { case (_, cards) =>
        val sorted = cards.toSeq.sortBy { _.rank }
        handValue(strength + sorted.last.rank.id, sorted.takeRight(cardsToAFlush).toSet, hand)
      }
    }
  }
}

object FullHouseEvaluator extends HandRankEvaluator(HandRank.FullHouse) {
  override def apply(hand: Hand) = {
    ThreeOfAKindEvaluator(hand) flatMap { threeOfAKind =>
      val remainingCards = hand.cards.diff(threeOfAKind.cards)
      val otherPairs     = Hand(remainingCards).byRank.values.filter {
        _.size >= 2
      } // N.B. Can't use OnePairEvaluator because pair of the full house could be made from small trips

      if (otherPairs.nonEmpty) {
        val highestOtherPair = otherPairs maxBy { _.head.rank }

        val value = strength + threeOfAKind.cards.head.rank.id
        val cards = highestOtherPair ++ threeOfAKind.cards
        Some(handValue(value, cards, hand))
      } else {
        None
      }
    }
  }
}

object FourOfAKindEvaluator extends HandRankEvaluator(HandRank.FourOfAKind) {
  override def apply(hand: Hand) = {
    hand.byRank.find { case (_, cards) =>
      cards.size == 4
    } map { case (_, cards) =>
      handValue(strength + cards.head.rank.id, cards, hand)
    }
  }
}

object StraightFlushEvaluator extends HandRankEvaluator(HandRank.StraightFlush) {
  override def apply(hand: Hand) = {
    StraightEvaluator(hand) collect {
      case straightFlush if FlushEvaluator(Hand(straightFlush.cards)).isDefined =>
        val highCard = straightFlush.cards.maxBy { _.rank }
        handValue(strength + highCard.rank.id, FlushEvaluator(Hand(straightFlush.cards)).get.cards, hand)
    }
  }
}

object HoleCardEquity {
  import scala.io.{AnsiColor => color}

  type Results = Map[HandRank.Value, Int]
  case class Summary(cards: Cards, numberOfOpponents: Int, stats: Stats) {
    val numberFormatter = java.text.NumberFormat.getIntegerInstance
    val handsWon = stats.winsByRank.values.sum
    val handsLost = stats.numberOfTrials - handsWon
    val winningPercentage = (handsWon.toFloat / stats.numberOfTrials * 100).toInt

    val winningRankBreakDown = stats.winsByRank.toSeq sortBy {
      case (_, count) => -count
    } map { case (rank, count) =>
      val losesByRank = stats.losesByRank.getOrElse(rank, 0)
      val totalHandsPlayedWithRank = count + losesByRank
      val winPercentage =  (count.toFloat / handsWon * 100).toInt
      val losesToSameRank = stats.losesAgainstSameRank.getOrElse(rank, 1)
      Seq(
        rank.toString,
        totalHandsPlayedWithRank.toString,
        (totalHandsPlayedWithRank.toFloat / stats.numberOfTrials * 100).toInt.toString + "%",
        winPercentage.toString + "%",
//        count.toString,
//        losesByRank.toString,
        (count.toFloat / totalHandsPlayedWithRank * 100).toInt.toString + "%"
//        (losesToSameRank.toFloat / losesByRank * 100).toInt.toString + "%" TODO Fix
      )
    }

    def toText = {
      val tr = new TabularReport()

      tr.addRow("Rank", "Count", "Of total", "Of wins", "W/L %")

      tr.addSeparator
      winningRankBreakDown foreach { row =>
        tr.addRow(row: _*)
      }
      tr.addSeparator

      raw"""${cards.head} ${cards.last}  won $winningPercentage% of ${numberFormatter.format(stats.numberOfTrials)} hands against $numberOfOpponents opponent(s)
           !${tr.toTable}""".stripMargin('!')
    }
  }

  case class Stats(
    winsByRank: Results = Map.empty[HandRank.Value, Int],
    losesByRank: Results = Map.empty[HandRank.Value, Int],
    winsAgainstSameRank: Results = Map.empty[HandRank.Value, Int],
    losesAgainstSameRank: Results = Map.empty[HandRank.Value, Int]
  ) {
    lazy val numberOfTrials = winsByRank.values.sum + losesByRank.values.sum

    def wonWith(handRank: HandRank.Value, against: HandRank.Value) = {
      if (handRank == against) {
        copy(winsByRank = incr(winsByRank, handRank), winsAgainstSameRank = incr(winsAgainstSameRank, handRank))
      } else {
        copy(winsByRank = incr(winsByRank, handRank))
      }
    }

    def lostWith(handRank: HandRank.Value, against: HandRank.Value) = {
      if (handRank == against) {
        copy(losesByRank = incr(losesByRank, against), losesAgainstSameRank = incr(losesAgainstSameRank, handRank))
      } else {
        copy(losesByRank = incr(losesByRank, against))
      }
    }

    private[this] def incr(map: Results, key: HandRank.Value) = {
      val currentValue = map.getOrElse(key, 0)
      map + (key -> (currentValue + 1))
    }
  }

  trait Outcome {
    def bestHand: HandValue
    def secondBestHand: HandValue
    def ourHand: HandValue
  }

  case class Win(
    bestHand: HandValue,
    secondBestHand: HandValue,
    ourHand: HandValue
  ) extends Outcome

  case class Loss(
    bestHand: HandValue,
    secondBestHand: HandValue,
    ourHand: HandValue
  ) extends Outcome

  def apply(holeCards: Set[Card], numberOfOpponents: Int, trials: Int) = {
    val holeCardEquity = new HoleCardEquity(holeCards, numberOfOpponents)
    holeCardEquity.simulate(trials)
  }
}

class HoleCardEquity(holeCards: Set[Card], numberOfOpponents: Int) {
  import HoleCardEquity._

  def simulate(trials: Int) = {
    val finalStats = 0.until(trials).foldLeft(Stats()) { case (stats, _) =>
      playHand() match {
        case Win(bestHand, secondBest, ourHand) =>
          stats.wonWith(bestHand.handRank, against = secondBest.handRank)
        case Loss(bestHand, secondBest, ourHand) =>
          stats.lostWith(ourHand.handRank, against = bestHand.handRank)
      }
    }

    HoleCardEquity.Summary(holeCards, numberOfOpponents, finalStats)
  }

  def playHand() = {
    val deck = Deck() - holeCards
    val opponentHoleCards = 0.to(numberOfOpponents) map { _ => deck.deal(2) }

    val board = Board(
      flop  = deck.deal(3),
      turn  = deck.deal(1).headOption,
      river = deck.deal(1).headOption
    )

    val opponentHandValues = opponentHoleCards map { opponentCards =>
      HandRankEvaluator(Hand(board.cards ++ opponentCards))
    }

    val maxOpponentHand = opponentHandValues.maxBy { _.value }
    val handValue = HandRankEvaluator(Hand(board.cards ++ holeCards))

    if (handValue.value > maxOpponentHand.value) {
      Win(handValue, maxOpponentHand, handValue)
    } else {
      val secondBestHand = (opponentHandValues.diff(Seq(maxOpponentHand) :+ handValue)).maxBy { _.value }
      Loss(maxOpponentHand, secondBestHand, handValue)
    }
  }
}

// TODO Potentially offer a way to center some text across an entire row
class TabularReport {
  var columnWidths = scala.collection.mutable.IndexedSeq[Int]()
  val rows = scala.collection.mutable.Map[Int, Seq[String]]()
  val separators = scala.collection.mutable.Set[Int]()
  val numberFormatter = java.text.NumberFormat.getIntegerInstance
  val Numeric = """^(\d+)$""".r

  def updateColumnWidths(fields: Seq[String]) = {
    fields.zipWithIndex foreach { case (field, index) =>
      if (columnWidths.isDefinedAt(index) && columnWidths(index) < field.size) {
        columnWidths(index) = field.size
      } else if (!columnWidths.isDefinedAt(index)) {
        columnWidths = columnWidths.padTo(index + 1, -1)
        columnWidths.update(index, field.size)
      }
    }
  }

  def addRow(fields: String*) = {
    val commifiedFields = commify(fields: _*)
    updateColumnWidths(commifiedFields)
    rows(nextRowNumber) = commifiedFields
  }

  def commify(fields: String*) = {
    fields map { field =>
      field match {
        case Numeric(number) =>
          numberFormatter.format(number.toInt)
        case _ =>
          field
      }
    }
  }

  def toTable = {
    rows.toSeq.sortBy { case (rowNumber, fields) =>
      rowNumber
    } map { case (rowNumber, fields) =>
      val sb = new StringBuilder
      val fullRow = fields.padTo(maxColumns, "")
      val paddedFields = fullRow.zipWithIndex map { case (field, index) =>
        rightPad(field, columnWidths(index))
      }
      if (rowNumber == 1) {
        sb.append("+-" + (columnWidths map { width => "-" * width} mkString("-+-")) + "-+")
        sb.append("\n")
      }
      sb.append("| " + (paddedFields mkString(" | ")) + " |")
      if (separators.contains(rowNumber + 1)) {
        sb.append("\n")
        sb.append("+-" + (columnWidths map { width => "-" * width} mkString("-+-")) + "-+")
      }
      sb.toString
    } mkString("\n")
  }

  def addSeparator = { // TODO optional argument for the Char to file the separator with
    separators.add(nextRowNumber)
  }

  private[this] def rightPad(string: String, length: Int) = {
    string.reverse.padTo(length, " ").mkString.reverse
  }

  private[this] def nextRowNumber = {
    if (rows.isEmpty) {
      1
    } else {
      rows.keys.max + 1
    }
  }

  private[this] def maxColumns = {
    columnWidths.size
  }
}