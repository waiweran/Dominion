package cards.base;
import cards.Card;


public class Village extends Card {

	private static final long serialVersionUID = 23L;

	public Village() {
		super("Village", "Action", "Base", 3);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
	}

}
