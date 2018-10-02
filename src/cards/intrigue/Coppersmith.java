package cards.intrigue;

import cards.Card;

public class Coppersmith extends Card {

	private static final long serialVersionUID = 30L;

	public Coppersmith() {
		super("Coppersmith", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		//Coppersmith's action is partially implemented inside the Copper card
		getPlayer().coppersmith++;
	}
	
}
