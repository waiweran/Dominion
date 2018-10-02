package cards.hinterlands;

import cards.Card;
import selectors.SupplySelector;

public class Haggler extends Card {

	private static final long serialVersionUID = 137L;

	public Haggler() {
		super("Haggler", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
	}
	
	@Override
	public void respondGain(Card c) {
		if(getPlayer().buying) {
			SupplySelector gain = new SupplySelector(getGame(), getName(), 
					"Select a card to gain", 0, c.getCost() - 1); 
			gain.setPotion(c.costsPotion());
			getPlayer().buying = false;
			getPlayer().deck.gain(gain.getGainedCard());
			getPlayer().buying = true;
		}
	}

}
