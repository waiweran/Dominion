package cards.defaults;
import cards.Card;
import cards.hinterlands.Duchess;
import gameBase.Supply;
import selectors.Selector;

public class Duchy extends Card {

	private static final long serialVersionUID = -4L;

	public Duchy() {
		super("Duchy", "Victory", "Default Cards", 5, 3, 0);
	}

	@Override
	public void gainAction() {
		if(getGame().board.findSupply(new Duchess()) != null) {
			Selector sel = new Selector(getGame());
			sel.setPlayer(getPlayer());
			Supply supply = getGame().board.findSupply(new Duchess());
			if(1 == sel.showCardDialog(this, "Would you like to Gain a Duchess?", 
					supply.getTopCard(), "No", "Yes")) {
				boolean temp = getPlayer().buying;
				getPlayer().buying = false;
				getPlayer().deck.gain(supply.takeCard());
				getPlayer().buying = temp;
			}
		}
	}

}
