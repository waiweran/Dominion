package cards.seaside;
import cards.Card;
import gameBase.Player;
import gameBase.Supply;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Ambassador extends Card {

	private static final long serialVersionUID = 52L;

	public Ambassador() {
		super("Ambassador", "Action", "Seaside", 3);
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.hand.isEmpty()) return;
		SingleCardSelector sc = new SingleCardSelector(getGame(), getPlayer().deck.hand, 
				"Return a Card to the Supply", this, true);
		Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
		Supply s = getGame().board.findSupply(c);
		if(s == null) return;
		s.putOneBack(c);
		if(getPlayer().deck.hand.contains(c) &&
				new Selector(getGame()).showQuestionDialog(this, "Return a second copy?", "Yes", "No") == 0) {
			getPlayer().deck.hand.remove(c);
			s.putOneBack(c);
		}
		for(Player p : getGame().getAttackedPlayers()) {
			p.deck.gain(s.takeCard());
		}
	}

}
