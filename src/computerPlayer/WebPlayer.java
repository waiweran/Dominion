package computerPlayer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Player that queries a website to determine what to play.
 * @author Nathaniel
 * @version 04-10-2019
 */
public class WebPlayer extends ComputerPlayer {
	
	private static final String WEBSITE = "http://152.3.64.49:5000/predict/";
	
	private ExpertSystem exsys;
	
	public WebPlayer(Player pComputer, DominionGame game) {
		super("Gain Neural Net", pComputer, game);
		exsys = new ExpertSystem();
	}

	@Override
	public Supply chooseGain(List<Supply> options, boolean required) {
		try {
			double[] outputs = readSite(options);
			
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
//				if(choice != null) {
//					System.out.println("Net selected card that could not be bought");
//				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private double[] readSite(List<Supply> options) throws MalformedURLException, IOException, ProtocolException {
		// Initialize HTTP Connection
		String urlText = WEBSITE + "?datain={\"GainChoice\": " + 
				Arrays.toString(dataOut.getGainDataShort(options)) + "}&";
		urlText = urlText.replace(" ", "%20").replace("\"", "%22");
		URL url = new URL(urlText);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(2000);
		con.setReadTimeout(2000);
		con.setRequestMethod("GET");
		
		// Read HTTP Response
		Scanner input = new Scanner(con.getInputStream());
		StringBuffer content = new StringBuffer();
		while (input.hasNextLine()) {
		    content.append(input.nextLine());
		}
		input.close();
		con.disconnect();
		String html = content.toString();
		
		String[] vals = html.substring(html.indexOf("[") + 1, 
				html.indexOf("]")).split(",");
		double[] outputs = new double[vals.length];
		for(int i = 0; i < vals.length; i++) {
			outputs[i] = Double.parseDouble(vals[i].trim());
		}
		return outputs;
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

}
