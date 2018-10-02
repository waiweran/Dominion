package cards.prosperity;

import cards.Card;


public class Bank extends Card {

	private static final long serialVersionUID = 90L;

	public Bank() {
		super("Bank", "Treasure", "Prosperity", 7, 0, 0);
	}

	@Override
	public void performAction() {
		for (Card p: getPlayer().deck.play)	{
			if (p.isTreasure())	{
				getPlayer().addTreasure(1);
			}
		}
	}
	
}
