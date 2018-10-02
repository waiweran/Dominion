package cards.cornucopia;

import cards.Card;
import gameBase.Player;
import gameBase.Supply;
import selectors.Selector;

public class Jester extends Card {

	private static final long serialVersionUID = 123L;

	public Jester() {
		super("Jester", "Action-Attack", "Cornucopia", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		for(Player p : getGame().getAttackedPlayers()) {
			Card c = p.deck.getDrawCard();
			p.deck.discardCard(c);
			if(c.isVictory()) {
				new Selector(getGame()).showCardDialog(this, "", c, "Give Them a Curse");
				p.deck.gain(getGame().board.getCurse().takeCard());
			}
			else {
				if(0 == new Selector(getGame()).showCardDialog(this, "", c,  
						"Give Me a Copy", "Give Them a Copy")) {
					Supply s = getGame().board.findSupply(c);
					if(s != null) getPlayer().deck.gain(s.takeCard());
				}
				else {
					Supply s = getGame().board.findSupply(c);
					if(s != null) p.deck.gain(s.takeCard());
				}
			}
		}
	}

}
