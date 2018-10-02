package computerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cards.Card;
import cards.defaults.Copper;
import cards.defaults.Curse;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Computer player that uses the Big Money strategy.
 * @author Nathaniel Brooke
 * @version 05-12-2016
 */
public class BigMoneyPlayer extends ComputerPlayer {

	public BigMoneyPlayer(Player pComputer, DominionGame game) {
		super(pComputer, game);
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		ArrayList<Supply> treasures = new ArrayList<>();
		ArrayList<Supply> victories = new ArrayList<>();
		for(Supply s : options) {
			if(s.getTopCard().isTreasure()) {
				treasures.add(s);
			}
			else if(s.getTopCard().isVictory()) {
				victories.add(s);
			}
		}
		
		// Determine best victory card available
		Supply bestVictory = null;
		if(victories.size() > 0) {
			bestVictory = victories.get(0);
			for(Supply s : victories) {
				if(s.getTopCard().getCost() > bestVictory.getTopCard().getCost()) {
					bestVictory = s;
				}
			}
		}
		
		// Determine best treasure card available
		Supply bestTreasure = null;
		if(treasures.size() > 0) {
			bestTreasure = treasures.get(0);
			for(Supply s : treasures) {
				if(s.getTopCard().getCost() > bestTreasure.getTopCard().getCost()) {
					bestTreasure = s;
				}
			}			
		}
		
		// Choose victory or treasure
		if(bestTreasure != null && bestVictory == null) return bestTreasure;
		if(bestVictory != null && bestTreasure == null) return bestVictory;
		if(bestVictory != null && bestTreasure != null) {
			if(		// Colony or equivalent
					bestVictory.getTopCard().getCost() > 9 && access.setup.useProsperity() ||
					// Province or equivalent
					bestVictory.getTopCard().getCost() > 7 && !access.setup.useProsperity() ||
					bestVictory.getTopCard().getCost() > 7 && access.board.defaultCards.get(3).getQuantity() < 5 && access.setup.useProsperity() ||
					// Duchy or equivalent
					bestVictory.getTopCard().getCost() > 2 && access.board.defaultCards.get(2).getQuantity() < 4 && !access.setup.useProsperity() ||
					bestVictory.getTopCard().getCost() > 2 && access.board.defaultCards.get(3).getQuantity() < 4 && access.board.defaultCards.get(2).getQuantity() < 3 && access.setup.useProsperity() ||
					// Estate or equivalent
					access.board.defaultCards.get(2).getQuantity() < 2 && !access.setup.useProsperity() ||
					access.board.defaultCards.get(3).getQuantity() < 2 && access.board.defaultCards.get(2).getQuantity() < 3 && access.setup.useProsperity()
					) {
				return bestVictory;
			}
			return bestTreasure;
		}
			
		// No treasure or victory available, choose random supply
		Supply randChoice = options.get(access.random.nextInt(options.size()));
		while(randChoice.getTopCard().equals(new Curse())) {
			randChoice = options.get(access.random.nextInt(options.size()));
		}
		return randChoice;
	}

	@Override
	public Supply chooseSupply(List<Supply> options, boolean required, String choiceName) {
		if(!required) return null;
		else return options.get(access.random.nextInt(options.size()));
	}

	@Override
	public ArrayList<Integer> chooseCards(List<Card> choices, int num, boolean required, String choiceName) {
		ArrayList<Integer> out = new ArrayList<>();
		if(required) {
			while(out.size() < num) {
				int pick = access.random.nextInt(choices.size());
				if(!out.contains(pick)) {
					out.add(pick);
				}
			}
		}
		Collections.sort(out);
		return out;

	}

	@Override
	public int chooseCard(List<Card> choices, boolean required, String choiceName) {
		if(!required) return -1;
		else return access.random.nextInt(choices.size());
	}

	@Override
	public int choose(List<String> options, String choiceName) {
		return access.random.nextInt(options.size());
	}

	@Override
	public Card enterCard(Card messenger) {
		return new Copper();
	}

	@Override
	protected Card chooseAction(List<Card> options) {
		// Big money never plays actions.
		return null;
	}
	
	@Override
	protected Card chooseTreasure(List<Card> options) {
		// Play all treasures in order
		return options.get(0);
	}

	@Override
	protected Supply chooseBuy(List<Supply> options) {
		// Get a Copper
		Supply s = access.board.defaultCards.get(0);

		// Get a Silver
		if(player.getTreasure() >= 3 && access.board.defaultCards.get(1).isEmpty() == 0) {
			s = access.board.defaultCards.get(1);
		}

		// Get an Estate if there's only one province/colony
		if(player.getTreasure() >= 2 && !access.setup.useProsperity() && access.board.defaultCards.get(3).isEmpty() == 0
				&& access.board.defaultCards.get(5).getQuantity() < 2) {
			s = access.board.defaultCards.get(3);
		}
		else if(player.getTreasure() >= 2 && access.setup.useProsperity() && access.board.defaultCards.get(4).isEmpty() == 0 
				&& access.board.defaultCards.get(7).getQuantity() < 2) {
			s = access.board.defaultCards.get(4);
		}

		// Get a Gold
		if(player.getTreasure() >= 6 && access.board.defaultCards.get(2).isEmpty() == 0) {
			s = access.board.defaultCards.get(2);
		}

		// Get a Duchy if there's two or fewer provinces/colonies
		if(player.getTreasure() >= 5 && !access.setup.useProsperity() && access.board.defaultCards.get(4).isEmpty() == 0
				&& access.board.defaultCards.get(5).getQuantity() < 3) {
			s = access.board.defaultCards.get(4);
		}
		else if(player.getTreasure() >= 5 && access.setup.useProsperity() && access.board.defaultCards.get(5).isEmpty() == 0 
				&& access.board.defaultCards.get(7).getQuantity() < 3) {
			s = access.board.defaultCards.get(5);
		}

		// Get a Platinum
		if(access.setup.useProsperity() && player.getTreasure() >= 9 && access.board.defaultCards.get(3).isEmpty() == 0) {
			s = access.board.defaultCards.get(3);
		}

		// Get a Province (if there's 2 or fewer colonies)
		if(player.getTreasure() >= 8 && !access.setup.useProsperity() && access.board.defaultCards.get(5).isEmpty() == 0) {
			s = access.board.defaultCards.get(5);
		}
		else if(player.getTreasure() >= 8 && access.setup.useProsperity() && access.board.defaultCards.get(6).isEmpty() == 0 
				&& access.board.defaultCards.get(7).getQuantity() < 3) {
			s = access.board.defaultCards.get(6);
		}

		// Get a Colony
		if(access.setup.useProsperity() && player.getTreasure() >= 11 && access.board.defaultCards.get(7).isEmpty() == 0) {
			s = access.board.defaultCards.get(7);
		}
		
		// Final check
		if(!options.contains(s)) throw new RuntimeException("Supply " + s + " not on purchase list " + options);
		
		// Buy it
		return s;
	}
	
	@Override
	public String getName() {
		return "Big Money";
	}

}
