package cards.adventures;
import cards.Card;
import selectors.Selector;


public class Guide extends Card {

	private static final long serialVersionUID = 212L;

	public Guide() {
		super("Guide", "Action-Reserve", "Adventures", 3);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
		if (getGame().gamePhase == 0) {
			getPlayer().deck.callFromTavern(this);
			getPlayer().deck.discardCards(getPlayer().deck.hand);
			getPlayer().deck.hand.clear();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
		}
		else {
			new Selector(getGame()).showTavernError(getName() + " cannot be called now");
		}
	}

}
