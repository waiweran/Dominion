package cards.seaside;

import cards.Card;

public class TreasureMap extends Card {

	private static final long serialVersionUID = 74L;

	public TreasureMap() {
		super("Treasure Map", "Action", "Seaside", 4);
	}

	@Override
	public void performAction() {
		int secondIndex = getPlayer().deck.hand.indexOf(new TreasureMap());
		if(secondIndex >= 0 && secondIndex < getPlayer().deck.hand.size()) {
			getGame().board.trashCard(getPlayer().deck.hand.remove(secondIndex));
			getGame().board.trashCard(this);
			getPlayer().deck.play.remove(this);
			getPlayer().deck.gain(getGame().board.getGold().takeCard(), 1);
			getPlayer().deck.gain(getGame().board.getGold().takeCard(), 1);
			getPlayer().deck.gain(getGame().board.getGold().takeCard(), 1);
			getPlayer().deck.gain(getGame().board.getGold().takeCard(), 1);
		}
	}
	
}
