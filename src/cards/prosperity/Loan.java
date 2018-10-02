package cards.prosperity;
import javax.swing.JOptionPane;

import cards.Card;
import selectors.Selector;


public class Loan extends Card {

	private static final long serialVersionUID = 101L;

	public Loan() {
		super("Loan", "Treasure", "Prosperity", 3, 0, 1);
	}

	@Override
	public void performAction() {
		int i = 0;
		Card c;
		for(int j = 0; i < 1 && j < getPlayer().deck.drawSize()*2; j++) {
			c = getPlayer().deck.getDrawCard();
			if(c == null) return;
			if(c.isTreasure()) {
				int bob = new Selector(getGame()).showCardDialog(this, "Choose one", c,
						"Discard", "Trash");
				if (bob == JOptionPane.YES_OPTION)	{
					getPlayer().deck.discardCard(c);
				}
				else	{
					getGame().board.trashCard(c);
				}
				i++;
			}
			else {
				getPlayer().deck.discardCard(c);
			}
		}
	}
	
}
