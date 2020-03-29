package cards.defaults;
import cards.Card;
import cards.hinterlands.FoolsGold;
import gameBase.Player;
import gameBase.Supply;
import selectors.Selector;




public class Province extends Card {

	private static final long serialVersionUID = -8L;

	public Province() {
		super("Province", "Victory", "Default Cards", 8, 6, 0);
	}
	
	@Override
	public void gainAction() {
		// Implementation of Fool's Gold reaction
		for(Player p : getGame().players) {
			for(int i = 0; i < p.deck.hand.size(); i++) {
				Card c = p.deck.hand.get(i);
				if(c instanceof FoolsGold) {
					Selector sel = new Selector(getGame());
					sel.setPlayer(p);
					Supply supply = getGame().board.getGold();
					if(0 == sel.showCardDialog(c, "Trash Fool's Gold and gain a gold to "
							+ "the top of your deck?", supply.getTopCard(), "Yes", "No")) {
						p.deck.hand.remove(i--);
						getGame().board.trashCard(c);
						p.deck.gain(supply.takeCard(), 1);
					}
				}
			}
		}
	}

}
