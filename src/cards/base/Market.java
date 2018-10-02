package cards.base;
import cards.Card;


public class Market extends Card {

	private static final long serialVersionUID = 12L;

	public Market() {
		super("Market", "Action", "Base", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
		getPlayer().addBuy();
		getPlayer().deck.deal();
	}

}
