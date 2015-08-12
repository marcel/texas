package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.{Street, ParentSpec}
import com.andbutso.poker.cards.Cards
import com.andbutso.poker.cards.Rank._

class StreetLineParserSpec extends ParentSpec {
  "StreetLineParser" should {
    val flopLine  = "*** FLOP *** [Ks 4s 2c]"
    val turnLine  = "*** TURN *** [Ks 4s 2c] [9d]"
    val riverLine = "*** RIVER *** [Ks 4s 2c 9d] [Qd]"

    "match" in {
      "FLOP" in {
        flopLine match {
          case StreetLineParser(street, board, None) =>
            street mustEqual Street.Flop
            board mustEqual Cards(K♤, 4♤, 2♧)
          case _ =>
            failure(raw"`$flopLine` should have matched")
        }

        ok
      }

      "TURN" in {
        turnLine match {
          case StreetLineParser(street, board, Some(nextCard)) =>
            street mustEqual Street.Turn
            board mustEqual Cards(K ♤, 4 ♤, 2 ♧)
            nextCard mustEqual(9♢)
          case _ =>
            failure(raw"`$turnLine` should have matched")
        }

        ok
      }

      "RIVER" in {
        riverLine match {
          case StreetLineParser(street, board, Some(nextCard)) =>
            street mustEqual Street.River
            board mustEqual Cards(K ♤, 4 ♤, 2 ♧, 9 ♢)
            nextCard mustEqual(Q♢)
          case _ =>
            failure(raw"`$flopLine` should have matched")
        }

        ok
      }
    }

    val nonMatchingLines = Seq(
      flopLine.replaceFirst("\\*", ""),              // Missing first '*'
      flopLine.replaceFirst("\\* ", "*"),            // Missing space after third '*'
      flopLine.replaceFirst(" \\*", "*"),            // Missing space before fourth '*'
      flopLine.replaceFirst("FLOP", "flop"),         // Case is wrong
      flopLine.replaceFirst("\\[", ""),              // Board is missing opening '['
      flopLine.replaceFirst("\\]", ""),              // Board missing closing ']'
      riverLine.replaceFirst("\\[Qd\\]", "[Qd Js]"), // Next card has too many cards
      riverLine + " [Jd]"                            // Line has additional card beyond next card
    )

    "ignore non matching lines" in {
      nonMatchingLines foreach { nonMatchingLine =>
        nonMatchingLine match {
          case StreetLineParser(street, board, nextCard) =>
            failure(raw"`$nonMatchingLine` should not have matched")
          case _ =>
        }
      }

      ok
    }
  }
}
