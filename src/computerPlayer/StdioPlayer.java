package computerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

public class StdioPlayer extends ComputerPlayer {
	
	private DataWriter data;
	private Scanner in;
	private RandomPlayer random; //TODO replace with more nets
	
	public StdioPlayer(Player pComputer, DominionGame game) {
		super(pComputer, game);
		data = new DataWriter(pComputer, game);
		in = new Scanner(System.in);
		random = new RandomPlayer(pComputer, access);
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		data.writeGainData(options);
		data.saveData();
		while(true) {
			System.out.print("Enter Gain Choice for Neural Net Player: ");
			int output = Integer.parseInt(in.nextLine());
			Card selected = data.convertTargetShort(output);
			if(selected == null) {
				if(required) {
					System.out.println("Invalid Choice [required]");
					continue;
				}
				data.writeGainTarget(null);
				System.out.println("Reward: 0");
				return null;
			}
			for(Supply s : options) {
				if(s.getCard().equals(selected)) {
					data.writeGainTarget(s.getTopCard());
					System.out.println("Reward: " + s.getTopCard().getCost());
					return s;
				}
			}
			System.out.println("Invalid Choice [not available]");
		}
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
		data.writePlayData(options);
		data.saveData();
		while(true) {
			System.out.print("Enter Play Choice for Neural Net Player: ");
			int output = Integer.parseInt(in.nextLine());		
			Card selected = data.convertTargetShort(output);
			if(selected == null || options.contains(selected)) {
				data.writePlayTarget(selected);
				return selected;
			}
			System.out.println("Invalid Choice");
		}
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
		return "Neural Net";
	}

}
