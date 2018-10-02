package cards.intrigue;
import cards.Card;
import selectors.Selector;


public class Nobles extends Card {

	private static final long serialVersionUID = 39L;

	public Nobles() {
		super("Nobles", "Action - Victory", "Intrigue", 6, 2, 0);
	}

	@Override
	public void performAction() {
		int bob = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
				"+3 Cards", "+2 Actions");
		if (bob == 0)	{
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
		}
		else	{
			getPlayer().addAction(2);
		}
	}
	
}
