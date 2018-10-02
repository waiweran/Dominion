package computerPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.neuroph.core.NeuralNetwork;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

public class NeurophNetPlayer extends ComputerPlayer {
	
	private NeuralNetwork<?> playNet, gainNet;
	private DataWriter data;
	private RandomPlayer random; //TODO replace with more nets
	
	/**
	 * Initializes the Neural Network player.
	 * @param pComputer The player this will play for.
	 * @param game The main game.
	 * @param playNetFile File holding the play neural network.
	 * @param gainNetFile File holding the gain neural network.
	 */
	public NeurophNetPlayer(Player pComputer, DominionGame game, 
			File playNetFile, File gainNetFile) {
		super(pComputer, game);
		data = new DataWriter(pComputer, game);
		random = new RandomPlayer(pComputer, access);
		playNet = NeuralNetwork.createFromFile(playNetFile);
		gainNet = NeuralNetwork.createFromFile(gainNetFile);
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		int[] gainData = data.getGainDataShort(options);
		int output = processNet(gainNet, gainData);			
		Card selected = data.convertTargetShort(output);
		if(selected == null) {
			if(required) {
				System.out.println("Invalid Choice [required]");
			}
			data.writeGainTarget(null);
			System.out.println("Buy Nothing");
			return null;
		}
		for(Supply s : options) {
			if(s.getCard().equals(selected)) {
				data.writeGainTarget(s.getTopCard());
				System.out.println("Buy " + s.getTopCard());
				return s;
			}
		}
		System.out.println("Invalid Choice [not available]");
		return null;
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
		int[] playData = data.getPlayDataShort(options);
		int output = processNet(playNet, playData);			
		Card selected = data.convertTargetShort(output);
		if(selected == null || options.contains(selected)) {
			data.writePlayTarget(selected);
			return selected;
		}
		System.out.println("Invalid Choice");
		return null;
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
	
	/**
	 * Processes the input through the neural network.
	 * Extracts the output from the neural network.
	 * @param net The NeuralNetwork to process.
	 * @param input The input to the network.
	 * @return The index of the highest value output.
	 */
	private int processNet(NeuralNetwork<?> net, int[] input) {
		double[] netInput = new double[input.length];
		for(int i = 0; i < input.length; i++) {
			netInput[i] = input[i];
		}
		net.setInput(netInput);
		net.calculate();
		double[] netOutput = net.getOutput();
		System.out.println(Arrays.toString(netOutput));
		int output = 0;
		double max = netOutput[0];
		for(int i = 0; i < netOutput.length; i++) {
			if(netOutput[i] > max) {
				max = netOutput[i];
				output = i;
			}
		}
		return output;
	}
	
	@Override
	public String getName() {
		return "Neural Net";
	}

}
