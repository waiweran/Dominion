package cards.prosperity;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class KingsCourt extends Card {

	private static final long serialVersionUID = 100L;

	public KingsCourt() {
		super("King's Court", "Action", "Prosperity", 7);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		ArrayList<Card> actions = new ArrayList<Card>();
		ArrayList<Integer> actionIndexes = new ArrayList<Integer>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			Card c = getPlayer().deck.hand.get(i);
			if(c.isAction()) {
				actions.add(c);
				actionIndexes.add(new Integer(i));
			}
		}
		if(actions.size() < 1) {
			return;
		}
		SingleCardSelector sc = new SingleCardSelector(getGame(), actions,
				"Play an action card three times", this, true);
		int index = sc.getSelectedIndex();
		Card c = getPlayer().deck.hand.get(index);
		getPlayer().playCard(index);
		
		try {
			c.passGame(getGame());
			c.performAction();
			c.performAction();
		} catch(Exception e) {
			System.err.println(c.getName() + " may behave incorrectly with the King's Court");
		}
	}
	
}
