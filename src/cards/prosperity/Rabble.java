package cards.prosperity;
import java.util.List;

import cards.Card;
import gameBase.Player;


public class Rabble extends Card {

	private static final long serialVersionUID = 107L;

	public Rabble() {
		super("Rabble","Action-Attack", "Prosperity", 5);
		
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		for(Player p : getGame().getAttackedPlayers()) {
			List<Card> revealed = p.deck.getDrawCards(3);
			for (Card c : revealed)	{
				if (c.isAction() || c.isTreasure())	{
					p.deck.discardCard(c);
				}
				else {
					p.deck.topOfDeck(c);
				}
			}
		}

	}
	
}
