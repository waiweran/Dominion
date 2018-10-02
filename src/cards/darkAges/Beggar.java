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
		getPlayer().deck.gainTopDeck(getGame().board.getCopper().takeCard());
		getPlayer().deck.deal();
		getPlayer().deck.gainTopDeck(getGame().board.getCopper().takeCard());
		getPlayer().deck.deal();
		getPlayer().deck.gainTopDeck(getGame().board.getCopper().takeCard());
		getPlayer().deck.deal();
	}

	@Override
	public void reactAttack() {
		if(new Selector(getGame()).checkReact(getPlayer(), this)) {
			getPlayer().deck.discardCard(this);
			getPlayer().deck.hand.remove(this);
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
			getPlayer().deck.gainTopDeck(getGame().board.getSilver().takeCard());
		}
	}

}
