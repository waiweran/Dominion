package cards.prosperity;
import cards.Card;


public class Monument extends Card {

	private static final long serialVersionUID = 103L;

	public Monument() {
		super("Monument", "Action", "Prosperity", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		getPlayer().extraVictory += 1;
	}
	
}
