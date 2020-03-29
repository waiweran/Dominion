package cards.hinterlands;
import java.util.List;

import cards.Card;
import cards.defaults.Gold;
import cards.defaults.Silver;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;


public class NobleBrigand extends Card {

	private static final long serialVersionUID = 144L;

	public NobleBrigand() {
		super("Noble Brigand", "Action-Attack", "Hinterlands", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(1);
		steal();
	}

	@Override
	public void gainAction() {
		if(getPlayer().buying) {
			getPlayer().buying = false;
			steal();
			getPlayer().buying = true;
		}
	}
	
	private void steal() {
		for(Player p : getGame().getAttackedPlayers()) {
			List<Card> revealed = p.deck.getDrawCards(2);
			for(int i = revealed.size() - 1; i >= 0; i--){
				if(!revealed.get(i).isTreasure()) {
					p.deck.discardCard(revealed.remove(i));
				}
			}
			if(revealed.size() == 0) {
				p.deck.gain(getGame().board.getCopper().takeCard());
			}
			for(int i = revealed.size() - 1; i >= 0; i--){
				if(!(revealed.get(i) instanceof Silver
						|| revealed.get(i) instanceof Gold)) {
					p.deck.discardCard(revealed.remove(i));
				}
			}
			if(revealed.size() > 0) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), revealed,
						"Steal a treasure", this, true);
				Card c = revealed.remove(sc.getSelectedIndex());
				getPlayer().deck.gain(c);
			}
			else {
				new Selector(getGame()).showQuestionDialog(this, p.getPlayerName()
						+ " revealed no silver or gold", getName(), "Ok");
			}
			p.deck.discardCards(revealed);
		}
	}

}
