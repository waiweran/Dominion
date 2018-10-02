package cards.darkAges;

import cards.Card;
import selectors.Selector;
import selectors.SupplySelector;

public class Squire extends Card {

	private static final long serialVersionUID = 184L;

	public Squire() {
		super("Squire", "Action", "Dark Ages", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(1);
		int bob = new Selector(getGame()).showQuestionDialog(this, "", 
				"+2 Actions", "+2 Buys", "Gain a Silver");
		if (bob == 2)	{
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		}
		else if (bob == 1)	{
			getPlayer().addBuy();
			getPlayer().addBuy();
		}
		else {			
			getPlayer().addAction(2);
		}
	}
	
	@Override
	public void trashAction() {
		SupplySelector getm = new SupplySelector(getGame(), getName(), 
				"Gain an Attack", 0, 20);
		getm.setPlayer(getPlayer());
		getm.setPotion(true);
		getm.setCardSelector(c -> c.isAttack());
		getPlayer().deck.gain(getm.getGainedCard());
	}

}
