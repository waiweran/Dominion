package cards.guilds;

import java.util.List;

import cards.Card;
import selectors.SingleCardSelector;

public class Advisor extends Card {

	private static final long serialVersionUID = 189L;

	public Advisor() {
		super("Advisor", "Action", "Guilds", 4);
	}
	
	@Override
	public void performAction() {
		getPlayer().addAction(1);
		List<Card> drawn = getPlayer().deck.getDrawCards(3);
		if(drawn.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getGame().getLeftPlayer(),
				drawn, "Choose a card for your neighbor to discard", this, true);
		getPlayer().deck.discardCard(drawn.remove(sc.getSelectedIndex()));
		getPlayer().deck.hand.addAll(drawn);
	}

}
