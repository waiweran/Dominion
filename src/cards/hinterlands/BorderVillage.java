package cards.hinterlands;

import cards.Card;
import selectors.SupplySelector;

public class BorderVillage extends Card {

	private static final long serialVersionUID = 128L;

	public BorderVillage() {
		super("Border Village", "Action", "Hinterlands", 6);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
	}
	
	@Override
	public void gainAction() {
		SupplySelector gain = new SupplySelector(getGame(), getName(), 
				"Select a card to gain", 0, getCost() - 1);
		gain.setPlayer(getPlayer());
		boolean temp = getPlayer().buying;
		getPlayer().buying = false;
		getPlayer().deck.gain(gain.getGainedCard());
		getPlayer().buying = temp;
	}

}
