package cards.darkAges;

import cards.Card;
import selectors.TextInput;

public class Mystic extends Card {

	private static final long serialVersionUID = 175L;

	public Mystic() {
		super("Mystic", "Action", "Dark Ages", 5);
	}
	
	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(2);
		
		TextInput td = new TextInput(getGame());
		Card cardIn = td.getEnteredCard(this, "Name a card:");
		if(cardIn.equals(getPlayer().deck.draw.get(getPlayer().deck.draw.size() - 1))) {
			getPlayer().deck.deal();
		}
	}

}
