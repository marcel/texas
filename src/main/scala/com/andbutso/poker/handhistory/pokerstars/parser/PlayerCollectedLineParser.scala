package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.handhistory.ChipAmount

import scala.util.matching.Regex.Match

// e.g.
//
//  pardama collected $37 from pot
object PlayerCollectedLineParser extends LineParser2[(String, ChipAmount)] {
  val pattern = """(?x)^
                   (?<username>\S+)
                   \s+collected\s+
                   (?<amount>\S+)
                   \s+from\s+pot$
                """.r

  override def extractMatches(matchData: Match) = {
    (
      matchData.group(1),
      ChipAmount.fromString(matchData.group(2))
      )
  }
}
