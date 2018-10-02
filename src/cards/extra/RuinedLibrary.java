package cards.extra;
import cards.Card;


public class RuinedLibrary extends Card {

	private static final long serialVersionUID = -16L;

	public RuinedLibrary() {
		super("Ruined Library", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().deck.deal();
	}
	
}
