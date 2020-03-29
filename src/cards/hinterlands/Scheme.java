package cards.hinterlands;

import java.util.ArrayList;
import java.util.List;

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
		int index = sc.getSelectedIndex();
		if(index < 0) return;
		Card choice = available.get(index);
		List<Card> remList;
		if(choice.isDuration()) {
			remList = getPlayer().deck.duration;
		}
		else {
			remList = getPlayer().deck.play;
		}
		for(int i = 0; i < remList.size(); i++) {
			if(remList.get(i) == choice) {
				remList.remove(i);
			}
		}
		getPlayer().deck.topOfDeck(choice);
	}

}
