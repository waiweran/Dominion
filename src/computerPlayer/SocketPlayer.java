package computerPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Player that uses a socket connection to a server to determine gains.
 * @author Nathaniel
 * @version 04-18-2019
 */
public class SocketPlayer extends ComputerPlayer {
	
	private ExpertSystem exsys;	
	private RandomPlayer2 rand2;

	private int lastGain;
	private double gainReward;
	
	private NetSocket ns;
	
	public SocketPlayer(Player pComputer, DominionGame game) {
		super(pComputer, game);
		rand2 = new RandomPlayer2(pComputer, game);
		exsys = new ExpertSystem();	
		lastGain = 0;
		gainReward = 0;
		try {
			ns = new NetSocket();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public SocketPlayer(Player pComputer, DominionGame game, NetSocket netSocket) {
		super(pComputer, game);
		rand2 = new RandomPlayer2(pComputer, game);
		exsys = new ExpertSystem();	
		lastGain = 0;
		gainReward = 0;
		ns = netSocket;
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {

		try {
			String jsonArray = ns.respond("{\"Player\": " + player.getPlayerNum() + 
					", \"GainChoice\": " + Arrays.toString(dataOut.getGainDataShort(options)) + 
					", \"Reward\": " + gainReward + ", \"Done\": false, \"Action\": " + 
					lastGain + ", \"Score\": \"NA\"}");
			if(jsonArray.contains("random")) {
				return rand2.chooseGain(options, required);
			}
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
					lastGain = maxIndex;
					if(choice != null && choice.getCard().getName().equals("Province")) {
						gainReward = 0.5;
					}
					else {
						gainReward = 0;
					}
					return choice;
				}
				outputs[maxIndex] = -1;
				if(choice != null) {
				}
			}
		} catch(Exception e) {
			ns.close();
			throw new RuntimeException(e);
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
	public void close() {
		StringBuilder scores = new StringBuilder();
		int score = player.getScore();
		int maxScore = 0;
		for(Player p : access.players) {
			int psc = p.getScore();
			scores.append(psc + " ");
			if(psc > maxScore) maxScore = psc;
		}
		int reward = 0;
		if(score == maxScore) reward = 1;
		ns.send("{\"Player\": " + player.getPlayerNum() + 
				", \"GainChoice\": [], \"Reward\": " + reward +
				", \"Done\": true, \"Action\": " + lastGain + 
				", \"Score\": \"" + scores.substring(0, scores.length() - 1) + "\"}");
		super.close();
		ns.close();
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
