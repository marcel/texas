package com.andbutso.poker

package object cards {
  type Cards = Set[Card]
  def Cards(cards: Card*) = Set(cards: _*)
  import com.andbutso.poker.cards.Rank._

  implicit def cardsToHand(cards: Cards) = new Hand(cards)
}