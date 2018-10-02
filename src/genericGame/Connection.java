package genericGame;

/**
 * Determines how frontend commands are connected to the backend game state.
 * @author Nathaniel
 * @version 05-17-2018
 */
public abstract class Connection {
	
	private GameClient client;
	
	/**
	 * Initializes the connection.
	 */
	public Connection(GameClient gameClient) {
		client = gameClient;
	}
	
	/**
	 * Sends a string describing an action to apply to the game state.
	 * @param string to be sent.
	 */
	public abstract void sendString(String string);
	
	/**
	 * Backs the current game up.
	 * @param game the game to send.
	 */
	public abstract void backupGame(BoardGame game);
	
	/**
	 * @return the number of the player this client is playing for.
	 * Returns zero if local client representing all players.
	 */
	public abstract int getPlayerNumber();
	
	/**
	 * @return the GameClient this connects.
	 */
	protected GameClient getClient() {
		return client;
	}
	
}
