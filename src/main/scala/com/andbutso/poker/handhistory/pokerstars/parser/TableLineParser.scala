package com.andbutso.poker.handhistory.pokerstars.parser

import scala.util.matching.Regex.Match

// e.g.
//
//   Table 'Medusa' 9-max Seat #2 is the button
object TableLineParser extends LineParser2[(String, Int, Int)] {
  val pattern =
    """(?x)
       (?<tableName>[^']+)
       '\s+
       (?<capacity>\d+)
       -max\s+Seat\s+\#
       (?<button>\d+)
       \s+is\s+the\s+button
    """.r

  override def extractMatches(matchData: Match) = {
    (
      matchData.group(1),
      matchData.group(2).toInt,
      matchData.group(3).toInt
      )
  }
}