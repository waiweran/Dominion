package cards.hinterlands;

import cards.Card;

public class FoolsGold extends Card {

	private static final long serialVersionUID = 136L;

	public FoolsGold() {
		super("Fool's Gold", "Treasure", "Hinterlands", 2, 0, 1);
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.play.contains(this)) {
			getPlayer().addTreasure(3);
		}
	}
	
	//TODO if someone gains a province get a real gold

}
