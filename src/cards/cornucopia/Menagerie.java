package cards.cornucopia;

import cards.Card;

public class Menagerie extends Card {

	private static final long serialVersionUID = 124L;

	public Menagerie() {
		super("Menagerie", "Action", "Cornucopia", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		for(Card c : getPlayer().deck.hand) {
			if(getPlayer().deck.hand.indexOf(c) != getPlayer().deck.hand.lastIndexOf(c)) {
				getPlayer().deck.deal();
				return;
			}
		}
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
	}

}
