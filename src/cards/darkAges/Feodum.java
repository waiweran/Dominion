package cards.darkAges;
import cards.Card;


public class Feodum extends Card {

	private static final long serialVersionUID = 164L;

	public Feodum() {
		super("Feodum", "Victory", "Dark Ages", 4);
	}

	@Override
	public int gameEndAction() {
		int score = 0;
		for(Card c : getPlayer().deck.getDeck()) {
			if(c.getName().equals("Silver")) {
				score++;
			}
		}
		return score / 3;
	}

	@Override
	public void trashAction() {
		getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		getPlayer().deck.gain(getGame().board.getSilver().takeCard());
	}
	
}
