package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Procession extends Card {

	private static final long serialVersionUID = 178L;

	public Procession() {
		super("Procession", "Action", "Dark Ages", 4);
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
				"Play an Action Twice", this, true);
		int index = actionIndexes.get(sc.getSelectedIndex());
		Card c = getPlayer().deck.hand.get(index);
		getPlayer().playCard(index);
		try {
			c.performAction();
		} catch(Exception e) {
			System.err.println(c.getName() + " may behave incorrectly with Procession");
		}
		
		SupplySelector gain = new SupplySelector(getGame(), getName(),
				"Gain a card costing up to " + (c.getCost() + 1), 0, c.getCost() + 1);
		gain.setPotion(c.costsPotion());
		getPlayer().deck.gain(gain.getGainedCard());
		getGame().board.trashCard(c);

	}
	
}
