package cards.seaside;

import cards.Card;

public class Wharf extends Card {

	private static final long serialVersionUID = 77L;

	public Wharf() {
		super("Wharf", "Action-Duration", "Seaside", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addBuy();
	}

	@Override
	public void durationAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addBuy();
	}

}
