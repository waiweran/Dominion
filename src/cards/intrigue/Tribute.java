package cards.intrigue;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import gameBase.Supply;
import selectors.Selector;

public class Tribute extends Card {

	private static final long serialVersionUID = 49L;

	public Tribute() {
		super("Tribute", "Action", "Intrigue", 5);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		return null;
	}

	@Override
	public void performAction() {
		List<Card> cards = getGame().getLeftPlayer().deck.getDrawCards(2);
		getGame().getLeftPlayer().deck.discardCards(cards);

		new Selector(getGame()).showCardListDialog(this, getGame().getLeftPlayer().getPlayerName() + "Revealed", 
				cards, "Ok");
		try {
			if(cards.get(0).equals(cards.get(1))) {
				cards.remove(0);
			}
		} catch(NullPointerException e) {}
		for(Card c : cards) {
			if(c.isAction()) {
				getPlayer().addAction(2);
			}
			else if(c.isTreasure()) {
				getPlayer().addTreasure(2);
			}
			else if(c.isVictory()) {
				getPlayer().deck.deal();
				getPlayer().deck.deal();
			}
		}

	}

}
