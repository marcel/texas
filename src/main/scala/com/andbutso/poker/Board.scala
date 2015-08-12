package com.andbutso.poker

import com.andbutso.poker.cards._

case class Board(
  flop: Cards = Cards(),
  turn: Option[Card] = None,
  river: Option[Card] = None
) {
  def cards = {
    flop ++ turn ++ river
  }
}
