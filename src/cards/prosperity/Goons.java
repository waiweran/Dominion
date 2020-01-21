package cards.prosperity;

import java.util.ArrayList;

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

		ArrayList<MultiCardSelector> selectors = new ArrayList<>();
		for(Player p : getGame().getAttackedPlayers()) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, p.deck.hand.size() - 3, true);
			sd.show();
			selectors.add(sd);
		}
		for(int selnum = 0; selnum < selectors.size(); selnum++) {
			Player p = getGame().getAttackedPlayers().get(selnum);
			MultiCardSelector sd = selectors.get(selnum);
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
