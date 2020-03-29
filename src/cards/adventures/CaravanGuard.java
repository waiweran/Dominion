package cards.adventures;
import cards.Card;
import selectors.Selector;


public class CaravanGuard extends Card {

	private static final long serialVersionUID = 205L;

	public CaravanGuard() {
		super("Caravan Guard", "Action-Duration-Reaction", "Adventures", 3);

	}

	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
	}

	@Override
	public void reactAttack() {
		if(new Selector(getGame()).checkReact(getPlayer(), this)) {
			for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
				if(getPlayer().deck.hand.get(i) == this) {
					getPlayer().deck.play.add(getPlayer().deck.hand.remove(i));
				}
			}
			getPlayer().deck.deal();
		}
	}

	@Override
	public void durationAction() {
		getPlayer().addTreasure(1);
	}

}
