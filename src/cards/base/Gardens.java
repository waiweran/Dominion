package cards.base;
import cards.Card;


public class Gardens extends Card {

	private static final long serialVersionUID = 9L;

	public Gardens() {
		super("Gardens", "Victory", "Base", 4);
	}

	@Override
	public int gameEndAction() {
		return getPlayer().deck.getDeck().size() / 10;
	}

}
