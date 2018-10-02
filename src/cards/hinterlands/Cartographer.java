package cards.hinterlands;

import java.util.List;

import cards.Card;
import selectors.MultiCardSelector;
import selectors.SingleCardSelector;

public class Cartographer extends Card {

	private static final long serialVersionUID = 130L;

	public Cartographer() {
		super("Cartographer", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		List<Card> revealed = getPlayer().deck.getDrawCards(4);
		MultiCardSelector sd = new MultiCardSelector(getGame(), revealed, 
				"Please Choose Cards to Discard, the rest will go back on your Deck", 
				this, getPlayer().deck.hand.size(), false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(revealed.remove(i - backIndex));
			backIndex++;
		}
		while(revealed.size() > 0) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), revealed,
					"Click a card to put it back", this, true);
			getPlayer().deck.topOfDeck(revealed.remove(sc.getSelectedIndex()));
		}	
	}

}
