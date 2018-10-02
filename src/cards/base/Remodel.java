package cards.base;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Remodel extends Card {

	private static final long serialVersionUID = 17L;

	public Remodel() {
		super("Remodel", "Action", "Base", 4);
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a Card", this, true);
		Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
		SupplySelector gain = new SupplySelector(getGame(), getName(), "Gain a Card", 0, 
				c.getCost() + 2);
		gain.setPotion(c.costsPotion());
		getPlayer().deck.gain(gain.getGainedCard());
		getGame().board.trashCard(c);
	}

}
