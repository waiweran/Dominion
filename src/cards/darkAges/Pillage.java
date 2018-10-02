package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import cards.extra.Spoils;
import gameBase.Player;
import gameBase.Supply;
import selectors.SingleCardSelector;

public class Pillage extends Card {

	private static final long serialVersionUID = 176L;

	public Pillage() {
		super("Pillage", "Action-Attack", "Dark Ages", 5);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Spoils(), 15));
		return out;
	}

	@Override
	public void performAction() {
		getPlayer().deck.play.remove(this);
		getGame().board.trashCard(this);
		for(Player p : getGame().getAttackedPlayers()) {
			if(p.deck.hand.size() >= 5) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), p.deck.hand,
						"Pick a card for " + p.getPlayerName() + " to discard", this, true);
				p.deck.discardCard(p.deck.hand.remove(sc.getSelectedIndex()));
			}
		}
		getPlayer().deck.gain(getGame().board.findSupply(new Spoils()).takeCard());
		getPlayer().deck.gain(getGame().board.findSupply(new Spoils()).takeCard());
	}

}
