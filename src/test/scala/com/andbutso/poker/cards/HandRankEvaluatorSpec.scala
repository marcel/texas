package com.andbutso.poker.cards

import com.andbutso.poker.ParentSpec

import com.andbutso.poker.cards.Rank._

class HandRankEvaluatorSpec extends ParentSpec {
  "HandRankEvaluator" should {
    "identifies a high card" in {
      val handValue = HandRankEvaluator(Hand(Cards(2♢, 5♧, A♤, 9♧, J♡, 6♡, K♤)))
      handValue.handRank mustEqual HandRank.HighCard
      handValue.cards mustEqual Cards(A♤)
    }

    "identifies pairs" in {
      val pairs = Seq(Cards(2♡, 3♧, 9♢, 2♤, A♤))

      pairs foreach { cards =>
        val handValue = HandRankEvaluator(Hand(cards))
        handValue.handRank mustEqual HandRank.OnePair
        handValue.hand mustEqual Hand(cards)
        handValue.cards mustEqual Cards(2♡, 2♤)
      }

      ok
    }

    "identifies two pairs" in {
      HandFixture.TwoPairHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.TwoPair
        val byRank = handValue.cards.groupBy { _.rank }
        byRank.values foreach { _.size mustEqual 2 }
        byRank.keys.size mustEqual 2
      }

      ok
    }

    "identifies three of a kind" in {
      HandFixture.ThreeOfAKindHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.ThreeOfAKind
        val byRank = handValue.cards.groupBy { _.rank }
        byRank.values foreach { _.size mustEqual 3 }
        byRank.keys.size mustEqual 1
      }

      ok
    }

    "identifies a straight" in {
      HandFixture.StraightHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.Straight
        handValue.hand mustEqual hand
      }

      val handValue = StraightEvaluator(Hand(3♢, 4♢, 2♧, 2♡, 2♢)) // Was identified as a regression
      handValue must beNone

      ok
    }

    "identifies a flush" in {
      HandFixture.FlushHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.Flush
        val bySuit = handValue.cards.groupBy { _.suit }
        bySuit.values foreach { _.size mustEqual 5 }
        bySuit.keys.size mustEqual 1
        handValue.hand mustEqual hand
      }

      ok
    }

    "identifies a full house" in {
      HandFixture.FullHouseHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.FullHouse
        val byRank = handValue.cards.groupBy { _.rank }
        byRank.values.map { _.size }.toSeq.sorted mustEqual Seq(2, 3)
        byRank.keys.size mustEqual 2
        handValue.hand mustEqual hand
      }

      ok
    }

    "identifies four of a kind" in {
      HandFixture.FourOfAKindHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.FourOfAKind
        val byRank = handValue.cards.groupBy { _.rank }
        byRank.values foreach { _.size mustEqual 4 }
        byRank.keys.size mustEqual 1
      }

      ok
    }

    "identifies a straight flush" in {
      HandFixture.StraightFlushHands foreach { hand =>
        val handValue = HandRankEvaluator(hand)
        handValue.handRank mustEqual HandRank.StraightFlush
        val bySuit = handValue.cards.groupBy { _.suit }
        bySuit.values foreach { _.size mustEqual 5 }
        bySuit.keys.size mustEqual 1
        handValue.hand mustEqual hand
      }

      ok
    }
  }

  "StraightDrawEvaluator" should {
    "identify an inside straight draw" in {
      val wheelDraw = Hand(A♧, 3♡, 4♢, 5♧, Q♧, 9♢)

      StraightDrawEvaluator(wheelDraw) match {
        case Some(handValue) =>
          handValue.handRank mustEqual HandRank.StraightDraw
          handValue.cards mustEqual Cards(A♧, 3♡, 4♢, 5♧)
        case _ =>
          failure(s"$wheelDraw should have been identified as a straight draw")
      }


      val insideStraightDraw = Hand(5♢, 6♧, 8♤, 9♢, A♧, A♤)

      StraightDrawEvaluator(insideStraightDraw) match {
        case Some(handValue) =>
          handValue.handRank mustEqual HandRank.StraightDraw
          handValue.cards mustEqual Cards(5♢, 6♧, 8♤, 9♢)
        case _ =>
          failure(s"$insideStraightDraw should have been identified as a straight draw")
      }

      ok
    }

    "identify the highest possible straight" in {
      val draw = Hand(A♡, 2♢, 3♧, 5♧, 6♤, 7♢, 8♢)

      StraightDrawEvaluator(draw) match {
        case Some(handValue) =>
          handValue.handRank mustEqual HandRank.StraightDraw
          handValue.cards mustEqual Cards(5♧, 6♤, 7♢, 8♢)
        case _ =>
          failure(s"$draw should have been identified as a straight draw")
      }

      ok
    }

    "identify an open-ended straight draw" in {
      val openEnded = Hand(4♢, 5♧, 6♡, 7♧, K♢, J♧)

      StraightDrawEvaluator(openEnded) match {
        case Some(handValue) =>
          handValue.handRank mustEqual HandRank.StraightDraw
          handValue.cards mustEqual Cards(4♢, 5♧, 6♡, 7♧)
        case _ =>
          failure(s"$openEnded should have been identified as a straight draw")
      }

      ok
    }

    "not evaluate a non-draw as a draw" in {
      val nonDraws = Seq(
        Hand(2♧, 3♢, 5♡, 7♧, 8♢, J♧),
        Hand(A♧, A♤, 3♧, 5♢, 10♢, J♧),
        Hand(A♧, 2♢, 3♧, 6♢, 7♢, 8♧)
      )

      nonDraws foreach { nonDraw =>
        StraightDrawEvaluator(nonDraw) must beNone
      }

      ok
    }
  }

  "FlushDrawEvaluator" should {
    "identify a flush draw" in {
      val draw = Hand(Cards(A♤, 2♡, 7♤, 10♡, 3♡, K♡))

      FlushDrawEvaluator(draw) match {
        case Some(handValue) =>
          handValue.handRank mustEqual HandRank.FlushDraw
          handValue.cards mustEqual draw.cards.filter { _.suit == Suit.Hearts }
          handValue.value mustEqual (FlushDrawEvaluator.strength + HighCardEvaluator(Hand(handValue.cards)).get.value)
        case _ =>
          failure("%s should have been evaluated as a flush draw".format(draw))
      }

      ok
    }

    "not identify a made flush" in {
      val flush = Hand(Cards(A♤, J♡, 2♡, 3♢, 5♡, K♡, 8♤, 7♡))
      FlushDrawEvaluator(flush) must beNone
      FlushEvaluator(flush) must beSome
    }
  }
}
