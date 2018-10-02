package cards.alchemy;
import gameBase.Supply;

import java.util.ArrayList;

import cards.Card;
import cards.extra.Potion;


public class Vineyard extends Card {

	private static final long serialVersionUID = 89L;

	public Vineyard() {
		super("Vineyard", "Victory", "Alchemy", 0);
		setPotionCost();
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Potion(), 15));
		return out;
	}

	@Override
	public int gameEndAction() {
		int numAction = 0;
		for(Card c : getPlayer().deck.getDeck()) {
			if(c.isAction()) {
				numAction++;
			}
		}
		return numAction / 3;
	}

}
