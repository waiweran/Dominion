package cards.base;
import cards.Card;
import selectors.Selector;


public class Library extends Card {

	private static final long serialVersionUID = 11L;

	public Library() {
		super("Library", "Action", "Base", 5);
	}

	@Override
	public void performAction() {
		for(int i = getPlayer().deck.hand.size(); i <= 7; i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c != null && (!c.isAction() || 0 == new Selector(getGame()).showCardDialog(this, "",
					c, "Keep", "Discard"))) {
				getPlayer().deck.hand.add(c);
			}
			else if(c != null) getPlayer().deck.discardCard(c);
		}
	}

}
