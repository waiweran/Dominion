package genericGame.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import genericGame.BoardGame;
import genericGame.GameClient;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Connects the frontend to the backend through the server.
 * @author Nathaniel
 * @version 05-18-2018
 */
public class OnlineConnection extends Connection {

	public static final String SERVER = "localhost";//"152.3.64.49";
	private static final int PORT = 8901;

	private int myPlayerNumber;
	private GameWaiter wait;
	private GameChooser chooser;

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	/**
	 * Constructs the client by connecting to a server.
	 * @param address the server address.
	 * @param name the player's name.
	 * @throws Exception if something goes wrong.
	 */
	public OnlineConnection(GameClient client, String address, String name) throws Exception {
		super(client);
		setupConnection(address, name);
		sendString(client.getClass().getName());
		wait = new GameWaiter(this, false);
		startListener();
	}

	/**
	 * Constructs the client by connecting to a server.
	 * Sets up a new game by passing a game to the server.
	 * @param address the server address.
	 * @param name the player's name.
	 * @param game the starting BoardGame object.
	 * @throws Exception if something goes wrong.
	 */
	public OnlineConnection(GameClient client, String address, String name, BoardGame game) throws Exception {
		super(client);
		setupConnection(address, name);
		sendString("START " + client.getClass().getName());
		output.writeObject(game);
		output.flush();
		wait = new GameWaiter(this, true);
		String names = name + "\t";
		for(int i = 0; i < game.getNumNPC(); i++) {
			names += "NPC " + (i + 1) + "\t";
		}
		wait.playerJoined("JOIN\t" + names + game.getNumPlayers());
		startListener();
	}

	/**
	 * Sets up the server connection for the game.
	 * @param address the server address.
	 * @param name the player's name.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws Exception
	 */
	private void setupConnection(String address, String name) throws UnknownHostException, IOException {
		socket = new Socket(address, PORT);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		output.writeObject(name);
		output.flush();
		chooser = new GameChooser(this);

	}
	
	/**
	 * Starts the Threads that listen to the server.
	 */
	private void startListener() {
		Thread listener = new Thread(() -> serverListen());
		listener.setName("Server Listener");
		listener.start();
		getClient().startGameUpdate();
	}

	@Override
	public void sendString(String string) {
		if(string.startsWith("GAME END")
				|| string.startsWith("GAME CANCEL")
				|| string.startsWith("CHOOSE CANCEL")) {
			getClient().stopThreads();
		}
		try {
			if(socket.isClosed()) return;
			output.writeObject(string);
			output.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void backupGame(BoardGame game) {
		try {
			output.writeObject(game);
			output.flush();
		} catch (IOException e) {
			throw new RuntimeException("Could not back up", e);
		}
	}
	
	@Override
	public int getPlayerNumber() {
		return myPlayerNumber;
	}

	/**
	 * Listens to the server.
	 * Responds to commands from the server.
	 */
	private void serverListen() {
		try {
			while(true) {
				Object fromServer = input.readObject();
				if(fromServer instanceof String) {
					String response = (String) fromServer;				
					if(getClient().stop()) {
						return;
					}
					if(response == null || response.length() == 0) {
						continue;
					}	
					processResponse(response);
					if (response.startsWith("GAME END") 
							|| response.startsWith("GAME CANCEL")) {
						return;
					}
				}
				else if(fromServer instanceof BoardGame) {
					getClient().resetGame((BoardGame) fromServer);
				}
			}
		}
		catch (NullPointerException | IOException | ClassNotFoundException e) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Server Crashed.  Close game window and join a new game.");
				alert.setHeaderText("Game Error");
				alert.show();
				getClient().getGame().getGUI().showScores();
			});
			throw new RuntimeException("Server Crashed", e);
		}
		finally {
			getClient().stopThreads();
			try {
				socket.close();
			} catch (IOException e) {
				// sockets already closed, do nothing
			}
		}

	}

	/**
	 * Processes a command from the server.
	 * Checks specialized commands for connection protocol, 
	 * then sends all other commands to the game update thread.
	 * @param response The command.
	 */
	private void processResponse(String response) {
		if(response.startsWith("SELECT")) {
			getClient().sendToSelections(response);
		}
		else if (response.startsWith("WELCOME")) {
			try {
				BoardGame newGame = (BoardGame) input.readObject();
				wait.close();
				myPlayerNumber = Integer.parseInt(response.substring(response.length() - 1));	
				getClient().loadGame(newGame);
				sendString("NAME " + myPlayerNumber + " " + 
						getClient().getGame().getPlayers().get(myPlayerNumber - 1).getPlayerName());
			} catch (ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if (response.startsWith("CHOOSE")) {
			chooser.updateListings(response.substring(7));
		}
		else if (response.startsWith("JOIN")) {
			wait.playerJoined(response);
		}
		else if (response.startsWith("GAME END")) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(response + ".  Close game window and join a new game.");
				alert.setHeaderText("Game Error");
				alert.show();
				getClient().getGame().getGUI().showScores();
			});
		}
		else if (response.startsWith("GAME CANCEL")) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(response + ".  Join a new game.");
				alert.setHeaderText("Game Error");
				alert.show();
			});
			wait.close();
		}
		else if (response.startsWith("NAME")) {
			getClient().getGame().getPlayers().get(Integer.parseInt(
					response.substring(5, 6)) - 1).setPlayerName(response.substring(7));
		}
		else if (response.startsWith("MESSAGE")) {
			getClient().getGame().getGUI().addToChat(response.substring(8));
		}
		else {
			getClient().sendToGameUpdate(response);
		}
	}


}