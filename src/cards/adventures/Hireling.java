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
		for(int i = 0; i < getPlayer().deck.duration.size(); i++) {
			if(getPlayer().deck.duration.get(i) == this) {
				getPlayer().deck.play.add(getPlayer().deck.duration.remove(i));
			}
		}
	}

}
