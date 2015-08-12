package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.ParentSpec

class TableLineParserSpec extends ParentSpec {
  "TableLineParser" should {
    val matching = "Table 'Medusa' 9-max Seat #2 is the button"

    val nonMatching = Seq(
      matching.replace("'", ""), // Quotes around table name missing
      matching.replace("-", ""), // Dash between capacity and 'max' missing
      matching.replace("#", ""), // Number sign before button is missing
      matching.replace("Medusa", ""), // Table name missing
      matching.replace("Seat", "seat"), // Case is wrong
      matching.replace("is the button", "") // End of line missing
    )

    "matching line extracts table info" in {
      matching match {
        case TableLineParser(name, capacity, button) =>
          name mustEqual "Medusa"
          capacity mustEqual 9
          button mustEqual 2
        case _ =>
          failure(s"$matching should have matched")
      }// must beSome
      ok
    }
//
//    "non-matching lines don't execute partial function" in {
//      nonMatching foreach { nonMatch =>
//        TableLineParser(nonMatch)(identityPF) must beNone
//      }
//
//      ok
//    }
  }
}
