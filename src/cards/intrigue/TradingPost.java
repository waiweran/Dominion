package cards.intrigue;
import cards.Card;
import selectors.MultiCardSelector;


public class TradingPost extends Card {

	private static final long serialVersionUID = 48L;

	public TradingPost() {
		super("Trading Post", "Action", "Intrigue", 5);
	}

	@Override
	public void performAction() {
		int numTrash = 2;
		if(getPlayer().deck.hand.size() < 2) {
			numTrash = getPlayer().deck.hand.size();
		}
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select 2 cards to trash", this, numTrash, true);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getGame().board.trashCard(getPlayer().deck.hand.remove(i - backIndex));
			backIndex++;
		}
		if(numTrash == 2) {
			getPlayer().deck.gain(getGame().board.getSilver().takeCard(), 2);
		}
	}
	
}
