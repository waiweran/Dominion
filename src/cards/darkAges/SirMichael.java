package cards.darkAges;

import java.util.ArrayList;

import gameBase.Player;
import selectors.MultiCardSelector;

public class SirMichael extends Knight {

	private static final long serialVersionUID = -31L;

	public SirMichael() {
		super("Sir Michael", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		ArrayList<MultiCardSelector> selectors = new ArrayList<>();
		for(Player p : getGame().getAttackedPlayers()) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 3", this, p.deck.hand.size() - 3, true);
			sd.show();
			selectors.add(sd);
		}
		for(int selnum = 0; selnum < selectors.size(); selnum++) {
			Player p = getGame().getAttackedPlayers().get(selnum);
			MultiCardSelector sd = selectors.get(selnum);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex++);
			}
		}
	}

}
