package cards.seaside;

import cards.Card;
import selectors.MultiCardSelector;

public class Warehouse extends Card {

	private static final long serialVersionUID = 76L;

	public Warehouse() {
		super("Warehouse", "Action", "Seaside", 3);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select 3 cards to discard", this, 3, true);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}

	}
	
}
