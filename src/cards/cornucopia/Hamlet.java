package cards.cornucopia;

import cards.Card;
import selectors.SingleCardSelector;

public class Hamlet extends Card {

	private static final long serialVersionUID = 118L;

	public Hamlet() {
		super("Hamlet", "Action", "Cornucopia", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		if(getPlayer().deck.hand.isEmpty()) return;
		try {
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Discard a card to get +1 action", this, false);
		getPlayer().deck.discardCard(sc.getSelectedIndex());
		getPlayer().addAction(1);
		SingleCardSelector sc2 = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Discard a card to get +1 buy", this, false);
		getPlayer().deck.discardCard(sc2.getSelectedIndex());
		getPlayer().addBuy();
		}
		catch(IndexOutOfBoundsException e) {}
	}

}
