package cards.seaside;

import java.util.ArrayList;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;

public class NativeVillage extends Card {

	private static final long serialVersionUID = 65L;

	public NativeVillage() {
		super("Native Village", "Action", "Seaside", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		if(!getPlayer().deck.reserve.containsKey(this)) {
			getPlayer().deck.reserve.put(this, new ArrayList<Card>());
		}
		if(0 == new Selector(getGame()).showCardListDialog(this, "Native Village Mat", 
				getPlayer().deck.reserve.get(this),
				"Add Mat to Hand", "Add Card To Mat")) {
			getPlayer().deck.hand.addAll(getPlayer().deck.reserve.get(this));
			getPlayer().deck.reserve.get(this).clear();
		}
		else if(getPlayer().deck.hand.size() > 0) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
					"Place a card on the Native Village Mat", this, true);
			getPlayer().deck.reserve.get(this).add(
					getPlayer().deck.hand.remove(sc.getSelectedIndex()));
		}
	}

}
