package cards.cornucopia;

import java.util.ArrayList;

import cards.Card;
import cards.seaside.Lookout;
import gameBase.Player;
import gameBase.Supply;
import selectors.MultiCardSelector;

public class YoungWitch extends Card {

	private static final long serialVersionUID = 127L;

	public YoungWitch() {
		super("Young Witch", "Action-Attack", "Cornucopia", 4);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		if(getGame().setup.hasBane()) {
			out.add(new Supply(getGame().setup.getBane(), 10));
		}
		else {
			out.add(new Supply(new Lookout(), 10));
		}
		return out;
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select 2 cards to discard", this, 2, true);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}
		for(Player p : getGame().getAttackedPlayers()) {
			if(!p.deck.hand.contains(getGame().setup.getBane())) {
				p.deck.gain(getGame().board.getCurse().takeCard());
			}
		}
	}

}
