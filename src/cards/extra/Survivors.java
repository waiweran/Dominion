package cards.extra;
import java.util.List;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Survivors extends Card {

	private static final long serialVersionUID = -19L;

	public Survivors() {
		super("Survivors", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		List<Card> revealed = getPlayer().deck.getDrawCards(2);
		
		if(0 == new Selector(getGame()).showCardListDialog(this, "", revealed, 
				"Discard Cards", "Put Back on Deck")) {
			for(Card c : revealed) {
				getPlayer().deck.discardCard(c);
			}
		}
		else {
			while(revealed.size() > 0) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), revealed, 
						"Click a card to put it back", this, true);
				getPlayer().deck.topOfDeck(revealed.remove(sc.getSelectedIndex()));
			}	
		}
	}

}
