package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import cards.extra.Spoils;
import gameBase.Supply;

public class BanditCamp extends Card {

	private static final long serialVersionUID = 156L;

	public BanditCamp() {
		super("Bandit Camp", "Action", "Dark Ages", 5);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Spoils(), 15));
		return out;
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
		getPlayer().deck.gain(getGame().board.findSupply(new Spoils()).takeCard());
	}
	
}
