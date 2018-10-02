package cards.intrigue;

import cards.Card;
import selectors.SupplySelector;

public class Ironworks extends Card {

	private static final long serialVersionUID = 35L;

	public Ironworks() {
		super("Ironworks", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Select a card to gain, costing up to 4", 0, 4);
		Card c = sd.getGainedCard();
		getPlayer().deck.gain(c);
		if(c.isAction()) {
			getPlayer().addAction(1);
		}
		else if(c.isTreasure()) {
			getPlayer().addTreasure(1);
		}
		else if(c.isVictory()) {
			getPlayer().deck.deal();
		}
	}
	
}
