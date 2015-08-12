package com.andbutso.poker.handhistory.pokerstars

import com.andbutso.poker.cards.{Card, Rank, Suit}

object CardShortHandTranslator {
  val ShortHandToSuit = Map(
    "c" -> Suit.Clubs,
    "d" -> Suit.Diamonds,
    "h" -> Suit.Hearts,
    "s" -> Suit.Spades
  )

  val ShortHandPattern = {
    val ranks = Rank.values.mkString("|")
    val suits = ShortHandToSuit.keys.mkString("|")

    """(?i)\b((?:%s)(?:%s))\b""".format(ranks, suits).r
  }

  def translateAll(shorthand: String) = {
    ShortHandPattern.findAllIn(shorthand) flatMap { translate(_) } toSet
  }

  def translate(shorthand: String) = {
    if (shorthand.size == 2) {
      val Array(rankString, suitString) = shorthand.split("")

      try {
        val rank = Rank.withName(rankString)
        val suit = ShortHandToSuit.getOrElse(suitString, Suit.withName(suitString))

        Some(Card(rank, suit))
      } catch {
        case e: java.util.NoSuchElementException => None
      }
    } else {
      None
    }
  }
}