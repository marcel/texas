package com.andbutso.poker.cards

import com.andbutso.poker.cards.HandRank.HandRank

case class HandValue(handRank: HandRank, value: Double, cards: Set[Card], hand: Hand) {
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

abstract class HandRankEvaluator(val rank: HandRank) {
  def apply(hand: Hand): Option[HandValue]
  def strength = Math.pow(10, rank.id)

  protected def handValue = HandValue(rank, _: Double, _: Set[Card], _: Hand)
}

object HighCardEvaluator extends HandRankEvaluator(HandRank.HighCard) {
  override def apply(hand: Hand) = {
    val sortedByValue = hand.cards.toSeq.sortBy {
      _.rank
    }

    sortedByValue.headOption map { card =>
      handValue(strength + card.rank.id, Set(card), hand)
    }
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

    if (pairs.size == 2) {
      val topPair = pairs.maxBy { _.head.rank }
      Some(handValue(strength + topPair.head.rank.id, pairs.toSet.flatten, hand))
    } else {
      None
    }
  }
}

object ThreeOfAKindEvaluator extends HandRankEvaluator(HandRank.ThreeOfAKind) {
  override def apply(hand: Hand) = {
    hand.byRank.find { case (_, cards) =>
      cards.size == 3
    } map { case (_, cards) =>
      handValue(strength + cards.head.rank.id, cards, hand)
    }
  }
}

object StraightEvaluator extends HandRankEvaluator(HandRank.Straight) { // TODO This can't deal with ...,3, 4, Diamond(5), Club(5), 6, 7...
  override def apply(hand: Hand) = {
    val sortedByRank = hand.cards.toSeq.sortBy { _.rank.id }
    sortedByRank.sliding(5).find { cards =>
      cards.sliding(2).forall { case Seq(lhs, rhs) =>
        lhs.rank.id + 1 == rhs.rank.id
      }
    } map { straight =>
      val highCard = straight.maxBy { _.rank }
      handValue(strength + highCard.rank.id, straight.toSet, hand)
    }
  }
}

object FlushEvaluator extends HandRankEvaluator(HandRank.Flush) {
  override def apply(hand: Hand) = {
    hand.bySuit.find { case (_, cards) =>
      cards.size == 5
    } map { case (_, cards) =>
      val highCard = cards.maxBy { _.rank }
      handValue(strength + highCard.rank.id, cards, hand)
    }
  }
}

object FullHouseEvaluator extends HandRankEvaluator(HandRank.FullHouse) {
  override def apply(hand: Hand) = {
    ThreeOfAKindEvaluator(hand) flatMap { threeOfAKind =>
      OnePairEvaluator(hand) collect {
        case pair if pair.cards.head.rank != threeOfAKind.cards.head.rank =>
          handValue(strength, pair.cards ++ threeOfAKind.cards, hand)
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
    StraightEvaluator(hand) flatMap { straight =>
      FlushEvaluator(hand) collect {
        case flush if flush.cards == straight.cards =>
          val highCard = straight.cards.maxBy { _.rank }
          handValue(strength + highCard.rank.id, straight.cards, hand)
      }
    }
  }
}