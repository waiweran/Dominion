package cards.seaside;
import cards.Card;


public class Bazaar extends Card {

	private static final long serialVersionUID = 53L;

	public Bazaar() {
		super("Bazaar", "Action", "Seaside", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		getPlayer().addTreasure(1);
		getPlayer().deck.deal();
	}

}
