package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.ParentSpec
import com.andbutso.poker.handhistory.ChipAmount

class TotalPotSummaryLineParserSpec extends ParentSpec {
  "TotalPotSummaryLineParser" should {
    val totalPotLine        = "Total pot $10 | Rake $0.45"
    val totalPotWithSidePot = "Total pot $467.23 Main pot $192.89. Side pot $271.54. | Rake $2.80"

    "extract total pot and rake" in {
      totalPotLine match {
        case TotalPotSummaryLineParser(pot, rake) =>
          pot.total mustEqual $("$10")
          pot.main mustEqual pot.total
          pot.side mustEqual ChipAmount.Zero
          rake mustEqual $("$0.45")
        case _ =>
          failure(raw"`$totalPotLine` should have matched")
      }

      ok
    }

    "match pots with main and side pots" in {
      totalPotWithSidePot match {
        case TotalPotSummaryLineParser(pot, rake) =>
          pot.total mustEqual (pot.main + pot.side)
          pot.main mustEqual $("$192.89")
          pot.side mustEqual $("$271.54")
          rake mustEqual $("$2.80")
        case _ =>
          failure(raw"`$totalPotWithSidePot` should have matched")
      }

      ok
    }

    val nonMatchingLines = Seq(
      totalPotLine.replaceFirst("Total", "total"),
      totalPotLine.replaceFirst("\\|", ""),
      totalPotLine.replaceFirst(" | Rake $0.45", ""),
      totalPotLine.replaceFirst("Rake ", ""),
      totalPotLine.replaceFirst(" pot", ""),
      totalPotWithSidePot.replaceFirst("89\\.", "89"),
      totalPotWithSidePot.replaceFirst("54\\.", "54"),
      totalPotWithSidePot.replaceFirst("Total", "Main").replaceFirst("3 Main", "3 Total"),
      totalPotWithSidePot.replaceFirst(" Main pot", ""),
      totalPotWithSidePot.replaceFirst("\\. | Rake $2.80", "")
    )

    "ignore lines that don't match" in {
      nonMatchingLines foreach { nonMatchingLine =>
        nonMatchingLine match {
          case TotalPotSummaryLineParser(pot, rake) =>
            failure(raw"Should not have matched line `$nonMatchingLine`")
          case _ =>
        }
      }

      ok
    }
  }
}
