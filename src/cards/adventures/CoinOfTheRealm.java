package cards.adventures;
import cards.Card;
import selectors.Selector;


public class CoinOfTheRealm extends Card {

	private static final long serialVersionUID = 206L;

	public CoinOfTheRealm() {
		super("Coin of the Realm", "Treasure-Reserve", "Adventures", 2, 0, 1);
	}

	@Override
	public void performAction() {
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
			if (getGame().gamePhase == 1) {
				getPlayer().addAction(2);
				getPlayer().deck.callFromTavern(this);
			}
			else {
				new Selector(getGame()).showTavernError(getName() + " cannot be called now");
			}
	}

}
