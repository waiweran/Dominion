package cards.seaside;
import cards.Card;


public class FishingVillage extends Card {

	private static final long serialVersionUID = 58L;

	public FishingVillage() {
		super("Fishing Village", "Action-Duration", "Seaside", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(2);
		getPlayer().addTreasure(1);
	}
	
	@Override
	public void durationAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
	}

}
