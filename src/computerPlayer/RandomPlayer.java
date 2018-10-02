package computerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cards.Card;
import cards.defaults.Curse;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

public class RandomPlayer extends ComputerPlayer {

	public RandomPlayer(Player pComputer, DominionGame game) {
		super(pComputer, game);
	}
	
	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		int choice = 0;
		Collections.sort(options, new CostComparator());
		int maxCost = options.get(0).getCard().getCost();
		for(int i = 1; i < options.size(); i++) {
			if(i + 1 == options.size() || options.get(i).getCard().getCost() < maxCost) {
				choice = access.random.nextInt(i);
				while(options.size() > 1 && options.get(choice).getCard().equals(new Curse())) {
					choice = access.random.nextInt(i);
				}
				break;
			}
		}
		return options.get(choice);
	}
	
	@Override
	public Supply chooseSupply(List<Supply> options, boolean required, String choiceName) {
		Supply s = options.get(access.random.nextInt(options.size()));
		if(required) {
			return s;
		}
		else {
			return access.random.nextInt(options.size() + 1) == 0 ? null : s;
		}
	}

	@Override
	public ArrayList<Integer> chooseCards(List<Card> choices, 
			int num, boolean required, String choiceName) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		if(required) {
			while(out.size() < num) {
				int pick = access.random.nextInt(choices.size());
				if(!out.contains(pick)) {
					out.add(pick);
				}
			}
		}
		else {
			for(int i = 0; i < num; i++) {
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
		if(required) {
			return access.random.nextInt(choices.size());
		}
		else {
			return access.random.nextInt(choices.size() + 1) - 1;
		}
	}


	@Override
	public int choose(List<String> options, String choiceName) {
		return access.random.nextInt(options.size());
	}

	@Override
	public Card enterCard(Card messenger) {
		return access.allCards.findCard("Silver");
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
		int choice = -1;
		Collections.sort(options, new CostComparator());
		int maxCost = options.get(0).getCard().getCost();
		for(int i = 1; i < options.size(); i++) {
			if(i + 1 == options.size() || options.get(i).getCard().getCost() < maxCost) {
				choice = access.random.nextInt(i);
				while(options.get(choice).getCard().equals(new Curse())) {
					choice = access.random.nextInt(i);
					if(options.size() == 1) {
						choice = -1; 
						break;
					}
				}
				break;
			}
		}
		// Decide whether or not to buy
		if(choice < 0 || access.random.nextInt(options.get(choice).getCard().getCost()*5 + 1) == 0) {
			return null;		
		}
		
		return options.get(choice);
	}

	@Override
	public String getName() {
		return "Random Player";
	}

}
