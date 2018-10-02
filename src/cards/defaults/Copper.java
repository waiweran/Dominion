package cards.defaults;
import cards.Card;

public class Copper extends Card {

	private static final long serialVersionUID = -2L;

	public Copper() {
		super("Copper", "Treasure", "Default Cards", 0, 0, 1);
	}
	
	@Override
	public int getTreasure() {
		return 1 + getPlayer().coppersmith;
	}
}
