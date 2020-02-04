package cards.prosperity;

import cards.Card;
import gameBase.Supply;
import selectors.SupplySelector;

public class Contraband extends Card {

	private static final long serialVersionUID = 93L;
	
	
	public Contraband() {
		super("Contraband", "Treasure", "Prosperity", 5, 0, 3);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		SupplySelector supp = new SupplySelector(getGame(), getName(), 
				"Choose a Card that the Current Player Cannot Buy", -1, 20);
		supp.setPotion(true);
		supp.setCardSelector(c -> !getPlayer().contraband.contains(c));
		supp.setPlayer(getGame().getLeftPlayer());
		Supply selected = supp.getSelectedSupply();
		if(selected != null) {
			getPlayer().contraband.add(selected.getTopCard());
		}
	}
	
}
