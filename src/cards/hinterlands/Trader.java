package cards.hinterlands;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;

public class Trader extends Card {

	private static final long serialVersionUID = 152L;
	
	private boolean reacting;

	public Trader() {
		super("Trader", "Action-Reaction", "Hinterlands", 4);
		reacting = false;
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand,
				"Trash a card", this, true);
		Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
		getGame().board.trashCard(c);
		for(int i = 0; i < c.getCost(); i++) {
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		}
		
	}

	@Override
	public void reactGain(Card c) {
		if(!reacting && getPlayer().deck.discard.size() > 0
				&& getPlayer().deck.discard.get(
				getPlayer().deck.discard.size() - 1) == c
				&& getPlayer().deck.gained.size() > 0
				&& getPlayer().deck.gained.get(
				getPlayer().deck.gained.size() - 1) == c) {
			reacting = true;
			if(new Selector(getGame()).checkReact(getPlayer(), this)) {
				getPlayer().deck.discard.remove(getPlayer().deck.discard.size() - 1);
				getPlayer().deck.gained.remove(getPlayer().deck.gained.size() - 1);
				boolean temp = getPlayer().buying;
				getPlayer().buying = false;
				getPlayer().deck.gain(getGame().board.getSilver().takeCard());
				getPlayer().buying = temp;
				getGame().board.findSupply(c).putOneBack(c);
			}
		}
		reacting = false;

	}

}
