package cards.base;
import cards.Card;
import selectors.MultiCardSelector;


public class Chapel extends Card {

	private static final long serialVersionUID = 5L;

	public Chapel() {
		super("Chapel", "Action", "Base", 2);
	}

	@Override
	public void performAction() {
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select up to 4 cards to trash", this, 4, false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getGame().board.trashCard(getPlayer().deck.hand.remove(i - backIndex));
			backIndex++;
		}
	}

}
