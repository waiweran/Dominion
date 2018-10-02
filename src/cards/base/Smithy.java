package cards.base;
import cards.Card;


public class Smithy extends Card {

	private static final long serialVersionUID = 19L;

	public Smithy() {
		super("Smithy", "Action", "Base", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
	}

}
