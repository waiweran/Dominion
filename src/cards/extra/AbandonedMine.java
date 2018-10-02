package cards.extra;
import cards.Card;


public class AbandonedMine extends Card {

	private static final long serialVersionUID = -15L;

	public AbandonedMine() {
		super("Abandoned Mine", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().addTreasure(1);
	}

}
