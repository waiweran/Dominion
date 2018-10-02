package cards.intrigue;
import cards.Card;


public class GreatHall extends Card {

	private static final long serialVersionUID = 33L;

	public GreatHall() {
		super("Great Hall", "Action-Victory", "Intrigue", 3, 1, 0);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().deck.deal();
	}
	
}
