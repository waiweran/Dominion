package cards.prosperity;

import cards.Card;

public class Peddler extends Card {

	private static final long serialVersionUID = 105L;

	public Peddler() {
		super("Peddler", "Action", "Prosperity", 8);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
	}

	@Override
	public int getCost() {
		int cost = super.getCost();
		try {
			if(getPlayer().buying) {
				for(Card c : getPlayer().deck.play) {
					if(c.isAction()) {
						cost -= 2;
					}
				}
			}
			if(cost < 0) {
				return 0;
			}
		} catch(Exception e) {}
		return cost;
	}

}
