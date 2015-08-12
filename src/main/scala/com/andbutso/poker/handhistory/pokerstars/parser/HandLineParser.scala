package com.andbutso.poker.handhistory.pokerstars.parser

import java.text.SimpleDateFormat

import scala.util.matching.Regex.Match

// e.g.
//                   [hand_id     ]  [game_type     ] [stakes   ]   [date                ]
//   PokerStars Hand #137352938919:  Hold'em No Limit ($1/$2 USD) - 2015/06/28 19:30:02 ET
object HandLineParser extends LineParser2[(Long, String, String, java.util.Date)] {
  val dateFormat = new SimpleDateFormat("y/M/d H:m:s")

  val pattern = """(?x)\#
                   (?<handId>\w+)
                   :\s+
                   (?<gameType>[^(]+)
                   \s+\(
                   (?<stakes>[^)]+)
                   \)\s+-\s+
                   (?<date>\d{4}/\d{1,2}/\d{1,2}\s+\d{1,2}:\d{1,2}:\d{1,2})
                   """.r

  def extractMatches(matchData: Match) = {
    (
      matchData.group(1).toLong,
      matchData.group(2),
      matchData.group(3),
      dateFormat.parse(matchData.group(4))
      )
  }
}