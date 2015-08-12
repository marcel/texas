package com.andbutso.poker.handhistory.pokerstars

case class Seat(
  number: Int,
  player: PokerStarsPlayer,
  role: Option[Seat.Role.Value] = None
)

object Seat {
  object Role extends Enumeration {
    val Button     = Value
    val BigBlind   = Value
    val SmallBlind = Value
    // TODO CutOff, UTG, HiJack, LoJack
  }
}