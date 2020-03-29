package cards.intrigue;
import cards.Card;
import selectors.SingleCardSelector;


public class Courtyard extends Card {

	private static final long serialVersionUID = 31L;

	public Courtyard() {
		super("Courtyard", "Action", "Intrigue", 2);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Put a  card on top of your deck", this, true);
		getPlayer().deck.putBack(sc.getSelectedIndex());
		
	}
	
}
