package cards.darkAges;

import cards.Card;

public class Fortress extends Card {

	private static final long serialVersionUID = 167L;

	public Fortress() {
		super("Fortress", "Action", "Dark Ages", 4);
	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		getPlayer().addAction(1);
	}

	@Override
	public void trashAction() {
		getPlayer().deck.hand.add(getGame().board.trash.get(getGame().board.trash.size() -1));
	}

}
