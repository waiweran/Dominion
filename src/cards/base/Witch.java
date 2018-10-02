package cards.base;
import cards.Card;
import gameBase.Player;


public class Witch extends Card {

	private static final long serialVersionUID = 24L;

	public Witch() {
		super("Witch","Action", "Base", 5);
		
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		for(Player p : getGame().getAttackedPlayers()) {
			p.deck.gain(getGame().board.getCurse().takeCard());
		}
	}
	
}
