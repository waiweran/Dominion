package cards.seaside;

import cards.Card;
import cards.defaults.Province;

public class Explorer extends Card {

	private static final long serialVersionUID = 57L;

	public Explorer() {
		super("Explorer", "Action", "Seaside", 5);
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.hand.contains(new Province())) {
			getPlayer().deck.gain(getGame().board.getGold().takeCard());
		}
		else {
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());			
		}
	}
	
}
