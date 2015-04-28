package com.andbutso.poker.cards

object HandRank extends Enumeration {
  type HandRank = Value

  val HighCard      = Value(0, "High Card")
  val OnePair       = Value(1, "One Pair")
  val TwoPair       = Value(2, "Two Pair")
  val ThreeOfAKind  = Value(3, "Three of a Kind")
  val Straight      = Value(4, "Straight")
  val Flush         = Value(5, "Flush")
  val FullHouse     = Value(6, "Full House")
  val FourOfAKind   = Value(7, "Four of a Kind")
  val StraightFlush = Value(8, "Straight Flush")
}