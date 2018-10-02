package cards.hinterlands;

import cards.Card;

public class NomadCamp extends Card {

	private static final long serialVersionUID = 145L;

	public NomadCamp() {
		super("Nomad Camp", "Action", "Hinterlands", 4);
	}
	
	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(2);
	}
	
	@Override
	public void gainAction() {
		if(getPlayer().deck.getDiscardTop().equals(this)) { //TODO card isn't there until after this runs
			getPlayer().deck.topOfDeck(getPlayer().deck.discard.remove(getPlayer().deck.discard.size() - 1));
		}
	}

	@Override
	public int gameEndAction() {
		return 0;
	}

}
