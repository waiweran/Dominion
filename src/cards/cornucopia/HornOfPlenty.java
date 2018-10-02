package cards.cornucopia;

import java.util.ArrayList;

import cards.Card;
import selectors.SupplySelector;

public class HornOfPlenty extends Card {

	private static final long serialVersionUID = 120L;

	public HornOfPlenty() {
		super("Horn of Plenty", "Treasure", "Cornucopia", 5);
	}

	@Override
	public void performAction() {
		ArrayList<Card> diffs = new ArrayList<Card>();
		for(Card c : getPlayer().deck.play) {
			if(!diffs.contains(c)) {
				diffs.add(c);
			}
		}
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Select a card to gain, costing up to " + diffs.size(), 0, diffs.size());
		Card c = sd.getGainedCard();
		getPlayer().deck.gain(c);
		if(c.isVictory()) {
			getGame().board.trashCard(this);
			getPlayer().deck.play.remove(this);
		}
	}

}
