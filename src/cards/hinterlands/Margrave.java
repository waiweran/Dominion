package cards.hinterlands;

import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;

public class Margrave extends Card {

	private static final long serialVersionUID = 143L;

	public Margrave() {
		super("Margrave", "Action-Attack", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addBuy();
		for(Player p : getGame().getAttackedPlayers()) {
			p.deck.deal();
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, p.deck.hand.size() - 3, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex++);
			}
		}

	}

}
