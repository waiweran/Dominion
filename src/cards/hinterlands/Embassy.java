package cards.hinterlands;

import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;

public class Embassy extends Card {

	private static final long serialVersionUID = 134L;

	public Embassy() {
		super("Embassy", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select 3 cards to discard", this, 3, true);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}
	}
	
	@Override
	public void gainAction() {
		for(Player p : getGame().getOtherPlayers()) {
			p.deck.gain(getGame().board.getSilver().takeCard());
		}
	}

}
