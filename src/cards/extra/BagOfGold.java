package cards.extra;
import cards.Card;


public class BagOfGold extends Card {

	private static final long serialVersionUID = -33L;

	public BagOfGold() {
		super("Bag of Gold", "Action-Prize", "Extras/Prize", 0);
	}
	
	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().deck.gain(getGame().board.getGold().takeCard(), 1);	
	}

}
