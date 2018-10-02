package cards.darkAges;

public class SirBailey extends Knight {

	private static final long serialVersionUID = -28L;

	public SirBailey() {
		super("Sir Bailey", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
	}

}
