package cards.darkAges;

public class DameSylvia extends Knight {

	private static final long serialVersionUID = -27L;

	public DameSylvia() {
		super("Dame Sylvia", "Action-Attack-Knight", 5, 0);
	}

	@Override
	public void specificAction() {
		getPlayer().addTreasure(2);
	}

}
