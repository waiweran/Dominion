package cards.intrigue;
import cards.Card;
import gameBase.Player;
import selectors.MultiCardSelector;
import selectors.Selector;


public class Torturer extends Card {

	private static final long serialVersionUID = 47L;

	public Torturer() {
		super("Torturer", "Action-Attack", "Intrigue", 5);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		for(Player p : getGame().getAttackedPlayers()) {
			String[] options = {"Discard 2 Cards", "Gain a Curse"};
			Selector sel = new Selector(getGame());
			sel.setPlayer(p);
			if(p.deck.hand.size() >= 2 && 0 == sel.showQuestionDialog(
					this, "What do you Want?", options)) {
				MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
						"Discard 2 Cards", this, 2, true);
				int backIndex = 0;
				for(int i : sd.getSelectedIndex()) {
					p.deck.discardCard(i - backIndex++);
				}
			}
			else {
				p.deck.gain(getGame().board.getCurse().takeCard());
			}
		}
	}
	
}
