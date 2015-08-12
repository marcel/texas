package com.andbutso.poker.cards

import java.security.SecureRandom
import java.util.Collections

import scala.collection.JavaConversions._
import scala.collection.mutable

// TODO Look into custom impl of SortedSet to avoid having to maintain Map and ArrayStack
case class Deck(cards: Set[Card], numberOfShuffles: Int = 7) {
  val random     = SecureRandom.getInstance("SHA1PRNG")
  val randomized = 0.until(numberOfShuffles).foldLeft(mutable.ArrayStack(1.until(cards.size): _*)) { case (shuffledCards, i) =>
    Collections.shuffle(shuffledCards, random)
    shuffledCards
  }

  val cardMap = cards.zipWithIndex map { case (card, index) =>
    (index + 1) -> card
  } toMap

  def -(cardsToRemove: Cards) = {
    copy(cards = cards.diff(cardsToRemove))
  }

  def deal(numberOfCards: Int = 1): Cards = {
    require(randomized.size >= numberOfCards) // TODO Don't allow this to happen

    val indexes = 0.until(numberOfCards) map { _ => randomized.pop } // N.B. Mutation side effects here
    val delt = indexes map { index => cardMap(index) }
    delt.toSet
  }

  override def toString = {
    runtime.ScalaRunTime._toString(Deck(randomized map { cardMap(_) } toSet))
  }
}

object Deck {
  def apply(): Deck = {
    Deck.Full
  }

  def Full = Deck(Card.All)
}