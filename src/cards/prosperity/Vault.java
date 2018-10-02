package cards.prosperity;
import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;


public class Vault extends Card {

	private static final long serialVersionUID = 111L;

	public Vault() {
		super("Vault", "Action", "Prosperity", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		MultiCardSelector sd1 = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select any number of cards to discard", this, 
				getPlayer().deck.hand.size(), false);
		int backIndex = 0;
		for(int i : sd1.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex++);
		}
		getPlayer().addTreasure(backIndex);
		
		for(Player p : getGame().getOtherPlayers()) {
			MultiCardSelector sd2 = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard up to 2 cards", this, p.deck.hand.size() - 2, false);
			int backIndex2 = 0;
			for(int i : sd2.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex2++);
			}
			if(backIndex2 >= 2) {
				p.deck.deal();
			}
		}


	}
	
}
