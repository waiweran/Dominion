package cards.darkAges;

import cards.Card;
import selectors.Selector;

public class Ironmonger extends Card {

	private static final long serialVersionUID = 170L;

	public Ironmonger() {
		super("Ironmonger", "Action", "Dark Ages", 4);
	}
		
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		
		Card revealed = getPlayer().deck.getDrawCard();
		if(revealed == null) return;
		if(0 == new Selector(getGame()).showCardDialog(this, "", revealed, 
				"Discard", "Return to Top of Deck")) {
			getPlayer().deck.discardCard(revealed);
		}
		else {
			getPlayer().deck.topOfDeck(revealed);
		}
		
		if(revealed.isAction()) {
			getPlayer().addAction(1);
		}
		if(revealed.isTreasure()) {
			getPlayer().addTreasure(1);
		}
		if(revealed.isVictory()) {
			getPlayer().deck.deal();
		}
	}

}
