package cards.adventures;

import cards.Card;

public class TreasureTrove extends Card {

	private static final long serialVersionUID = 230L;

	public TreasureTrove() {
		super("Treasure Trove", "Treasure", "Adventures", 5, 0, 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.gain(getGame().board.getCopper().takeCard());
		getPlayer().deck.gain(getGame().board.getGold().takeCard());
	}
	
}
