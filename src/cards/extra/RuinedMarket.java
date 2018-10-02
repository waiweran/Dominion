package cards.extra;
import cards.Card;


public class RuinedMarket extends Card {

	private static final long serialVersionUID = -17L;

	public RuinedMarket() {
		super("Ruined Market", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().addBuy();
	}

}
