package cards.adventures;
import cards.Card;


public class Port extends Card {

	private static final long serialVersionUID = 221L;

	public Port() {
		super("Port", "Action", "Adventures", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(2);
	}
	
	@Override
	public void gainAction() {
		if(getPlayer().buying) {
			getPlayer().buying = false;
			getPlayer().deck.gain(getGame().board.findSupply(this).takeCard());
			getPlayer().buying = true;
		}
	}

}
