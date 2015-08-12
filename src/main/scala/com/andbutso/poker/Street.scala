package com.andbutso.poker

object Street extends Enumeration {
  val PreFlop = Value
  val Flop    = Value("FLOP")
  val Turn    = Value("TURN")
  val River   = Value("RIVER")
}