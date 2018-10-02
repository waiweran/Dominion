package cards.darkAges;

import java.util.ArrayList;
import java.util.HashSet;

import cards.Card;
import cards.extra.Madman;
import gameBase.Supply;
import selectors.Selector;
import selectors.SingleCardSelector;
import selectors.SupplySelector;

public class Hermit extends Card {

	private static final long serialVersionUID = 168L;

	public Hermit() {
		super("Hermit", "Action", "Dark Ages", 3);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Madman(), 10));
		return out;
	}

	@Override
	public void performAction() {
		//Trash card from hand or discard
		HashSet<Card> choices = new HashSet<Card>();
		for(Card c : getPlayer().deck.hand) {
			if(!c.isTreasure()) {
				choices.add(c);
			}
		}
		for(Card c : getPlayer().deck.discard) {
			if(!c.isTreasure()) {
				choices.add(c);
			}
		}
		if(choices.size() > 0) {
			ArrayList<Card> cardPicks = new ArrayList<>();
			cardPicks.addAll(choices);
			SingleCardSelector sc = new SingleCardSelector(getGame(), cardPicks, 
					"Trash a card from your hand or discard pile", this, false);
			int selected = sc.getSelectedIndex();
			if(selected > 0) {
				Card trashMe = cardPicks.get(selected);
				if(!getPlayer().deck.discard.remove(trashMe)) {
					getPlayer().deck.hand.remove(trashMe);
				}
				getGame().board.trashCard(trashMe);
			}
		}
		//Gain card costing up to 3
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Gain a card costing up to 3", 0, 3);
		getPlayer().deck.gain(sd.getGainedCard());
	}

	@Override
	public void cleanupAction() {
		if(getPlayer().bought.size() == 0) { 
			if(new Selector(getGame()).checkExchange(this, new Madman(), 
					"You may trash your Hermit to gain a Madman")) {			
				getPlayer().deck.play.remove(this);
				getGame().board.trashCard(this);
				getPlayer().deck.gain(getGame().board.findSupply(new Madman()).takeCard());
			}
		}
	}

}
