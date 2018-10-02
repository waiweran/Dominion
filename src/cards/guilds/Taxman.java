package cards.guilds;

import java.util.ArrayList;

import cards.Card;
import gameBase.Player;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Taxman extends Card {

	private static final long serialVersionUID = 201L;

	public Taxman() {
		super("Taxman", "Action-Attack", "Guilds", 4);
	}
	
	@Override
	public void performAction() {
		ArrayList<Card> money = new ArrayList<Card>();

		for(Card c : getPlayer().deck.hand) {
			if(c.isTreasure()) money.add(c);
		}
		if(money.size() > 0) {
			SingleCardSelector sc = new SingleCardSelector(getGame(), money,
					"Trash a Treasure", this, true);
			int index = sc.getSelectedIndex();			
			Card c = getPlayer().deck.hand.get(index);
			
			for(Player p : getGame().getAttackedPlayers()) {
				if(p.deck.hand.size() > 4) {
					if(p.deck.hand.remove(c)) {
						p.deck.discardCard(c);
					}
				}
			}
			
			getGame().board.trashCard(getPlayer().deck.hand.remove(index));
			SupplySelector getm = new SupplySelector(getGame(), getName(), 
					"Select a treasure to gain, costing up to 3 more than the one that you trashed", 
					0, c.getCost() + 3);
			getm.setPotion(c.costsPotion());
			getm.setCardSelector(card -> card.isTreasure());
			getPlayer().deck.gainHand(getm.getGainedCard());
		}

	}

}
