package com.andbutso.poker.handhistory.model

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

case class HandHistory(
  handId: Long,
  mainPot: Double,
  sidePot: Double,
  bigBlind: Int,
  smallBlind: Int,
  playedAt: java.util.Date
) extends ActiveRecord {
  lazy val playerActions = hasMany[PlayerAction](foreignKey = "handId")

  def existingRecord = {
    HandHistory.findBy("handId", handId)
  }
}

object HandHistory extends ActiveRecordCompanion[HandHistory]