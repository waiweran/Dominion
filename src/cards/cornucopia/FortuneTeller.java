package cards.cornucopia;

import cards.Card;
import cards.defaults.Curse;
import gameBase.Player;

public class FortuneTeller extends Card {

	private static final long serialVersionUID = 117L;

	public FortuneTeller() {
		super("Fortune Teller", "Action-Attack", "Cornucopia", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		for(Player p : getGame().getAttackedPlayers()) {
			for(int i = 0; i < p.deck.size()*2; i++) {
				Card c = p.deck.getDrawCard();
				if(c.isVictory() || c.equals(new Curse())) {
					p.deck.topOfDeck(c);
					break;
				}
				else {
					p.deck.discardCard(c);
				}
			}
		}
	}

}
