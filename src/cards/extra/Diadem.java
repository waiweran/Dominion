package cards.extra;
import cards.Card;


public class Diadem extends Card {

	private static final long serialVersionUID = -34L;

	public Diadem() {
		super("Diadem", "Treasure-Prize", "Extras/Prize", 0, 0, 2);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(getPlayer().getActions());
	}

}
