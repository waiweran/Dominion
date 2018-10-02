package cards.extra;
import cards.Card;
import selectors.Selector;


public class TrustySteed extends Card {

	private static final long serialVersionUID = -37L;

	public TrustySteed() {
		super("Trusty Steed", "Action-Prize", "Extras/Prize", 0);
	}

	@Override
	public void performAction() {
		int bob = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
				"+2 Cards", "+2 Actions", "+$2", "Gain 4 Silver and Discard your Deck");
		if (bob == 0)	{
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			int bob2 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+2 Actions", "+$2", "Gain 4 Silver and Discard your Deck");
			if (bob2 == 0)	{
				getPlayer().addAction(2);
			}
			else if (bob2 == 1)	{
				getPlayer().addTreasure(2);
			}
			else {
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.discard.addAll(getPlayer().deck.draw);
				getPlayer().deck.draw.clear();
			}
		}
		else if (bob == 1)	{
			getPlayer().addAction(2);
			int bob3 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+2 Cards", "+$2", "Gain 4 Silver and Discard your Deck");
			if (bob3 == 0)	{
				getPlayer().deck.deal();
				getPlayer().deck.deal();
			}
			else if (bob3 == 1)	{
				getPlayer().addTreasure(2);
			}
			else {
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.discard.addAll(getPlayer().deck.draw);
				getPlayer().deck.draw.clear();
			}
		}
		else if (bob == 2)	{
		
			getPlayer().addTreasure(2);
			int bob3 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+2 Cards", "+2 Actions", "Gain 4 Silver and Discard your Deck");
			if (bob3 == 0)	{
				getPlayer().deck.deal();
				getPlayer().deck.deal();
			}
			else if (bob3 == 1)	{
				getPlayer().addAction(2);
			}
			else {
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().deck.discard.addAll(getPlayer().deck.draw);
				getPlayer().deck.draw.clear();
			}
		}
		else {
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
			getPlayer().deck.discard.addAll(getPlayer().deck.draw);
			getPlayer().deck.draw.clear();
			int bob3 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+2 Cards", "+2 Actions", "+$2");
			if (bob3 == 0)	{
				getPlayer().deck.deal();
				getPlayer().deck.deal();
			}
			else if (bob3 == 1)	{
				getPlayer().addAction(2);
			}
			else {
				getPlayer().addTreasure(2);
			}
		}
	}

}
