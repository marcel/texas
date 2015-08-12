package com.andbutso.poker.handhistory.model

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

case class PlayerAction(
  userName: String,
  number: Int,
  action: String,
  from: Double = 0.0d,
  to: Double = 0.0d,
  amount: Double = 0.0d,
  street: String
) extends ActiveRecord {
  val handId = 0L

  lazy val handHistory = belongsTo[HandHistory](foreignKey = "handId")

  def existingRecord = {
    if (handId == 0L) {
      None
    } else {
      PlayerAction.findBy("handId" -> handId, "number" -> number)
    }
  }
}

object PlayerAction extends ActiveRecordCompanion[PlayerAction]
