package cards.intrigue;
import cards.Card;
import cards.defaults.Duchy;


public class Duke extends Card {

	private static final long serialVersionUID = 32L;

	public Duke() {
		super("Duke", "Victory", "Intrigue", 5);
	}

	@Override
	public int gameEndAction() {
		int count = 0;
		for (Card i : getPlayer().deck.getDeck())	{
			if (i instanceof Duchy) {	
				count++;
			}
		}
		return count;
	}

}
