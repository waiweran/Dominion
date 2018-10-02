package cards.hinterlands;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Scheme extends Card {

	private static final long serialVersionUID = 148L;
	
	private boolean played;

	public Scheme() {
		super("Scheme", "Action", "Hinterlands", 3);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		played = false;
	}
	
	@Override
	public void cleanupAction() {
		if(played) return;
		played = true;
		ArrayList<Card> available = new ArrayList<Card>();
		for(Card c : getPlayer().deck.play) {
			if(c.isAction() && !c.isDuration()) {
				available.add(c);
			}
		}
		for(Card c : getPlayer().deck.duration) {
			if(c.isAction()) {
				available.add(c);
			}
		}
		if(available.size() == 0) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), available,
				"Choose a card to put back on top of your deck", this, false);
		try {
			Card choice = available.get(sc.getSelectedIndex());
			if(choice.isDuration()) {
				getPlayer().deck.duration.remove(choice);
			}
			else {
				getPlayer().deck.play.remove(choice);
			}
			getPlayer().deck.topOfDeck(choice);
		}
		catch(Exception e) {};
	}

}
