package cards.hinterlands;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Mandarin extends Card {

	private static final long serialVersionUID = 142L;

	public Mandarin() {
		super("Mandarin", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(3);
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Put a card on top of your deck", this, true);
		getPlayer().deck.putBack(sc.getSelectedIndex());

	}
	
	@Override
	public void gainAction() {
		ArrayList<Card> revealed = new ArrayList<Card>();
		for(int i = getPlayer().deck.play.size() - 1; i >= 0; i--) {
			if(getPlayer().deck.play.get(i).isTreasure()) {
				revealed.add(getPlayer().deck.play.remove(i));
			}
		}
		while(revealed.size() > 0) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), revealed,
					"Click a card to put it back on your deck", this, true);
			getPlayer().deck.topOfDeck(revealed.remove(sc.getSelectedIndex()));
		}	

	}

}
