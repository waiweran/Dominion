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
	private List<Integer> playTarget;
	private List<int[]> gainData;
	private List<Integer> gainTarget;
	
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
	 * @return the Supply to select.
	 */
	public Supply convertTargetShort(int index) {
		if(index == 0) return null;
		List<Supply> supplies = access.board.getAllSupplies();
		return supplies.get(index - 1);
	}
	
	/**
	 * Converts a given index into a card to select.
	 * Converts from index in long version of data.
	 * @param index the index of the card.
	 * @return the card to select.
	 */
	public Supply convertTargetLong(int index) {
		if(index == 0) return null;
		Card selected = access.allCards.getAllCards().get(index - 1);
		for(Supply s : access.board.getAllSupplies()) {
			if(s.getCard().equals(selected)) return s;
		}
		throw new RuntimeException("Selected card not in game");
	}
	
	/**
	 * Generates data about the game from which a play can be decided.
	 * Generates the short version that is game-specific.
	 * @param available the cards the player can play.
	 * @return the data as an array.
	 */
	public int[] getPlayDataShort(List<Card> available) {
		List<Supply> supplies = access.board.getAllSupplies();
		int cardset = supplies.size();
		int[] data = new int[cardset*5 + 3];
		//Hand
		for(Card c : player.deck.hand) {
			data[supplies.indexOf(access.board.findSupply(c))]++;
		}
		//Playspace
		for(Card c : player.deck.play) {
			data[cardset + supplies.indexOf(access.board.findSupply(c))]++;
		}
		//Supplies
		for(Supply s : supplies) {
			data[cardset * 2 + supplies.indexOf(s)] += s.getQuantity();
		}
		//Available
		for(Card c : available) {
			data[cardset * 3 + supplies.indexOf(access.board.findSupply(c))]++;
		}
		//Deck
		for(Card c : player.deck.getDeck()) {
			data[cardset * 4 + supplies.indexOf(access.board.findSupply(c))]++;
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
		List<Supply> supplies = access.board.getAllSupplies();
		int cardset = supplies.size();
		int[] data = new int[cardset*5 + 2];
		//Play Space
		for(Card c : player.deck.play) {
			System.err.println(c);
			data[supplies.indexOf(access.board.findSupply(c))]++;
		}
		//Supplies
		for(Supply s : supplies) {
			data[cardset + supplies.indexOf(s)] += s.getQuantity();
		}
		//Deck
		for(Card c : player.deck.getDeck()) {
			data[cardset * 2 + supplies.indexOf(access.board.findSupply(c))]++;
		}
		//Available
		for(Supply s : available) {
			data[cardset * 3 + supplies.indexOf(s)]++;
		}
		//Prices
		for(int i = 0; i < supplies.size(); i++) {
			data[cardset * 4 + i] = supplies.get(i).getTopCard().getCost();
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
	public int getTargetShort(Card choice) {
		List<Card> supplies = access.board.getAllSupplies()
				.stream().map(s -> s.getCard()).collect(Collectors.toList());
		if(choice == null) {
			return 0;
		}
		else {
			return supplies.indexOf(choice) + 1;
		}
	}
	
	/**
	 * Gets the target array for the given choice.
	 * Gets the long version that is general to all games.
	 * @param choice the choice to make the target array for.
	 * @return the target array.
	 */	
	public int getTargetLong(Card choice) {
		if(choice == null) {
			return 0;
		}
		else {
			return access.allCards.getCardNum(choice) + 1;
		}
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
			boolean start = true;
			for(Player p : access.players) if(!p.equals(player)) {
				if(!start) out.print(", ");
				if(p.isComputerPlayer()) 
					out.print("\"" + p.getComputerPlayer().getName() + "\"");
				else out.print("\"human\"");
				start = false;
			}
			out.println("],");
			out.print("\"board\": [");
			start = true;
			for(Supply s : access.board.kingdomCards) {
				if(!start) out.print(", ");
				out.print("\"" + s.getCard().getName() + "\"");
				start = false;
			}
			out.println("],");
			out.println("\"deck\": \"" + player.deck.toString() + "\",");
			out.println("\"playData\": [");
			start = true;
			for(int[] row : playData) {
				if(!start) out.println(",");
				out.print("  " + Arrays.toString(row));
				start = false;
			}
			out.println("\n],");
			out.println("\"playTarget\": " + playTarget + ",");
			out.println("\"gainData\": [");
			start = true;
			for(int[] row : gainData) {
				if(!start) out.println(",");
				out.print("  " + Arrays.toString(row));
				start = false;
			}
			out.println("\n],");
			out.println("\"gainTarget\": " + gainTarget);
			out.println("}");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
