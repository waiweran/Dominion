package cards.prosperity;

import cards.Card;
import selectors.Selector;

public class RoyalSeal extends Card {

	private static final long serialVersionUID = 108L;

	public RoyalSeal() {
		super("Royal Seal", "Treasure", "Prosperity", 5, 0, 2);
	}

	@Override
	public void respondGain(Card c) {
		if(getPlayer().deck.discard.size() > 0
				&& getPlayer().deck.discard.get(getPlayer().deck.discard.size() - 1) == c) {
			if(0 == new Selector(getGame()).showQuestionDialog(this, "", 
					"Put Gained Card on Top of Deck", "Put in Discard")) {
				getPlayer().deck.topOfDeck(getPlayer().deck.discard.remove(getPlayer().deck.discard.size() - 1));
			}
		}
	}

}
