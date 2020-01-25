package cards.darkAges;

import java.io.File;
import java.util.List;

import cards.Card;
import gameBase.Player;
import selectors.SingleCardSelector;


public class Knight extends Card {
	
	private static final long serialVersionUID = -8122332251224756408L;
	
	public static final Card[] KNIGHTS = {new DameAnna(), new DameJosephine(), new DameMolly(), new DameNatalie(), new DameSylvia(), 
		new SirBailey(), new SirDestry(), new SirMartin(), new SirMichael(), new SirVander()};

	public Knight() {
		super("Knight", "Action-Attack-Knight", "Dark Ages", 5, 0, 0);
	}
	
	public Knight(String name, String type, int cost, int victory) {
		super(name, type, "Dark Ages", cost, victory, 0);		
	}

	@Override
	public void performAction() {
		specificAction(); 
		boolean knightTrashed = false;
		for(Player p : getGame().getAttackedPlayers()) {
			List<Card> revealed = p.deck.getDrawCards(2);
			for(int i = revealed.size() - 1; i >= 0; i--) {
				if(revealed.get(i).costsPotion() 
						|| revealed.get(i).getCost() < 3 
						|| revealed.get(i).getCost() > 6) {
					p.deck.discardCard(revealed.remove(i));
				}
			}
			if(revealed.size() > 0) {
				SingleCardSelector sc = new SingleCardSelector(getGame(), p, revealed, 
						"Trash a Card", this, true);
				Card choice = revealed.get(sc.getSelectedIndex());
				getGame().board.trashCard(choice);
				if(choice instanceof Knight) knightTrashed = true;
			}
		}
		if(knightTrashed) {
			getPlayer().deck.play.remove(this);
			getGame().board.trashCard(this);
		}		
	}
		
	@Override
	public File getImage() {
		String filename = getName().toLowerCase();
		if(filename == "knight") {
			return new File("Images/Cards/Dark Ages/Knights/knightsrandomizer.jpg");
		}
		String temp;
		int i = 0;
		while(filename.contains(" ")) {
			temp = filename.substring(i, filename.indexOf(" "));
			i = filename.indexOf(" ") + 1;
			if(i < filename.length()) temp += filename.substring(i);
			filename = temp;
		}
		i = 0;
		while(filename.contains("'")) {
			temp = filename.substring(i, filename.indexOf("'"));
			i = filename.indexOf("'") + 1;
			if(i < filename.length()) temp += filename.substring(i);
			filename = temp;
		}
		return new File("Images/Cards/Dark Ages/Knights/" + filename + ".jpg");
	}
	

	@Override
	public void gainAction() {
		throw new RuntimeException("Cannot gain generic knight card");
	}
			
	/**
	 * The specific action that each knight performs.
	 * In addition to the general knight action.
	 */
	protected void specificAction() {}

}
