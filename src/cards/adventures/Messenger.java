package cards.adventures;
import cards.Card;
import gameBase.Player;
import gameBase.Supply;
import selectors.Selector;
import selectors.SupplySelector;


public class Messenger extends Card {

	private static final long serialVersionUID = 217L;

	public Messenger() {
		super("Messenger", "Action", "Adventures", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().addTreasure(2);
		if(1 == new Selector(getGame()).showQuestionDialog(this, "Put your deck into your discard?", 
				"Don't Discard Deck", "Discard Deck")) {
			getPlayer().deck.discardAllDraw();
		}
	}
	
	@Override
	public void gainAction() {
		System.out.print(getPlayer().bought);
		System.out.println(getPlayer().buying);
		if(getPlayer().buying && getPlayer().bought.size() == 1 && getPlayer().bought.get(0).equals(this)) {
			getPlayer().buying = false;
			SupplySelector sd = new SupplySelector(getGame(), getName(), 
					"Gain a card costing up to 4, and each other player gains a copy of it", 0, 4);
			Card choice = sd.getGainedCard();
			getPlayer().deck.gain(choice);
			Supply s = getGame().board.findSupply(choice);
			for(Player p : getGame().getOtherPlayers()) {
				p.deck.gain(s.takeCard());
			}
			getPlayer().buying = true;
		}
	}


}
