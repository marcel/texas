package com.andbutso.poker.handhistory.pokerstars.parser

import com.andbutso.poker.handhistory.pokerstars.{Seat, SeatAssignment, PokerStarsPlayer}
import com.andbutso.poker.{Street, Board, ParentSpec}
import com.andbutso.poker.handhistory._

import com.andbutso.poker.cards._
import com.andbutso.poker.cards.Rank._

import scala.io.BufferedSource

class HandParserSpec extends ParentSpec {
  "HandParser" should {
    val bufferedSource = mock[BufferedSource]

    "load multiple hands" in {
      ok
    }

    "parse hand that ends pre flop" in {
      val handHistory = HandParser(PokerStars.PreFlop.split("\n"))

      handHistory.game mustEqual Game(137309721600L, "Hold'em No Limit", "$1/$2 USD")
      handHistory.table mustEqual PokerStarsTable("Medusa", 9)

      val expectedSeatAssignment = Seq(
        mkSeat(1, "EsKoTeiRo",    "$200"),
        mkSeat(3, "butcheN18",    "$250.33", Some(Seat.Role.Button)),
        mkSeat(5, "RUS)Timur",    "$301.94", Some(Seat.Role.SmallBlind)),
        mkSeat(6, "YaDaDaMeeN21", "$240.20", Some(Seat.Role.BigBlind)),
        mkSeat(7, "tarlardon1",   "$89.82"),
        mkSeat(8, "CI58",         "$244.27"),
        mkSeat(9, "FERA PB",      "$114.55")
      ).foldLeft(SeatAssignment(onTheButton = Some(3))) { _ + _ }

      val player: String => PokerStarsPlayer = expectedSeatAssignment.byUserName(_).player

      handHistory.seatAssignment mustEqual expectedSeatAssignment

      val expectedActions = onStreet(Street.PreFlop) { implicit street =>
        Seq(
          player("tarlardon1").folds,
          player("CI58").folds,
          player("FERA PB").folds,
          player("EsKoTeiRo").raises(from = $("$4"), to = $("$6")),
          player("butcheN18").folds,
          player("RUS)Timur").folds,
          player("YaDaDaMeeN21").folds,
          player("EsKoTeiRo").collects($("$5")),
          player("EsKoTeiRo").mucks
        )
      }

      handHistory.actions must containTheSameElementsAs(expectedActions)

      handHistory.pot mustEqual Pot($("$5"))
      handHistory.rake mustEqual $("$0")

      handHistory.board mustEqual Board()
      handHistory.lastStreet mustEqual Street.PreFlop

      handHistory.winners mustEqual Seq(
        player("EsKoTeiRo").collects($("$5"))(Street.PreFlop)
      )
    }

    "parse hand that goes to the flop" in {
      val handHistory = HandParser(PokerStars.ToTheFlop.split("\n"))

      handHistory.game mustEqual Game(134029107358L, "Hold'em No Limit", "€1/€2 EUR")
      handHistory.table mustEqual PokerStarsTable("Seinajoki IV", 9)

      val expectedSeatAssignment = Seq(
        mkSeat(1, "FREDY BULLIT", "€373.96"),
        mkSeat(2, "Zockermicha",  "€200"),
        mkSeat(3, "ssljvr",       "€369.73"),
        mkSeat(4, "MrChiffo",     "€403.01",  Some(Seat.Role.Button)),
        mkSeat(5, "kismolala",    "€200",     Some(Seat.Role.SmallBlind)),
        mkSeat(6, "Vittinator",   "€200",     Some(Seat.Role.BigBlind)),
        mkSeat(7, "chaoui0594",   "€118"),
        mkSeat(8, "TheCastinOut", "€122.87"),
        mkSeat(9, "chaps1988",    "€265.32")
      ).foldLeft(SeatAssignment(onTheButton = Some(4))) { _ + _ }

      val player: String => PokerStarsPlayer = expectedSeatAssignment.byUserName(_).player

      handHistory.seatAssignment mustEqual expectedSeatAssignment

      val preFlopActions = onStreet(Street.PreFlop) { implicit street =>
        Seq(
          player("chaoui0594").folds,
          player("TheCastinOut").folds,
          player("chaps1988").raises($("€3"), $("€5")),
          player("FREDY BULLIT").folds,
          player("Zockermicha").folds,
          player("ssljvr").folds,
          player("MrChiffo").folds,
          player("kismolala").folds,
          player("Vittinator").calls($("€3"))
        )
      }

      handHistory.board.flop mustEqual Cards(8♡, 2♡, 9♤)

      val flopActions = onStreet(Street.Flop) { implicit street =>
        Seq(
          player("Vittinator").checks,
          player("chaps1988").bets($("€7")),
          player("Vittinator").folds,
          player("chaps1988").collects($("€10.50")),
          player("chaps1988").mucks
        )
      }

      val expectedActions = preFlopActions ++ flopActions

      handHistory.actions must containTheSameElementsAs(expectedActions)

      handHistory.pot mustEqual Pot($("€11"))
      handHistory.rake mustEqual $("€0.50")

      handHistory.lastStreet mustEqual Street.Flop

      handHistory.winners mustEqual Seq(
        player("chaps1988").collects($("€10.50"))(Street.Flop)
      )
    }

    "parse hand that goes to the river" in {
      val handHistory = HandParser(PokerStars.ToTheRiver.split("\n"))

      handHistory.game mustEqual Game(137309662962L, "Hold'em No Limit", "$1/$2 USD")
      handHistory.table mustEqual PokerStarsTable("Medusa", 9)

      val expectedSeatAssignment = Seq(
        mkSeat(1, "EsKoTeiRo",    "$200",     Some(Seat.Role.SmallBlind)),
        mkSeat(3, "butcheN18",    "$253.33",  Some(Seat.Role.BigBlind)),
        mkSeat(4, "Icantoo61",    "$119.56"),
        mkSeat(5, "RUS)Timur",    "$200"),
        mkSeat(6, "YaDaDaMeeN21", "$240.20"),
        mkSeat(7, "tarlardon1",   "$189"),
        mkSeat(8, "CI58",         "$244.27"),
        mkSeat(9, "FERA PB",      "$118.91",  Some(Seat.Role.Button))
      ).foldLeft(SeatAssignment(onTheButton = Some(9))) { _ + _ }

      val player: String => PokerStarsPlayer = expectedSeatAssignment.byUserName(_).player

      handHistory.seatAssignment mustEqual expectedSeatAssignment

      val preFlopActions = onStreet(Street.PreFlop) { implicit street =>
        Seq(
          player("Icantoo61").folds,
          player("RUS)Timur").raises($("$2.36"), $("$4.36")),
          player("YaDaDaMeeN21").folds,
          player("tarlardon1").calls($("$4.36")),
          player("CI58").folds,
          player("FERA PB").calls($("$4.36")),
          player("EsKoTeiRo").folds,
          player("butcheN18").folds
        )
      }

      handHistory.board.flop mustEqual Cards(K♤, 4♤, 2♧)

      val flopActions = onStreet(Street.Flop) { implicit street =>
        Seq(
          player("RUS)Timur").bets($("$7.37")),
          player("tarlardon1").raises($("$7.37"), $("$14.74")),
          player("FERA PB").folds,
          player("RUS)Timur").calls($("$7.37"))
        )
      }

      handHistory.board.turn mustEqual Some(9♢)

      val turnActions = onStreet(Street.Turn) { implicit street =>
        Seq(
          player("RUS)Timur").checks,
          player("tarlardon1").bets($("$32")),
          player("RUS)Timur").calls($("$32"))
        )
      }

      handHistory.board.river mustEqual Some(Q♢)

      val riverActions = onStreet(Street.River) { implicit street =>
        Seq(
          player("RUS)Timur").checks,
          player("tarlardon1").checks,
          player("RUS)Timur").shows(Cards(K♢, J♧)),
          player("tarlardon1").mucks,
          player("RUS)Timur").collects($("$106.76"))
        )
      }

      val expectedActions = preFlopActions ++ flopActions ++ turnActions ++ riverActions

      handHistory.actions must containTheSameElementsAs(expectedActions)

      handHistory.pot mustEqual Pot($("$109.56"))
      handHistory.rake mustEqual $("$2.80")

      handHistory.lastStreet mustEqual Street.River

      handHistory.winners mustEqual Seq(
        player("RUS)Timur").collects($("$106.76"))(Street.River)
      )
    }
  }

  def mkSeat(
    number: Int,
    userName: String,
    stackSize: String,
    role: Option[Seat.Role.Value] = None
  ) = {
    Seat(number, PokerStarsPlayer(userName, $(stackSize)), role)
  }

  def onStreet[R](street: Street.Value)(f: Street.Value => R) = {
    f(street)
  }
}
