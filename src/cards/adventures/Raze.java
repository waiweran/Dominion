package cards.adventures;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import selectors.SingleCardSelector;

public class Raze extends Card {

	private static final long serialVersionUID = 224L;

	public Raze() {
		super("Raze", "Action", "Adventures", 2);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		ArrayList<Card> options = new ArrayList<>();
		options.addAll(getPlayer().deck.hand);
		options.add(this);
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a card", this, true);
		int index = sc.getSelectedIndex();
		Card choice = this;
		if(index < getPlayer().deck.hand.size()) {
			choice = getPlayer().deck.hand.remove(index);
		}
		else {
			getPlayer().deck.play.remove(getPlayer().deck.play.size() - 1);
		}
		getGame().board.trashCard(choice);
		
		if(choice.getCost() > 0) {
			List<Card> revealed = getPlayer().deck.getDrawCards(choice.getCost());
			SingleCardSelector sc2 = new SingleCardSelector(getGame(), revealed,
					"Add a card to your hand", this, true);
			getPlayer().deck.hand.add(revealed.remove(sc2.getSelectedIndex()));
			for(Card c : revealed) {
				getPlayer().deck.discardCard(c);
			}
		}


	}

}
