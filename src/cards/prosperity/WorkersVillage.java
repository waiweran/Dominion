package cards.prosperity;
import cards.Card;


public class WorkersVillage extends Card {

	private static final long serialVersionUID = 114L;

	public WorkersVillage() {
		super("Worker's Village", "Action", "Prosperity", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
		getPlayer().addBuy();	
	}

}
