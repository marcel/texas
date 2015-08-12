package com.andbutso.poker.handhistory.model

import com.github.aselab.activerecord.ActiveRecordTables
import com.github.aselab.activerecord.dsl._

object Tables extends ActiveRecordTables {
  val handHistory  = table[HandHistory]
  val playerAction = table[PlayerAction]
}