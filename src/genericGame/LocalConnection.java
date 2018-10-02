package genericGame;

/**
 * Locally connects the frontend to the backend.
 * @author Nathaniel
 * @version 05-18-2018
 */
public class LocalConnection extends Connection {

	/**
	 * Initializes the local connection.
	 * @param client the GameClient.
	 */
	public LocalConnection(GameClient client, BoardGame game) {
		super(client);
		client.loadGame(game);
		client.startGameUpdate();
	}

	@Override
	public void sendString(String string) {
		if(string.startsWith("GAME END")
				|| string.startsWith("GAME CANCEL")
				|| string.startsWith("CHOOSE CANCEL")) {
			getClient().stopThreads();
		}
		if(string.startsWith("SELECT")) {
			getClient().sendToSelections(string);
		}
		else {
			getClient().sendToGameUpdate(string);
		}
	}

	@Override
	public void backupGame(BoardGame game) {
		// Do nothing, only one copy of game exists.
	}
	
	@Override
	public int getPlayerNumber() {
		return 0; // Representing all players
	}

}