package cards.seaside;

import java.util.List;

import cards.Card;
import selectors.SingleCardSelector;

public class Lookout extends Card {

	private static final long serialVersionUID = 63L;

	public Lookout() {
		super("Lookout", "Action", "Seaside", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		List<Card> cards = getPlayer().deck.getDrawCards(3);
		if(!cards.isEmpty()) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), cards,
					"Select a card to trash", 
					this, true);
			getGame().board.trashCard(cards.remove(sc.getSelectedIndex()));
		}
		if(!cards.isEmpty()) {
			SingleCardSelector sc2 = new SingleCardSelector(getGame(), cards,
					"Select a card to discard", 
					this, true);
			getPlayer().deck.discardCard(cards.remove(sc2.getSelectedIndex()));
		}
		if(!cards.isEmpty()) {
			getPlayer().deck.topOfDeck(cards.get(0));
		}
	}
	
}
