package cards.base;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class ThroneRoom extends Card {

	private static final long serialVersionUID = 22L;

	public ThroneRoom() {
		super("Throne Room", "Action", "Base", 4);
	}

	@Override
	public void performAction() {
		ArrayList<Card> actions = new ArrayList<Card>();
		ArrayList<Integer> actionIndexes = new ArrayList<Integer>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			Card c = getPlayer().deck.hand.get(i);
			if(c.isAction()) {
				actions.add(c);
				actionIndexes.add(new Integer(i));
			}
		}
		if(actions.size() < 1) return;
		getPlayer().addAction(1);

		SingleCardSelector sc = new SingleCardSelector(getGame(), actions,
				"Play an action card twice", this, true);

		int index = actionIndexes.get(sc.getSelectedIndex());
		Card c = getPlayer().deck.hand.get(index);
		getPlayer().playCard(index);
		try {
			c.performAction();
		} catch(Exception e) {
			System.err.println(c.getName() + " may behave incorrectly with the Throne Room");
		}
	}

}
