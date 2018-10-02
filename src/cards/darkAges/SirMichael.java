package cards.darkAges;

import gameBase.Player;
import selectors.MultiCardSelector;

public class SirMichael extends Knight {

	private static final long serialVersionUID = -31L;

	public SirMichael() {
		super("Sir Michael", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
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
