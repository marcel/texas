package com.andbutso.poker.cards

object HandRank extends Enumeration {
  type HandRank = Value

  val StraightDraw  = Value(-2, "Straight Draw")
  val FlushDraw     = Value(-1, "Flush Draw")
  val HighCard      = Value(1, "High Card")
  val OnePair       = Value(2, "One Pair")
  val TwoPair       = Value(3, "Two Pair")
  val ThreeOfAKind  = Value(4, "Three of a Kind")
  val Straight      = Value(5, "Straight")
  val Flush         = Value(6, "Flush")
  val FullHouse     = Value(7, "Full House")
  val FourOfAKind   = Value(8, "Four of a Kind")
  val StraightFlush = Value(9, "Straight Flush")
}

//class HandRankOdds(handRank: HandRank) {
//  import HandRank._
//
//  def outsToGet(betterRank: HandRank) = {
//    (handRank, betterRank) match {
//      case (OnePair, ThreeOfAKind) => 2
//      case (TwoPair, FullHouse)    => 4
//      case ()
//    }
//  }
//}


class Percent(self: Double) {
  def odds = 100 / self
}