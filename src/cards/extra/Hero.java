package cards.extra;
import cards.Card;
import gameBase.Supply;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import selectors.Selector;
import selectors.SupplySelector;


public class Hero extends Card {

	private static final long serialVersionUID = -40L;

	public Hero() {
		super("Hero", "Action-Traveller", "Extras/Page", 5);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		SupplySelector getm = new SupplySelector(getGame(), getName(), 
				"Gain a Treasure", 0, 20);
		getm.setPotion(true);
		getm.setCardSelector(c -> c.isTreasure());
		getPlayer().deck.gain(getm.getGainedCard());
	}
	
	@Override
	public boolean canBeGained() {
		return false;
	}

	@Override
	public void cleanupAction() {
		Supply getFrom = getGame().board.findSupply(new Champion());
		if(getFrom.isEmpty() == 1) return;
		HBox message = new HBox(10);
		message.setAlignment(Pos.CENTER);
		message.getChildren().addAll(new ImageView(getImage().toURI().toString()), new Text("-->"), 
				new ImageView(getFrom.getTopCard().getImage().toURI().toString()));
		if(new Selector(getGame()).checkExchange(this, getFrom.getTopCard(), 
				"You may exchange your " + getName() + " for a " + getFrom.getCard().getName())) {			
			Card upgrade = getFrom.takeCard().clone();
			upgrade.passPlayer(getPlayer());
			upgrade.passGame(getGame());
			getPlayer().deck.play.remove(this);
			getGame().board.findSupply(this).putOneBack(this);
			getPlayer().deck.play.add(upgrade);
		}
	}

}
