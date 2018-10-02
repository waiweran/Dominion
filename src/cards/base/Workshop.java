package cards.base;

import cards.Card;
import selectors.SupplySelector;

public class Workshop extends Card {

	private static final long serialVersionUID = 26L;

	public Workshop() {
		super("Workshop", "Action", "Base", 3);
	}

	@Override
	public void performAction() {
		SupplySelector sd = new SupplySelector(getGame(), getName(),
				"Gain a card costing up to 4", 0, 4);
		getPlayer().deck.gain(sd.getGainedCard());
	}

}
