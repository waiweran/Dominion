package cards.seaside;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Haven extends Card {

	private static final long serialVersionUID = 60L;

	public Haven() {
		super("Haven", "Action-Duration", "Seaside", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Set a card aside", this, true);
		if(!getPlayer().deck.reserve.containsKey(this)) {
			getPlayer().deck.reserve.put(this, new ArrayList<Card>());
		}
		getPlayer().deck.reserve.get(this).add(
				getPlayer().deck.hand.remove(sc.getSelectedIndex()));
	}

	@Override
	public void durationAction() {
		getPlayer().deck.hand.addAll(
				getPlayer().deck.reserve.get(this));
		getPlayer().deck.reserve.get(this).clear();
	}

}
