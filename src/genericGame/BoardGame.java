package genericGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Interface for building Board Games around to work with online server.
 * @author Nathaniel
 * @version 06-19-2015
 */
public abstract class BoardGame implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int whoseTurn = 1;				//determines who's turn it is
	public final Random random;				//stores the random number generator for shuffling
	private final boolean online; 			//records whether the game is played online
	protected boolean gameStarted;			//records whether the game has been started
	protected boolean showGraphics;			//determines whether graphics should be shown
	private transient GameClient client;	//this is the online client
	private int numPlayers;					//records the number of players
	private String name;					//records the name of the game
	private StringBuilder log;				//records a log of all that happened in the game.

	/**
	 * Constructor for BoardGame.  
	 */
	public BoardGame(boolean isOnline) {
		name = "";
		gameStarted = false;
		random = new Random();
		online = isOnline;
		log = new StringBuilder();
	}
	
	
	/**
	 * Adds an entry to the game log.
	 * @param entry the entry to be added.
	 */
	public void log(String entry) {
		String text = System.currentTimeMillis() + ": " + entry + "\n";
		log.append(text);
		getGUI().updateLog(entry);
	}
	
	/**
	 * @return the game log.
	 */
	public String getGameLog() {
		return log.toString();
	}
	
	/**
	 * Changes whose turn it is to the next player.
	 */
	public void nextTurn() {
		if(whoseTurn < numPlayers) whoseTurn++;
		else whoseTurn = 1;		
	}
	
	/**
	 * @return the number of the player whose turn it is.
	 */
	public int getWhoseTurn() {
		return whoseTurn;
	}
	
	/**
	 * Sets the number of players in the game.
	 * @param num the new number of players.
	 */
	public void setNumPlayers(int num) {
		if(!gameStarted) {
			numPlayers = num;
		}
	}
	
	/**
	 * Sets the name of the game.
	 * @param gameName the game's new name.
	 */
	public void setGameName(String gameName) {
		if(!gameStarted) {
			name = gameName;
		}
	}
			
	/**
	 * Sets the GameClient for online interactions in the game.
	 * This must be called before running the game.
	 * @param gc the GameClient.
	 */
	public void setGameClient(GameClient gc) {
		client = gc;
	}
	
	/**
	 * Gets the current player.
	 * @return the Player object for the current player.
	 */
	public abstract AbstractPlayer getCurrentPlayer();
	
	/**
	 * Gets all the players.
	 * @return ArrayList containing all the players.
	 */
	public abstract ArrayList<AbstractPlayer> getPlayers();
	
	/**
	 * Gets the name of the game.
	 * @return the game's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the number of players in a game.
	 * @return the number of players.
	 */
	public int getNumPlayers() {
		return numPlayers;
	}	
	
	/**
	 * Determines whether the game is played online.
	 * @return online status of game.
	 */ 
	public boolean isOnline() {
		return online;
	}
	
	/**
	 * Determines whether graphics will be displayed.
	 * @return true to display, false to hide graphics.
	 */
	public boolean showGraphics() {
		return showGraphics;
	}
	
	/**
	 * @return the client for the specific instance of the game.
	 */
	public GameClient getClient() {
		return client;
	}
	
	/**
	 * Starts the game, setting everything up.
	 */
	public abstract void startGame();
	
	/**
	 * @return the local GUI for the game.
	 */
	public abstract GameGUI getGUI();
	
}
