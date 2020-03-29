package cards.cornucopia;

import cards.Card;
import selectors.MultiCardSelector;
import selectors.Selector;

public class HorseTraders extends Card {

	private static final long serialVersionUID = 121L;

	public HorseTraders() {
		super("Horse Traders", "Action-Reaction", "Cornucopia", 4);
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.hand.size() >= 2) {
			getPlayer().addBuy();
			getPlayer().addTreasure(3);
			MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
					"Discard 2 Cards", this, 2, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				getPlayer().deck.discardCard(i - backIndex++);
			}
		}
	}

	@Override
	public void reactAttack() {
		if(new Selector(getGame()).checkReact(getPlayer(), this)) {
			for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
				if(getPlayer().deck.hand.get(i) == this) {
					getPlayer().deck.duration.add(getPlayer().deck.hand.remove(i));
				}
			}
		}
	}

	@Override
	public void durationAction() {
		for(int i = 0; i < getPlayer().deck.duration.size(); i++) {
			if(getPlayer().deck.duration.get(i) == this) {
				getPlayer().deck.hand.add(getPlayer().deck.duration.remove(i));
			}
		}
		getPlayer().deck.deal();
	}

}
