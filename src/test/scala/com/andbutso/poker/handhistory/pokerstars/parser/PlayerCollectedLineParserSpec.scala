package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.ParentSpec

class PlayerCollectedLineParserSpec extends ParentSpec {
  "PlayerCollectedLineParser" should {
    val matchingLine = "paRd)a:ma24 collected $37.54 from pot"

    "extract info from matching lines" in {
      matchingLine match {
        case PlayerCollectedLineParser(userName, amount) => {
          userName mustEqual "paRd)a:ma24"
          amount mustEqual $("$37.54")
        }
        case _ =>
          failure(raw"`$matchingLine` should have matched")
      }

      ok
    }

    val nonMatchingLines = Seq(
      matchingLine.replaceFirst(" from pot", ""),
      matchingLine.replaceFirst("paR", " paR"),
      matchingLine.replaceFirst("collected", "collects"),
      matchingLine.replaceFirst("ma", " "),
      matchingLine.replaceFirst("\\$37.54 ", "")
    )

    "ignore lines that don't match" in {
      nonMatchingLines foreach { nonMatchingLine =>
        nonMatchingLine match {
          case PlayerCollectedLineParser(_, _) =>
            failure("Should not have matched line `%s`".format(nonMatchingLine))
          case _ =>
        }
      }

      ok
    }
  }
}
