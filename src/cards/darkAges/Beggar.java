package cards.darkAges;

import cards.Card;
import selectors.Selector;

public class Beggar extends Card {

	private static final long serialVersionUID = 158L;

	public Beggar() {
		super("Beggar", "Action-Reaction", "Dark Ages", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.gain(getGame().board.getCopper().takeCard(), 1);
		getPlayer().deck.deal();
		getPlayer().deck.gain(getGame().board.getCopper().takeCard(), 1);
		getPlayer().deck.deal();
		getPlayer().deck.gain(getGame().board.getCopper().takeCard(), 1);
		getPlayer().deck.deal();
	}

	@Override
	public void reactAttack() {
		if(new Selector(getGame()).checkReact(getPlayer(), this)) {
			for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
				if(getPlayer().deck.hand.get(i) == this) {
					getPlayer().deck.discardCard(i);
				}
			}
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
			getPlayer().deck.gain(getGame().board.getSilver().takeCard(), 1);
		}
	}

}
