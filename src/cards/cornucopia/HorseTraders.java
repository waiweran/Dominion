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
			getPlayer().deck.hand.remove(this);
			getPlayer().deck.duration.add(this);
		}
	}

	@Override
	public void durationAction() {
		getPlayer().deck.duration.remove(this);
		getPlayer().deck.hand.add(this);
		getPlayer().deck.deal();
	}

}
