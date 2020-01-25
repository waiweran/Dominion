package cards.extra;

public class RuinedMarket extends Ruins {

	private static final long serialVersionUID = -17L;

	public RuinedMarket() {
		super("Ruined Market", "Action-Ruins", "Extras/Ruins", 0);
	}

	@Override
	public void performAction() {
		super.getPlayer().addBuy();
	}
	
	@Override
	public void gainAction() {}

}
