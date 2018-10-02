package cards.seaside;
import cards.Card;


public class Caravan extends Card {

	private static final long serialVersionUID = 54L;

	public Caravan() {
		super("Caravan", "Action-Duration", "Seaside", 4);
		
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
	}

	@Override
	public void durationAction() {
		getPlayer().deck.deal();
	}

}
