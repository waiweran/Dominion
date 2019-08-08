package cards.prosperity;
import cards.Card;


public class Venture extends Card {

	private static final long serialVersionUID = 112L;

	public Venture() {
		super("Venture", "Treasure", "Prosperity", 5, 0, 1);
	}

	@Override
	public void performAction() {
		int i = 0;
		Card c;
		for(int j = 0; i < 1 && j < getPlayer().deck.size()*2; j++) {
			c = getPlayer().deck.getDrawCard();
			if(c == null) return;
			if(c.isTreasure()) {
				getPlayer().deck.hand.add(0, c);
				getPlayer().playCard(0);
				i++;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}
	
}
