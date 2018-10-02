package cards.prosperity;

import cards.Card;
import cards.defaults.Curse;
import gameBase.Player;


public class Mountebank extends Card {

	private static final long serialVersionUID = 104L;

	public Mountebank() {
		super("Mountebank","Action-Attack", "Prosperity", 5);
		
	}

	@Override
	public void performAction() {
		getPlayer().addTreasure(2);
		for(Player p : getGame().getAttackedPlayers()) {
			Card derp = new Curse();
			if (p.deck.hand.contains(derp))	{
				int index = 0;
				for (int i = 0; i < p.deck.hand.size(); i++)	{
					if (p.deck.hand.get(i).compareTo(derp) == 0)	{
						index = i;
					}
				}
				p.deck.discardCard(index);
			}
			else	{
				p.deck.gain(getGame().board.getCurse().takeCard());
				p.deck.gain(getGame().board.getCopper().takeCard());
			}
		}

	}
	
}
