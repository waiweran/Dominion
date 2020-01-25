package cards.prosperity;
import cards.Card;
import cards.defaults.Copper;


public class GrandMarket extends Card {

	private static final long serialVersionUID = 98L;

	public GrandMarket() {
		super("Grand Market", "Action", "Prosperity", 6);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(2);
		getPlayer().addBuy();
		getPlayer().deck.deal();
	}
	
	@Override
	public boolean canBeGained() {
		if(getGame().getCurrentPlayer().buying && 
				getGame().getCurrentPlayer().deck.play.contains(new Copper()))	{
			return false;
		}
		return true;
	}

}
