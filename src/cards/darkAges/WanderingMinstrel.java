package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class WanderingMinstrel extends Card {

	private static final long serialVersionUID = 188L;

	public WanderingMinstrel() {
		super("Wandering Minstrel", "Action", "Dark Ages", 4);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
		
		ArrayList<Card> actions = new ArrayList<>();
		for(int i = 0; i < 3; i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c != null && c.isAction()) {
				actions.add(c);
			}
			else if (c != null) {
				getPlayer().deck.discardCard(c);
			}
		}
		while(actions.size() > 0) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), actions,
					"Click a card to put it back", this, true);
			getPlayer().deck.topOfDeck(actions.remove(sc.getSelectedIndex()));
		}	

	}

}
