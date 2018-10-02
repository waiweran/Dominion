package cards.adventures;
import java.util.ArrayList;

import cards.Card;
import selectors.MultiCardSelector;


public class Gear extends Card {

	private static final long serialVersionUID = 210L;

	public Gear() {
		super("Gear", "Action-Duration", "Adventures", 3);
		
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Choose up to two cards to set aside", this, 2, false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			if(!getPlayer().deck.reserve.containsKey(this)) {
				getPlayer().deck.reserve.put(this, new ArrayList<Card>());
			}
			getPlayer().deck.reserve.get(this).add(
					getPlayer().deck.hand.remove(i - backIndex++));
		}
	}
	
	@Override
	public void durationAction() {
		getPlayer().deck.hand.addAll(
				getPlayer().deck.reserve.get(this));
		getPlayer().deck.reserve.get(this).clear();
	}

}
