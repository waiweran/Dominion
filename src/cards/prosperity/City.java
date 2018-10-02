package cards.prosperity;
import cards.Card;
import gameBase.Supply;


public class City extends Card {

	private static final long serialVersionUID = 92L;

	public City() {
		super("City", "Action", "Prosperity", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		getPlayer().deck.deal();
		int numEmpty = 0;
		for(Supply s : getGame().board.getAllSupplies()) {
			numEmpty += s.isEmpty();
		}
		if(numEmpty == 1)	{
			getPlayer().deck.deal();
		}
		else if(numEmpty >= 2)	{
			getPlayer().addTreasure(1);
			getPlayer().addBuy();
		}
	}
	
}
