package cards.extra;

import cards.Card;

public class Potion extends Card {

	private static final long serialVersionUID = -11L;

	public Potion() {
		super("Potion", "Treasure", "Extras", 4);
	}

	@Override
	public void performAction() {
		getPlayer().potion++;
	}

}
