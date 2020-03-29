package cards.prosperity;

import cards.Card;
import selectors.SingleCardSelector;

public class TradeRoute extends Card {

	private static final long serialVersionUID = 110L;

	public TradeRoute() {
		super("Trade Route", "Action", "Prosperity", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(getGame().board.getTradeRouteMat());
		if(!getPlayer().deck.hand.isEmpty()) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
					"Trash a Card", this, true);
			getGame().board.trashCard(getPlayer().deck.hand.remove(sc.getSelectedIndex()));
		}
	}

}
