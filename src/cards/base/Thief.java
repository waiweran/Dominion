package cards.base;

import java.util.List;

import cards.Card;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;


public class Thief extends Card {

	private static final long serialVersionUID = 21L;

	public Thief() {
		super("Thief", "Action-Attack", "Base", 4);
	}

	@Override
	public void performAction() {
		for(Player p : getGame().getAttackedPlayers()) {
			List<Card> revealed = p.deck.getDrawCards(2);
			for(int i = revealed.size() - 1; i >= 0; i--){
				if(!revealed.get(i).isTreasure()) {
					p.deck.discardCard(revealed.remove(i));
				}
			}
			if(revealed.size() > 0) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), revealed,
						"Steal a treasure", this, true);
				Card c = revealed.get(sc.getSelectedIndex());
				if (new Selector(getGame()).showCardDialog(this, "", c,
						"Trash", "Take") == 0)	{
					getGame().board.trashCard(c);
				}
				else	{
					getPlayer().deck.gain(c);
				}
			}
			else {
				new Selector(getGame()).showQuestionDialog(this, p.getPlayerName()
						+ " revealed no treasures", "Ok");
			}
			p.deck.discardCards(revealed);
		}
	}

}
