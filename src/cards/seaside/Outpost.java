package cards.seaside;

import cards.Card;

public class Outpost extends Card {

	private static final long serialVersionUID = 67L;

	public Outpost() {
		super("Outpost", "Action-Duration", "Seaside", 5);
	}
	
	public void cleanupAction() {
		if(!getPlayer().deck.duration.contains(this)) {
			getGame().extraTurn();
		}
	}
	
	public void durationAction() {
		while(getPlayer().deck.hand.size() > 3) {
			getPlayer().deck.putBack(getGame().random.nextInt(getPlayer().deck.hand.size()));
		}
	}

}
