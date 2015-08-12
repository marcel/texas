package com.andbutso.poker.handhistory.pokerstars.parser

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

trait LineParser {
  type Container

  def pattern: Regex

  def apply[R](line: String)(provideMatches: PartialFunction[Container, R]) = {
    pattern.findFirstMatchIn(line) map { matchData =>
      provideMatches(extractMatches(matchData))
    }
  }

  def extractMatches(matchData: Match): Container
}

trait LineParser2[M] {
  def pattern: Regex
  def unapply(line: String) = {
    pattern.findFirstMatchIn(line) map { matches =>
      extractMatches(matches)
    }
  }

  def extractMatches(matchData: Match): M
}