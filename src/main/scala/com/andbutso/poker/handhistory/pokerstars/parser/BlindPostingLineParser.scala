package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.handhistory.ChipAmount
import com.andbutso.poker.handhistory.pokerstars.Seat

import scala.util.matching.Regex.Match

// e.g.
//
//   EsKoTeiRo: posts small blind $1
//   butcheN18: posts big blind $2
object BlindPostingLineParser extends LineParser2[(String, Seat.Role.Value, ChipAmount)] {
  val pattern =
    """(?x)^
      (?<userName>[^:]+)
      :\s+posts\s+
      (?<smallOrBig>small|big)
      \s+blind\s+
      (?<amount>\S+)
       $
    """.r

  override def extractMatches(matchData: Match) = {
    val userName = matchData.group(1)
    val role = RoleStringToRole(matchData.group(2))
    val blindAmount = ChipAmount.fromString(matchData.group(3))

    (
      userName,
      role,
      blindAmount
      )
  }

  private val RoleStringToRole = Map(
    "big" -> Seat.Role.BigBlind,
    "small" -> Seat.Role.SmallBlind
  )
}
