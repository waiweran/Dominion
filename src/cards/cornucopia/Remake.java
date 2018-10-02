package cards.cornucopia;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Remake extends Card {

	private static final long serialVersionUID = 125L;

	public Remake() {
		super("Remake", "Action", "Cornucopia", 4);
	}

	@Override
	public void performAction() {
		for(int j = 0; j < 2 && getPlayer().deck.hand.size() > 0; j++) {
			if(getPlayer().deck.hand.isEmpty()) return;
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
					"Trash a Card", this, true);
			Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
			SupplySelector gain = new SupplySelector(getGame(), getName(), 
					"Select a card to gain", 0, c.getCost() + 1);
			gain.setPotion(c.costsPotion());
			getPlayer().deck.gain(gain.getGainedCard());
			getGame().board.trashCard(c);
		}
	}

}
