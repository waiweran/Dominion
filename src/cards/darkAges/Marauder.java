package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import cards.extra.AbandonedMine;
import cards.extra.RuinedLibrary;
import cards.extra.RuinedMarket;
import cards.extra.RuinedVillage;
import cards.extra.Ruins;
import cards.extra.Spoils;
import cards.extra.Survivors;
import gameBase.Player;
import gameBase.ShuffledSupply;
import gameBase.Supply;

public class Marauder extends Card {

	private static final long serialVersionUID = 173L;

	public Marauder() {
		super("Marauder", "Action-Attack-Looter", "Dark Ages", 4);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Spoils(), 15));
		Card[] ruins = {new AbandonedMine(), new RuinedLibrary(), new RuinedMarket(), new RuinedVillage(), new Survivors()};
		out.add(new ShuffledSupply(getGame().random, new Ruins(), ruins, 50));
		return out;
	}

	@Override
	public void performAction() {
		getPlayer().deck.gain(getGame().board.findSupply(new Spoils()).takeCard());
		for(Player p : getGame().getAttackedPlayers()) {
			p.deck.gain(getGame().board.findSupply(new Ruins()).takeCard());
		}
	}
	
}
