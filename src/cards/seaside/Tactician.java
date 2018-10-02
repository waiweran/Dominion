package cards.seaside;

import cards.Card;

public class Tactician extends Card {

	private static final long serialVersionUID = 73L;

	private boolean shouldDoDuration;

	public Tactician() {
		super("Tactician", "Action-Duration", "Seaside", 5);
		shouldDoDuration = false;
	}

	@Override
	public void performAction() {
		if(getPlayer().deck.hand.size() > 0) {
			for(int i = getPlayer().deck.hand.size() - 1; i >= 0; i--) {
				getPlayer().deck.discardCard(i);
			}
			shouldDoDuration = true;
		}
	}

	@Override
	public void durationAction() {
		if(shouldDoDuration) {
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().deck.deal();
			getPlayer().addBuy();
			getPlayer().addAction(1);
			shouldDoDuration = false;
		}
	}

}
