package cards.darkAges;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import gameBase.Player;
import selectors.SingleCardSelector;

public class Rogue extends Card {

	private static final long serialVersionUID = 181L;

	public Rogue() {
		super("Rogue", "Action-Attack", "Dark Ages", 5);
	}
	
	public void performAction() {
		getPlayer().addTreasure(2);
		
		ArrayList<Card> trashPossibilities = new ArrayList<Card>();
		for(Card c : getGame().board.trash) {
			if(c.canBeGained() && c.getCost() >= 3 && c.getCost() <= 6) {
				trashPossibilities.add(c);
			}
		}
		
		if(trashPossibilities.isEmpty()) {
			for(Player p : getGame().getAttackedPlayers()) {
				List<Card> revealed = p.deck.getDrawCards(2);
				for(int i = revealed.size() - 1; i >= 0; i--) {
					if(revealed.get(i).costsPotion() 
							|| revealed.get(i).getCost() < 3 
							|| revealed.get(i).getCost() > 6) {
						p.deck.discardCard(revealed.remove(i));
					}
				}
				if(revealed.size() > 0) {
					SingleCardSelector sc = new SingleCardSelector(getGame(), p, revealed, 
							"Trash a Card", this, true);
					Card choice = revealed.get(sc.getSelectedIndex());
					getGame().board.trashCard(choice);
				}
			}
		}
		else {
			SingleCardSelector sc = new SingleCardSelector(getGame(), trashPossibilities, 
					"Gain a card from the trash", this, true);
			Card select = trashPossibilities.get(sc.getSelectedIndex());
			getPlayer().deck.gain(select);
			getGame().board.trash.remove(select);
		}

	}

}
