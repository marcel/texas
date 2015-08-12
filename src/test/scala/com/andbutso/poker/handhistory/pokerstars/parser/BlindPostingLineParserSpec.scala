package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.ParentSpec
import com.andbutso.poker.handhistory.pokerstars.Seat.Role._

//class BlindPostingLineParserSpec extends ParentSpec {
//  "BlindPostingLineParser" should {
//    val matchingLines = Map(
//      "butcheN18: posts big blind $2" ->
//        ("butcheN18", BigBlind, $("$2")),
//      "butcheN18: posts big blind $2.57" ->
//        ("butcheN18", BigBlind, $("$2.57")),
//      "butcheN18: posts big blind €2.57" ->
//        ("butcheN18", BigBlind, $("€2.57")),
//      "EsKoTeiRo: posts small blind $1" ->
//        ("EsKoTeiRo", SmallBlind, $("$1")),
//      "EsKoTeiRo: posts small blind $1.73" ->
//        ("EsKoTeiRo", SmallBlind, $("$1.73"))
//    )
//
//    val matchingLine = matchingLines.keys.toSeq.sorted.head
//    val nonMatchingLines = Seq(
//      matchingLine.replaceFirst("posts", "posted"),
//      matchingLine.replaceFirst("small ", ""),
//      matchingLine.replaceFirst(" \\$1", ""),
//      matchingLine.replaceFirst("EsKoTeiRo", ""),
//      matchingLine.replaceFirst(": ", ":")
//    )
//
//    "extract info from matching lines" in {
//      matchingLines foreach {
//        case (line, (expectedUserName, expectedRole, expectedChipAmount)) =>
//          BlindPostingLineParser(line) {
//            case (userName, role, chipAmount) =>
//              userName mustEqual expectedUserName
//              role mustEqual expectedRole
//              chipAmount mustEqual expectedChipAmount
//          } must beSome
//      }
//
//      ok
//    }
//
//    "skip non matching lines" in {
//      nonMatchingLines foreach { line =>
//        BlindPostingLineParser(line)(identityPF) map { _ =>
//          failure("Should not have matched line `%s`".format(line))
//        } must beNone
//      }
//
//      ok
//    }
//  }
//}
