package cards.intrigue;

import java.util.List;

import cards.Card;

public class Scout extends Card {

	private static final long serialVersionUID = 42L;
	
	public Scout() {
		super("Scout", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		List<Card> cards = getPlayer().deck.getDrawCards(4);
		for(Card c : cards) {
			if(c.isVictory()) {
				getPlayer().deck.hand.add(c);
			}
			else {
				getPlayer().deck.topOfDeck(c);
			}
		}
	}
	
}
