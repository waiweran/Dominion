package cards.adventures;

import cards.Card;
import gameBase.Player;

public class Relic extends Card {

	private static final long serialVersionUID = 225L;

	public Relic() {
		super("Relic", "Treasure-Attack", "Adventures", 5, 0, 2);
	}

	@Override
	public void performAction() {
		for(Player p : getGame().getAttackedPlayers()) {
			p.deck.minusOneCard = true;
		}
	}
	
}
