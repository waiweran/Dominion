package cards.extra;
import java.util.ArrayList;

import cards.Card;
import cards.defaults.Estate;
import gameBase.Player;
import selectors.MultiCardSelector;


public class Followers extends Card {

	private static final long serialVersionUID = -35L;

	public Followers() {
		super("Followers", "Action-Attack-Prize", "Extras/Prize", 0);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.gain(getGame().board.findSupply(new Estate()).takeCard());
		
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

}
