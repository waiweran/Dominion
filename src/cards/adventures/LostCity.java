package cards.adventures;

import cards.Card;
import gameBase.Player;

public class LostCity extends Card {

	private static final long serialVersionUID = 215L;

	public LostCity() {
		super("Lost City", "Action", "Adventures", 5);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addAction(2);
	}
	
	@Override
	public void gainAction() {
		for(Player p : getGame().getOtherPlayers()) {
			p.deck.deal();
		}
	}

}
