package cards.adventures;

import cards.Card;
import selectors.Selector;

public class Magpie extends Card {

	private static final long serialVersionUID = 216L;

	public Magpie() {
		super("Magpie", "Action", "Adventures", 4);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		Card c = getPlayer().deck.revealDrawTop();
		Selector sel = new Selector(getGame());
		if(c.isTreasure()) {
			sel.showCardDialog(this, "Magpie revealed", c, "Put it into your hand");
			getPlayer().deck.deal();
		}
		else if(c.isAction() || c.isVictory()) {
			sel.showCardDialog(this, "Magpie revealed", c, "Gain a Magpie");
			getPlayer().deck.gain(getGame().board.findSupply(this).takeCard());
		}
		else {
			sel.showCardDialog(this, "Magpie revealed", c, "Do Nothing");
		}
	}

}
