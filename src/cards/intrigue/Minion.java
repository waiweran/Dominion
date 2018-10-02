package cards.intrigue;
import cards.Card;
import gameBase.Player;
import selectors.Selector;


public class Minion extends Card {

	private static final long serialVersionUID = 38L;

	public Minion() {
		super("Minion", "Action-Attack", "Intrigue", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		if(0 == new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
				"+$2", "Discard your hand, +4 Cards, and each other getPlayer() with at least 5 cards in hand discards his hand and draws 4 cards")) {
			getPlayer().addTreasure(2);
		}
		else {
			for (int i = 0; i < getPlayer().deck.hand.size(); i++) {
					getPlayer().deck.discardCard(i);
			}
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			for(Player p : getGame().getAttackedPlayers()) {
				if (p.deck.hand.size() > 4)	{
					for (int i = 0; i < p.deck.hand.size(); i++)	{
						p.deck.discardCard(i);
					}
					p.deck.deal();
					p.deck.deal();
					p.deck.deal();
					p.deck.deal();
				}
			}
		}
	}
	
}
