package cards.seaside;

import cards.Card;
import selectors.Selector;

public class Treasury extends Card {

	private static final long serialVersionUID = 75L;

	public Treasury() {
		super("Treasury", "Action", "Seaside", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
	}
	
	@Override
	public void cleanupAction() {
		for(Card c : getPlayer().bought) {
			if(c.isVictory()) return;
		}
		if(0 == new Selector(getGame()).showQuestionDialog(this, "Would you like to put the Treasury on top of your deck?", 
				"Put Treasury on top of Deck", "Discard Treasury")) {
			getPlayer().deck.play.remove(this);
			getPlayer().deck.topOfDeck(this);
		}
	}

}
