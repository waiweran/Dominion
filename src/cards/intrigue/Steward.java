package cards.intrigue;
import cards.Card;
import selectors.MultiCardSelector;
import selectors.Selector;


public class Steward extends Card {

	private static final long serialVersionUID = 45L;

	public Steward() {
		super("Steward", "Action", "Intrigue", 3);
	}

	@Override
	public void performAction() {
		String[] options = {"+2 Cards", "+$2", "Trash 2 cards"};
		if(getPlayer().deck.hand.size() < 2) {
			options = new String[2];
			options[0] = "+2 Cards";
			options[1] = "+$2";
		}

		int bob = new Selector(getGame()).showQuestionDialog(this, "", options);
		if (bob == 2)	{
			MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
					"Please select to 2 cards to trash", this, 2, true);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				getGame().board.trashCard(getPlayer().deck.hand.remove(i - backIndex));
				backIndex++;
			}
		}
		else if (bob == 1)	{
			getPlayer().addTreasure(2);
		}
		else {			
			getPlayer().deck.deal();
			getPlayer().deck.deal();
		}
	}

}
