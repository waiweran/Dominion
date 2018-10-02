package cards.darkAges;

import java.util.List;

import cards.Card;
import selectors.Selector;
import selectors.SupplySelector;

public class Catacombs extends Card {

	private static final long serialVersionUID = 159L;

	public Catacombs() {
		super("Catacombs", "Action", "Dark Ages", 5);
	}

	@Override
	public void performAction() {
		List<Card> cards = getPlayer().deck.getDrawCards(3);
		if( 1 == new Selector(getGame()).showCardListDialog(this, "", cards, 
				"Add Cards to Hand", "Discard and Draw 3 New Cards")) {
			getPlayer().deck.discardCards(cards);
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
		}
		else {
			getPlayer().deck.hand.addAll(cards);
		}
	}

	@Override
	public void trashAction() {
		SupplySelector gain = new SupplySelector(getGame(), getName(), 
				"Select a card to gain", 0, getCost() - 1);
		getPlayer().deck.gain(gain.getGainedCard());
	}

}
