package com.andbutso.poker

import com.andbutso.poker.cards._
import com.andbutso.poker.cards.Rank._

class BoardSpec extends ParentSpec {
  "Board" should {
    val flop  = Cards(A♤, Q♢, 2♡)
    val turn  = J♢
    val river = 10♡

    "combine flop, turn and river to create a full board" in {

      Board().cards must beEmpty
      Board(flop = flop).cards mustEqual flop
      Board(flop = flop, turn = Some(turn)).cards mustEqual (flop + turn)
      Board(flop = flop, turn = Some(turn), river = Some(river)).cards mustEqual ((flop + turn) + river)
    }
  }
}
