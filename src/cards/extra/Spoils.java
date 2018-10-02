package cards.extra;

import cards.Card;

public class Spoils extends Card {

	private static final long serialVersionUID = -10L;

	public Spoils() {
		super("Spoils", "Treasure", "Extras", 0, 0, 3);
	}
	
	@Override
	public boolean canBeGained() {
		return false;
	}
	
	@Override
	public void cleanupAction() {
		if(getGame() == null) throw new RuntimeException("Never gave Spoils getGame() to game");
		getGame().board.findSupply(this).putOneBack(this);
		getPlayer().deck.play.remove(this);
	}

}
