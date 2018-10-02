package cards.base;
import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;


public class Militia extends Card {

	private static final long serialVersionUID = 13L;

	public Militia() {
		super("Militia", "Action-Attack", "Base", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		for(Player p : getGame().getAttackedPlayers()) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, p.deck.hand.size() - 3, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex++);
			}
		}
	}
	
}
