package cards.extra;
import cards.Card;


public class Champion extends Card {

	private static final long serialVersionUID = -41L;
	
	private boolean played;

	public Champion() {
		super("Champion", "Action-Duration", "Extras/Page", 6);
		played = false;
	}
	
	//Prevents attacks while in duration.
	//Implemented in the getAttackedPlayers() method of DominionGame.
	//Every action played gives +1 action.
	//Implemented in the playCard(int index) method of Player.

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		played = true;
	}
	
	@Override
	public boolean canBeGained() {
		return false;
	}

	@Override
	public void gainAction() {
		played = false;
	}

	@Override
	public void trashAction() {
		played = false;
	}

	@Override
	public void durationAction() {
		if(played) {
			getPlayer().deck.duration.remove(this);
			getPlayer().deck.play.add(this);
		}
	}

}
