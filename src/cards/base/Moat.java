package cards.base;

import cards.Card;

public class Moat extends Card {

	private static final long serialVersionUID = 15L;

	public Moat() {
		super("Moat", "Action", "Base", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
	}

	//Prevents attacks while in hand.  
	//Implemented in the getAttackedPlayers() method of DominionGame.

}
