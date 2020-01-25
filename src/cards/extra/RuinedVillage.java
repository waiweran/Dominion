package cards.extra;

public class RuinedVillage extends Ruins {

	private static final long serialVersionUID = -18L;

	public RuinedVillage() {
		super("Ruined Village", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().addAction(1);
	}
	
	@Override
	public void gainAction() {}
	
}
