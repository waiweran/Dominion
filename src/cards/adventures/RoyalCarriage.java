package cards.adventures;
import cards.Card;
import selectors.Selector;


public class RoyalCarriage extends Card {

	private static final long serialVersionUID = 226L;

	public RoyalCarriage() {
		super("Royal Carriage", "Action-Reserve", "Adventures", 5);
	}
	
	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().deck.sendToTavern(this);
	}

	@Override
	public void tavernAction() {
		if (getGame().gamePhase == 1 && getPlayer().deck.play.size() >= 2) {
			Card c = getPlayer().deck.play.get(
					getPlayer().deck.play.size() - 2);
			if(c.isAction()) {
				if (1 == new Selector(getGame()).showQuestionDialog(this,
						"Replay " + c.getName() + "?", "Cancel", "Ok")) {
					getPlayer().deck.callFromTavern(this);
					try {
						c.performAction();
					}
					catch(Exception e) {
						throw new RuntimeException(c.getName() + 
								" may behave incorrectly with the Royal Carriage", e);
					}
				}
			}
			else {
				new Selector(getGame()).showTavernError(getName() + " cannot be called on " + c.getName());
			}
		}
		else {
			new Selector(getGame()).showTavernError( getName() + " cannot be called now");
		}
	}

}
