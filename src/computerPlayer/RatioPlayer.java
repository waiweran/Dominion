package computerPlayer;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Random player that purchases actions, victory, and treasures in a predefined ratio.
 * @author Nathaniel
 * @version 05-04-2017
 */
public class RatioPlayer extends ComputerPlayer {
	
	private static final double[] WEIGHTS = {0.37, 0.48, 0.15}; //action, treasure, victory
	
	private BigMoneyPlayer money;
	private RandomPlayer random;

	public RatioPlayer(Player pComputer, DominionGame game) {
		super("Ratio Player", pComputer, game);
		money = new BigMoneyPlayer(pComputer, access);
		random = new RandomPlayer(pComputer, access);
	}
	
	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		
		// Determine which category to buy from based on ratio	
		ArrayList<Supply> possibilities = new ArrayList<>();
		double actions = 0, treasure = 0, victory = 0;
		for(Card c : player.deck.getDeck()) {
			if(c.isAction()) actions++;
			if(c.isTreasure()) treasure++;
			if(c.isVictory()) victory++;
		}
		double size = (double)player.deck.size();
		actions = WEIGHTS[0] - actions / size;
		treasure = WEIGHTS[1] - treasure / size;
		victory = WEIGHTS[2] - victory / size;
		
		// Select cards to buy from based on category
		if(actions > treasure && actions > victory) {
			// buy an action
			for(Supply s : options) {
				if(s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
		}
		else if(treasure > victory) {
			// buy a treasure
			for(Supply s : options) {
				if(s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
		}
		else {
			// buy a victory
			for(Supply s : options) {
				if(s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
		}
		
		if(possibilities.size() > 0) {
			return random.chooseGain(possibilities, required);
		}
		
		// gain randomly if no ratio options available.
		return random.chooseGain(options, required);	
	}
	
	@Override
	public Supply chooseSupply(List<Supply> options, boolean required, String choiceName) {
		return random.chooseSupply(options, required, choiceName);
	}

	@Override
	public ArrayList<Integer> chooseCards(List<Card> choices, int num, boolean required, String choiceName) {
		return random.chooseCards(choices, num, required, choiceName);
	}

	@Override
	public int chooseForCards(List<Card> cards, List<String> options, String choiceName) {
		return random.chooseForCards(cards, options, choiceName);
	}

	@Override
	public int choose(List<String> options, String choiceName) {
		return random.choose(options, choiceName);
	}

	@Override
	public Card enterCard(Card messenger) {
		return random.enterCard(messenger);
	}
	
	@Override
	protected Card chooseAction(List<Card> options) {
		return random.chooseAction(options);
	}
	
	@Override
	protected Card chooseTreasure(List<Card> options) {
		return random.chooseTreasure(options);
	}

	@Override
	protected Supply chooseBuy(List<Supply> options) {
		
		// If near the end of the game, just buy like big money
		if((!access.setup.useProsperity() && access.board.defaultCards.get(5).getQuantity() < 3)
				|| (access.setup.useProsperity() && access.board.defaultCards.get(7).getQuantity() < 3)) {
			return money.chooseBuy(options);
		}
		
		// Determine which category to buy from based on ratio	
		ArrayList<Supply> possibilities = new ArrayList<>();
		double actions = 0, treasure = 0, victory = 0;
		for(Card c : player.deck.getDeck()) {
			if(c.isAction()) actions++;
			if(c.isTreasure()) treasure++;
			if(c.isVictory()) victory++;
		}
		double size = (double)player.deck.size();
		actions = WEIGHTS[0] - actions / size;
		treasure = WEIGHTS[1] - treasure / size;
		victory = WEIGHTS[2] - victory / size;
		
		// Select cards to buy from based on category
		if(actions > treasure && actions > victory) {
			// buy an action
			for(Supply s : options) {
				if(s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
		}
		else if(treasure > victory) {
			// buy a treasure
			for(Supply s : options) {
				if(s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
		}
		else {
			// buy a victory
			for(Supply s : options) {
				if(s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
		}
		
		if(possibilities.size() > 0) {
			return random.chooseBuy(possibilities);
		}
		
		// Buy with big money if no ratio options available.
		return money.chooseBuy(options);
	}	
	


}
