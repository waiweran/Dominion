package cards.darkAges;

import java.util.ArrayList;

import cards.Card;
import cards.extra.Mercenary;
import gameBase.Player;
import gameBase.Supply;
import selectors.MultiCardSelector;
import selectors.Selector;

public class Urchin extends Card {

	private static final long serialVersionUID = 186L;
	
	public Urchin() {
		super("Urchin", "Action-Attack", "Dark Ages", 3);
	}
	
	@Override
	public ArrayList<Supply> setupRequirement() {
		ArrayList<Supply> out = new ArrayList<Supply>();
		out.add(new Supply(new Mercenary(), 10));
		return out;
	}
	
	@Override
	public void performAction() {
		getPlayer().deck.deal();
		getPlayer().addAction(1);
		
		ArrayList<MultiCardSelector> selectors = new ArrayList<>();
		for(Player p : getGame().getAttackedPlayers()) {
			MultiCardSelector sd = new MultiCardSelector(getGame(), p, p.deck.hand, 
					"Discard down to 4", this, p.deck.hand.size() - 4, true);
			sd.show();
			selectors.add(sd);
		}
		for(int selnum = 0; selnum < selectors.size(); selnum++) {
			Player p = getGame().getAttackedPlayers().get(selnum);
			MultiCardSelector sd = selectors.get(selnum);
			int backIndex = 0;
			for(int i : sd.getSelectedIndex()) {
				p.deck.discardCard(i - backIndex++);
			}
		}
	}

	@Override
	public void cleanupAction() {
		boolean afterUrchin = false;
		for(Card c : getPlayer().deck.play) {
			if(!afterUrchin) {
				afterUrchin = c instanceof Urchin;
			}
			else if(c.isAttack()) {
				if(new Selector(getGame()).checkExchange(this, new Mercenary(), 
						"You may trash your Urchin to gain a Mercenary")) {
					getPlayer().deck.play.remove(this);
					getGame().board.trashCard(this);
					getPlayer().deck.gain(getGame().board.findSupply(new Mercenary()).takeCard());
				}
				return;
			}
		}
	}
	
}
