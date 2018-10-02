package cards.prosperity;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Mint extends Card {

	private static final long serialVersionUID = 102L;

	public Mint() {
		super("Mint", "Action", "Prosperity", 5);
	}

	@Override
	public void performAction() {
		ArrayList<Integer> treasures = new ArrayList<Integer>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			if(getPlayer().deck.hand.get(i).isTreasure()) {
				treasures.add(i);
			}
		}
		if(treasures.size() < 1) {
			return;
		}
		ArrayList<Card> cards = new ArrayList<>();
		for(int i = 0; i < treasures.size(); i++) {
			cards.add(getPlayer().deck.hand.get(treasures.get(i)));
		}
		SingleCardSelector sc = new SingleCardSelector(getGame(), cards, 
				"Select a Treasure to Gain a Copy Of", this, true);
				
		Card selected = getPlayer().deck.hand.get(treasures.get(sc.getSelectedIndex()));
		getPlayer().deck.gain(getGame().board.findSupply(selected).takeCard());
	}

	@Override
	public void gainAction() {
		if(getPlayer().buying) {
			for(int i = getPlayer().deck.play.size() - 1; i >= 0; i--) {
				if(getPlayer().deck.play.get(i).isTreasure()) {
					getGame().board.trashCard(getPlayer().deck.play.remove(i));
				}
			}
		}
	}

}
