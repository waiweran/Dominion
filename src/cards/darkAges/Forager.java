package cards.darkAges;

import java.util.HashSet;

import cards.Card;
import selectors.SingleCardSelector;

public class Forager extends Card {

	private static final long serialVersionUID = 166L;

	public Forager() {
		super("Forager", "Action", "Dark Ages", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addBuy();
		
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a card", this, true);
		getGame().board.trashCard(getPlayer().deck.hand.remove(sc.getSelectedIndex()));
		
		HashSet<Card> treasures = new HashSet<Card>();
		for(Card c : getGame().board.trash) {
			if(c.isTreasure()) {
				treasures.add(c);
			}
		}
		getPlayer().addTreasure(treasures.size());
	}

}
