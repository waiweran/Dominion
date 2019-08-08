package cards.intrigue;
import cards.Card;
import gameBase.Player;
import selectors.SupplySelector;


public class Saboteur extends Card {

	private static final long serialVersionUID = 41L;

	public Saboteur() {
		super("Saboteur", "Action-Attack", "Intrigue", 5);
	}

	@Override
	public void performAction() {
		for(Player p : getGame().getAttackedPlayers()) {
			Card c = p.deck.getDrawCard();
			for(int i = 0; c != null && c.getCost() < 3
					&& i < getPlayer().deck.size()*2; i++) {
				p.deck.discardCard(c);
				c = p.deck.getDrawCard();
			}
			getGame().board.trashCard(c);
			SupplySelector sd = new SupplySelector(getGame(), getName(), "Select a card to gain",
					0, c.getCost() - 2);
			sd.setPotion(c.costsPotion());
			sd.setPlayer(p);
			p.deck.gain(sd.getGainedCard());
		}
	}
	
}
