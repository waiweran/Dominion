package cards.darkAges;

import selectors.MultiCardSelector;

public class DameAnna extends Knight {

	private static final long serialVersionUID = -23L;

	public DameAnna() {
		super("Dame Anna", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select up to 2 cards to trash", this, 2, false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getGame().board.trashCard(getPlayer().deck.hand.remove(i - backIndex));
			backIndex++;
		}
	}

}
