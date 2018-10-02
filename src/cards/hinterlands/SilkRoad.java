package cards.hinterlands;
import cards.Card;


public class SilkRoad extends Card {

	private static final long serialVersionUID = 148L;

	public SilkRoad() {
		super("Silk Road", "Victory", "Hinterlands", 4);
	}

	@Override
	public int gameEndAction() {
		int numVictory = 0;
		for(Card c : getPlayer().deck.getDeck()) {
			if(c.isVictory()) {
				numVictory++;
			}
		}
		return numVictory / 4;
	}

}
