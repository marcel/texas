package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.handhistory.ChipAmount
import com.andbutso.poker.handhistory.pokerstars.{PokerStarsPlayer, Seat}

import scala.util.matching.Regex.Match

// e.g.
//
//   Seat 6: YaDaDaMeeN21 ($240.20 in chips)
object SeatAssignmentLineParser extends LineParser2[Seat] {
  val pattern = """(?x)
                   ^
                   Seat\s+
                   (?<seatNumber>\d+)
                   :\s+
                   (?<userName>.+)
                   \s+\(
                   (?<stackSize>\S+)
                   \s+in\s+chips\)
                   $
                """.r

  override def extractMatches(matchData: Match) = {
    Seat(
      matchData.group(1).toInt,
      PokerStarsPlayer(
        matchData.group(2),
        ChipAmount.fromString(matchData.group(3))
      )
    )
  }
}
