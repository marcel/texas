package com.andbutso.poker.fixtures

import org.specs2.specification.Scope

trait PokerStarsHandHistoryFixture extends Scope {
  object PokerStars {
    val ToTheRiver =
      """
        |PokerStars Hand #137309662962:  Hold'em No Limit ($1/$2 USD) - 2015/06/27 22:47:51 ET
        |Table 'Medusa' 9-max Seat #9 is the button
        |Seat 1: EsKoTeiRo ($200 in chips)
        |Seat 3: butcheN18 ($253.33 in chips)
        |Seat 4: Icantoo61 ($119.56 in chips)
        |Seat 5: RUS)Timur ($200 in chips)
        |Seat 6: YaDaDaMeeN21 ($240.20 in chips)
        |Seat 7: tarlardon1 ($189 in chips)
        |Seat 8: CI58 ($244.27 in chips)
        |Seat 9: FERA PB ($118.91 in chips)
        |EsKoTeiRo: posts small blind $1
        |butcheN18: posts big blind $2
        |*** HOLE CARDS ***
        |Icantoo61: folds
        |RUS)Timur: raises $2.36 to $4.36
        |YaDaDaMeeN21: folds
        |tarlardon1: calls $4.36
        |CI58: folds
        |FERA PB: calls $4.36
        |EsKoTeiRo: folds
        |butcheN18: folds
        |*** FLOP *** [Ks 4s 2c]
        |RUS)Timur: bets $7.37
        |tarlardon1: raises $7.37 to $14.74
        |FERA PB: folds
        |RUS)Timur: calls $7.37
        |*** TURN *** [Ks 4s 2c] [9d]
        |RUS)Timur: checks
        |tarlardon1: bets $32
        |RUS)Timur: calls $32
        |*** RIVER *** [Ks 4s 2c 9d] [Qd]
        |RUS)Timur: checks
        |tarlardon1: checks
        |*** SHOW DOWN ***
        |RUS)Timur: shows [Kd Jc] (a pair of Kings)
        |tarlardon1: mucks hand
        |RUS)Timur collected $106.76 from pot
        |*** SUMMARY ***
        |Total pot $109.56 | Rake $2.80
        |Board [Ks 4s 2c 9d Qd]
        |Seat 1: EsKoTeiRo (small blind) folded before Flop
        |Seat 3: butcheN18 (big blind) folded before Flop
        |Seat 4: Icantoo61 folded before Flop (didn't bet)
        |Seat 5: RUS)Timur showed [Kd Jc] and won ($106.76) with a pair of Kings
        |Seat 6: YaDaDaMeeN21 folded before Flop (didn't bet)
        |Seat 7: tarlardon1 mucked
        |Seat 8: CI58 folded before Flop (didn't bet)
        |Seat 9: FERA PB (button) folded on the Flop
      """.stripMargin

    val ToTheFlop =
      """
        |PokerStars Hand #134029107358:  Hold'em No Limit (€1/€2 EUR) - 2015/04/20 11:16:01 ET
        |Table 'Seinajoki IV' 9-max Seat #4 is the button
        |Seat 1: FREDY BULLIT (€373.96 in chips)
        |Seat 2: Zockermicha (€200 in chips)
        |Seat 3: ssljvr (€369.73 in chips)
        |Seat 4: MrChiffo (€403.01 in chips)
        |Seat 5: kismolala (€200 in chips)
        |Seat 6: Vittinator (€200 in chips)
        |Seat 7: chaoui0594 (€118 in chips)
        |Seat 8: TheCastinOut (€122.87 in chips)
        |Seat 9: chaps1988 (€265.32 in chips)
        |kismolala: posts small blind €1
        |Vittinator: posts big blind €2
        |*** HOLE CARDS ***
        |chaoui0594: folds
        |TheCastinOut: folds
        |chaps1988: raises €3 to €5
        |FREDY BULLIT: folds
        |Zockermicha: folds
        |ssljvr: folds
        |MrChiffo: folds
        |kismolala: folds
        |Vittinator: calls €3
        |*** FLOP *** [8h 2h 9s]
        |Vittinator: checks
        |chaps1988: bets €7
        |Vittinator: folds
        |Uncalled bet (€7) returned to chaps1988
        |chaps1988 collected €10.50 from pot
        |chaps1988: doesn't show hand
        |*** SUMMARY ***
        |Total pot €11 | Rake €0.50
        |Board [8h 2h 9s]
        |Seat 1: FREDY BULLIT folded before Flop (didn't bet)
        |Seat 2: Zockermicha folded before Flop (didn't bet)
        |Seat 3: ssljvr folded before Flop (didn't bet)
        |Seat 4: MrChiffo (button) folded before Flop (didn't bet)
        |Seat 5: kismolala (small blind) folded before Flop
        |Seat 6: Vittinator (big blind) folded on the Flop
        |Seat 7: chaoui0594 folded before Flop (didn't bet)
        |Seat 8: TheCastinOut folded before Flop (didn't bet)
        |Seat 9: chaps1988 collected (€10.50)
      """.stripMargin

