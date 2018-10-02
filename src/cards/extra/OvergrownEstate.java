package cards.extra;
import cards.Card;


public class OvergrownEstate extends Card {

	private static final long serialVersionUID = -12L;

	public OvergrownEstate() {
		super("Overgrown Estate", "Victory-Shelter", "Extras/Shelter", 1);
	}

	@Override
	public void trashAction() {
		getPlayer().deck.deal();
	}

}
