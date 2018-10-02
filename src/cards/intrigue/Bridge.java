package cards.intrigue;

import cards.Card;

public class Bridge extends Card {

	private static final long serialVersionUID = 28L;

	public Bridge() {
		super("Bridge", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(1);
		getPlayer().bridge++;
	}
	
}
