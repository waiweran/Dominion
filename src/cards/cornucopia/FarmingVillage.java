package cards.cornucopia;

import cards.Card;

public class FarmingVillage extends Card {

	private static final long serialVersionUID = 116L;

	public FarmingVillage() {
		super("Farming Village", "Action", "Cornucopia", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		Card c;
		for(int j = getPlayer().deck.size(); j > 0; j--) {
			c = getPlayer().deck.getDrawCard();
			if(c.isTreasure() || c.isAction()) {
				getPlayer().deck.hand.add(c);
				break;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}

}
