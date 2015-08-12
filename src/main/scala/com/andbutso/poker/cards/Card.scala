package com.andbutso.poker.cards

import com.andbutso.poker.cards.Suit.Suit
import com.andbutso.poker.cards.Rank.Rank

case class Card(rank: Rank, suit: Suit) {
  override def toString = {
    "%s%s".format(rankName, suit)
  }

  def rankName = {
    if (rank >= Rank.Ten) rank.toString else rank.id.toString
  }
}

object Suit extends Enumeration {
  type Suit    = Value
  val Clubs    = Value("♧")
  val Diamonds = Value("♢")
  val Hearts   = Value("♡")
  val Spades   = Value("︎♤")

  implicit def suitToRichSuit(suit: Suit.Value) = new RichSuit(suit)
}

class RichSuit(suit: Suit) {
  def all = {
    Rank.values map { rank =>
      Card(rank, suit)
    }
  }
}

object Rank extends Enumeration {
  type Rank = Value

  val Two   = Value(2, "2")
  val Three = Value(3, "3")
  val Four  = Value(4, "4")
  val Five  = Value(5, "5")
  val Six   = Value(6, "6")
  val Seven = Value(7, "7")
  val Eight = Value(8, "8")
  val Nine  = Value(9, "9")
  val Ten   = Value(10, "T")
  def T = Ten
  val Jack  = Value(11, "J")
  def J = Jack
  val Queen = Value(12, "Q")
  def Q = Queen
  val King  = Value(13, "K")
  def K = King
  val Ace   = Value(14, "A")
  def A = Ace

  implicit def rankToRichRank(rank: Rank.Value) = new RichRank(rank)
  implicit def intToRichRank(int: Int) = new RichRank(Rank(int))
}

class RichRank(rank: Rank) {
  import Card._

  def all = {
    Suit.values map { suit =>
      Card(rank, suit)
    }
  }

  def ♧ = Club(rank)
  def ♢ = Diamond(rank)
  def ♡ = Heart(rank)
  def ♤ = Spade(rank)

  def +(number: Int) = {
    if (rank.id + number < Rank.maxId) {
      Some(Rank(rank.id + number))
    } else {
      None
    }
  }
}

object Card {
  import Suit._
  import Rank._

  def Diamond(rank: Rank): Card  = Card(rank, Diamonds)
  def Diamond(number: Int): Card = Diamond(Rank(number))

  def Spade(rank: Rank): Card    = Card(rank, Spades)
  def Spade(number: Int): Card   = Spade(Rank(number))

  def Club(rank: Rank): Card     = Card(rank, Clubs)
  def Club(number: Int): Card    = Club(Rank(number))

  def Heart(rank: Rank): Card    = Card(rank, Hearts)
  def Heart(number: Int): Card   = Heart(Rank(number))

  val All = {
    Suit.values.foldLeft(Set.empty[Card]) { case (deck, suit) =>
      val cards = Rank.values map { Card(_, suit) }
      deck ++ cards
    }
  }

  val AllPossibleHoleCards = {
    All flatMap { card =>
      All map { otherCard =>
        Set(card, otherCard)
      }
    } filter { _.size == 2 }
  }

