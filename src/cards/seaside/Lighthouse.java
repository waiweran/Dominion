package cards.seaside;

import cards.Card;

public class Lighthouse extends Card {

	private static final long serialVersionUID = 62L;

	public Lighthouse() {
		super("Lighthouse", "Action-Duration", "Seaside", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
	}
	
	@Override
	public void durationAction() {
		//Prevents attacks while in duration.
		//Implemented in the getAttackedPlayers() method of DominionGame.
		getPlayer().addTreasure(1);
	}

}
