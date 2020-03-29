package cards.darkAges;

import cards.Card;
import cards.defaults.Duchy;
import selectors.MultiCardSelector;
import selectors.Selector;
import selectors.SingleCardSelector;

public class Count extends Card {

	private static final long serialVersionUID = 160L;

	public Count() {
		super("Count", "Action", "Dark Ages", 5);
	}

	@Override
	public void performAction() {
		int choice = new Selector(getGame()).showQuestionDialog(this, "",
				"Discard 2 Cards", "Put a Card on Top of Your Deck", "Gain a Copper");
		if(choice == 0) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
					"Please select 2 cards to discard", this, 2, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				getPlayer().deck.discardCard(i - backIndex);
				backIndex++;
			}
		}
		else if(choice == 1) {
			if(!getPlayer().deck.hand.isEmpty()) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
						"Put a card on top of your deck", this, true);
				getPlayer().deck.putBack(sc.getSelectedIndex());
			}
		}
		else {
			getPlayer().deck.gain(getGame().board.getCopper().takeCard());
		}
		
		choice = new Selector(getGame()).showQuestionDialog(this, "", 
				"+ 3 Treasure", "Trash Your Hand", "Gain a Duchy");
		if(choice == 0) {
			getPlayer().addTreasure(3);
		}
		else if(choice == 1) {
			for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
				getGame().board.trashCard(getPlayer().deck.hand.remove(i));
			}
		}
		else {
			getPlayer().deck.gain(getGame().board.findSupply(new Duchy()).takeCard());
		}

	}

}
