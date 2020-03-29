package cards.intrigue;

import java.util.ArrayList;

import cards.Card;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;

public class Masquerade extends Card {

	private static final long serialVersionUID = 36L;

	public Masquerade() {
		super("Masquerade", "Action", "Intrigue", 3);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		ArrayList<Card> passes = new ArrayList<Card>();
		for(Player p : getGame().players) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), p, p.deck.hand,
					"Pass a card to the player on your left", this, true);
			passes.add(p.deck.hand.remove(sc.getSelectedIndex()));
		}
		for(Player p : getGame().players) {
			if(p.getPlayerNum() < passes.size()) {
				Card c = passes.get(p.getPlayerNum());
				c.passPlayer(p);
				p.deck.hand.add(c);
			}
			else {
				Card c = passes.get(0);
				c.passPlayer(p);
				p.deck.hand.add(c);
			}
		}
		if(!getPlayer().deck.hand.isEmpty() && 1 == new Selector(getGame()).showQuestionDialog(this, "Would you like to Trash a Card from your Hand", 
				"Don't Trash a Card", "Trash a Card")) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
					"Trash a card", this, true);

			getGame().board.trashCard(getPlayer().deck.hand.remove(sc.getSelectedIndex()));
		}
	}

}
