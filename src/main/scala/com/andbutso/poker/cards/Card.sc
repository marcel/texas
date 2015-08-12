import com.andbutso.poker.cards._
import com.andbutso.poker.cards.Suit._
import com.andbutso.poker.cards.Rank._
import com.andbutso.poker.cards.Card._

//val cards = Cards(Club(Queen), Club(Jack), Heart(2), Diamond(10), Spade(3))
//val hand = Hand(cards)
//hand.bySuit foreach println
//StraightEvaluator(hand)
//FlushEvaluator(hand)
//StraightFlushEvaluator(hand)

//val an = hand.absoluteNuts(2)
//an.size
//an.last
//an foreach { nuts =>
//  println(nuts.hand.cards.diff(cards))
//  println(nuts)
//}


//Hand(Cards(Q♡, 2♡, 3♡, J♧, T♢, Q♧))

//HandRankEvaluator(Hand(Cards(2♡, 3♧, 3♤, 4♡, 5♡, K♡, 6♡)))

val table = Table(Seq(Player(1),Player(2), Player(3), Player(4)))
val dealer = Dealer(Deck(), table)
val updatedDealer = dealer.deal

updatedDealer.table.players foreach println

updatedDealer.deck.cards.size