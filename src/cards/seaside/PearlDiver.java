package cards.seaside;

import cards.Card;
import selectors.Selector;

public class PearlDiver extends Card {

	private static final long serialVersionUID = 68L;

	public PearlDiver() {
		super("Pearl Diver", "Action", "Seaside", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		String[] options = {"Return to Bottom of Deck", "Put on Top of Deck"};
		if(getPlayer().deck.draw.size() == 0) {
			Card c = getPlayer().deck.getDrawCard();
			if(c == null) return;
			getPlayer().deck.topOfDeck(c);
		}
		Card c = getPlayer().deck.draw.get(0);
		if(0 == new Selector(getGame()).showCardDialog(this, "", 
				c, options)) {
			getPlayer().deck.topOfDeck(c);
			getPlayer().deck.draw.remove(0);
		}
	
	}

}
