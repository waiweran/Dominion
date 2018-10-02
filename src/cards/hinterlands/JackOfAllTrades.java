package cards.hinterlands;

import java.util.ArrayList;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;

public class JackOfAllTrades extends Card {

	private static final long serialVersionUID = 141L;

	public JackOfAllTrades() {
		super("Jack of All Trades", "Action", "Hinterlands", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		Card c = getPlayer().deck.getDrawCard();
		if(c != null) {
			if(0 == new Selector(getGame()).showCardDialog(this, "", 
					c, "Return to Deck", "Discard")) {
				getPlayer().deck.topOfDeck(c);
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
		for(int i = getPlayer().deck.hand.size(); i <= 5; i++) {
			getPlayer().deck.deal();
		}

		ArrayList<Card> available = new ArrayList<Card>();
		for(Card c2 : getPlayer().deck.hand) {
			if(!c2.isTreasure()) {
				available.add(c2);
			}
		}
		if(available.size() == 0) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), available,
				"You may trash a card", this, false);
		try {
			Card choice = available.get(sc.getSelectedIndex());
			getPlayer().deck.hand.remove(choice);
			getGame().board.trashCard(choice);
		}
		catch(Exception e) {};

	}

}
