package cards.alchemy;
import gameBase.Supply;

import java.util.ArrayList;

import cards.Card;
import cards.extra.Potion;


public class PhilosophersStone extends Card {

	private static final long serialVersionUID = 84L;

	public PhilosophersStone() {
		super("Philosopher's Stone", "Treasure", "Alchemy", 3);
		setPotionCost();
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Potion(), 15));
		return out;
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(getPlayer().deck.drawSize() / 5);
	}
	
}
