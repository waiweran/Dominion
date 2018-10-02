package cards.darkAges;

import cards.Card;

public class PoorHouse extends Card {

	private static final long serialVersionUID = 177L;

	public PoorHouse() {
		super("Poor House", "Action", "Dark Ages", 1);
	}

	@Override
	public void performAction() {
		int money = 4;
		for(Card c : getPlayer().deck.hand) {
			if(c.isTreasure()) {
				if(--money < 1) {
					return;
				}
			}
		}
		for(Card c : getPlayer().deck.play) {
			if(c.isTreasure()) {
				if(--money < 1) {
					return;
				}
			}
		}
		getPlayer().addTreasure(money);
	}

}
