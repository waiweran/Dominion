package cards.seaside;

import cards.Card;

public class MerchantShip extends Card {

	private static final long serialVersionUID = 64L;

	public MerchantShip() {
		super("Merchant Ship", "Action-Duration", "Seaside", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
	}
	
	@Override
	public void durationAction() {
		getPlayer().addTreasure(2);
	}

}
