package com.andbutso.poker.cards

import com.andbutso.poker.ParentSpec
import com.andbutso.poker.cards.Rank._

class HandSpec extends ParentSpec {
  "Hand" should {
    "be empty at first" in {
      Hand.Empty mustEqual Hand(Cards())
    }

    "be comparable to another hand" in {
      Hand.Empty mustEqual Hand.Empty

      Hand(A♤, J♡) mustEqual Hand(J♡, A♤)

      Hand(A♤, J♡) mustNotEqual Hand(K♡, A♤)

      val hand = Hand(A♡, J♢)
      Seq(hand, hand, hand).distinct mustEqual Seq(hand)
    }

    "determine its value" in {
      Hand(A♤, 2♧, A♢).value.handRank mustEqual HandRank.OnePair
    }

    "add cards with +" in {
      (Hand(2♡) + (5♢)) mustEqual Hand(5♢, 2♡)

      (Hand(5♢, 2♡) + Cards(7♢, 8♡)) mustEqual Hand(2♡, 5♢, 7♢, 8♡)
    }

    "group by suit" in {
      Hand(2♢, 3♢, A♤).bySuit mustEqual Map(
        Suit.Diamonds -> Cards(2♢, 3♢),
        Suit.Spades -> Cards(A♤)
      )
    }

    "group by rank" in {
      Hand(5♧, 5♤, 4♢).byRank mustEqual Map(
        Rank.Five -> Cards(5♧, 5♤),
        Rank.Four -> Cards(4♢)
      )
    }

    "determine all possible outs" in {
      "for a high card" in {
        val highCard = Hand(2♢, 5♡)

        val higherHighCard = Rank.values filter { _ > Rank.Five } flatMap { _.all }
        val pairOuts       = highCard.cards flatMap { _.rank.all } diff(highCard.cards)

        val outs = (higherHighCard ++ pairOuts) map { out => highCard + out }
        highCard.outs mustEqual outs

        val outsToABetterHandRank = pairOuts map { out => highCard + out }
        highCard.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        highCard.nuts() mustEqual outsToABetterHandRank.map { _.value }
        highCard.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.ThreeOfAKind
        highCard.absoluteNuts().map { _.hand } mustEqual (Rank.Five.all - (5♡)).map { highCard + _ }
      }

      "for a pair" in {
        val pair        = Hand(2♢, 2♤, 3♡)
        val tripsOuts   = Cards(2♡, 2♧)
        val twoPairOuts = Rank.Three.all - (3♡)

        val outs = (tripsOuts ++ twoPairOuts) map { out => pair + out }

        pair.outs mustEqual outs
        pair.outsToImproveToBetterHandRank mustEqual outs

        pair.nuts() mustEqual tripsOuts.map { out => (pair + out).value }
        pair.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.FourOfAKind
        pair.absoluteNuts().map { _.hand } mustEqual (Rank.Two.all.diff(pair.cards)).map { pair + _ }
      }

      "for two pair" in {
        val twoPair = Hand(4♢, 5♤, 4♡, 5♡, A♡, 2♡)

        val higherTwoPair     = Rank.Ace.all - (A♡)
        val straightOuts      = Rank.Three.all
        val flushOuts         = Suit.Hearts.all diff(twoPair.cards)
        val fullHouseOuts     = Cards(4♧, 5♧, 5♢, 4♤)
        val straightFlushOuts = Cards(3♡)

        val outs = (higherTwoPair ++ straightOuts ++ flushOuts ++ fullHouseOuts ++ straightFlushOuts) map { out => twoPair + out }
        twoPair.outs mustEqual outs

        val outsToABetterHandRank = (straightOuts ++ flushOuts ++ fullHouseOuts ++ straightFlushOuts) map { out => twoPair + out }
        twoPair.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        twoPair.nuts() mustEqual straightFlushOuts.map { out => (twoPair + out).value }
        twoPair.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.StraightFlush
        twoPair.absoluteNuts().map { _.hand } mustEqual Set(twoPair + straightFlushOuts)
      }

      "for trips" in {
        val trips         = Hand(2♡, 2♢, 2♧, 3♢)
        val quadsOuts     = Cards(2♤)
        val fullHouseOuts = Rank.Three.all - (3♢)

        val outs = (quadsOuts ++ fullHouseOuts) map { out => trips + out }
        trips.outs mustEqual outs

        val outsToABetterHandRank = (quadsOuts ++ fullHouseOuts) map { out => trips + out }
        trips.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        trips.nuts() mustEqual quadsOuts.map { out => (trips + out).value }
        trips.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.FourOfAKind
        trips.absoluteNuts().map { _.hand } mustEqual Set(trips + quadsOuts)
      }

      "for an inside straight draw" in {
        val insideStraightDraw = Hand(5♢, 6♧, 8♤, 9♡)

        val highCardOuts = Rank.values filter { _ > Rank.Nine } flatMap { _.all }
        val pairOuts     = insideStraightDraw.cards flatMap { _.rank.all } diff(insideStraightDraw.cards)
        val straightOuts = Rank.Seven.all

        val outs = (highCardOuts ++ pairOuts ++ straightOuts) map { out => insideStraightDraw + out }
        insideStraightDraw.outs mustEqual outs

        val outsToABetterHandRank = (pairOuts ++ straightOuts) map { out => insideStraightDraw + out }
        insideStraightDraw.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        insideStraightDraw.nuts() mustEqual straightOuts.map { out => (insideStraightDraw + out).value }
        insideStraightDraw.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.Straight
        insideStraightDraw.absoluteNuts().map { _.hand } mustEqual straightOuts.map { insideStraightDraw + _ }
      }

      "for an open ended straight draw" in {
        val openEndedStraightDraw = Hand(6♧, 7♧, 8♤, 9♧)

        val highCardOuts = Rank.values filter { _ > Rank.Ten } flatMap { _.all }
        val pairOuts     = openEndedStraightDraw.cards flatMap { _.rank.all } diff(openEndedStraightDraw.cards)
        val straightOuts = Rank.Five.all ++ Rank.Ten.all

        val outs = (highCardOuts ++ pairOuts ++ straightOuts) map { out => openEndedStraightDraw + out }
        openEndedStraightDraw.outs mustEqual outs

        val outsToABetterHandRank = (pairOuts ++ straightOuts) map { out => openEndedStraightDraw + out }
        openEndedStraightDraw.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        openEndedStraightDraw.nuts() mustEqual straightOuts.map { out => (openEndedStraightDraw + out).value }
        openEndedStraightDraw.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.StraightFlush
        openEndedStraightDraw.absoluteNuts().map { _.hand } mustEqual Rank.Ten.all.map { openEndedStraightDraw + _ }
      }

      "for a straight" in {
        val straight = Hand(4♢, 5♢, 6♢, 7♧, 8♢)

        val higherStraightOuts = Rank.Nine.all
        val flushOuts          = Suit.Diamonds.all diff(straight.cards)
        val straightFlushOuts  = Cards(7♢)

        val outs = (higherStraightOuts ++ flushOuts ++ straightFlushOuts) map { out => straight + out }
        straight.outs mustEqual outs

        val outsToABetterHandRank = (flushOuts ++ straightFlushOuts) map { out => straight + out }
        straight.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        straight.nuts() mustEqual straightFlushOuts.map { out => (straight + out).value }
        straight.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.StraightFlush
        straight.absoluteNuts().map { _.hand } mustEqual Set(straight + straightFlushOuts)
      }

      "for a flush draw" in {
        val flushDraw = Hand(J♡, 5♡, 8♡, K♡)

        val highCardOuts = Rank.Ace.all
        val flushOuts    = Suit.Hearts.all.diff(flushDraw.cards)
        val pairOuts     = flushDraw.cards.flatMap { _.rank.all }.diff(flushDraw.cards)

        val outs = (highCardOuts ++ flushOuts ++ pairOuts) map { out => flushDraw + out }
        flushDraw.outs mustEqual outs

        val outsToABetterHandRank = (flushOuts ++ pairOuts) map { out => flushDraw + out }
        flushDraw.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        flushDraw.nuts() mustEqual flushOuts.map { out => (flushDraw + out).value }
        flushDraw.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.Flush
        flushDraw.absoluteNuts().map { _.hand } mustEqual Set(flushDraw + (A♡))
      }

      "for a flush" in {
        val flush = Hand(2♢, 5♢, 7♢, J♢, Q♢, Q♧)
        val higherFlushOuts = Cards(K♢, A♢)

        val outs = higherFlushOuts map { out => flush + out }

        flush.outs mustEqual outs
        flush.outsToImproveToBetterHandRank mustEqual Set.empty

        // TODO nuts() assertion
        flush.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.FourOfAKind
        flush.absoluteNuts().map { _.hand } mustEqual Set(flush + (A♢))
      }

      "for a full house" in {
        val fullHouse = Hand(9♢, 9♧, 9♤, Q♢, Q♧)

        val higherFullHouseOuts = Cards(Q♡, Q♤)
        val quadsOuts           = Cards(9♡)

        val outs = (higherFullHouseOuts ++ quadsOuts) map { out => fullHouse + out }
        fullHouse.outs mustEqual outs

        val outsToABetterHandRank = quadsOuts map { out => fullHouse + out }
        fullHouse.outsToImproveToBetterHandRank mustEqual outsToABetterHandRank

        fullHouse.nuts() mustEqual quadsOuts.map { out => (fullHouse + out).value }
        fullHouse.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.FourOfAKind
        fullHouse.absoluteNuts().map { _.hand } mustEqual Set(fullHouse + quadsOuts)
      }

      "for a straight flush draw" in {
        val straightFlushDraw = Hand(5♢, 6♢, 8♢, 9♢, A♢)
        val straightFlushOuts = Cards(7♢)

        val outs = straightFlushOuts map { out => straightFlushDraw + out }

        straightFlushDraw.outs mustEqual outs
        straightFlushDraw.outsToImproveToBetterHandRank mustEqual outs

        straightFlushDraw.nuts() mustEqual outs.map { _.value }
        straightFlushDraw.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.StraightFlush
        straightFlushDraw.absoluteNuts().map { _.hand } mustEqual Set(straightFlushDraw + straightFlushOuts)
      }

      "for a straight flush" in {
        val straightFlush = Hand(9♢, 10♢, J♢, Q♢, K♢, K♤)

        val higherStraightFlushOuts = Cards(A♢)

        val outs = higherStraightFlushOuts map { out => straightFlush + out }

        straightFlush.outs mustEqual outs
        straightFlush.outsToImproveToBetterHandRank mustEqual Set.empty

        // TODO nuts() assertion
        straightFlush.nuts(numberOfAdditionalCards = 2).head.handRank mustEqual HandRank.StraightFlush
        straightFlush.absoluteNuts().map { _.hand } mustEqual Set(straightFlush + higherStraightFlushOuts)
      }
    }
  }
}