package cards.base;
import java.util.ArrayList;

import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;


public class Militia extends Card {

	private static final long serialVersionUID = 13L;

	public Militia() {
		super("Militia", "Action-Attack", "Base", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		
		ArrayList<MultiCardSelector> selectors = new ArrayList<>();
		for(Player p : getGame().getAttackedPlayers()) {
			int discardNum = p.deck.hand.size() - 3;
			if(discardNum < 0) discardNum = 0;
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, discardNum, true);
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
