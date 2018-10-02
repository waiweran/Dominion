package cards.extra;
import cards.Card;


public class Princess extends Card {

	private static final long serialVersionUID = -36L;

	public Princess() {
		super("Princess", "Action-Prize", "Extras/Prize", 0);
	}
	
	@Override
	public void performAction() {
		getPlayer().addBuy();
		getPlayer().bridge += 2;		
	}
	
}
