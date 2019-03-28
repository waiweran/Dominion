package computerPlayer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

public class SocketPlayer extends ComputerPlayer {
	
	private Socket socket;
	private Scanner input;
	private PrintStream output;
	private ExpertSystem exsys;
	private int total;
	
	public SocketPlayer(Player pComputer, DominionGame game) {
		super(pComputer, game);
		exsys = new ExpertSystem();
		total = 0;
	}

	@Override
	public synchronized Supply chooseGain(List<Supply> options, boolean required) {
		try {
			socket = new Socket();
			socket.setSoTimeout(1000);
			socket.connect(new InetSocketAddress("152.3.64.49", 12345));
			output = new PrintStream(socket.getOutputStream());
			input = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			try {
				socket = new Socket();
				socket.setSoTimeout(1000);
				socket.connect(new InetSocketAddress("152.3.64.49", 12345));
				output = new PrintStream(socket.getOutputStream());
				input = new Scanner(socket.getInputStream());
			} catch (IOException e2) {
				throw new RuntimeException(e);
			}
		}
		output.println("{\"GainChoice\": " + 
				Arrays.toString(dataOut.getGainDataShort(options)) + "}");
		output.flush();
		System.out.print(total++ + " ");
		String jsonArray = input.nextLine();
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