package cards.prosperity;

import cards.Card;
import selectors.MultiCardSelector;
import selectors.SupplySelector;


public class Forge extends Card {

	private static final long serialVersionUID = 96L;

	public Forge() {
		super("Forge", "Action", "Prosperity", 7);
	}
	
	@Override
	public void performAction() {
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Select Cards to Trash", this, getPlayer().deck.hand.size(), false);
		int backIndex = 0, value = 0;
		for(int i : sd.getSelectedIndex()) {
			value += getPlayer().deck.hand.get(i - backIndex).getCost();
			getGame().board.trashCard(getPlayer().deck.hand.remove(i - backIndex));
			backIndex++;
		}
		SupplySelector gain = new SupplySelector(getGame(), getName(), 
				"Select a card to gain", value, value);
		getPlayer().deck.gain(gain.getGainedCard());
	}
	
}
