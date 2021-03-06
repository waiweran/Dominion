package cards.seaside;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Island extends Card {

	private static final long serialVersionUID = 61L;

	public Island() {
		super("Island", "Action-Victory", "Seaside", 4, 2, 0);
	}

	@Override
	public void performAction() {
		for(int i = 0; i < getPlayer().deck.play.size(); i++) {
			if(getPlayer().deck.play.get(i) == this) {
				getPlayer().deck.play.remove(i);
			}
		}
		if(!getPlayer().deck.reserve.containsKey(this)) {
			getPlayer().deck.reserve.put(this, new ArrayList<Card>());
		}
		getPlayer().deck.reserve.get(this).add(this);	
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Place a card aside with the island", this, true);

		getPlayer().deck.reserve.get(this).add(
				getPlayer().deck.hand.remove(sc.getSelectedIndex()));
	}
	
}
