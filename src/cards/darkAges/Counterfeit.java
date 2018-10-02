package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Counterfeit extends Card {

	private static final long serialVersionUID = 161L;
	
	private Card trashedThisTurn;

	public Counterfeit() {
		super("Counterfeit", "Treasure", "Dark Ages", 5, 0, 1);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		ArrayList<Card> treasures = new ArrayList<Card>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			if(getPlayer().deck.hand.get(i).isTreasure()) {
				treasures.add(getPlayer().deck.hand.get(i));
				index.add(i);
			}
		}
		if(treasures.size() < 1) {
			return;
		}
		SingleCardSelector sc = new SingleCardSelector(getGame(), treasures, 
				"Play a treasure twice, then trash", this, false);
		try {
			int selected = sc.getSelectedIndex();
			Card c = getPlayer().deck.hand.get(selected);
			getPlayer().playCard(selected);
			try {
				c.performAction();
				getPlayer().addTreasure(c.getTreasure());
			} catch(Exception e) {
				System.err.println(c.getName() + " may behave incorrectly with the Counterfeit");
			}
			getPlayer().deck.play.remove(c);
			trashedThisTurn = c;
			getGame().board.trashCard(c);
		}	
		catch(IndexOutOfBoundsException e) {}
	}

	@Override
	public void respondGain(Card c) {
		if(trashedThisTurn != null) {
			try {
				trashedThisTurn.respondGain(c);
				trashedThisTurn.respondGain(c);
			} catch(Exception e) {
				System.err.println(trashedThisTurn.getName() + " may behave incorrectly with the Counterfeit");
			}
		}
	}
	
	@Override
	public void tavernAction() {
		if(trashedThisTurn != null) {
			trashedThisTurn.tavernAction();
		}
	}

	@Override
	public void trashAction() {
		trashedThisTurn = null;
	}

	@Override
	public void cleanupAction() {
		trashedThisTurn = null;
	}

}
