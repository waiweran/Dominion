package cards.promo;
import cards.Card;


public class WalledVillage extends Card {

	private static final long serialVersionUID = 237L;

	public WalledVillage() {
		super("Walled Village", "Action", "Promo", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
	}
	
	@Override
	public void cleanupAction() {
		int actions = 0;
		for(Card c : getPlayer().deck.play) {
			if(c.isAction()) actions++;
		}
		if(actions < 3) {
			getPlayer().deck.play.remove(this);
			getPlayer().deck.topOfDeck(this);
		}
	}

}
