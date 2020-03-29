package cards.darkAges;

import cards.Card;
import cards.defaults.Curse;
import cards.extra.Hovel;
import cards.extra.Necropolis;
import cards.extra.OvergrownEstate;
import cards.extra.Ruins;

public class Vagrant extends Card {

	private static final long serialVersionUID = 187L;

	public Vagrant() {
		super("Vagrant", "Action", "Dark Ages", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		Card c = getPlayer().deck.getDrawCard();
		if(c == null) return;
		if(c instanceof Curse || c.isVictory() || c instanceof Ruins || c instanceof Hovel 
				|| c instanceof OvergrownEstate || c instanceof Necropolis) {
			getPlayer().deck.hand.add(c);
		}
		else {
			getPlayer().deck.topOfDeck(c);
		}
	}

}
