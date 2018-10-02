package cards.promo;

import java.util.List;

import cards.Card;
import selectors.SingleCardSelector;

public class Envoy extends Card {

	private static final long serialVersionUID = 233L;

	public Envoy() {
		super("Envoy", "Action", "Promo", 4);
	}
	
	@Override
	public void performAction() {
		List<Card> drawn = getPlayer().deck.getDrawCards(5);
		SingleCardSelector sc = new SingleCardSelector(getGame(), getGame().getLeftPlayer(),
				drawn, "Choose a card for your neighbor to discard", this, true);
		getPlayer().deck.discardCard(drawn.remove(sc.getSelectedIndex()));
		getPlayer().deck.hand.addAll(drawn);
	}

}
