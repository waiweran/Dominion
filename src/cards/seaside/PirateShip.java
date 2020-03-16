package cards.seaside;

import java.util.ArrayList;

import cards.Card;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;

public class PirateShip extends Card {

	private static final long serialVersionUID = 69L;

	public PirateShip() {
		super("Pirate Ship", "Action-Attack", "Seaside", 4);
	}

	@Override
	public void performAction() {
		if(0 == new Selector(getGame()).showQuestionDialog(this, getPlayer().pirateShip + " Coin Tokens",
				"Attack Other Players for More Coin Tokens", "+" + getPlayer().pirateShip + " Money")) {
			boolean gotCoins = false;
			for(Player p : getGame().getAttackedPlayers()) {
				ArrayList<Card> treasures = new ArrayList<Card>();
				Card c = p.deck.getDrawCard();
				if(c.isTreasure())
					treasures.add(c);
				else p.deck.discardCard(c);
				c = p.deck.getDrawCard();
				if(c.isTreasure())
					treasures.add(c);
				else p.deck.discardCard(c);

				if(treasures.size() < 1) {
					new Selector(getGame()).showQuestionDialog(this, p.getPlayerName() + " had no treasure", "Ok");
				}
				else if(treasures.size() >= 1) {
					SingleCardSelector sc = new SingleCardSelector(getGame(), treasures,
							"Choose a treasure from " + p.getPlayerName() + " to Trash",
							this, true);
					getGame().board.trashCard(treasures.remove(sc.getSelectedIndex()));
					p.deck.discardCards(treasures);
					gotCoins = true;
				}
			}
			if(gotCoins) {
				getPlayer().pirateShip++;
			}
		}
		else {
			getPlayer().addTreasure(getPlayer().pirateShip);
		}

	}
	
}
