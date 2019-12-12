package gameBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cards.Card;

/**
 * Creates a supply of multiple cards that is shuffled.
 * @author Nathaniel
 * @version 08-03-2015
 */
public class ShuffledSupply extends Supply {

	private static final long serialVersionUID = 3730777425913598957L;

	private ArrayList<Card> pileCards;

	/**
	 * Constructor for ShuffledSupply.
	 * @param rnd random number generator used to shuffle the Supply.
	 * @param placeholder the placeholder card for the Supply.
	 * @param cards the types of cards in the supply.
	 * @param num the total number of cards *Must be a multiple of cards.size()*
	 */
	public ShuffledSupply(Random rnd, Card placeholder, Card[] cards, int num) {
		super(placeholder, num);
		pileCards = new ArrayList<Card>();
		for(int i = 0; i < num; i++) {
			pileCards.add(cards[i%cards.length]);
		}
		Collections.shuffle(pileCards, rnd);
	}

	@Override
	public Card takeCard() {
		if(super.takeCard() != null) {
			Card retVal = pileCards.remove(pileCards.size() - 1);
			notifyObservers();
			return retVal;
		}
		return null;
	}
	
	@Override
	public void putOneBack(Card c) {
		super.putOneBack(c);
		pileCards.add(c);
		notifyObservers();
	}

	@Override
	public Card getTopCard() {
		if(isEmpty() == 0) {
			return pileCards.get(pileCards.size() - 1);
		}
		else {
			return getCard();
		}
	}
	
	@Override
	public void passGameToCards(DominionGame g) {
		super.passGameToCards(g);
		for(Card c : pileCards) {
			c.passGame(g);
		}
	}


}
