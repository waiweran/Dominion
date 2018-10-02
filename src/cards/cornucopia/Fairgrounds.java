package cards.cornucopia;
import java.util.ArrayList;

import cards.Card;


public class Fairgrounds extends Card {

	private static final long serialVersionUID = 115L;

	public Fairgrounds() {
		super("Fairgrounds", "Victory", "Cornucopia", 6);
	}

	@Override
	public int gameEndAction() {
		ArrayList<Card> cardList = new ArrayList<Card>();
		for(Card c : getPlayer().deck.getDeck()) {
			if(!cardList.contains(c)) {
				cardList.add(c);
			}
		}
		return 2 * (cardList.size() / 5);
	}

}
