package cards.extra;
import cards.Card;


public class RuinedVillage extends Card {

	private static final long serialVersionUID = -18L;

	public RuinedVillage() {
		super("Ruined Village", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().addAction(1);
	}
	
}
