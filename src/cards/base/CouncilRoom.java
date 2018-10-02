package cards.base;
import cards.Card;
import gameBase.Player;


public class CouncilRoom extends Card {

	private static final long serialVersionUID = 6L;

	public CouncilRoom() {
		super("Council Room", "Action", "Base", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addBuy();
		for(Player p : getGame().getOtherPlayers()) {
			p.deck.deal();
		}
	}

}
