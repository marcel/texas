package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.Street
import com.andbutso.poker.cards._
import com.andbutso.poker.handhistory.pokerstars.CardShortHandTranslator

import scala.util.matching.Regex.Match

// e.g.
//
//   *** FLOP *** [Ks 4s 2c]
//   *** TURN *** [Ks 4s 2c] [9d]
//   *** RIVER *** [Ks 4s 2c 9d] [Qd]
object StreetLineParser extends LineParser2[(Street.Value, Cards, Option[Card])] {
  val pattern =
    """(?x)
       ^
       \*\*\*\s+
       (?<street>FLOP|TURN|RIVER)
       \s+\*\*\*\s+\[
       (?<board>[^\]]{8,11})
       \](?:\s+\[
       (?<nextCard>[^\]]{2})
       \])?
       $
    """.r

  override def extractMatches(matchData: Match) = {
    (
      Street.withName(matchData.group(1)),
      CardShortHandTranslator.translateAll(matchData.group(2)),
      CardShortHandTranslator.translate(Option(matchData.group(3)) getOrElse(""))
      )
  }
}
