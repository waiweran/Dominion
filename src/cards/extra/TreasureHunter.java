package cards.extra;
import cards.Card;
import gameBase.Supply;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import selectors.Selector;


public class TreasureHunter extends Card {

	private static final long serialVersionUID = -38L;

	public TreasureHunter() {
		super("Treasure Hunter", "Action-Traveller", "Extras/Page", 3);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		getPlayer().addTreasure(1);
		int numSilver = getGame().getRightPlayer().deck.gained.size();
		for(int i = 0; i < numSilver; i++) {
			getPlayer().deck.gain(getGame().board.getSilver().takeCard());
		}
	}
	
	@Override
	public boolean canBeGained() {
		return false;
	}

	@Override
	public void cleanupAction() {
		Supply getFrom = getGame().board.findSupply(new Warrior());
		if(getFrom.isEmpty() == 1) return;
		HBox message = new HBox(10);
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
