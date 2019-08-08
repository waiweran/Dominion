package cards.cornucopia;

import cards.Card;

public class HuntingParty extends Card {

	private static final long serialVersionUID = 122L;

	public HuntingParty() {
		super("Hunting Party", "Action", "Cornucopia", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		for(int i = 0; i < getPlayer().deck.size(); i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(!getPlayer().deck.hand.contains(c)) {
				getPlayer().deck.hand.add(c);
				break;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}

}
