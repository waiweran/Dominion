package cards.darkAges;

public class DameMolly extends Knight {

	private static final long serialVersionUID = -25L;

	public DameMolly() {
		super("Dame Molly", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		getPlayer().addAction(2);
	}

}
