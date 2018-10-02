package server;

import java.io.IOException;
import java.util.List;

/**
 * Allows player to choose a game to join.
 * @author Nathaniel
 * @version 03-10-2018
 */
public class GameChooser {
	
	private List<GameHandler> games;
	private boolean stop;

	/**
	 * Listens for clients to choose a game to join
	 * @param games The list of all available games.
	 * @param client The client's socket
	 */
	public void runChooser(List<GameHandler> starting, Client client, String gameType) throws IOException {		
		games = starting;
		Thread lister = new Thread(() -> listUpdate(client, gameType));
		lister.setName("Game List Updater");
		lister.start();
		while(true) {
			String response = client.readLine();
			if(response.startsWith("CHOOSE")) {
				if(response.substring(7).equals("CANCEL")) {
					client.println(response);
					stop = true;
					return;
				}
				else {
					int i = Integer.parseInt(response.substring(7));
					if(games.get(i).getType().equals(gameType)
							&& games.get(i).getNumSlots() > 0) {
						synchronized(games) {
							games.get(i).addPlayer(client);
							client.println("CHOOSE CONFIRM");
							stop = true;
							games.notifyAll();
						}
						client.listenClient();
						return;

					}
				}
			}
			else {
				// Garbage Response received
				stop = true;
				return;
			}
			// Continue in loop in case that game selected was full (or wrong type)
		}
	}
	
	/**
	 * Updates the client's list of available games
	 * @param games the list of all games on the server
	 * @param out the client
	 * @param type the game type the client is looking for
	 */
	private void listUpdate(Client out, String type) {
		stop = false;
		while(!stop) {
			synchronized(games) {
				String options = "CHOOSE ";
				for(int i = 0; i < games.size(); i++) {
					if(games.get(i).getNumSlots() > 0 && games.get(i).getType().equals(type)) {
						String gameName = games.get(i).getGame().getName();
						options += gameName + " " + games.get(i).getNumSlots() + " " + i;
						options += "\t";
					}
				}
				out.println(options);
				try {
					games.wait();
				} catch (InterruptedException e) {}
			}
		}
	}

}