    val PreFlop =
      """
        |PokerStars Hand #137309721600:  Hold'em No Limit ($1/$2 USD) - 2015/06/27 22:50:57 ET
        |Table 'Medusa' 9-max Seat #3 is the button
        |Seat 1: EsKoTeiRo ($200 in chips)
        |Seat 3: butcheN18 ($250.33 in chips)
        |Seat 5: RUS)Timur ($301.94 in chips)
        |Seat 6: YaDaDaMeeN21 ($240.20 in chips)
        |Seat 7: tarlardon1 ($89.82 in chips)
        |Seat 8: CI58 ($244.27 in chips)
        |Seat 9: FERA PB ($114.55 in chips)
        |RUS)Timur: posts small blind $1
        |YaDaDaMeeN21: posts big blind $2
        |*** HOLE CARDS ***
        |tarlardon1: folds
        |CI58: folds
        |FERA PB: folds
        |EsKoTeiRo: raises $4 to $6
        |butcheN18: folds
        |RUS)Timur: folds
        |YaDaDaMeeN21: folds
        |Uncalled bet ($4) returned to EsKoTeiRo
        |EsKoTeiRo collected $5 from pot
        |EsKoTeiRo: doesn't show hand
        |*** SUMMARY ***
        |Total pot $5 | Rake $0
        |Seat 1: EsKoTeiRo collected ($5)
        |Seat 3: butcheN18 (button) folded before Flop (didn't bet)
        |Seat 5: RUS)Timur (small blind) folded before Flop
        |Seat 6: YaDaDaMeeN21 (big blind) folded before Flop
        |Seat 7: tarlardon1 folded before Flop (didn't bet)
        |Seat 8: CI58 folded before Flop (didn't bet)
        |Seat 9: FERA PB folded before Flop (didn't bet)
      """.stripMargin

    val PreFlop2 =
      """"
        |PokerStars Hand #137309633028:  Hold'em No Limit ($1/$2 USD) - 2015/06/27 22:46:16 ET
        |Table 'Medusa' 9-max Seat #7 is the button
        |Seat 3: butcheN18 ($253.33 in chips)
        |Seat 4: Icantoo61 ($116.56 in chips)
        |Seat 5: RUS)Timur ($200 in chips)
        |Seat 6: YaDaDaMeeN21 ($240.20 in chips)
        |Seat 7: tarlardon1 ($195 in chips)
        |Seat 8: CI58 ($236.95 in chips)
        |Seat 9: FERA PB ($121.91 in chips)
        |CI58: posts small blind $1
        |FERA PB: posts big blind $2
        |EsKoTeiRo: sits out
        |*** HOLE CARDS ***
        |butcheN18: folds
        |Icantoo61: raises $4 to $6
        |RUS)Timur: folds
        |YaDaDaMeeN21: folds
        |tarlardon1: folds
        |CI58: folds
        |FERA PB: folds
        |Uncalled bet ($4) returned to Icantoo61
        |Icantoo61 collected $5 from pot
        |Icantoo61: doesn't show hand
        |*** SUMMARY ***
        |Total pot $5 | Rake $0
        |Seat 3: butcheN18 folded before Flop (didn't bet)
        |Seat 4: Icantoo61 collected ($5)
        |Seat 5: RUS)Timur folded before Flop (didn't bet)
        |Seat 6: YaDaDaMeeN21 folded before Flop (didn't bet)
        |Seat 7: tarlardon1 (button) folded before Flop (didn't bet)
        |Seat 8: CI58 (small blind) folded before Flop
        |Seat 9: FERA PB (big blind) folded before Flop
      """.stripMargin
  }
}
