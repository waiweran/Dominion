package cards.intrigue;
import cards.Card;


public class ShantyTown extends Card {

	private static final long serialVersionUID = 44L;

	public ShantyTown() {
		super("Shanty Town", "Action", "Intrigue", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		boolean derp = false;
		for(Card c: getPlayer().deck.hand)	{
			if (c.isAction())	{
				derp = true;
			}
		}
		if(!derp)	{
			getPlayer().deck.deal();
			getPlayer().deck.deal();
		}
	}
	
}
