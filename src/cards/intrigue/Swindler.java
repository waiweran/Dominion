package cards.intrigue;
import cards.Card;
import gameBase.Player;
import selectors.SupplySelector;


public class Swindler extends Card {

	private static final long serialVersionUID = 46L;

	public Swindler() {
		super("Swindler", "Action-Attack", "Intrigue", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		
		for(Player p : getGame().getAttackedPlayers()) {
			Card c = p.deck.getDrawCard();
			int money = c.getCost();
			getGame().board.trashCard(c);
			SupplySelector sd = new SupplySelector(getGame(), getName(), "Select a card for "
					+ p.getPlayerName() + " to gain, costing " + money, money, money);
			sd.setPotion(c.costsPotion());
			c = sd.getGainedCard();
			p.deck.gain(c);
		}
	}
	
}
