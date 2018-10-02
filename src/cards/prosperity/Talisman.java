package cards.prosperity;

import cards.Card;
import selectors.Selector;

public class Talisman extends Card {

	private static final long serialVersionUID = 109L;

	public Talisman() {
		super("Talisman", "Treasure", "Prosperity", 4, 0, 1);
	}

	@Override
	public void respondGain(Card c) {
		if(getPlayer().buying) {
			if(getPlayer().buying && c.getCost() < 5 && !c.costsPotion()
					&& !c.isVictory()
					&& 0 == new Selector(getGame()).showCardDialog(this, "", c,
							"Take a Second Copy", "Do Not Take a Second Copy")) {
				getPlayer().buying = false;
				getPlayer().deck.gain(getGame().board.findSupply(c).takeCard());
				getPlayer().buying = true;
			}
		}
	}

}
