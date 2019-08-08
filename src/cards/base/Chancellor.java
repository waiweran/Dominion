package cards.base;
import cards.Card;
import selectors.Selector;


public class Chancellor extends Card {

	private static final long serialVersionUID = 4L;

	public Chancellor() {
		super("Chancellor", "Action", "Base", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		if(1 == new Selector(getGame()).showQuestionDialog(this, "Put your deck into your discard?", 
				"Don't Discard Deck", "Discard Deck")) {
			getPlayer().deck.discardAllDraw();
		}
	}

}
