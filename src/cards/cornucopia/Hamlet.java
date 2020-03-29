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
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Discard a card to get +1 action", this, false);
		int index = sc.getSelectedIndex();
		if(index < 0) return;
		getPlayer().deck.discardCard(index);
		getPlayer().addAction(1);
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc2 = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Discard a card to get +1 buy", this, false);
		int index2 = sc2.getSelectedIndex();
		if(index2 < 0) return;
		getPlayer().deck.discardCard(index2);
		getPlayer().addBuy();
	}

}
