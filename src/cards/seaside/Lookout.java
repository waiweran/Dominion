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
		SingleCardSelector sc = new SingleCardSelector(getGame(), cards,
				"Select a card to trash.  The other cards will be discarded and returned to your deck", 
				this, true);
		getGame().board.trashCard(cards.remove(sc.getSelectedIndex()));
		SingleCardSelector sc2 = new SingleCardSelector(getGame(), cards,
				"Select a card to trash.  The other cards will be discarded and returned to your deck", 
				this, true);
		getPlayer().deck.discardCard(cards.remove(sc2.getSelectedIndex()));
		getPlayer().deck.topOfDeck(cards.get(0));
		
	}
	
}
