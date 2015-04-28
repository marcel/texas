package com.andbutso.poker.cards


case class Deck(cards: Set[Card]) {
  def -(cardsToRemove: Set[Card]) = {
    copy(cards = cards.diff(cardsToRemove))
  }
}

object Deck {
  def apply(): Deck = {
    Deck(
      Suit.values.foldLeft(Set.empty[Card]) { case (deck, suit) =>
        val cards = Rank.values map { Card(_, suit) }
        deck ++ cards
      }
    )
  }
}
