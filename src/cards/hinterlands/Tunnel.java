package cards.hinterlands;

import cards.Card;
import selectors.Selector;

public class Tunnel extends Card {

	private static final long serialVersionUID = 153L;

	public Tunnel() {
		super("Tunnel", "Victory-Reaction", "Hinterlands", 3, 2, 0);
	}
	
	@Override
	public void discardAction() {
		if(new Selector(getGame()).checkReact(getPlayer(), this)) {
			getPlayer().deck.gain(getGame().board.getGold().takeCard());
		}
	}

}
