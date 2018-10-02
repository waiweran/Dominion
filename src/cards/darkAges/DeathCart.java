package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import cards.extra.AbandonedMine;
import cards.extra.RuinedLibrary;
import cards.extra.RuinedMarket;
import cards.extra.RuinedVillage;
import cards.extra.Ruins;
import cards.extra.Survivors;
import gameBase.ShuffledSupply;
import gameBase.Supply;
import selectors.Selector;
import selectors.SingleCardSelector;

public class DeathCart extends Card {

	private static final long serialVersionUID = 163L;

	public DeathCart() {
		super("Death Cart", "Action-Looter", "Dark Ages", 4);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		Card[] ruins = {new AbandonedMine(), new RuinedLibrary(), new RuinedMarket(), new RuinedVillage(), new Survivors()};
		out.add(new ShuffledSupply(getGame().random, new Ruins(), ruins, 50));
		return out;
	}

	@Override
	public void performAction() {
		ArrayList<Card> actions = new ArrayList<Card>();
		ArrayList<Integer> actionIndexes = new ArrayList<Integer>();
		for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
			Card c = getPlayer().deck.hand.get(i);
			if(c.isAction() && (!getGame().setup.balanceCards() || c.getType().indexOf("Ruins") < 0)) {
				actions.add(c);
				actionIndexes.add(new Integer(i));
			}
		}
		if(actions.size() < 1) {
			getPlayer().addTreasure(5);
			getPlayer().deck.play.remove(this);
			getGame().board.trashCard(this);
		}
		else {
			SingleCardSelector sc = new SingleCardSelector(getGame(), actions,
					"Trash a card", this, true);
			if(0 == new Selector(getGame()).showQuestionDialog(this, "What would you like to Trash?", 
					"Trash an Action", "Trash This")) {
				int index = actionIndexes.get(sc.getSelectedIndex());
				getGame().board.trashCard(getPlayer().deck.hand.remove(index));
				getPlayer().addTreasure(5);
			}
			else {
				getPlayer().addTreasure(5);
				getPlayer().deck.play.remove(this);
				getGame().board.trashCard(this);
			}
		}
	}

	@Override
	public void gainAction() {
		getPlayer().deck.gain(getGame().board.findSupply(new Ruins()).takeCard());
		getPlayer().deck.gain(getGame().board.findSupply(new Ruins()).takeCard());
	}

}
