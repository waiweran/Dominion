package cards.hinterlands;

import cards.Card;
import gameBase.Player;
import selectors.Selector;

public class IllGottenGains extends Card {

	private static final long serialVersionUID = 139L;

	public IllGottenGains() {
		super("Ill-Gotten Gains", "Treasure", "Hinterlands", 5, 0, 1);
	}

	@Override
	public void performAction() {
		if(0 == new Selector(getGame()).showQuestionDialog(this, 
				"Do you want to gain a copper into your hand?", "Yes", "No")) {
			getPlayer().deck.gain(getGame().board.getCopper().takeCard(), 1);
			getPlayer().deck.deal();
		}
	}

	@Override
	public void gainAction() {
		for(Player p : getGame().getOtherPlayers()) {
			p.deck.gain(getGame().board.getCurse().takeCard());
		}
	}

}
