package cards.adventures;
import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Ratcatcher extends Card {

	private static final long serialVersionUID = 223L;

	public Ratcatcher() {
		super("Ratcatcher", "Action-Reserve", "Adventures", 2);
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
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand, 
					"Trash a Card from your Hand", this, true);
			Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
			getGame().board.trashCard(c);
		}
		else {
			new Selector(getGame()).showTavernError(getName() + " cannot be called now");
		}
	}

}
