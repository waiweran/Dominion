package cards.darkAges;

import cards.Card;
import selectors.Selector;
import selectors.SupplySelector;
import selectors.TextInput;

public class Rebuild extends Card {

	private static final long serialVersionUID = 180L;

	public Rebuild() {
		super("Rebuild", "Action", "Dark Ages", 5);
	}
	
	@Override
	public void performAction() {
		TextInput td = new TextInput(getGame());
		Card cardIn = td.getEnteredCard(this, "Name a card:");
		for(int i = 0; i < 1000; i++) {
			Card c = getPlayer().deck.getDrawCard();
			if(c.isVictory() && !c.equals(cardIn)) {
				new Selector(getGame()).showCardDialog(this, "This victory card is trashed, gain one costing up to 3 more", 
						c, "Ok");
				SupplySelector getm = new SupplySelector(getGame(), getName(), 
						"Gain a victory card costing up to " + (c.getCost() + 3), 
						0, c.getCost() + 3);
				getm.setPotion(c.costsPotion());
				getm.setCardSelector(card -> card.isVictory());
				getPlayer().deck.gain(getm.getGainedCard());
				break;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}

}