  // TODO Generate this programmatically
  val AllHoleCardScenarios = Set[Set[Card]](
    Set(6♡, 9♡),
    Set(K♢, A♡),
    Set(3♡, 9♡),
    Set(4♢, 4♡),
    Set(Q♢, J♡),
    Set(J♢, J♡),
    Set(7♢, 7♡),
    Set(7♢, 2♡),
    Set(A♡, T♡),
    Set(7♢, J♡),
    Set(Q♡, 7♡),
    Set(K♢, T♡),
    Set(5♡, 3♡),
    Set(5♢, 8♡),
    Set(5♡, J♡),
    Set(5♢, 7♡),
    Set(K♢, 9♡),
    Set(8♢, 7♡),
    Set(2♢, 9♡),
    Set(A♢, 5♡),
    Set(3♢, K♡),
    Set(9♢, 4♡),
    Set(Q♢, 7♡),
    Set(J♢, 3♡),
    Set(K♡, 8♢),
    Set(6♢, 9♡),
    Set(6♡, 8♢),
    Set(5♡, 7♡),
    Set(3♡, 2♡),
    Set(6♢, T♡),
    Set(4♢, 7♡),
    Set(T♢, J♡),
    Set(9♢, A♡),
    Set(4♢, A♡),
    Set(7♢, T♡),
    Set(7♢, 9♡),
    Set(3♢, 7♡),
    Set(K♡, T♡),
    Set(T♢, 8♡),
    Set(5♡, 8♡),
    Set(K♡, 4♡),
    Set(3♢, Q♡),
    Set(A♢, 2♡),
    Set(T♡, 8♡),
    Set(8♡, 9♡),
    Set(K♡, 2♡),
    Set(T♡, 9♡),
    Set(Q♢, 2♡),
    Set(7♢, A♡),
    Set(4♢, J♡),
    Set(Q♡, 3♡),
    Set(Q♢, 9♡),
    Set(Q♡, 8♡),
    Set(9♢, 8♡),
    Set(K♡, 7♡),
    Set(K♢, 2♡),
    Set(5♢, Q♡),
    Set(2♡, 2♢),
    Set(5♢, 2♡),
    Set(T♡, 2♡),
    Set(T♡, 2♢),
    Set(3♢, A♡),
    Set(5♡, T♢),
    Set(A♡, 9♡),
    Set(8♡, 2♡),
    Set(J♢, 9♡),
    Set(5♡, 4♢),
    Set(6♢, K♡),
    Set(2♡, 7♡),
    Set(J♡, 2♡),
    Set(4♢, T♡),
    Set(6♡, 4♡),
    Set(Q♢, 6♡),
    Set(6♡, 7♡),
    Set(Q♡, 2♡),
    Set(8♡, 3♡),
    Set(Q♡, 9♡),
    Set(5♡, 6♡),
    Set(5♡, 2♡),
    Set(6♢, 6♡),
    Set(3♡, 7♡),
    Set(4♢, K♡),
    Set(T♡, 7♡),
    Set(6♢, J♡),
    Set(Q♢, A♡),
    Set(5♡, 6♢),
    Set(T♢, 3♡),
    Set(5♡, 4♡),
    Set(K♡, 6♡),
    Set(5♢, 9♡),
    Set(2♡, 4♡),
    Set(K♡, Q♡),
    Set(A♢, T♡),
    Set(6♡, 3♡),
    Set(8♢, J♡),
    Set(5♢, 3♡),
    Set(K♢, 7♡),
    Set(3♢, 8♡),
    Set(T♡, 3♡),
    Set(A♢, 8♡),
    Set(6♡, Q♡),
    Set(Q♡, 4♡),
    Set(3♢, 9♡),
    Set(3♡, 4♡),
    Set(Q♢, 8♡),
    Set(3♢, 2♡),
    Set(6♡, A♡),
    Set(5♡, K♡),
    Set(8♢, 2♡),
    Set(Q♡, A♡),
    Set(T♢, T♡),
    Set(6♢, 7♡),
    Set(4♢, 2♡),
    Set(6♢, 2♡),
    Set(A♡, 8♡),
    Set(5♢, J♡),
    Set(8♡, J♡),
    Set(4♡, 9♡),
    Set(6♡, J♡),
    Set(6♡, 8♡),
    Set(K♢, Q♡),
    Set(4♢, 3♡),
    Set(5♡, 5♢),
    Set(Q♢, 4♡),
    Set(4♡, 7♡),
    Set(7♡, 9♡),
    Set(T♡, 4♡),
    Set(5♡, K♢),
    Set(8♡, 4♡),
    Set(A♡, 2♡),
    Set(3♢, 3♡),
    Set(A♡, J♡),
    Set(K♡, 3♡),
    Set(K♡, A♡),
    Set(Q♡, T♢),
    Set(K♡, 8♡),
    Set(K♢, K♡),
    Set(A♡, 7♡),
    Set(5♡, 9♡),
    Set(J♡, 2♢),
    Set(3♢, 6♡),
    Set(8♢, 8♡),
    Set(5♡, A♡),
    Set(6♢, 4♡),
    Set(A♢, 6♡),
    Set(8♡, 7♡),
    Set(Q♡, J♡),
    Set(2♡, 9♡),
    Set(9♢, 9♡),
    Set(T♡, J♡),
    Set(J♡, 4♡),
    Set(J♢, A♡),
    Set(9♢, T♡),
    Set(Q♢, Q♡),
    Set(A♡, 4♡),
    Set(6♡, 2♡),
    Set(J♡, 9♡),
    Set(5♡, T♡),
    Set(6♡, T♡),
    Set(5♡, Q♡),
    Set(J♢, K♡),
    Set(Q♡, T♡),
    Set(J♡, 7♡),
    Set(K♡, J♡),
    Set(A♢, A♡),
    Set(8♢, 4♡),
    Set(A♡, 3♡),
    Set(J♡, 3♡),
    Set(K♡, 9♡)
  )
}

