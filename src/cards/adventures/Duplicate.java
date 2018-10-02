package cards.adventures;
import cards.Card;
import selectors.Selector;


public class Duplicate extends Card {

	private static final long serialVersionUID = 209L;

	public Duplicate() {
		super("Duplicate", "Action-Reserve", "Adventures", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
		if(getPlayer().deck.gained.isEmpty()) {
			new Selector(getGame()).showTavernError("You have not gained a card");
			return;
		}
		Card c = getPlayer().deck.gained.get(
				getPlayer().deck.gained.size() - 1);
		if (c.getCost() <= 6) {
			if (1 == new Selector(getGame()).showQuestionDialog(this,
					"Gain another " + c.getName() + "?", "Cancel", "Ok")) {
				getPlayer().deck.gain(getGame().board.findSupply(c).takeCard());
				getPlayer().deck.callFromTavern(this);
			}
		}
		else {
			new Selector(getGame()).showTavernError(c.getName() + " is too expensive to duplicate");
		}
	}

}
