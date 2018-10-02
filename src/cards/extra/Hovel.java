package cards.extra;
import cards.Card;
import selectors.Selector;


public class Hovel extends Card {

	private static final long serialVersionUID = -14L;

	public Hovel() {
		super("Hovel", "Reaction-Shelter", "Extras/Shelter", 1);
	}

	@Override
	public void reactGain(Card c) {
		if(getPlayer().buying && c.isVictory() &&
				new Selector(getGame()).checkReact(getPlayer(), this)) {
			getPlayer().deck.hand.remove(this);
			getGame().board.trashCard(this);
		}
	}

}
