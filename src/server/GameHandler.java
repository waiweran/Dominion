package server;

import java.util.ArrayList;
import java.util.List;

import genericGame.BoardGame;

/**
 * Sets up and runs a game.
 * @author Nathaniel
 * @version 03-10-2018
 */
public class GameHandler {

	private List<GameHandler> starting, running;
	private ArrayList<Client> players;
	private String type;
	private int gameID;
	private BoardGame game;

	/**
	 * Initializes the game handler.
	 * @param allGames List of all current games.
	 * @param gameType The type of game this is.
	 */
	public GameHandler(List<GameHandler> startingGames, List<GameHandler> runningGames, 
			Client client, String gameType, int gameNum) {
		starting = startingGames;
		running = runningGames;
		players = new ArrayList<>();
		type = gameType;
		gameID = gameNum;
		players.add(client);
		client.setHandler(this);
		synchronized(starting) {
			starting.add(this);
			starting.notifyAll();
		}
		client.listenClient();
	}

	/**
	 * Adds a player to the game.
	 * @param gp the player to be added.
	 */
	public void addPlayer(Client gp) {
		synchronized(this) {
			players.add(gp);
			gp.setHandler(this);
			String names = "";
			for(Client messageMe : players) {
				names += messageMe.getName() + "\t";
			}
			for(int i = 0; i < game.getNumNPC(); i++) {
				names += "NPC " + (i + 1) + "\t";
			}
			for(Client messageMe : players) {
				messageMe.println("JOIN\t" + names + game.getNumPlayers());
			}
			if(getNumSlots() == 0) {
				startGame();
			}
		}
	}

	/**
	 * Removes a player from the current game.
	 * @param gp the player to remove.
	 */
	public synchronized void removePlayer(Client gp) {
		if(gp.equals(players.get(0))) {
			// Starting player canceled
			cancelGame();
		}
		else if(players.remove(gp)) {
			String names = "";
			for(Client messageMe : players) {
				names += messageMe.getName() + "\t";
			}
			for(int i = 0; i < game.getNumNPC(); i++) {
				names += "NPC " + (i + 1) + "\t";
			}
			for(Client messageMe : players) {
				messageMe.println("JOIN " + names + game.getNumPlayers());
			}
		}
		else {
			System.err.println("Player Removal Failed: Player not found");
		}
	}
	
	/**
	 * Evicts the specified player for disconnecting.
	 * @param gp the player to evict.
	 */
	public synchronized void evictPlayer(Client gp) {
		// TODO implement
	}
	
	/**
	 * Allows a client to rejoin a game from which they were evicted.
	 * @param gp the client rejoining.
	 */
	public synchronized void rejoin(Client gp) {
		// TODO implement
	}

	/**
	 * Starts the game, and prepares all players.
	 */
	public void startGame() {
		try {
			game.setNumPlayers(players.size() + game.getNumNPC());
			int i = 1;
			for(Client gp : players) {
				gp.setIndex(i - 1);
				gp.start();
				gp.println("WELCOME " + i);
				gp.writeGame(game);
				i++;
			}
			synchronized(running) {
				running.add(this);
			}
		} finally {
			synchronized(starting) {
				starting.remove(this);
				starting.notifyAll();
			}
		}
	}
	
	/**
	 * Cancels the game and notifies connected players.
	 */
	public void cancelGame() {
		for(Client gp : players) {
			gp.println("GAME CANCEL Starting Player Disconnected");
		}
		synchronized(starting) {
			starting.remove(this);
			starting.notifyAll();
		}
		synchronized(running) {
			running.remove(this);
		}
	}
	
	/**
	 * Passes the given message on to other clients in the game.
	 * @param index of the client that sent the message.
	 * @param message The content of the message.
	 */
	public synchronized void passMessage(int index, String message) {
		if(message.length() < 40) {
			System.out.print(index + 1 + " " + message);
		}
		else {
			System.out.print(index + 1 + " " + message.substring(0, 40) + "...\t");
		}
		
		if (message.startsWith("GAME END")) {
			for(Client p : players) {
				p.println(message);
				p.stop();
			}
			for(int i = 0; i < 6 - (message.length() + 2)/8; i++) {
				System.out.print("\t");
			}
			System.out.println("Game Ended");
			synchronized(running) {
				running.remove(this);
			}
			return;
		} 
		else if(message.startsWith("RESET")) {
			players.get(index).writeGame(game);
			System.out.println("\t\t\t\t\t\tSent Game");
		}
		else {
			for(Client p : players) {
				p.println(message);
			}
			for(int i = 0; i < 6 - (message.length() + 2)/8; i++) {
				System.out.print("\t");
			}
			System.out.println("Command Relayed");
		} 
	}
	
	public synchronized void backup(BoardGame gameBackup) {
		game = gameBackup;
		System.out.println("<Game>\t\t\t\t\t\tBackup Complete");
	}
	
	/**
	 * Gets the number of free slots in the game.
	 * @return number of free slots.
	 */
	public int getNumSlots() {
		return game.getNumPlayers() - players.size() - game.getNumNPC();
	}
	
	/**
	 * Gets the game associated with this handler.
	 * @return the game.
	 */
	public BoardGame getGame() {
		return game;
	}
	
	/**
	 * Gets the game type.
	 * @return game type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Gets the game's ID in the server.
	 * @return the game's ID.
	 */
	public int getID() {
		return gameID;
	}

}

