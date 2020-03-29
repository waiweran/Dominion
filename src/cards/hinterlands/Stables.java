package cards.hinterlands;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Stables extends Card {

	private static final long serialVersionUID = 151L;

	public Stables() {
		super("Stables", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		ArrayList<Card> available = new ArrayList<Card>();
		for(Card c : getPlayer().deck.hand) {
			if(c.isTreasure()) {
				available.add(c);
			}
		}
		if(available.size() == 0) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), available,
				"Discard a Treasure", this, false);
		try {
			Card choice = available.get(sc.getSelectedIndex());
			for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
				if(getPlayer().deck.hand.get(i) == choice) {
					getPlayer().deck.discardCard(i);
				}
			}
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().addAction(1);
		}
		catch(Exception e) {};
	}
	
}
