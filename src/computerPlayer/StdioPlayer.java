package computerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Player that uses input from STDIO to determine purchases.
 * @author Nathaniel
 * @version 03-22-2019
 */
public class StdioPlayer extends ComputerPlayer {
	
	private Scanner in;
	private ExpertSystem exsys;
	
	public StdioPlayer(Player pComputer, DominionGame game) {
		super(pComputer, game);
		in = new Scanner(System.in);
		exsys = new ExpertSystem();
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		System.out.println("{\"GainChoice\": " + 
				Arrays.toString(dataOut.getGainDataShort(options)) + "}");
		String jsonArray = in.nextLine();
		String[] vals = jsonArray.substring(jsonArray.indexOf("[") + 1, 
				jsonArray.indexOf("]")).split(",");
		double[] outputs = new double[vals.length];
		for(int i = 0; i < vals.length; i++) {
			outputs[i] = Double.parseDouble(vals[i].trim());
		}
		
		Supply choice = null;
		while(true) {
			int maxIndex = 0;
			double maxVal = outputs[0];
			for(int i = 1; i < outputs.length; i++) {
				if(maxVal < outputs[i]) {
					maxIndex = i;
					maxVal = outputs[i];
				}
			}
			choice = dataOut.convertTargetShort(maxIndex);
			if(choice == null && !required || choice != null && player.getBuys() > 0 
					&& player.getTreasure() - choice.getTopCard().getCost() >= 0 
					&& choice.getTopCard().canBeGained() 
					&& (!choice.getTopCard().costsPotion() || player.potion > 0)
					&& options.contains(choice)) {
				return choice;
			}
			outputs[maxIndex] = -1;
			if(choice != null) {
				System.out.println("Net selected card that could not be bought");
			}
		}
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
		return chooseGain(options, false);
	}
	
	@Override
	public String getName() {
		return "Gain Neural Net";
	}

}
