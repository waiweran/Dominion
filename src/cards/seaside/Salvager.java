package cards.seaside;

import cards.Card;
import selectors.SingleCardSelector;

public class Salvager extends Card {

	private static final long serialVersionUID = 70L;

	public Salvager() {
		super("Salvager", "Action", "Seaside", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a Card", this, true);
		int index = sc.getSelectedIndex();
		getPlayer().addTreasure((getPlayer().deck.hand.get(index)).getCost());
		if((getPlayer().deck.hand.get(index)).costsPotion()) getPlayer().potion++;
		getGame().board.trashCard(getPlayer().deck.hand.remove(index));
	}
	
}
