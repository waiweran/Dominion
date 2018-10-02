package cards.seaside;

import cards.Card;
import selectors.SupplySelector;

public class Embargo extends Card {

	private static final long serialVersionUID = 56L;

	public Embargo() {
		super("Embargo", "Action", "Seaside", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		getGame().board.trashCard(this);
		getPlayer().deck.play.remove(this);
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Select a supply pile to embargo", -1, 20);
		sd.setPotion(true);
		sd.getSelectedSupply().embargo();
	}
	
}
