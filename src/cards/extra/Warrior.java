package cards.extra;
import cards.Card;
import gameBase.Player;
import gameBase.Supply;
import selectors.Selector;


public class Warrior extends Card {

	private static final long serialVersionUID = -39L;

	public Warrior() {
		super("Warrior", "Action-Traveller", "Extras/Page", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		int numTravellers = 0;
		for(Card c : getPlayer().deck.play) {
			if(c.isTraveller()) numTravellers++;
		}
		for(Player p : getGame().getAttackedPlayers()) {
			for(int i = 0; i < numTravellers; i++) {
				Card c = p.deck.getDrawCard();
				if(c != null && (c.getCost() == 3 || c.getCost() == 4) && !c.costsPotion()) {
					getGame().board.trashCard(c);
				}
			}
		}
	}
	
	@Override
	public boolean canBeGained() {
		return false;
	}

	@Override
	public void cleanupAction() {
		Supply getFrom = getGame().board.findSupply(new Hero());
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
