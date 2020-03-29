package cards.adventures;
import cards.Card;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Amulet extends Card {

	private static final long serialVersionUID = 208L;

	public Amulet() {
		super("Amulet", "Action-Duration", "Adventures", 3);
		
	}

	@Override
	public void performAction() {
		makeChoice();
	}
	
	@Override
	public void durationAction() {
		makeChoice();
	}
	
	private void makeChoice() {
		String[] options = {"Gain a Silver", "+$1", "Trash a Card"};
		if(getPlayer().deck.hand.size() < 2) {
			options = new String[2];
			options[0] = "+$1";
			options[1] = "Gain a Silver";
		}

		int choice = new Selector(getGame()).showQuestionDialog(this, "", options);
		
		if (choice == 2 && !getPlayer().deck.hand.isEmpty())	{
			SingleCardSelector sc = new SingleCardSelector(getGame(), 
					getPlayer().deck.hand, "Trash a Card", this, true);			
			Card c = getPlayer().deck.hand.remove(sc.getSelectedIndex());
			getGame().board.trashCard(c);
		}
		
		else if (choice == 1)	{
			getPlayer().addTreasure(1);
		}
		
		else {			
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		}

	}

}
