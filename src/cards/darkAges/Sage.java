package cards.darkAges;

import cards.Card;

public class Sage extends Card {

	private static final long serialVersionUID = 182L;

	public Sage() {
		super("Sage", "Action", "Dark Ages", 3);
	}
	
	@Override
	public void performAction() {
		getPlayer().addAction(1);
		for(int i = 0; i < 1000; i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c.getCost() >= 3) {
				getPlayer().deck.hand.add(c);
				return;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}

}
