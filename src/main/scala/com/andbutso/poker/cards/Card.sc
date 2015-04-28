import com.andbutso.poker.cards._
import com.andbutso.poker.cards.Suit._
import com.andbutso.poker.cards.Rank._
import com.andbutso.poker.cards.Card._

val cards = Set(Club(Queen), Club(Jack), Heart(2), Diamond(10), Spade(3))
val hand = Hand(cards.toSet)
//hand.bySuit foreach println
//StraightEvaluator(hand)
//FlushEvaluator(hand)
//StraightFlushEvaluator(hand)

val an = hand.absoluteNuts(2)
an.size
an.last
an foreach { nuts =>
  println(nuts.hand.cards.diff(cards.toSet))
  println(nuts)
}