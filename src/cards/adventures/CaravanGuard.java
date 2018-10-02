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
			getPlayer().deck.hand.remove(this);
			getPlayer().deck.play.add(this);
			getPlayer().deck.deal();
		}
	}

	@Override
	public void durationAction() {
		getPlayer().addTreasure(1);
	}

}
