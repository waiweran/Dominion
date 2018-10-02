package cards.extra;

import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;
import selectors.Selector;

public class Mercenary extends Card {

	private static final long serialVersionUID = -22L;

	public Mercenary() {
		super("Mercenary", "Action-Attack", "Extras", 0);
	}
	
	@Override
	public void performAction() {
		if(0 == new Selector(getGame()).showQuestionDialog(this, "Trash 2 cards to gets stuff?", 
				"Yes", "No")) {
			MultiCardSelector ms = new MultiCardSelector(getGame(), 
					getPlayer().deck.hand, "Trash 2 cards", this, 2, true);
			int backIndex1 = 0;
			for(int i : ms.getSelectedIndex()) {
				getGame().board.trashCard(getPlayer().deck.hand.remove(i - backIndex1));
				backIndex1++;
			}
			
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().addTreasure(2);
			
			getPlayer().addTreasure(2);
			for(Player p : getGame().getAttackedPlayers()) {
				MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
						"Discard down to 3", this, p.deck.hand.size() - 3, true);
				int backIndex = 0;
				for(int i : sd.getSelectedIndex()) {
					p.deck.discardCard(i - backIndex++);
				}
			}

		}
	}
	
	@Override
	public boolean canBeGained() {
		return false;
	}

}
