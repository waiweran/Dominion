package cards.cornucopia;

import java.util.ArrayList;

import cards.Card;

public class Harvest extends Card {

	private static final long serialVersionUID = 119L;

	public Harvest() {
		super("Harvest", "Action", "Cornucopia", 5);
	}

	@Override
	public void performAction() {
		ArrayList<Card> revealed = new ArrayList<Card>();
		Card c = getPlayer().deck.getDrawCard();
		if(!revealed.contains(c)) {
			revealed.add(c);
		}
		getPlayer().deck.discardCard(c);
		c = getPlayer().deck.getDrawCard();
		if(!revealed.contains(c)) {
			revealed.add(c);
		}
		getPlayer().deck.discardCard(c);
		c = getPlayer().deck.getDrawCard();
		if(!revealed.contains(c)) {
			revealed.add(c);
		}
		getPlayer().deck.discardCard(c);
		c = getPlayer().deck.getDrawCard();
		if(!revealed.contains(c)) {
			revealed.add(c);
		}
		getPlayer().deck.discardCard(c);
		getPlayer().addTreasure(revealed.size());
	}

}
