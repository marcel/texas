package com.andbutso.poker.handhistory

import java.util.{Currency, Date, Locale}

import com.andbutso.poker.cards.Cards
import com.andbutso.poker.handhistory.pokerstars.{PokerStarsPlayer, SeatAssignment}
import com.andbutso.poker.{Board, Street}

import scala.collection.JavaConversions._

case class Money(value: Double, currency: Currency)

object Money {
  val Currencies = Currency.getAvailableCurrencies map { currency =>
    currency.getSymbol -> currency
  } toMap

  def currencyFromString(string: String) = {
    string match {
      case "€" => Currencies("EUR")
      case "£" => Currencies("GBP")
      case _ => Currencies(string)
    }
  }
}

case class Game(
  id: Long,
  gameType: String,
  stakes: String
) {
  val BlindsPattern = """^(?<smallBlind>\S+)/(?<bigBlind>\S+).*$""".r

  val (smallBlind, bigBlind) = stakes match {
    case BlindsPattern(small, big) =>
      (ChipAmount.fromString(small), ChipAmount.fromString(big))
  }

}

case class HandHistoryBuilder(
  game: Game = null,
  date: Date = null,
  table: PokerStarsTable = null,
  bigBlind: ChipAmount = null,
  smallBlind: ChipAmount = null,
  seatAssignment: SeatAssignment = SeatAssignment(),
  board: Board = Board(),
  pot: Option[Pot] = None,
  rake: Option[ChipAmount] = None,
  lastStreet: Street.Value = Street.PreFlop,
  actions: IndexedSeq[PlayerAction] = IndexedSeq.empty[PlayerAction]
) {
  def build() = {
    HandHistory(
      game = game,
      date = date,
      table = table,
      seatAssignment = seatAssignment,
      board = board,
      pot = pot.get, // TODO Deal with this
      rake = rake.get, // TODO Deal with this
      lastStreet = lastStreet,
      actions = actions
    )
  }
}

object HandHistoryBuilder {
  object Update
}

case class HandHistory(
  game: Game,
  date: Date,
  table: PokerStarsTable,
  seatAssignment: SeatAssignment,
  board: Board,
  pot: Pot,
  rake: ChipAmount,
  lastStreet: Street.Value,
  actions: IndexedSeq[PlayerAction]
) {
  def winners = {
    actions filter { _.action.name == Action.Name.collects }
  }

  def pp = {
    import org.kiama.output.PrettyPrinter._
    pretty(any(this), w = 1)
  }
}

case class Pot(main: ChipAmount, side: ChipAmount = ChipAmount.Zero) {
  def total = {
    main + side
  }
}

case class ChipAmount(amount: Money) {
  def +(otherAmount: ChipAmount) = {
    copy(
      amount = amount.copy(
        value = amount.value + otherAmount.amount.value
      )
    )
  }

  override def toString = {
    s"❂❂ ${amount.currency.getSymbol()}${amount.value} ❂❂"
  }
}

object ChipAmount {
  val Zero = ChipAmount(
    Money(
      value = 0.0d,
      currency = Currency.getInstance(Locale.getDefault)
    )
  )

  val Pattern = """^(?<symbol>[^0-9]+)(?<value>[0-9.]+)""".r

  def fromString(string: String) = {
    string match {
      case Pattern(symbol, value) =>
        ChipAmount(
          Money(
            value.toDouble,
            Money.currencyFromString(symbol)
          )
        )
    }
  }
}

case class PokerStarsTable(name: String, capacity: Int)

abstract class Action(val name: Action.Name.Value)

object Action {
  object Name extends Enumeration {
    val folds    = Value
    val raises   = Value
    val calls    = Value
    val checks   = Value
    val bets     = Value
    val mucks    = Value
    val shows    = Value
    val sits     = Value
    val posts    = Value
    val collects = Value

    def fromString(string: String) = {
      string match {
        case "doesn't" => mucks
        case _ => withName(string)
      }
    }
  }

  case object Fold extends Action(Name.folds)
  case object Muck extends Action(Name.mucks)
  case object SitOut extends Action(Name.sits)
  case object Check extends Action(Name.checks)
  case object Posts extends Action(Name.posts)

  case class Bet(amount: ChipAmount) extends Action(Name.bets)
  case class Call(amount: ChipAmount) extends Action(Name.calls)

  case class Raise(from: ChipAmount, to: ChipAmount) extends Action(Name.raises)
  case class Collect(amount: ChipAmount) extends Action(Name.collects)

  case class Show(cards: Cards) extends Action(Name.shows)
}

case class PlayerAction(
  player: PokerStarsPlayer,
  action: Action,
  street: Street.Value
)

object PlayerAction {
  def apply(
    player: PokerStarsPlayer,
    street: Street.Value
  ): Action => PlayerAction = {
    PlayerAction(player, _: Action, street)
  }
}


