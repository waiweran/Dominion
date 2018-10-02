package cards.seaside;

import java.util.List;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;

public class Navigator extends Card {

	private static final long serialVersionUID = 66L;

	public Navigator() {
		super("Navigator", "Action", "Seaside", 4);
	}

	@Override
	public void performAction() {
		List<Card> revealed = getPlayer().deck.getDrawCards(5);
		if(1 > new Selector(getGame()).showCardListDialog(this, "", revealed, 
				"Discard Cards", "Put Back on Deck")) {
			getPlayer().deck.discardCards(revealed);
		}
		else {
			while(revealed.size() > 0) {
				try {
					SingleCardSelector sc = new SingleCardSelector(getGame(), revealed, 
							"Click a card to put it back", this, true);
					getPlayer().deck.topOfDeck(revealed.remove(sc.getSelectedIndex()));
				} catch(Exception e) {}
			}	
		}
	}

}
