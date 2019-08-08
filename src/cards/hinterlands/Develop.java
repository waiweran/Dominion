package cards.hinterlands;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Develop extends Card {

	private static final long serialVersionUID = 132L;

	public Develop() {
		super("Develop", "Action", "Hinterlands", 3);
	}
	
	@Override
	public void performAction() {
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a Card", this, true);
		Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
		SupplySelector gain = new SupplySelector(getGame(), getName(), 
				"Select a card to gain costing 1 more", c.getCost() + 1, c.getCost() + 1);
		gain.setPotion(c.costsPotion());
		getPlayer().deck.gain(gain.getGainedCard(), 1);
		SupplySelector gain2 = new SupplySelector(getGame(), getName(),
				"Select a card to gain costing 1 less", c.getCost() - 1, c.getCost() - 1);
		gain.setPotion(c.costsPotion());
		getPlayer().deck.gain(gain2.getGainedCard(), 1);
		getGame().board.trashCard(c);
	}
	
}
