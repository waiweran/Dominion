package cards.base;

import cards.Card;
import selectors.SupplySelector;

public class Feast extends Card {

	private static final long serialVersionUID = 7L;

	public Feast() {
		super("Feast", "Action", "Base", 4);
	}

	@Override
	public void performAction() {
		getGame().board.trashCard(this);
		getPlayer().deck.play.remove(this);
		
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Gain a card costing up to 5", 0, 5);
		getPlayer().deck.gain(sd.getGainedCard());
	}

}
