package cards.intrigue;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Upgrade extends Card {

	private static final long serialVersionUID = 50L;

	public Upgrade() {
		super("Upgrade", "Action", "Intrigue", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a card", this, true);
		Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
		SupplySelector gain = new SupplySelector(getGame(), getName(), 
				"Select a card to gain", c.getCost() + 1, c.getCost() + 1);
		gain.setPotion(c.costsPotion());
		getPlayer().deck.gain(gain.getGainedCard());
		getGame().board.trashCard(c);
	}
	
}
