package cards.hinterlands;

import java.util.ArrayList;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;

public class SpiceMerchant extends Card {

	private static final long serialVersionUID = 150L;

	public SpiceMerchant() {
		super("Spice Merchant", "Action", "Hinterlands", 4);
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
				"Trash a Treasure", this, false);

		try {
			Card choice = available.get(sc.getSelectedIndex());
			getPlayer().deck.hand.remove(choice);
			getGame().board.trashCard(choice);
			if(0 == new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+2 Cards and +1 Action", "+2 Money and +1 Buy")) {
				getPlayer().deck.deal();
				getPlayer().deck.deal();
				getPlayer().addAction(1);
			}
			else {
				getPlayer().addTreasure(2);
				getPlayer().addBuy();
			}
		}
		catch(Exception e) {};
	}
	
}
