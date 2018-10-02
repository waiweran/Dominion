package cards.base;

import java.util.ArrayList;

import cards.Card;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Bureaucrat extends Card {

	private static final long serialVersionUID = 2L;

	public Bureaucrat() {
		super("Bureaucrat", "Action-Attack", "Base", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.gainTopDeck(getGame().board.getSilver().takeCard());
		for(Player p : getGame().getAttackedPlayers()) {
			ArrayList<Card> victory = new ArrayList<Card>();
			ArrayList<Integer> location = new ArrayList<Integer>();
			for(int i = 0; i < p.deck.hand.size(); i++) {
				if(p.deck.hand.get(i).isVictory()) {
					victory.add(p.deck.hand.get(i));
					location.add(new Integer(i));
				}
			}
			if(victory.size() > 0) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), p, victory, 
						"Put a victory card back on your deck", this, true);

				
				int back = sc.getSelectedIndex();
				new Selector(getGame()).showCardDialog(this, p.getPlayerName() + " Put this back", 
						p.deck.hand.get(location.get(back)), "Ok");
				p.deck.putBack(location.get(back));

			}
			else {
				new Selector(getGame()).showCardListDialog(this, p.getPlayerName() + 
						" Revealed Hand with no Victories", p.deck.hand, "Ok");
			}
		}
	}
	
}
