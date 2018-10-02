package cards.adventures;
import cards.Card;


public class DistantLands extends Card {

	private static final long serialVersionUID = 207L;
	
	private boolean onTavern;

	public DistantLands() {
		super("Distant Lands", "Action-Reserve-Victory", "Adventures", 5);
		onTavern = false;
	}

	@Override
	public void performAction() {
		getPlayer().deck.sendToTavern(this);
		onTavern = true;
	}

	@Override
	public int gameEndAction() {
		if(onTavern) return 4;
		return 0;
	}

}
