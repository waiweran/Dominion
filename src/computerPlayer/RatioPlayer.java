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
	
	private static final int[] WEIGHTS = {48, 37, 15}; //Treasure, action, victory, must total 100
	
	private BigMoneyPlayer money;
	private RandomPlayer random;

	public RatioPlayer(Player pComputer, DominionGame game) {
		super("Ratio Player", pComputer, game);
		money = new BigMoneyPlayer(pComputer, access);
		random = new RandomPlayer(pComputer, access);
	}
	
	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		// if the game is nearing an end, just buy like big money
		if((!access.setup.useProsperity() && access.board.defaultCards.get(5).getQuantity() < 3)
				|| (access.setup.useProsperity() && access.board.defaultCards.get(7).getQuantity() < 3)) {
			return money.chooseGain(options, required);
		}
		
		// List options based on ratio, buy randomly from correct category.
		List<Supply> possibilities = getRatioOptions();
		if(possibilities.size() > 0) {
			return random.chooseGain(possibilities, required);
		}
		
		// Buy randomly if no ratio options available.
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
	public int chooseCard(List<Card> choices, boolean required, String choiceName) {
		return random.chooseCard(choices, required, choiceName);
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
		int choice = access.random.nextInt(options.size());
		// Choose whether to play a card
		Card selectedCard;
		if(access.random.nextInt(options.size()*5) == 0) {
			choice = -1;
			selectedCard = null;
		}
		else {
			selectedCard = options.get(choice);
		}
		return selectedCard;
	}
	
	@Override
	protected Card chooseTreasure(List<Card> options) {
		// Play all treasures in order
		return options.get(0);
	}

	@Override
	protected Supply chooseBuy(List<Supply> options) {
		
		// if the game is nearing an end, just buy like big money
		if((!access.setup.useProsperity() && access.board.defaultCards.get(5).getQuantity() < 3)
				|| (access.setup.useProsperity() && access.board.defaultCards.get(7).getQuantity() < 3)) {
			return money.chooseBuy(options);
		}
		
		// List options based on ratio, buy from correct category.
		List<Supply> possibilities = getRatioOptions();
		if(possibilities.size() > 0) {
			return random.chooseBuy(possibilities);
		}
		
		// Buy randomly if no ratio options available.
		return random.chooseBuy(options);
	}	
	
	/**
	 * Generates a list of cards in the category the player most needs.
	 * @return List of cards to choose buy from.
	 */
	private List<Supply> getRatioOptions() {
		// Count cards in deck
		int[] cardTypes = new int[WEIGHTS.length];
		int total = 0;
		for(Card c : player.deck.getDeck()) {
			if(c.isTreasure()) cardTypes[0] += c.getCost() + 1;
			if(c.isAction()) cardTypes[1] += c.getCost() + 1;
			if(c.isVictory()) cardTypes[2]+= c.getCost() + 1;
			total += c.getCost() + 1;
		}
		
		//Find the one most below the proper ratio
		for(int i = 0; i < cardTypes.length; i++) {
			cardTypes[i] = (cardTypes[i]*100)/total;
		}
		int maxIndex = 0;
		int maxVal = WEIGHTS[0] - cardTypes[0];
		for(int i = 1; i < cardTypes.length; i++) {
			int newVal = WEIGHTS[i] - cardTypes[i];
			if(newVal > maxVal) {
				maxVal = newVal;
				maxIndex = i;
			}
		}
		
		// Get card of correct type
		ArrayList<Supply> possibilities = new ArrayList<Supply>();
		if(maxIndex == 0) {
			for(Supply s : access.board.getAllSupplies()) {
				if(player.getTreasure() - s.getTopCard().getCost() == 0 && s.getTopCard().canBeGained()
						&& (!s.getTopCard().costsPotion() || player.potion > 0)
						&& s.getTopCard().isTreasure()) {
					possibilities.add(s);
				}
			}
		}
		else if(maxIndex == 1) {
			for(Supply s : access.board.getAllSupplies()) {
				if(player.getTreasure() - s.getTopCard().getCost() == 0 && s.getTopCard().canBeGained()
						&& (!s.getTopCard().costsPotion() || player.potion > 0)
						&& s.getTopCard().isAction()) {
					possibilities.add(s);
				}
			}
					}
		else if(maxIndex == 2) {
			for(Supply s : access.board.getAllSupplies()) {
				if(player.getTreasure() - s.getTopCard().getCost() == 0 && s.getTopCard().canBeGained()
						&& (!s.getTopCard().costsPotion() || player.potion > 0)
						&& s.getTopCard().isVictory()) {
					possibilities.add(s);
				}
			}
		}
		return possibilities;
	}



}
