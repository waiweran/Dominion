package cards.hinterlands;

import cards.Card;
import gameBase.Player;
import selectors.Selector;

public class Duchess extends Card {

	private static final long serialVersionUID = 133L;

	public Duchess() {
		super("Duchess", "Action", "Hinterlands", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		for(Player p : getGame().players) {
			Card c = p.deck.getDrawCard();
			Selector sel = new Selector(getGame());
			sel.setPlayer(p);
			if(1 == sel.showCardDialog(this, "", c, 
					"Put Back on Deck", "Discard")) {
				p.deck.discardCard(c);
			}
			else {
				p.deck.topOfDeck(c);
			}
		}
	}
	
}
