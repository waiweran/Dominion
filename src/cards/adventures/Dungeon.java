package cards.adventures;
import cards.Card;
import selectors.MultiCardSelector;


public class Dungeon extends Card {

	private static final long serialVersionUID = 208L;

	public Dungeon() {
		super("Dungeon", "Action-Duration", "Adventures", 3);
		
	}
	
	@Override
	public void performAction() {
		getPlayer().addAction(1);
		dungeonAction();

	}
	
	@Override
	public void durationAction() {
		dungeonAction();
	}
	
	private void dungeonAction() {
		getPlayer().deck.deal();
		getPlayer().deck.deal();
		MultiCardSelector sd = new MultiCardSelector(getGame(), getPlayer().deck.hand, 
				"Please select 2 cards to discard", this, 2, true);
		int backIndex = 0;
		for(int i : sd.getSelectedIndex()) {
			getPlayer().deck.discardCard(i - backIndex);
			backIndex++;
		}
	}

}
