package cards.hinterlands;

import cards.Card;

public class Cache extends Card {

	private static final long serialVersionUID = 129L;

	public Cache() {
		super("Cache", "Treasure", "Hinterlands", 5, 0, 3);
	}

	@Override
	public void gainAction() {
		boolean temp = getPlayer().buying;
		getPlayer().buying = false;
		getPlayer().deck.gain(getGame().board.getCopper().takeCard());
		getPlayer().deck.gain(getGame().board.getCopper().takeCard());
		getPlayer().buying = temp;
	}

}
