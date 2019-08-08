package cards.adventures;

import cards.Card;

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
		if(c.isTreasure()) {
			getPlayer().deck.deal();
		}
		else if(c.isAction() || c.isVictory()) {
			getPlayer().deck.gain(getGame().board.findSupply(this).takeCard());
		}
	}

}
