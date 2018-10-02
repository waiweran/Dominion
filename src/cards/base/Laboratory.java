package cards.base;
import cards.Card;


public class Laboratory extends Card {

	private static final long serialVersionUID = 10L;

	public Laboratory() {
		super("Laboratory", "Action", "Base", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().addAction(1);
	}

}
