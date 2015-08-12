package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.handhistory.{ChipAmount, Pot}

import scala.util.matching.Regex.Match

// e.g.
//
//   Total pot $10 | Rake $0.45
//   Total pot $467.23 Main pot $192.89. Side pot $271.54. | Rake $2.80
object TotalPotSummaryLineParser extends LineParser2[(Pot, ChipAmount)] {
  val pattern =
    """(?x)
       ^
       Total\s+pot\s+
       (?<totalPot>\S+)
       (?:\s+Main\s+pot\s+
       (?<main>\S+)
       \.\s+Side\s+pot\s+
       (?<side>\S+)
       \.)?
       \s+\|\s+Rake\s+
       (?<rake>\S+)
       $
    """.r

  def extractMatches(matchData: Match) = {
    val rake = ChipAmount.fromString(matchData.group(4))

    (Option(matchData.group(2)), Option(matchData.group(3))) match {
      case (Some(main), Some(side)) =>
        val pot = Pot(
          main = ChipAmount.fromString(main),
          side = ChipAmount.fromString(side)
        )

        (pot, rake)
      case _ =>
        (Pot(main = ChipAmount.fromString(matchData.group(1))), rake)
    }
  }
}
