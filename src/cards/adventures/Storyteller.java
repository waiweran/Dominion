package cards.adventures;
import java.util.ArrayList;

import cards.Card;
import selectors.MultiCardSelector;


public class Storyteller extends Card {

	private static final long serialVersionUID = 227L;

	public Storyteller() {
		super("Storyteller", "Action", "Adventures", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
		ArrayList<Card> treasures = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			Card c = getPlayer().deck.hand.get(i);
			if(c.isTreasure()) {
				treasures.add(c);
				indices.add(i);
			}
		}
		MultiCardSelector sd = new MultiCardSelector(getGame(), treasures, 
				"Choose up to 3 treasures to play", this, 3, false);
		for(int i : sd.getSelectedIndex()) {
			getPlayer().playCard(indices.get(i));
		}
		int treasure = getPlayer().getTreasure();
		for(int i = 0; i < treasure; i++) {
			getPlayer().deck.deal();
		}
		getPlayer().addTreasure(-treasure);
	}

}
