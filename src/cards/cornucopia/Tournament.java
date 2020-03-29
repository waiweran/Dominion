package cards.cornucopia;

import java.util.ArrayList;

import cards.Card;
import cards.defaults.Duchy;
import cards.defaults.Province;
import gameBase.Player;
import selectors.Selector;
import selectors.SingleCardSelector;

public class Tournament extends Card {

	private static final long serialVersionUID = 126L;

	public Tournament() {
		super("Tournament", "Action", "Cornucopia", 4);
	}

	@Override
	public void performAction() {
		getPlayer().addAction(1);
		boolean iHaveOne = false;
		boolean theyHaveOne = false;
		if(getPlayer().deck.hand.contains(new Province())) {
			iHaveOne = true;
		}
		for(Player p : getGame().getOtherPlayers()) {
			if(p.deck.hand.contains(new Province())) {
				theyHaveOne = true;
				break;
			}
		}
		if(iHaveOne) {
			for(int i = 0; i < getPlayer().deck.hand.size(); i++) {
				if(getPlayer().deck.hand.get(i) instanceof Province) {
					getPlayer().deck.discardCard(i);
					break;
				}
			}
			if(getGame().board.findSupply(new Duchy()).isEmpty() == 1) {
				if(!getGame().board.prizes.isEmpty()) {
					SingleCardSelector sc = new SingleCardSelector(getGame(), getGame().board.prizes,
							"Select a Prize to put on top of your Deck", this, true);
					getPlayer().deck.gain(getGame().board.prizes.remove(sc.getSelectedIndex()), 1);
				}
			}
			else {
				ArrayList<Card> selection = new ArrayList<>();
				selection.add(getGame().board.findSupply(new Duchy()).getCard());
				selection.addAll(getGame().board.prizes);
				SingleCardSelector sc = new SingleCardSelector(getGame(), selection,
						"Select a Prize to put on top of your Deck", this, true);
				int selected = sc.getSelectedIndex();
				if(selected == 0) {
					getPlayer().deck.gain(getGame().board.findSupply(new Duchy()).takeCard(), 1);
				}
				else {
					getPlayer().deck.gain(getGame().board.prizes.remove(selected - 1), 1);
				}
			}
		}
		if(theyHaveOne) {
			new Selector(getGame()).showQuestionDialog(this, "Other Players Revealed Provinces", "Ok");
		}
		else {
			new Selector(getGame()).showQuestionDialog(this, "No one else Revealed a Province", "Ok");
			getPlayer().deck.deal();
			getPlayer().addTreasure(1);
		}
	}

}
