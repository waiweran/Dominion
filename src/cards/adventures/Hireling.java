package cards.adventures;
import cards.Card;


public class Hireling extends Card {

	private static final long serialVersionUID = 214L;

	public Hireling() {
		super("Hireling", "Action-Duration", "Adventures", 6);
		
	}

	@Override
	public void durationAction() {
		getPlayer().deck.deal();
		getPlayer().deck.duration.remove(this);
		getPlayer().deck.play.add(this);
	}

}
