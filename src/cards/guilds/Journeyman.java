package cards.guilds;

import cards.Card;
import selectors.TextInput;

public class Journeyman extends Card {

	private static final long serialVersionUID = 195L;

	public Journeyman() {
		super("Journeyman", "Action", "Guilds", 5);
	}
	
	@Override
	public void performAction() {
		TextInput td = new TextInput(getGame());
		Card cardIn = td.getEnteredCard(this, "Name a card to not draw:");
		int count = 0;
		for(int i = 0; i < 1000; i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c.equals(cardIn)) {
				getPlayer().deck.discardCard(c);
			}
			else {
				getPlayer().deck.hand.add(c);
				count++;
				if(count == 3) {
					break;
				}
			}
		}
	}

}
