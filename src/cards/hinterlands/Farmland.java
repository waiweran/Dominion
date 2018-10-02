package cards.hinterlands;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Farmland extends Card {

	private static final long serialVersionUID = 135L;

	public Farmland() {
		super("Farmland", "Victory", "Hinterlands", 6, 2, 0);
	}

	@Override
	public void gainAction() {
		if(getPlayer().buying) {
			getPlayer().buying = false;
			if(getPlayer().deck.hand.size() == 0) {
				getPlayer().buying = true;
				return;
			}
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
					"Trash a card", this, true);
			Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
			SupplySelector gain = new SupplySelector(getGame(), getName(),
					"Gain a card", c.getCost() + 2, c.getCost() + 2);
			gain.setPotion(c.costsPotion());
			getPlayer().deck.gain(gain.getGainedCard());
			getGame().board.trashCard(c);

			getPlayer().buying = true;
		}
	}

}
