package cards.seaside;
import cards.Card;
import cards.defaults.Copper;
import gameBase.Player;


public class Cutpurse extends Card {

	private static final long serialVersionUID = 55L;

	public Cutpurse() {
		super("Cutpurse", "Action-Attack", "Seaside", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		
		for(Player p : getGame().getAttackedPlayers()) {
			if(p.deck.hand.contains(new Copper())) {
				p.deck.discardCard(p.deck.hand.indexOf(new Copper()));
			}
		}
	}

}
