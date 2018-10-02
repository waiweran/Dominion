package cards.intrigue;
import cards.Card;


public class Conspirator extends Card {

	private static final long serialVersionUID = 29L;

	public Conspirator() {
		super("Conspirator", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		int count = 0;
		for (Card c : getPlayer().deck.play)	{
			if (c.isAction())	{
				count++;
			}
		}
		if (count > 2)	{
			getPlayer().deck.deal();
			getPlayer().addAction(1);
		}
	}
	
}
