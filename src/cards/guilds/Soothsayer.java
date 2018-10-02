package cards.guilds;

import cards.Card;
import cards.defaults.Curse;
import gameBase.Player;

public class Soothsayer extends Card {

	private static final long serialVersionUID = 199L;

	public Soothsayer() {
		super("Soothsayer", "Action-Attack", "Guilds", 5);
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.gain(getGame().board.getGold().takeCard());
		for(Player p : getGame().getAttackedPlayers()) {
			int gainsize = p.deck.gained.size() + 1;
			p.deck.gain(getGame().board.getCurse().takeCard());
			if(p.deck.gained.size() == gainsize && p.deck.gained.get(p.deck.gained.size() - 1) instanceof Curse) {
				p.deck.deal();
			}
		}
	}

}
