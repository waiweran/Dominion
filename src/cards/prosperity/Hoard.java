package cards.prosperity;

import cards.Card;
import selectors.Selector;

public class Hoard extends Card {

	private static final long serialVersionUID = 99L;

	public Hoard() {
		super("Hoard", "Treasure", "Prosperity", 6, 0, 2);
	}

	@Override
	public void respondGain(Card c) {
		if(getPlayer().buying && c.isVictory() &&
				0 == new Selector(getGame()).showQuestionDialog(this, "", "Gain a Gold", "Do Not Gain a Gold")) {
			getPlayer().buying = false;
			getPlayer().deck.gain(getGame().board.getGold().takeCard());
			getPlayer().buying = true;
		}
	}

}
