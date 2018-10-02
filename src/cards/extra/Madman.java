package cards.extra;

import cards.Card;

public class Madman extends Card {

	private static final long serialVersionUID = -21L;

	public Madman() {
		super("Madman", "Action", "Extras", 0);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		getGame().board.findSupply(this).putOneBack(this);
		getPlayer().deck.play.remove(this);
		for(int i = getPlayer().deck.hand.size(); i > 0; i--) {
			getPlayer().deck.deal();
		}
	}

	@Override
	public boolean canBeGained() {
		return false;
	}

}
