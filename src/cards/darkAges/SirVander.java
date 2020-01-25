package cards.darkAges;

public class SirVander extends Knight {

	private static final long serialVersionUID = -32L;

	public SirVander() {
		super("Sir Vander", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {}
	
	@Override
	public void trashAction() {
		getPlayer().deck.gain(getGame().board.getGold().takeCard());
	}
	
	@Override
	public void gainAction() {}

}
