package cards.prosperity;

import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;


public class Goons extends Card {

	private static final long serialVersionUID = 97L;

	public Goons() {
		super("Goons", "Action-Attack", "Prosperity", 6);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		getPlayer().addBuy();
		for(Player p : getGame().getAttackedPlayers()) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, p.deck.hand.size() - 3, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex++);
			}
		}
	}
	
	@Override
	public void respondGain(Card c) {
		if(getPlayer().buying) {
			getPlayer().extraVictory++;
		}
	}
	
	
	
}
