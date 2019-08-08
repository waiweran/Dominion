package cards.adventures;
import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;
import selectors.SupplySelector;


public class Transmogrify extends Card {

	private static final long serialVersionUID = 229L;

	public Transmogrify() {
		super("Transmogrify", "Action-Reserve", "Adventures", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
		if (getGame().gamePhase == 0) {
			getPlayer().deck.callFromTavern(this);
			SingleCardSelector sc = new SingleCardSelector(getGame(), 
					getPlayer().deck.hand, "Trash a Card from your Hand", this, true);
			Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
			SupplySelector gain = new SupplySelector(getGame(), getName(), "Gain a Card", 0, 
					c.getCost() + 1);
			gain.setPotion(c.costsPotion());
			getPlayer().deck.gain(gain.getGainedCard(), 2);
			getGame().board.trashCard(c);
		}
		else {
			new Selector(getGame()).showTavernError(getName() + " cannot be called now");
		}
	}

}
