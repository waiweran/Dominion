package cards.prosperity;
import cards.Card;
import cards.defaults.Copper;


public class CountingHouse extends Card {

	private static final long serialVersionUID = 94L;

	public CountingHouse() {
		super("Counting House", "Action", "Prosperity", 5);
	}

	@Override
	public void performAction() {
		for(int i = getPlayer().deck.discard.size() - 1; i >= 0; i--) {
			if(getPlayer().deck.discard.get(i).equals(new Copper())) {
				getPlayer().deck.hand.add(
						getPlayer().deck.discard.remove(i));
			}
		}
	}

}
