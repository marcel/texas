package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.handhistory.Action
import com.andbutso.poker.handhistory.pokerstars.{Seat, SeatAssignment}

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

object PlayerActionLineParser {
  def apply(seatAssignment: SeatAssignment): PlayerActionLineParser = {
    new PlayerActionLineParser(seatAssignment)
  }
}

class PlayerActionLineParser(seatAssignment: SeatAssignment) extends LineParser {
  type ActionString = String
  type Container = (Seat, Action.Name.Value, ActionString)

  override def pattern = {
    val userNames = seatAssignment.seats map { seat =>
      Regex.quote(seat.player.userName)
    }

    """(?x)
       ^
       (?<userName>%s)
       :\s+
       (?<action>\S+)
       (?:\s+
       (?<actionString>.+)
       )?
       $
    """.format(userNames.mkString("|")).r
  }

  override def extractMatches(matchData: Match) = {
    (
      seatAssignment(matchData.group(1)).get, // N.B. `get` is ok because regex wouldn't match without user name present
      Action.Name.fromString(matchData.group(2)),
      matchData.group(3)
      )
  }
}