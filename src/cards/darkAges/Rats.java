package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import selectors.SingleCardSelector;

public class Rats extends Card {

	private static final long serialVersionUID = 179L;

	public Rats() {
		super("Rats", "Action", "Dark Ages", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().deck.gain(getGame().board.findSupply(new Rats()).takeCard());
		
		ArrayList<Integer> notRats = new ArrayList<Integer>();
		ArrayList<Card> notRatsCards = new ArrayList<>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			Card c = getPlayer().deck.hand.get(i);
			if(!(c instanceof Rats)) {
				notRats.add(i);
				notRatsCards.add(c);
			}
		}
		
		if(notRats.size() == 0) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), notRatsCards, 
				"Trash a Card", this, true);
		getGame().board.trashCard(getPlayer().deck.hand.remove(
				notRats.get(sc.getSelectedIndex()).intValue()));
		
	}

	@Override
	public void trashAction() {
		getPlayer().deck.deal();
	}

}
