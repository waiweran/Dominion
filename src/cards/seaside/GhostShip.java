package cards.seaside;

import java.util.ArrayList;

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
		
		ArrayList<MultiCardSelector> selectors = new ArrayList<>();
		for(Player p : getGame().getAttackedPlayers()) {
			if(p.deck.hand.size() >= 4) {
				MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
						"Put " + (p.deck.hand.size() - 3) + " cards back on your deck", this,
						p.deck.hand.size() - 3, true);
				sd.show();
				selectors.add(sd);
			}
		}
		for(int selnum = 0; selnum < selectors.size(); selnum++) {
			Player p = getGame().getAttackedPlayers().get(selnum);
			MultiCardSelector sd = selectors.get(selnum);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.topOfDeck(p.deck.hand.remove(i - backIndex++));
			}
		}
	}
	
}
