package cards.extra;
import cards.Card;
import cards.defaults.Estate;
import gameBase.Player;
import selectors.MultiCardSelector;


public class Followers extends Card {

	private static final long serialVersionUID = -35L;

	public Followers() {
		super("Followers", "Action-Attack-Prize", "Extras/Prize", 0);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.gain(getGame().board.findSupply(new Estate()).takeCard());
		for(Player p : getGame().getAttackedPlayers()) {			
			p.deck.gain(getGame().board.getCurse().takeCard());	
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, p.deck.hand.size() - 3, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex++);
			}
		}
	}

}
