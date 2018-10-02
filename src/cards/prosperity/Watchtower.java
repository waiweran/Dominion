package cards.prosperity;

import cards.Card;
import selectors.Selector;


public class Watchtower extends Card {

	private static final long serialVersionUID = 113L;

	public Watchtower() {
		super("Watchtower", "Action-Reaction", "Prosperity", 3);
	}

	@Override
	public void performAction() {
		for(int i = getPlayer().deck.hand.size(); i < 6; i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c != null) {
				getPlayer().deck.hand.add(c);
			}
		}
	}

	@Override
	public void reactGain(Card c) {
		if(getPlayer().deck.discard.size() > 0
				&& getPlayer().deck.discard.get(
				getPlayer().deck.discard.size() - 1).equals(c)) {
			Selector sel = new Selector(getGame());
			sel.setPlayer(getPlayer());
			int selection = sel.showCardDialog(this, "Choose one", 
					c, "Do Nothing", "Trash Card", "Put on Top of Deck");
			if(selection == 1) {
				getPlayer().deck.discard.remove(getPlayer().deck.discard.size() - 1);
				getGame().board.trashCard(c);
			}
			else if(selection == 2) {
				getPlayer().deck.discard.remove(getPlayer().deck.discard.size() - 1);
				getPlayer().deck.topOfDeck(c);
			}
		}
	}

}
