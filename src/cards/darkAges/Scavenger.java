package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;

public class Scavenger extends Card {

	private static final long serialVersionUID = 183L;

	public Scavenger() {
		super("Scavenger", "Action", "Dark Ages", 4);
	}
	
	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		if(1 == new Selector(getGame()).showQuestionDialog(this, "Put your deck into your discard?", 
				"Don't Discard Deck", "Discard Deck")) {
			getPlayer().deck.discard.addAll(getPlayer().deck.draw);
			getPlayer().deck.draw.clear();
		}
		ArrayList<Card> discardPossibilities = new ArrayList<Card>();
		for(Card c : getPlayer().deck.discard) {
			if(!discardPossibilities.contains(c)) {
				discardPossibilities.add(c);
			}
		}
		if(!discardPossibilities.isEmpty()) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), discardPossibilities, 
					"Put a card from your discard on top of your deck", this, true);
			Card select = discardPossibilities.get(sc.getSelectedIndex());
			getPlayer().deck.topOfDeck(select);
			getPlayer().deck.discard.remove(select);
		}


	}

}
