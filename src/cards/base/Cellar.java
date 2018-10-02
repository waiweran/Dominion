package cards.base;
import cards.Card;
import selectors.MultiCardSelector;


public class Cellar extends Card {

	private static final long serialVersionUID = 3L;

	public Cellar() {
		super("Cellar", "Action", "Base", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please Choose Cards to Discard", this, getPlayer().deck.hand.size(), false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			getPlayer().deck.deal();
			backIndex++;
		}
	}

}
