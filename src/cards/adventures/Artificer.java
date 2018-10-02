package cards.adventures;
import cards.Card;
import selectors.MultiCardSelector;
import selectors.SupplySelector;


public class Artificer extends Card {

	private static final long serialVersionUID = 203L;

	public Artificer() {
		super("Artificer", "Action", "Adventures", 5);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
		
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please Choose Cards to Discard", this, getPlayer().deck.hand.size(), false);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}
		SupplySelector ss = new SupplySelector(getGame(), getName(), 
				"Gain a card costing exactly " + backIndex, backIndex, backIndex);
		ss.setMust(false);
		getPlayer().deck.gain(ss.getGainedCard());
	}
	
}
