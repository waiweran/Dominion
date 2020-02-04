package cards.adventures;
import java.util.ArrayList;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Duplicate extends Card {

	private static final long serialVersionUID = 209L;

	public Duplicate() {
		super("Duplicate", "Action-Reserve", "Adventures", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
		if(getPlayer().deck.gained.isEmpty()) {
			new Selector(getGame()).showTavernError("You have not gained a card");
			return;
		}
		ArrayList<Card> duplicable = new ArrayList<>();
		for(Card c : getPlayer().deck.gained) {
			if (c.getCost() <= 6) {
				duplicable.add(c);
			}
		}
		if(!duplicable.isEmpty()) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.gained,
					"Select a card to gain a second copy of", this, false);
			int index = sc.getSelectedIndex();
			if(index >= 0) {
				Card c = duplicable.get(index);
				getPlayer().deck.gain(getGame().board.findSupply(c).takeCard());
				getPlayer().deck.callFromTavern(this);
				if(getGame().gamePhase > 2 && !getPlayer().deck.tavern.contains(new Duplicate())) {
					getGame().endTurn();
				}

			}
		}
		else {
			new Selector(getGame()).showTavernError("Gained card(s) too expensive to duplicate");
		}
	}

}
