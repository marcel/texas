package com.andbutso.poker.handhistory.pokerstars

import com.andbutso.poker.Street
import com.andbutso.poker.cards._
import com.andbutso.poker.handhistory.{Action, ChipAmount, PlayerAction}

case class PokerStarsPlayer(userName: String, initialStack: ChipAmount) {
  def folds(implicit street: Street.Value)   = action(Action.Fold, street)
  def mucks(implicit street: Street.Value)   = action(Action.Muck, street)
  def checks(implicit street: Street.Value)  = action(Action.Check, street)
  def sitsOut(implicit street: Street.Value) = action(Action.SitOut, street)

  def bets(amount: ChipAmount)(implicit street: Street.Value)     = action(Action.Bet(amount), street)
  def calls(amount: ChipAmount)(implicit street: Street.Value)    = action(Action.Call(amount), street)
  def collects(amount: ChipAmount)(implicit street: Street.Value) = action(Action.Collect(amount), street)

  def raises(from: ChipAmount, to: ChipAmount)(implicit street: Street.Value) = {
    action(Action.Raise(from, to), street)
  }

  def shows(cards: Cards)(implicit street: Street.Value) = action(Action.Show(cards), street)

  private[this] def action(a: Action, street: Street.Value) = {
    PlayerAction(this, a, street)
  }
}
