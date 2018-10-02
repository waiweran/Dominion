package cards.darkAges;

public class SirMartin extends Knight {

	private static final long serialVersionUID = -30L;

	public SirMartin() {
		super("Sir Martin", "Action-Attack-Knight", 4, 0);
	}

	@Override
	public void specificAction() {
		getPlayer().addBuy();
		getPlayer().addBuy();
	}

}
