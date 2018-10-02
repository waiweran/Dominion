package cards.prosperity;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;


public class Expand extends Card {

	private static final long serialVersionUID = 95L;

	public Expand() {
		super("Expand", "Action", "Prosperity", 7);
	}

	@Override
	public void performAction() {
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a Card", this, true);
		Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
		SupplySelector gain = new SupplySelector(getGame(), getName(), 
				"Select a card to gain", 0, c.getCost() + 3);
		gain.setPotion(c.costsPotion());
		getPlayer().deck.gain(gain.getGainedCard());
		getGame().board.trashCard(c);
	}
	
}
