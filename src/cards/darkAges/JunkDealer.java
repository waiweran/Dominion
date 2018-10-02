package cards.darkAges;

import cards.Card;
import selectors.SingleCardSelector;

public class JunkDealer extends Card {

	private static final long serialVersionUID = 171L;

	public JunkDealer() {
		super("Junk Dealer", "Action", "Dark Ages", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
		
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a card", this, true);
		getGame().board.trashCard(getPlayer().deck.hand.remove(sc.getSelectedIndex()));
	}

}
