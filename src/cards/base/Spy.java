package cards.base;
import cards.Card;
import gameBase.Player;
import selectors.Selector;


public class Spy extends Card {

	private static final long serialVersionUID = 20L;

	public Spy() {
		super("Spy", "Action-Attack", "Base", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		for(Player p : getGame().getAttackedPlayers()) {
			Card c = p.deck.getDrawCard();
			if(c != null) {
				if(0 == new Selector(getGame()).showCardDialog(this, p.getPlayerName() + " revealed", 
						c, "Return to Deck", "Discard")) {
					p.deck.topOfDeck(c);
				}
				else p.deck.discardCard(c);
			}
		}
		Card c = getPlayer().deck.getDrawCard();
		if(c != null) {
			if(0 == new Selector(getGame()).showCardDialog(this, "I revealed", 
					c, "Return to my Deck", "Discard")) {
				getPlayer().deck.topOfDeck(c);
			}
			else getPlayer().deck.discardCard(c);
		}

	}

}
