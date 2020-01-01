package cards.hinterlands;

import cards.Card;

public class NomadCamp extends Card {

	private static final long serialVersionUID = 145L;

	public NomadCamp() {
		super("Nomad Camp", "Action", "Hinterlands", 4);
	}
	
	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(2);
	}
	
	// Moving the card on gain implemented in Deck

}
