package cards.intrigue;
import cards.Card;
import selectors.Selector;


public class Pawn extends Card {

	private static final long serialVersionUID = 40L;

	public Pawn() {
		super("Pawn", "Action", "Intrigue", 2);
	}

	@Override
	public void performAction() {
		int bob = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
				"+1 Card", "+1 Action", "+1 Buy", "+$1");
		if (bob == 0)	{
			getPlayer().deck.deal();
			int bob2 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+1 Action", "+1 Buy", "+$1");
			if (bob2 == 0)	{
				getPlayer().addAction(1);
			}
			else if (bob2 == 1)	{
				getPlayer().addBuy();
			}
			else {
				getPlayer().addTreasure(1);
			}
		}
		else if (bob == 1)	{
			getPlayer().addAction(1);
			int bob3 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+1 Card", "+1 Buy", "+$1");
			if (bob3 == 0)	{
				getPlayer().deck.deal();
			}
			else if (bob3 == 1)	{
				getPlayer().addBuy();
			}
			else {
				getPlayer().addTreasure(1);
			}
		}
		else if (bob == 2)	{
		
			getPlayer().addBuy();
			int bob3 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+1 Card", "+1 Action", "+$1");
			if (bob3 == 0)	{
				getPlayer().deck.deal();
			}
			else if (bob3 == 1)	{
				getPlayer().addAction(1);
			}
			else {
				getPlayer().addTreasure(1);
			}
		}
		else {
			getPlayer().addTreasure(1);
			int bob3 = new Selector(getGame()).showQuestionDialog(this, "What do you want?", 
					"+1 Card", "+1 Action", "+1 Buy");
			if (bob3 == 0)	{
				getPlayer().deck.deal();
			}
			else if (bob3 == 1)	{
				getPlayer().addAction(1);
			}
			else {
				getPlayer().addBuy();
			}
		}

	}
	
}
