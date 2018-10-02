package cards.extra;
import cards.Card;


public class Necropolis extends Card {

	private static final long serialVersionUID = -13L;

	public Necropolis() {
		super("Necropolis", "Action-Shelter", "Extras/Shelter", 1);
	}

	@Override
	public void performAction() {
		super.getPlayer().addAction(2);
	}

}
