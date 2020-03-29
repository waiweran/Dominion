package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Graverobber extends Card {

	private static final long serialVersionUID = 167L;

	public Graverobber() {
		super("Graverobber", "Action", "Dark Ages", 5);
	}

	@Override
	public void performAction() {
		ArrayList<Card> trashPossibilities = new ArrayList<Card>();
		for(Card c : getGame().board.trash) {
			if(c.canBeGained() && c.getCost() >= 3 && c.getCost() <= 6) {
				trashPossibilities.add(c);
			}
		}
		ArrayList<Card> handActions = new ArrayList<Card>();
		ArrayList<Integer> handIndecies = new ArrayList<Integer>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			if(getPlayer().deck.hand.get(i).isAction())  {
				handActions.add(getPlayer().deck.hand.get(i));
				handIndecies.add(i);
			}
		}
		if(!handActions.isEmpty() && (trashPossibilities.isEmpty() || 1 == new Selector(getGame()).showCardListDialog( 
				this, "Choose 1", trashPossibilities, "Take a card from the trash (above)", 
				"Trash an action from my hand, gain a card costing up to 3 more"))) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), handActions, 
					"Trash an action card", this, true);
			int locIndex = sc.getSelectedIndex();
			getGame().board.trashCard(getPlayer().deck.hand.remove(handIndecies.get(locIndex).intValue()));
			int cost = handActions.get(locIndex).getCost() + 3;
			SupplySelector sd = new SupplySelector(getGame(), getName(),
					"Gain a card costing up to " + cost, 0, cost);
			sd.setPotion(handActions.get(locIndex).costsPotion());
			getPlayer().deck.gain(sd.getGainedCard());
		}
		else if(!trashPossibilities.isEmpty()) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), trashPossibilities, 
					"Gain a card from the trash", this, true);
			Card select = trashPossibilities.get(sc.getSelectedIndex());
			getPlayer().deck.gain(select);
			getGame().board.trash.remove(select);
		}

	}

}
