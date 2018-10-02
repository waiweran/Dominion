package cards.base;
import cards.Card;


public class Moneylender extends Card {

	private static final long serialVersionUID = 16L;

	public Moneylender() {
		super("Moneylender", "Action", "Base", 4);
	}

	@Override
	public void performAction() {
		for (int i = 0; i < getPlayer().deck.hand.size(); i++)	{
			if (getPlayer().deck.hand.get(i).getName().equals("Copper"))	{
				getGame().board.trashCard(getPlayer().deck.hand.remove(i));
				getPlayer().addTreasure(3);
				return;
			}
		}
	}

}
