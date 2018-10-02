package cards.seaside;
import cards.Card;
import gameBase.Player;


public class SeaHag extends Card {

	private static final long serialVersionUID = 71L;

	public SeaHag() {
		super("Sea Hag", "Action-Attack", "Seaside", 4);
	}

	@Override
	public void performAction() {
		for(Player p : getGame().getAttackedPlayers()) {
			getGame().board.trashCard(p.deck.getDrawCard());
			p.deck.gainTopDeck(getGame().board.getCurse().takeCard());
		}
	}
	
}
