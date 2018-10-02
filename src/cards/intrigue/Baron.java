package cards.intrigue;
import cards.Card;


public class Baron extends Card {

	private static final long serialVersionUID = 27L;

	public Baron() {
		super("Baron", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		int which = -1;
		for(int i = 0; i < getPlayer().deck.hand.size(); i++)	{
			if (getPlayer().deck.hand.get(i).getName().equals("Estate"))	{
				which = i;
			}
		}
		if (which > -1)	{
			getPlayer().deck.discardCard(which);
		}
		else	{
			getPlayer().deck.gain(getGame().board.getEstate().takeCard());
		}
	}
	
}
