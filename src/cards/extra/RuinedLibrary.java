package cards.extra;

public class RuinedLibrary extends Ruins {

	private static final long serialVersionUID = -16L;

	public RuinedLibrary() {
		super("Ruined Library", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().deck.deal();
	}
	
	@Override
	public void gainAction() {}
	
}
