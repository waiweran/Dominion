package cards.adventures;
import java.util.ArrayList;

import cards.Card;
import cards.extra.Champion;
import cards.extra.Hero;
import cards.extra.TreasureHunter;
import cards.extra.Warrior;
import gameBase.Supply;
import selectors.Selector;


public class Page extends Card {

	private static final long serialVersionUID = 219L;

	public Page() {
		super("Page", "Action-Traveller", "Adventures", 2);
	}

	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new TreasureHunter(), 5));
		out.add(new Supply(new Warrior(), 5));
		out.add(new Supply(new Hero(), 5));
		out.add(new Supply(new Champion(), 5));
		return out;

	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
	}
	
	@Override
	public void cleanupAction() {
		Supply getFrom = getGame().board.findSupply(new TreasureHunter());
		if(getFrom.isEmpty() == 1) return;
		if(new Selector(getGame()).checkExchange(this, getFrom.getTopCard(), 
				"You may exchange your " + getName() + " for a " + getFrom.getCard().getName())) {			
			Card upgrade = getFrom.takeCard().clone();
			upgrade.passPlayer(getPlayer());
			upgrade.passGame(getGame());
			getPlayer().deck.play.remove(this);
			getGame().board.findSupply(this).putOneBack(this);
			getPlayer().deck.play.add(upgrade);
		}
	}

}
