package cards.hinterlands;
import java.util.ArrayList;
import java.util.List;

import cards.Card;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Oracle extends Card {

	private static final long serialVersionUID = 147L;

	public Oracle() {
		super("Oracle", "Action-Attack", "Hinterlands", 3);
	}

	@Override
	public void performAction() {
		ArrayList<Player> players = new ArrayList<Player>();
		players.addAll(getGame().getAttackedPlayers());
		players.add(getPlayer());
		for(Player p : players) {
			List<Card> revealed = p.deck.getDrawCards(2);
			if(revealed.size() > 0) {
				if(0 == new Selector(getGame()).showCardListDialog(this, p.getPlayerName() + " revealed ", 
						revealed, "Return to Deck", "Discard")) {
					while(revealed.size() > 0) {
						SingleCardSelector sc = new SingleCardSelector(getGame(), p, revealed, 
								"Click a card to put it back on your Deck", this, true);
						p.deck.topOfDeck(revealed.remove(sc.getSelectedIndex()));
					}	

				}
				else p.deck.discardCards(revealed);
			}
		}
		getPlayer().deck.deal();
		getPlayer().deck.deal();
	}

}
