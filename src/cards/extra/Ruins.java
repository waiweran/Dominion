package cards.extra;
import cards.Card;


public class Ruins extends Card {

	private static final long serialVersionUID = -20L;

	public Ruins() {
		super("Ruins", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void gainAction() {
		throw new RuntimeException("Cannot gain generic ruins card");
	}

}
