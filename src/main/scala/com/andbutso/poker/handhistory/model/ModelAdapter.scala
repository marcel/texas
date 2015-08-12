package com.andbutso.poker.handhistory.model

import com.andbutso.poker.handhistory
import com.andbutso.poker.handhistory.{Action, model}

abstract class ModelAdapter[DO, M](domainObject: DO) {
  def toModel: M
}

object Implicits {
  implicit def domainObjectToModelAdapter(domainObject: handhistory.HandHistory) = {
    new HandHistoryModelAdapter(domainObject)
  }

  implicit def domainObjectToModelAdapter(domainObject: Seq[handhistory.PlayerAction]) = {
    new PlayerActionsModelAdapter(domainObject: Seq[handhistory.PlayerAction])
  }
}

class PlayerActionsModelAdapter(domainObject: Seq[handhistory.PlayerAction])
  extends ModelAdapter[Seq[handhistory.PlayerAction], Seq[model.PlayerAction]](
    domainObject
  ) {

  def toModel = {
    domainObject.zipWithIndex map { case (playerAction, index) =>
      val action = model.PlayerAction(
        userName = playerAction.player.userName,
        number = index + 1,
        action = playerAction.action.name.toString,
        street = playerAction.street.toString
      )

      playerAction.action match {
        case Action.Bet(amount) =>
          action.copy(amount = amount.amount.value)
        case Action.Call(amount) =>
          action.copy(amount = amount.amount.value)
        case Action.Collect(amount) =>
          action.copy(amount = amount.amount.value)
        case Action.Raise(from, to) =>
          action.copy(from = from.amount.value, to = to.amount.value)
        case _ => // TODO Show, etc
          action
      }
    }
  }
}

class HandHistoryModelAdapter(domainObject: handhistory.HandHistory)
  extends ModelAdapter[handhistory.HandHistory, model.HandHistory](
    domainObject
  ) {
  import Implicits._

  def toModel = {
    model.HandHistory(
      handId = domainObject.game.id,
      mainPot = domainObject.pot.main.amount.value,
      sidePot = domainObject.pot.side.amount.value,
      bigBlind = domainObject.game.bigBlind.amount.value.toInt,
      smallBlind = domainObject.game.smallBlind.amount.value.toInt,
      playedAt = domainObject.date
    )
  }

  def save = {
    val handHistory   = toModel
    val playerActions = domainObject.actions.toModel

    model.HandHistory.transaction {
      val savedHandHistory = handHistory.existingRecord getOrElse {
        handHistory.save
        handHistory
      }

      val actionsToSave = playerActions filter { action =>
        PlayerAction.findBy(
          "handId" -> savedHandHistory.handId,
          "number" -> action.number
        ).isEmpty
      }

      if (actionsToSave.nonEmpty) savedHandHistory.playerActions << actionsToSave

      savedHandHistory
    }
  }
}