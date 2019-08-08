package cards.darkAges;

import cards.Card;
import selectors.SupplySelector;

public class Armory extends Card {

	private static final long serialVersionUID = 155L;

	public Armory() {
		super("Armory", "Action", "Dark Ages", 4);
	}

	@Override
	public void performAction() {
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Gain a card costing up to 4, putting it on top of your deck", 0, 4);
		getPlayer().deck.gain(sd.getGainedCard(), 1);
	}

}
