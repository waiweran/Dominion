package cards.intrigue;
import cards.Card;
import selectors.Selector;


public class MiningVillage extends Card {

	private static final long serialVersionUID = 37L;

	public MiningVillage() {
		super("Mining Village", "Action", "Intrigue", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
		int bob = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
				"Keep Me", "Trash Me");
		if (bob == 0)	{
		}
		else	{
			getPlayer().addTreasure(2);
			getGame().board.trashCard(getPlayer().deck.play.remove(getPlayer().deck.play.size() - 1));
		}
	}
	
}
