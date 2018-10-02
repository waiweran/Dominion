package cards.prosperity;

import cards.Card;

public class Quarry extends Card {

	private static final long serialVersionUID = 106L;

	public Quarry() {
		super("Quarry", "Treasure", "Prosperity", 4, 0, 1);
	}

	@Override
	public void performAction() {
		getPlayer().quarry++;
	}
	
}
