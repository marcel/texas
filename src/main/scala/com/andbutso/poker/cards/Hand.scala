package com.andbutso.poker.cards

import com.andbutso.poker.cards.HandRank.HandRank

case class Hand(cards: Set[Card]) {
  def equals(otherHand: Hand) = {
    cards.equals(otherHand.cards)
  }

  override def hashCode() = cards.hashCode()

  def +(cardsToAdd: Set[Card]): Hand = {
    copy(cards = cards ++ cardsToAdd)
  }

  def +(cardToAdd: Card): Hand = {
    this + Set(cardToAdd)
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
      this + Set(card)
    }
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
      HandRankEvaluator(hand)
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
    "Hand(%s)".format(cards map { _.toString } mkString)
  }
}
