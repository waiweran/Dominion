package cards.hinterlands;

import cards.Card;

public class Highway extends Card {

	private static final long serialVersionUID = 138L;

	public Highway() {
		super("Highway", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		//Part of Highway action implemented in Card
		getPlayer().bridge++;
	}
	
}
