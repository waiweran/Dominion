package cards.defaults;
import cards.Card;
import cards.hinterlands.Duchess;
import selectors.Selector;

public class Duchy extends Card {

	private static final long serialVersionUID = -4L;

	public Duchy() {
		super("Duchy", "Victory", "Default Cards", 5, 3, 0);
	}

	@Override
	public void gainAction() {
		if(getGame().board.findSupply(new Duchess()) != null) {
			String[] options2 = {"No", "Yes"};
			Selector sel = new Selector(getGame());
			sel.setPlayer(getPlayer());
			if(1 == sel.showQuestionDialog(this, 
					"Would you like to Gain a Duchess?", options2)) {
				boolean temp = getPlayer().buying;
				getPlayer().buying = false;
				getPlayer().deck.gain(getGame().board.findSupply(new Duchess()).takeCard());
				getPlayer().buying = temp;
			}
		}
	}

}
