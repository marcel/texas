package com.andbutso.poker.cards

import org.specs2.mutable.Specification

class HandEvaluatorSpec extends Specification {
  import com.andbutso.poker.cards.Card._
  import com.andbutso.poker.cards.Rank._

   "identifies pairs" >> {
     val pair = Set(Heart(2), Club(2))
      val pairs = Seq(
        pair,
        pair + Club(3) // Other cards can be in the hand
      )

      pairs foreach { cards =>
        val handValue = HandRankEvaluator(Hand(cards))
            handValue.handRank mustEqual HandRank.OnePair
            handValue.hand mustEqual Hand(cards)
            handValue.cards mustEqual pair

      }

     ok
  }

  "identifies a straight" >> {
    val straights = Seq(
      Set(Heart(2), Club(3), Heart(4), Club(5), Heart(6)),
      Set(Heart(9), Club(10), Heart(Jack), Club(Queen), Heart(King)),
      Set(Diamond(2), Heart(9), Club(10), Heart(Jack), Club(Queen), Heart(King))
    )

    straights foreach { cards =>
      val handValue = HandRankEvaluator(Hand(cards))
          handValue.handRank mustEqual HandRank.Straight
          handValue.hand mustEqual Hand(cards)
//          handValue.cards mustEqual straights(0)
    }

    ok
  }

  "identifies a straight flush" >> {
    val handValue = HandRankEvaluator(
      Hand(
        Set(
          Heart(4), Heart(5), Heart(6), Heart(7), Heart(8)
        )
      )
    )

    handValue.handRank mustEqual HandRank.StraightFlush
  }

//  def evaluate(cards: Set[Card])(f: HandValue => MatchResult[_]) = {
//    HandEvaluator.Pair(Hand(cards)) must beSome.like {
//      case handValue => f(handValue)
//    }
//  }
}
