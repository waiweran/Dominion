package cards.defaults;
import cards.Card;




public class Trash extends Card {

	private static final long serialVersionUID = 0L;

	public Trash() {
		super("Trash", "Randomizer", "Default Cards", -1);
	}

	@Override
	public boolean canBeGained() {
		return false;
	}

	@Override
	public void gainAction() {
		throw new RuntimeException("Cannot gain trash card");
	}

}
