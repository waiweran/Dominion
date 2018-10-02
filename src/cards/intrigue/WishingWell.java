package cards.intrigue;
import cards.Card;
import selectors.Selector;
import selectors.TextInput;


public class WishingWell extends Card {

	private static final long serialVersionUID = 51L;

	public WishingWell() {
		super("Wishing Well", "Action", "Intrigue", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().deck.deal();
		TextInput td = new TextInput(getGame());
		Card cardIn = td.getEnteredCard(this, "Name a card:");
		Card c = getPlayer().deck.getDrawCard();
		if(c == null) return;
		new Selector(getGame()).showCardDialog(this, "Revealed", c, "Ok");
		if (c.equals(cardIn))	{
			getPlayer().deck.hand.add(c);
		}
		else {
			getPlayer().deck.topOfDeck(c);
		}
	}

}
