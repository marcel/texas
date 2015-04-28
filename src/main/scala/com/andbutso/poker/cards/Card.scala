package com.andbutso.poker.cards

import com.andbutso.poker.cards.Suit.Suit
import com.andbutso.poker.cards.Rank.Rank

case class Card(rank: Rank, suit: Suit) {
  override def toString = {
    "[%s%s]".format(rankName, suit)
  }

  def rankName = {
    if (rank >= Rank.Jack) rank.toString else rank.id.toString
  }
}

object Suit extends Enumeration {
  type Suit    = Value
  val Clubs    = Value("♣️")
  val Diamonds = Value("♦️")
  val Hearts   = Value("♥️")
  val Spades   = Value("♠️")
}

object Card {
  import Suit._

  def Diamond(rank: Rank): Card  = Card(rank, Diamonds)
  def Diamond(number: Int): Card = Diamond(Rank(number))

  def Spade(rank: Rank): Card    = Card(rank, Spades)
  def Spade(number: Int): Card   = Spade(Rank(number))

  def Club(rank: Rank): Card     = Card(rank, Clubs)
  def Club(number: Int): Card    = Club(Rank(number))

  def Heart(rank: Rank): Card    = Card(rank, Hearts)
  def Heart(number: Int): Card   = Heart(Rank(number))
}

object Rank extends Enumeration {
  type Rank = Value

  val Two   = Value(2)
  val Three = Value(3)
  val Four  = Value(4)
  val Five  = Value(5)
  val Six   = Value(6)
  val Seven = Value(7)
  val Eight = Value(8)
  val Nine  = Value(9)
  val Ten   = Value(10)
  val Jack  = Value(11)
  val Queen = Value(12)
  val King  = Value(13)
  val Ace   = Value(14)
}



