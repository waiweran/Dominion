package computerPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Saves gain and play information from a player to file.
 * @author Nathaniel Brooke
 * @version 05-08-2017
 */
public class DataWriter {
	
	private Player player;
	private DominionGame access;

	private List<int[]> playData;
	private List<int[]> playTarget;
	private List<int[]> gainData;
	private List<int[]> gainTarget;
	
	/**
	 * Initializes a new Data Writer with empty files.
	 * @param writeFor
	 */
	public DataWriter(Player writeFor, DominionGame game) {
		player = writeFor;
		access = game;
		playData = new ArrayList<>();
		playTarget = new ArrayList<>();
		gainData = new ArrayList<>();
		gainTarget = new ArrayList<>();
	}
	
	/**
	 * Writes current deck data to gain data file.
	 */
	public synchronized void writeGainData(List<Supply> available) {
		gainData.add(getGainDataShort(available));
	}
	
	/**
	 * Writes the gain choice based on the matching gain data to gain target file.
	 */
	public synchronized void writeGainTarget(Card choice) {
		gainTarget.add(getTargetShort(choice));
	}
	
	/**
	 * Writes current deck data to play data file.
	 */
	public synchronized void writePlayData(List<Card> options) {
		playData.add(getPlayDataShort(options));
	}
	
	/**
	 * Writes the gain choice based on the matching play data to play target file.
	 */
	public synchronized void writePlayTarget(Card choice) {
		playTarget.add(getTargetShort(choice));
	}
	
	/**
	 * Converts a given index into a card to select.
	 * Converts from index in short version of data.
	 * @param index the index of the card.
	 * @return the card to select.
	 */
	public Card convertTargetShort(int index) {
		if(index == 0) return null;
		List<Card> supplies = access.board.getAllSupplies()
				.stream().map(s -> s.getTopCard()).collect(Collectors.toList());
		return supplies.get(index - 1);
	}
	
	/**
	 * Converts a given index into a card to select.
	 * Converts from index in long version of data.
	 * @param index the index of the card.
	 * @return the card to select.
	 */
	public Card convertTargetLong(int index) {
		if(index == 0) return null;
		return access.allCards.getAllCards().get(index - 1);
	}
	
	/**
	 * Generates data about the game from which a play can be decided.
	 * Generates the short version that is game-specific.
	 * @param available the cards the player can play.
	 * @return the data as an array.
	 */
	public int[] getPlayDataShort(List<Card> available) {
		List<Card> supplies = access.board.getAllSupplies()
				.stream().map(s -> s.getCard()).collect(Collectors.toList());
		int cardset = supplies.size();
		int[] data = new int[cardset*5 + 3];
		//Hand
		for(Card c : player.deck.hand) {
			data[supplies.indexOf(c)]++;
		}
		//Playspace
		for(Card c : player.deck.play) {
			data[cardset + supplies.indexOf(c)]++;
		}
		//Supplies
		for(Supply s : access.board.getAllSupplies()) {
			data[cardset * 2 + supplies.indexOf(s.getCard())] += s.getQuantity();
		}
		//Available
		for(Card c : available) {
			data[cardset * 3 + supplies.indexOf(c)]++;
		}
		//Deck
		for(Card c : player.deck.getDeck()) {
			data[cardset * 4 + supplies.indexOf(c)]++;
		}
		//Actions, Treasure, and Buys
		data[cardset * 5] = player.getActions();
		data[cardset * 5 + 1] = player.getTreasure();
		data[cardset * 5 + 2] = player.getBuys();
		
		return data;
	}
	
	/**
	 * Generates data about the game from which a gain can be decided.
	 * Generates the short version that is game-specific.
	 * @param available the supplies the player can buy from.
	 * @return the data as an array.
	 */
	public int[] getGainDataShort(List<Supply> available) {
		List<Card> supplies = access.board.getAllSupplies()
				.stream().map(s -> s.getCard()).collect(Collectors.toList());
		int cardset = supplies.size();
		int[] data = new int[cardset*5 + 2];
		//Play Space
		for(Card c : player.deck.play) {
			data[supplies.indexOf(c)]++;
		}
		//Supplies
		for(Supply s : access.board.getAllSupplies()) {
			data[cardset + supplies.indexOf(s.getCard())] += s.getQuantity();
		}
		//Deck
		for(Card c : player.deck.getDeck()) {
			data[cardset * 2 + supplies.indexOf(c)]++;
		}
		//Available
		for(Supply s : available) {
			data[cardset * 3 + supplies.indexOf(s.getCard())]++;
		}
		//Prices
		for(int i = 0; i < supplies.size(); i++) {
			data[cardset * 4 + i] = supplies.get(i).getCost();
		}
		//Treasure and Buys
		data[cardset * 5] = player.getTreasure();
		data[cardset * 5 + 1] = player.getBuys();
		
		return data;
	}
	
