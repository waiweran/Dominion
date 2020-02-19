package computerPlayer;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

public class WebMLPlayer extends ComputerPlayer {
	
	private RandomPlayer rand;

	public WebMLPlayer(Player pComputer, DominionGame game) {
		super("ML Player", pComputer, game);
		rand = new RandomPlayer(pComputer, game);
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		List<Supply> supplies = access.board.getAllSupplies();
		String input = access.getClient().getSelection();
		int choice = Integer.parseInt(input.substring(7));
		if(choice >= supplies.size()) {
			return null;
		}
		Supply chosen = supplies.get(choice);
		if(options.contains(chosen)) {
			return supplies.get(choice);
		}
		else {
			throw new RuntimeException("Chose an unavailable supply: " 
					+ options + ", " + supplies.get(choice));
		}
	}

	@Override
	public Supply chooseSupply(List<Supply> options, boolean required, String choiceName) {
		return rand.chooseSupply(options, required, choiceName);
	}

	@Override
	public ArrayList<Integer> chooseCards(List<Card> choices, int num, boolean required, String choiceName) {
		return rand.chooseCards(choices, num, required, choiceName);
	}

	@Override
	public int chooseCard(List<Card> choices, boolean required, String choiceName) {
		return rand.chooseCard(choices, required, choiceName);
	}
	
	@Override
	public int chooseForCards(List<Card> cards, List<String> options, String choiceName) {
		return rand.chooseForCards(cards, options, choiceName);
	}

	@Override
	public int choose(List<String> options, String choiceName) {
		return rand.choose(options, choiceName);
	}

	@Override
	public Card enterCard(Card messenger) {
		return rand.enterCard(messenger);
	}

	@Override
	protected Card chooseAction(List<Card> options) {
		return rand.chooseAction(options);
	}

	@Override
	protected Card chooseTreasure(List<Card> options) {
		return rand.chooseTreasure(options);
	}

	@Override
	protected Supply chooseBuy(List<Supply> options) {
		return chooseGain(options, false);
	}

}
