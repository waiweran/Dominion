package cards.base;
import cards.Card;


public class Festival extends Card {

	private static final long serialVersionUID = 8L;

	public Festival()	{
		super("Festival", "Action", "Base", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		getPlayer().addBuy();
		getPlayer().addTreasure(2);
	}

}
