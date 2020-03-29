package cards.hinterlands;

import cards.Card;

public class Crossroads extends Card {

	private static final long serialVersionUID = 131L;

	public Crossroads() {
		super("Crossroads", "Action", "Hinterlands", 2);
	}
	
	@Override
	public void performAction() {
		int numCrossroads = 0;
		int numVictory = 0;
		for(Card c : getPlayer().deck.hand) {
			if(c.isVictory()) {
				numVictory++;
			}
		}
		for(Card c : getPlayer().deck.play) {
			if(c instanceof Crossroads) {
				numCrossroads++;
			}
		}
		for(int i = 0; i < numVictory; i++) {
			getPlayer().deck.deal();
		}
		if(numCrossroads < 2) {
			getPlayer().addAction(3);
		}
	}
	
}
