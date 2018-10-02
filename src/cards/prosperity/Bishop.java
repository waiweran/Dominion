package cards.prosperity;
import cards.Card;
import gameBase.Player;
import selectors.SingleCardSelector;


public class Bishop extends Card {

	private static final long serialVersionUID = 91L;

	public Bishop() {
		super("Bishop", "Action", "Prosperity", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(1);
		getPlayer().extraVictory += 1;

		if(!getPlayer().deck.hand.isEmpty()) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
					"Trash a Card", this, true);
			int index = sc.getSelectedIndex();
			getPlayer().extraVictory += getPlayer().deck.hand.get(index).getCost() / 2;
			getGame().board.trashCard(getPlayer().deck.hand.remove(index));
		}
		for(Player p : getGame().getOtherPlayers()) {
			if(p.deck.hand.size() > 0) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), p, p.deck.hand, 
						"You may trash a card", this, false);
				
				try {
					getGame().board.trashCard(p.deck.hand.remove(sc.getSelectedIndex()));
				}
				catch(Exception e) {};
			}
		}
	}

}
