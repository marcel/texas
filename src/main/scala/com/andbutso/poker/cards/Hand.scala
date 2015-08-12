package com.andbutso.poker.cards

object Hand {
  val Empty = Hand(Set.empty[Card])

  def apply(cards: Card*): Hand = {
    Hand(Cards(cards: _*))
  }
}

case class Hand(cards: Cards) {
  def equals(otherHand: Hand) = {
    cards.equals(otherHand.cards)
  }

  override def hashCode() = cards.hashCode()

  def +(cardsToAdd: Cards): Hand = {
    copy(cards = cards ++ cardsToAdd)
  }

  def +(cardToAdd: Card): Hand = {
    this + Cards(cardToAdd)
  }

  def value = {
    HandRankEvaluator(this)
  }

  def bySuit = {
    cards.groupBy { _.suit }
  }

  def byRank = {
    cards.groupBy { _.rank }
  }

  def possibleNextHands = {
    val deck = Deck() - cards
    deck.cards map { card =>
      this + card
    }
  }

  def outs = {
    possibleNextHands filter { _.value.value > value.value }
  }

  def outsToImproveToBetterHandRank = {
    possibleNextHands filter { _.value.magnitude > value.magnitude }
  }

  def oddsOfAnOut = {
    outs.size.toFloat / (52 - cards.size)
  }

  def oddsToImproveToABetterHandRank = {
    outsToImproveToBetterHandRank.size.toFloat / (52 - cards.size)
  }

  def nuts(numberOfAdditionalCards: Int = 1) = {
    val handsToConsider = if (numberOfAdditionalCards > 1) {
      1.until(numberOfAdditionalCards).foldLeft(possibleNextHands) { case (possibilities, _) =>
        possibilities flatMap { _.possibleNextHands }
      }
    } else {
      possibleNextHands
    }

    handsToConsider map { hand =>
      hand.value
    } groupBy {
      _.magnitude
    } maxBy { case (score, hands) =>
      score
    } match { case (score, hands) =>
      hands
    }
  }

  def absoluteNuts(numberOfAdditionalCards: Int = 1) = {
    nuts(numberOfAdditionalCards) groupBy {
      _.value
    } maxBy { case (score, hands) =>
      score
    } match { case (score, hands) =>
      hands
    }
  }

  override def toString = {
    "Hand(%s)".format(cards map { _.toString } mkString(" "))
  }
}

//// TODO reconcile with PokerStars version
//case class Player(name: Int, hand: Hand = Hand.Empty) {
//  def +(card: Card) = {
//    copy(hand = hand + card)
//  }
//}

//// TODO reconcile with PokerStars version
//case class Table(players: Seq[Player]) {
//  private[this] val seatingAssignments = players.zipWithIndex map { case (player, index) => player.name -> index } toMap
//
//  def update(player: Player)(f: Player => Player) = {
//    copy(players = players.patch(seatingAssignments(player.name), Seq(f(player)), 1))
//  }
//}

//case class Dealer(deck: Deck, table: Table) {
//  def deal = {
//    0.to(1).foldLeft(this) { case (updatedDealer, _) =>
//      updatedDealer.table.players.foldLeft((updatedDealer.deck, updatedDealer.table)) { case ((updatedDeck, updatedTable), player) =>
//        val card = updatedDeck.deal()
//
//        (updatedDeck - card, updatedTable.update(player) { playerToDealTo =>
//          playerToDealTo + card.head
//        })
//      } match {
//        case (d: Deck, t: Table) => copy(d, t)
//      }
//    }
//  }
//
//  def burnCard = ???
//
//}

