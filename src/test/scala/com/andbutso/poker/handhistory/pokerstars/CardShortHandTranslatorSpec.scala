package com.andbutso.poker.handhistory.pokerstars

import com.andbutso.poker.ParentSpec
import com.andbutso.poker.cards.{Card, Rank}

class CardShortHandTranslatorSpec extends ParentSpec {
  "CardShortHandTranslator" should {
    val shortHands = Rank.values flatMap { rank =>
      CardShortHandTranslator.ShortHandToSuit.map { case (suitShortHand, suit) =>
        "%s%s".format(rank, suitShortHand)  -> Card(rank, suit)
      }
    } toMap

    "translate a single card shorthand" in {
      shortHands foreach { case (shortHand, correspondingCard) =>
        CardShortHandTranslator.translate(shortHand) shouldEqual Some(correspondingCard)
      }

      ok
    }

    "translate all card shorthands, ignoring non-shorthands" in {
      val severalShortHands = shortHands.take(3)
      val string = severalShortHands.keys.mkString(" ")
      val expectedCards = severalShortHands.values.toSet

      val strings = Seq(
        string,
        "[%s]".format(string),
        " [%s] ".format(string),
        "[not short hand] [%s] [2X 10x]".format(string),
        "10x Kz 25h %s 2K sK".format(string)
      )

      strings foreach { s =>
        CardShortHandTranslator.translateAll(s) mustEqual expectedCards
      }

      ok
    }

    val invalidShortHands = Seq(
      "Ks ", // Too long
      "Kss",
      "J",   // Too short
      "Xs",  // Invalid rank
      "2j",  // Invalid suit
      "ss",  // Invalid suit
      "sK",  // Invalid order of rank/suit
      "22",
      ""
    )

    "ignores invalid shorthand" in {
      invalidShortHands foreach { invalidShortHand =>
        CardShortHandTranslator.translate(invalidShortHand) must beNone
      }

      ok
    }
  }
}