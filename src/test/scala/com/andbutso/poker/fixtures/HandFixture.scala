package com.andbutso.poker.fixtures

import org.specs2.specification.Scope
import com.andbutso.poker.cards._
import com.andbutso.poker.cards.Rank._

trait HandFixture extends Scope {
  object HandFixture {
    val HighPair = Cards(A♢, A♧)
    val MiddlePair = Cards(T♢, T♤)
    val LowPair = Cards(2♢, 2♤)

    val TwoPairHands = Seq(
      Hand(A♢, J♤, 5♧, A♤, 2♢, 8♡, 5♡),   // Aces and 5s
      Hand(J♡, J♢, 2♤, 4♧, 5♧, 8♢, 2♧),   // Jacks and 2s
      Hand(10♧, 3♧, 5♡, 9♡, 10♤, 5♧, A♢), // 10s and 5s
      Hand(Q♢, Q♤, K♡, 2♢, 6♤, K♤, 3♧)    // Queens and Kings
    )

    val ThreeOfAKindHands = Seq(
      Hand(10♢, 10♤, 10♧),              // 10s
      Hand(4♤, 5♢, 4♧, 4♡),             // 4s
      Hand(J♢, Q♡, 5♤, J♧, J♤),         // Jacks
      Hand(K♧, Q♡, Q♤, Q♧, J♡, 9♢),     // Queens
      Hand(A♢, A♧, 2♧, 4♤, K♢, A♡, J♤), // Aces
      Hand(2♡, 4♧, J♢, 10♤, 5♧, 2♤, 2♧) // 2s
    )

    val StraightHands = Seq(
      Hand(A♤, 10♢, 8♢, 2♧, 4♢, 5♤, 3♤),  // Wheel
      Hand(2♡, 3♧, 6♤, 4♡, 5♧, 6♡, 6♧),   // 2-6 (with trips)
      Hand(J♡, 9♡, 10♧, K♧, J♡, Q♧, K♡),  // 9-K (with 2 pair)
      Hand(2♢, 9♡, 10♧, J♡, Q♧, K♡, 5♢),  // 9-K
      Hand(2♤, 3♢, 5♧, 8♤, 4♧, 6♢, 7♡)    // 4-8
    )

    val FlushHands = Seq(
      Hand(5♡, 3♡, 10♡, Q♡, A♡),
      Hand(5♢, 2♢, 6♢, 10♢, 6♧, A♢),     // Flush with pair
      Hand(5♢, 5♧, 6♢, 6♧, 10♢, 8♢, J♢), // Flush with 2 pair
      Hand(5♢, 5♧, 5♡, 10♢, 7♢, A♢, J♢), // Flush with trips
      Hand(2♢, 3♧, 4♢, 5♢, 6♢, 7♢),      // Flush with straight (not straight-flush)
      Hand(2♢, 5♢, 7♢, J♢, Q♢, Q♧, A♢)   // Six card flush with pair
    )

    val FullHouseHands = Seq(
      Hand(2♢, 2♧, A♤, A♧, A♡),
      Hand(2♢, 2♧, A♢, A♤, A♧, Q♤, Q♢)
    )

    val FourOfAKindHands = Seq(
      Hand(4♢, 4♤, 4♧, 4♡),
      Hand(4♢, Q♧, Q♤, 4♡, 4♧, 4♤),    // Four of a kind with a pair isn't a full house
      Hand(5♢, 5♤, 5♡, 5♧, Q♢, Q♤, Q♡) // Four of a kind with trips is still quads
    )

    val StraightFlushHands = Seq(
      Hand(4♡, 5♡, K♢, 6♧, 6♡, 7♡, 8♡),
      Hand(5♢, 6♢, 8♢, 9♢, A♢, 7♢),      // 9-high straight flush with an Ace-high flush
      Hand(A♢, 2♢, 3♢, 4♢, 5♢),          // Wheel straight flush with an Ace-high flush
      Hand(4♢, 5♢, 6♢, 7♢, 8♢, 9♢)       // 6 card straight flush
    )
  }
}