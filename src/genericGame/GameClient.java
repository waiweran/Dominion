package genericGame;

import java.util.LinkedList;
import java.util.Queue;

import genericGame.network.Connection;

/**
 * A client for board game servers.
 * Contains the BGP (Board Game Protocol).
 * Here are the strings that are sent and received:
 *
 *  Sending						Receiving					Explanation of use
 *  ----------------          	----------------			----------------
 *  WELCOME	<text> (game type) 	WELCOME <n> (player #)		Initialization of the game
 *  CHOOSE <n> (game #)			CHOOSE <text> (games)		Player chooses what game to join
 *  GAME CANCEL <text>			GAME CANCEL <text>			Player cancels selection of a game
 *  							JOIN <text> (name, slots)	Indicates another player's entrance into game
 *  NAME <text>					NAME <text>					Changes a player's name	
 *  GAME END <text>				GAME END <text>				When the game ends due to player disappearance
 *  MESSAGE <text>             	MESSAGE <text>				When a message is sent through the chat
 *  SELECT <text>				SELECT <text>				When a selector is used
 *  RESET													Client requests game object
 *  							
 */
public abstract class GameClient {
	
	private boolean stop;
	private Queue<String> selections;
	private Queue<String> updates;
	
	private Connection connect;

	/**
	 * Initializes the GameClient.
	 */
	public GameClient() {
		stop = false;
		selections = new LinkedList<>();
		updates = new LinkedList<>();
	}
	
	/**
	 * Passes the connection to the GameClient for sending stuff.
	 * @param connection the connection object used.
	 */
	public void passConnection(Connection connection) {
		connect = connection;
	}
	
	/**
	 * Sends a string describing an action to apply to the game state.
	 * @param string to be sent.
	 */
	public void sendString(String string) {
		connect.sendString(string);
	}
	
	/**
	 * Backs the current game up.
	 * @param game the game to send.
	 */
	public void backupGame(BoardGame game) {
		connect.backupGame(game);
	}
	
	/**
	 * @return the number of the player this client is playing for.
	 * Returns zero if local client representing all players.
	 */
	public int getPlayerNumber() {
		return connect.getPlayerNumber();
	}
	
	/**
	 * Retrieves selection information received.
	 * Strings starting with "SELECT"
	 * @return the received information
	 */
	public String getSelection() {
		while(selections.isEmpty()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// Sleeping interrupted, just try again
			}
		}
		return selections.poll();
	}
	
	/**
	 * Starts the game backend updating thread.
	 */
	public void startGameUpdate() {
		Thread updater = new Thread(() -> gameUpdate());
		updater.setName("Backend Updater");
		updater.start();
	}
	
	/**
	 * Applies updates to the game state.
	 */
	private void gameUpdate() {
		while(!stop) {
			while(updates.isEmpty()) {
				if(stop) return;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// Sleeping interrupted, just try again
				}
			}
			monitorGame(updates.poll());
		}
	}
	
	/**
	 * Sends a command to the selector queue.
	 * @param response the command.
	 */
	public void sendToSelections(String response) {
		selections.add(response);
	}

	/**
	 * Sends a command to the game update thread.
	 * @param response the command.
	 */
	public void sendToGameUpdate(String response) {
		updates.add(response);
	}
	
	/**
	 * Stops running threads associated with this game client.
	 */
	public void stopThreads() {
		stop = true;
	}
	
	/**
	 * @return true if threads associated with this game client should be stopped.
	 */
	public boolean stop() {
		return stop;
	}
	
	/**
	 * Checks game-specific responses to commands.
	 * @param response the string containing the command.
	 */
	protected abstract void monitorGame(String response);
	
	/**
	 * Loads the game as the game that will be played.
	 * Used when a player joins an existing game.
	 * @param game the game to load.
	 */
	public abstract void loadGame(BoardGame game);
	
	/**
	 * Resets the current game object with an updated one.
	 * @param newGame the new object from the server.
	 */
	public abstract void resetGame(BoardGame newGame);

	/**
	 * @return the game.
	 */
	public abstract BoardGame getGame();


}
