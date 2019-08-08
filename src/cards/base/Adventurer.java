package cards.base;
import cards.Card;

public class Adventurer extends Card {

	private static final long serialVersionUID = 1L;

	public Adventurer() {
		super("Adventurer", "Action", "Base", 6);
	}

	@Override
	public void performAction() {
		int i = 0;
		for(int j = 0; i < 2 && j < getPlayer().deck.size()*2; j++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c.isTreasure()) {
				getPlayer().deck.hand.add(c);
				i++;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}

}
