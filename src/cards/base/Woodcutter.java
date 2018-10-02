package cards.base;
import cards.Card;


public class Woodcutter extends Card {

	private static final long serialVersionUID = 25L;

	public Woodcutter() {
		super("Woodcutter", "Action", "Base", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(2);
	}

}
