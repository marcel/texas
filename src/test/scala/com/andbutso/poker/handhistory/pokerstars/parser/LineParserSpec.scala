package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.ParentSpec

import scala.util.matching.Regex.Match

class LineParserSpec extends ParentSpec {
  "LineParser" should {
    val lineParser = new LineParser {
      type Container = (String, Int)
      def pattern = """(\w+): (\d+)""".r
      def extractMatches(matchData: Match) = {
        (matchData.group(1).toUpperCase, matchData.group(2).toInt)
      }
    }

    "extract matches when matches are made" in {
      lineParser("Age: 33") {
        case (attribute, value) =>
          attribute mustEqual "AGE"
          value mustEqual 33
      } must beSome
    }

    "not extract matches if none are made" in {
      lineParser("Age 33")(identityPF) must beNone
    }
  }
}
