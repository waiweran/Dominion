package cards.darkAges;

public class SirDestry extends Knight {

	private static final long serialVersionUID = -29L;

	public SirDestry() {
		super("Sir Destry", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
	}
	
	@Override
	public void gainAction() {}

}
