package cards.hinterlands;

import cards.Card;

public class FoolsGold extends Card {

	private static final long serialVersionUID = 136L;

	public FoolsGold() {
		super("Fool's Gold", "Treasure", "Hinterlands", 2, 0, 1);
	}

	@Override
	public void performAction() {
		for(Card c : getPlayer().deck.play) {
			if(c != this && c.equals(this)) {
				getPlayer().addTreasure(3);
				break;
			}
		}
	}
	
	// Reaction implemented in Province

}
