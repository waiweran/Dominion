package cards.adventures;
import cards.Card;
import selectors.Selector;


public class WineMerchant extends Card {

	private static final long serialVersionUID = 231L;

	public WineMerchant() {
		super("Wine Merchant", "Action-Reserve", "Adventures", 5);
	}


	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(4);
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
		if (getPlayer().getTreasure() >= 2) {
			getPlayer().addTreasure(2);
			getPlayer().deck.callFromTavern(this);
			getPlayer().deck.play.remove(this);
			getPlayer().deck.discardCard(this);
		}
		else {
			new Selector(getGame()).showTavernError("You do not have enough money to call "
						+ getName());
		}
	}

}
