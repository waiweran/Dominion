package computerPlayer;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import gameBase.Supply;

public class NetPlayer extends ComputerPlayer {
	
	private ExpertSystem exsys;
	private MultiLayerNetwork model;

	public NetPlayer() {
		exsys = new ExpertSystem();
		model = KerasModelImport.importKerasSequentialModelAndWeights("gain.h5");
	}
	
	private float[] predict(int[] input) {
		// TODO make prediction
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Supply chooseSupply(List<Supply> options, boolean required, String choiceName) {
		return null;
	}

	@Override
	public ArrayList<Integer> chooseCards(List<Card> choices, int num, boolean required, String choiceName) {
		if(choiceName.equals("Cellar")) {
			return exsys.chooseCardsCellar(choices);
		}
		if(choiceName.equals("Militia")) {
			return exsys.chooseCardsMilitia(choices, num);
		}
		return null;
	}

	@Override
	public int chooseCard(List<Card> choices, boolean required, String choiceName) {
		if(choiceName.equals("Mine")) {
			return exsys.chooseCardMine(choices);
		}
		if(choiceName.equals("Remodel")) {
			return exsys.chooseCardRemodel(choices);
		}
		return 0;
	}

	@Override
	public int choose(List<String> options, String choiceName) {
		return 0;
	}

	@Override
	public Card enterCard(Card messenger) {
		return null;
	}

	@Override
	protected Card chooseAction(List<Card> options) {
		return exsys.chooseAction(options);
	}

	@Override
	protected Card chooseTreasure(List<Card> options) {
		// Play all treasures in order
		return options.get(0);
	}

	@Override
	protected Supply chooseBuy(List<Supply> options) {
		int[] data_in = dataOut.getGainDataShort(options);
		float[] outputs = predict(data_in);
		while(true) {
			int maxIndex = 0;
			float maxVal = outputs[0];
			for(int i = 1; i < outputs.length; i++) {
				if(maxVal < outputs[i]) {
					maxIndex = i;
					maxVal = outputs[i];
				}
			}
			Supply choice = dataOut.convertTargetShort(maxIndex);
			if(choice == null || player.getBuys() > 0 
					&& player.getTreasure() - choice.getTopCard().getCost() >= 0 
					&& choice.getTopCard().canBeGained() 
					&& (!choice.getTopCard().costsPotion() || player.potion > 0)) {
				return choice;
			}
			outputs[maxIndex] = 0;
			System.out.println("Net selected card that could not be bought");
		}
	}

	@Override
	public String getName() {
		return "Gain Net Player";
	}

}
