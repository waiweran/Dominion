package cards.hinterlands;

import java.util.ArrayList;
import java.util.Collections;

import cards.Card;
import selectors.MultiCardSelector;

public class Inn extends Card {

	private static final long serialVersionUID = 140L;

	public Inn() {
		super("Inn", "Action", "Hinterlands", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addAction(2);
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Discard 2 Cards", this, 2, true);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}

	}

	@Override
	public void gainAction() {
		ArrayList<Card> actions = new ArrayList<Card>(); //TODO this isn't in the discard yet and it needs to be
		for(int i = getPlayer().deck.discard.size() - 1; i >= 0; i--) {
			if(getPlayer().deck.discard.get(i).isAction()) {
				actions.add(getPlayer().deck.discard.remove(i));
			}
		}
		
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer(), actions, 
				"Select Actions to Shuffle into your Deck", this, actions.size(), false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.draw.add(actions.remove(i - backIndex));
			backIndex++;
		}
		Collections.shuffle(getPlayer().deck.draw);
		getPlayer().deck.discardCards(actions);
	}

}
