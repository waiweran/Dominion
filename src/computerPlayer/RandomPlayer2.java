package computerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Random player that purchases cards at random, weighted by card cost.
 * @author Nathaniel
 * @version 04-20-2019
 */
public class RandomPlayer2 extends ComputerPlayer {
	
	private ExpertSystem exsys;

	public RandomPlayer2(Player pComputer, DominionGame game) {
		super(pComputer, game);
		exsys = new ExpertSystem();
	}
	
	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		double[] probs = new double[options.size() + 1];
		probs[0] = 2;
		for(int i = 1; i < probs.length; i++) {
			probs[i] = options.get(i - 1).getTopCard().getCost() + 1;
		}
		for(int i = 0; i < probs.length; i++) {
			probs[i] = Math.pow(probs[i], 2) * access.random.nextDouble();
		}
		int maxIndex = 0;
		if(required) maxIndex = 1;
		for(int i = 1; i < probs.length; i++) {
			if(probs[maxIndex] < probs[i]) {
				maxIndex = i;
			}
		}	
		if(maxIndex == 0) return null;
		return options.get(maxIndex - 1);
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
		if(choiceName.equals("Cellar")) {
			return exsys.chooseCardsCellar(choices);
		}
		if(choiceName.equals("Militia")) {
			return exsys.chooseCardsMilitia(choices, num);
		}
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
		if(choiceName.equals("Mine")) {
			return exsys.chooseCardMine(choices);
		}
		if(choiceName.equals("Remodel")) {
			return exsys.chooseCardRemodel(choices);
		}
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
		try {
			return exsys.chooseAction(options);
		} catch(Exception e) {} // Do nothing, probably not base game
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
		return chooseGain(options, false);
	}

	@Override
	public String getName() {
		return "Random Player 2";
	}

}
