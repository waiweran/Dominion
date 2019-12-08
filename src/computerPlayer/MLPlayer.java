package computerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;
import machineLearning.GainModel;
import machineLearning.SupplyData;

public class MLPlayer extends ComputerPlayer {
	
	RandomPlayer2 rand;
	GainModel model;

	public MLPlayer(Player pComputer, DominionGame game) {
		super("ML Player", pComputer, game);
		rand = new RandomPlayer2(pComputer, game);
		model = game.models.getGainModel();
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		List<Supply> supplies = access.board.getAllSupplies();
		SupplyData[] data = new SupplyData[supplies.size()];
		for(int i = 0; i < supplies.size(); i++) {
			int numInDeck = 0;
			for(Card c : player.deck.getDeck()) {
				if(c.equals(supplies.get(i).getCard())) numInDeck++;
			}
			data[i] = new SupplyData(supplies.get(i), options.contains(supplies.get(i)), numInDeck);
		}
		int choice = model.choose(data, !required);
		if(choice >= supplies.size()) {
			return null;
		}
		Supply chosen = supplies.get(choice);
		if(options.contains(chosen)) {
			return supplies.get(choice);
		}
		else {
			System.err.println(options + " " + supplies.get(choice));
			System.err.println(Arrays.toString(model.evaluateNet(data)));
			throw new RuntimeException("Chose an unavailable supply");
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
