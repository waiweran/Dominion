package cards.intrigue;
import cards.Card;
import selectors.MultiCardSelector;
import selectors.Selector;


public class SecretChamber extends Card {

	private static final long serialVersionUID = 43L;

	public SecretChamber() {
		super("Secret Chamber", "Action-Reaction", "Intrigue", 2);
	}

	@Override
	public void performAction() {
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select cards to discard", this, getPlayer().deck.hand.size(), false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			getPlayer().addTreasure(1);
			backIndex++;
		}
	}

	@Override
	public void reactAttack() {
		if(new Selector(getGame()).checkReact(getPlayer(), this)) {
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
					"Please select 2 cards to put on top of your deck", this, 2, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				getPlayer().deck.topOfDeck(getPlayer().deck.hand.remove(i - backIndex));
				backIndex++;
			}
		}
	}

}
