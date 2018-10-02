package cards.darkAges;

import cards.Card;
import cards.defaults.Duchy;
import cards.defaults.Estate;
import gameBase.Supply;
import selectors.Selector;

public class HuntingGrounds extends Card {

	private static final long serialVersionUID = 169L;

	public HuntingGrounds() {
		super("Hunting Grounds", "Action", "Dark Ages", 6);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
	}
		
	@Override
	public void trashAction() {
		if(0 == new Selector(getGame()).showQuestionDialog(this, "", "Gain 1 Duchy", "Gain 3 Estates")) {
			getPlayer().deck.gain(getGame().board.findSupply(new Duchy()).takeCard());
		}
		else {
			Supply s = getGame().board.findSupply(new Estate());
			getPlayer().deck.gain(s.takeCard());
			getPlayer().deck.gain(s.takeCard());
			getPlayer().deck.gain(s.takeCard());
		}
	}

}
