package cards.seaside;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Smugglers extends Card {

	private static final long serialVersionUID = 72L;

	public Smugglers() {
		super("Smugglers", "Action", "Seaside", 3);
	}

	@Override
	public void performAction() {
		ArrayList<Card> cards = new ArrayList<Card>();
		for(Card c : getGame().getLeftPlayer().deck.gained) {
			if(c.getCost() <= 6 && !c.costsPotion()) {
				cards.add(c);
			}
		}
		if(cards.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), cards,
				"Select a card to gain", this, true);
		getPlayer().deck.gain(getGame().board.findSupply(
				cards.get(sc.getSelectedIndex())).takeCard());
	}
	
}
