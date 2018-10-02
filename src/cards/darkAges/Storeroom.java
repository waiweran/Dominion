package cards.darkAges;

import cards.Card;
import selectors.MultiCardSelector;

public class Storeroom extends Card {

	private static final long serialVersionUID = 185L;

	public Storeroom() {
		super("Storeroom", "Action", "Dark Ages", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Discard cards to draw", this, getPlayer().deck.hand.size(), false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			getPlayer().deck.deal();
			backIndex++;
		}
		
		sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Discard cards for money", this, getPlayer().deck.hand.size(), false);
		backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}
		getPlayer().addTreasure(backIndex);
	}

}
