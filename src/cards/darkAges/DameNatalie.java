package cards.darkAges;

import selectors.SupplySelector;

public class DameNatalie extends Knight {

	private static final long serialVersionUID = -26L;

	public DameNatalie() {
		super("Dame Natalie", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Select a card to gain, costing up to 3", 0, 3);
		getPlayer().deck.gain(sd.getGainedCard());
	}
	
	@Override
	public void gainAction() {}

}
