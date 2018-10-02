package cards.adventures;
import cards.Card;
import gameBase.Player;


public class BridgeTroll extends Card {

	private static final long serialVersionUID = 204L;

	public BridgeTroll() {
		super("Bridge Troll", "Action-Attack-Duration", "Adventures", 5);

	}

	@Override
	public void performAction() {
		for(Player p : getGame().getAttackedPlayers()) {
			p.addTreasure(-1 - p.getTreasure());
		}
		getPlayer().addBuy();
		getPlayer().bridge++;
	}

	@Override
	public void durationAction() {
		getPlayer().addBuy();
		getPlayer().bridge++;
	}

}
