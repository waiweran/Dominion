package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import cards.extra.AbandonedMine;
import cards.extra.RuinedLibrary;
import cards.extra.RuinedMarket;
import cards.extra.RuinedVillage;
import cards.extra.Ruins;
import cards.extra.Survivors;
import gameBase.Player;
import gameBase.ShuffledSupply;
import gameBase.Supply;
import selectors.Selector;

public class Cultist extends Card {

	private static final long serialVersionUID = 162L;

	public Cultist() {
		super("Cultist", "Action-Attack-Looter", "Dark Ages", 5);
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
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		for(Player p : getGame().getAttackedPlayers()) {
			p.deck.gain(getGame().board.findSupply(new Ruins()).takeCard());
		}
		if(getPlayer().deck.hand.contains(new Cultist())) {
			if(0 == new Selector(getGame()).showQuestionDialog(this, "Would you like to Play another Cultist?",
					"Yes", "No")) {
				getPlayer().addAction(1);
				getPlayer().playCard(getPlayer().deck.hand.indexOf(new Cultist()));
			}
		}
	}

}
