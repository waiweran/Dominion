package cards.darkAges;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Altar extends Card {

	private static final long serialVersionUID = 154L;

	public Altar() {
		super("Altar", "Action", "Dark Ages", 6);
	}

	@Override
	public void performAction() {
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a card", this, true);
		getGame().board.trashCard(getPlayer().deck.hand.remove(sc.getSelectedIndex()));
		
		SupplySelector sd = new SupplySelector(getGame(), getName(),
				"Gain a card costing up to 5", 0, 5);
		getPlayer().deck.gain(sd.getGainedCard());
	}

}