	/**
	 * Generates data about the game from which a play can be decided.
	 * Generates the long version that is general to all games.
	 * @param available the cards the player can play.
	 * @return the data as an array.
	 */
	public int[] getPlayDataLong(List<Card> available) {
		int cardset = access.allCards.getNumCards();
		int[] data = new int[cardset*5 + 3];
		//Hand
		for(Card c : player.deck.hand) {
			data[access.allCards.getCardNum(c)]++;
		}
		//Playspace
		for(Card c : player.deck.play) {
			data[cardset + access.allCards.getCardNum(c)]++;
		}
		//Supplies
		for(Supply s : access.board.getAllSupplies()) {
			data[cardset * 2 + access.allCards.getCardNum(s.getCard())] += s.getQuantity();
		}
		//Available
		for(Card c : available) {
			data[cardset * 3 + access.allCards.getCardNum(c)]++;
		}
		//Deck
		for(Card c : player.deck.getDeck()) {
			data[cardset * 4 + access.allCards.getCardNum(c)]++;
		}
		//Actions, Treasure, and Buys
		data[cardset * 5] = player.getActions();
		data[cardset * 5 + 1] = player.getTreasure();
		data[cardset * 5 + 2] = player.getBuys();
		
		return data;
	}
	
	/**
	 * Generates data about the game from which a gain can be decided.
	 * Generates the long version that is general to all games.
	 * @param available the supplies the player can buy from.
	 * @return the data as an array.
	 */
	public int[] getGainDataLong(List<Supply> available) {
		int cardset = access.allCards.getNumCards();
		int[] data = new int[cardset*5 + 2];
		//Play Space
		for(Card c : player.deck.play) {
			data[access.allCards.getCardNum(c)]++;
		}
		//Supplies
		for(Supply s : access.board.getAllSupplies()) {
			data[cardset + access.allCards.getCardNum(s.getCard())] += s.getQuantity();
		}
		//Deck
		for(Card c : player.deck.getDeck()) {
			data[cardset * 2 + access.allCards.getCardNum(c)]++;
		}
		//Available
		for(Supply s : available) {
			data[cardset * 3 + access.allCards.getCardNum(s.getCard())]++;
		}
		//Prices
		for(int i = 0; i < access.allCards.getNumCards(); i++) {
			data[cardset * 4 + i] = access.allCards.getCardAt(i).getCost();
		}
		//Treasure and Buys
		data[cardset * 5] = player.getTreasure();
		data[cardset * 5 + 1] = player.getBuys();
		
		return data;
	}
	
	/**
	 * Gets the target array for the given choice.
	 * Gets the short version that is game-specific.
	 * @param choice the choice to make the target array for.
	 * @return the target array.
	 */
	public int[] getTargetShort(Card choice) {
		List<Card> supplies = access.board.getAllSupplies()
				.stream().map(s -> s.getCard()).collect(Collectors.toList());
		int[] target = new int[supplies.size() + 1];
		if(choice == null) {
			target[0] = 1;
		}
		else {
			target[supplies.indexOf(choice) + 1] = 1;
		}
		return target;
	}
	
	/**
	 * Gets the target array for the given choice.
	 * Gets the long version that is general to all games.
	 * @param choice the choice to make the target array for.
	 * @return the target array.
	 */	
	public int[] getTargetLong(Card choice) {
		int[] target = new int[access.allCards.getNumCards() + 1];
		if(choice == null) {
			target[0] = 1;
		}
		else {
			target[access.allCards.getCardNum(choice) + 1] = 1;
		}
		return target;
	}
	
	/**
	 * Saves the data input and target files for this player.
	 */
	public void saveData() {
		if(gainData.size() == 0) return;
		try {
			long fileTime = System.currentTimeMillis() + player.getPlayerNum();
			PrintStream out = new PrintStream(new File("Training/data-" + fileTime + ".json"));
			out.println("{");
			out.println("\"computerPlayer\": \"" + player.getComputerPlayer().getName() + "\",");
			out.println("\"numPlayers\": " + access.getNumPlayers() + ",");
			out.print("\"opponents\": [");
			for(Player p : access.players) if(!p.equals(player)) {
				if(p.isComputerPlayer()) 
					out.print("\"" + p.getComputerPlayer().getName() + "\", ");
				else out.print("\"human\", ");
			}
			out.println("],");
			out.print("\"board\": [");
			for(Supply s : access.board.kingdomCards) {
				out.print("  \"" + s.getCard().getName() + "\",");
			}
			out.println("],");
			out.println("\"deck\": \"" + player.deck.toString() + "\",");
			out.println("\"playData\": [");
			for(int[] row : playData) {
				out.println("  " + Arrays.toString(row) + ",");
			}
			out.println("],");
			out.println("\"playTarget\": [");
			for(int[] row : playTarget) {
				out.println("  " + Arrays.toString(row) + ",");
			}
			out.println("],");
			out.println("\"gainData\": [");
			for(int[] row : gainData) {
				out.println("  " + Arrays.toString(row) + ",");
			}
			out.println("],");
			out.println("\"gainTarget\": [");
			for(int[] row : gainTarget) {
				out.println("  " + Arrays.toString(row) + ",");
			}
			out.println("]");
			out.println("}");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
