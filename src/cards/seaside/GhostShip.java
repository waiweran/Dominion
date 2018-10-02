package cards.seaside;

import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;

public class GhostShip extends Card {

	private static final long serialVersionUID = 59L;

	public GhostShip() {
		super("Ghost Ship", "Action-Attack", "Seaside", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		for(Player p : getGame().getAttackedPlayers()) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Select cards to put back on your deck to 3 in hand", this,
					p.deck.hand.size() - 3, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.topOfDeck(p.deck.hand.remove(i - backIndex++));
			}
		}
	}
	
}
