package cards.hinterlands;

import cards.Card;
import selectors.SingleCardSelector;

public class Oasis extends Card {

	private static final long serialVersionUID = 146L;

	public Oasis() {
		super("Oasis", "Action", "Hinterlands", 3);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Discard a card", this, true);

		getPlayer().deck.discardCard(
				getPlayer().deck.hand.remove(sc.getSelectedIndex()));
	}
	
}
