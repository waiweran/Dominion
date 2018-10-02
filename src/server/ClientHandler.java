package server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Handles an individual client who just connected to the server.
 * @author Nathaniel
 * @version 03-10-2018
 */
public class ClientHandler {
	
	private List<GameHandler> start, current;

	/**
	 * Initializes a new thread to deal with the client.
	 * @param starting current games that are starting.
	 * @param running current games being played.
	 * @param client The client to handle.
	 * @param clientNum The client's number.
	 */
	public ClientHandler(List<GameHandler> starting, List<GameHandler> running, Socket client, int clientNum) {
		start = starting;
		current = running;
		Thread clientThread = new Thread(() -> runHandler(client, clientNum));
		clientThread.setName("Client " + clientNum + " Handler");
		clientThread.start();
	}

	/**
	 * Runs the handler for client messages
	 * @param clientSocket the client's socket.
	 * @param clientNum the client's number.
	 */
	private void runHandler(Socket clientSocket, int clientNum) {
		try {
			Client client = new Client(clientSocket);
			System.out.println("Received Client " + clientNum);
			String message = client.readLine();
			if(message.startsWith("START")) {
				System.out.println("Client " + clientNum + " is Starting Client");
				new GameHandler(start, current, client, message.substring(6), clientNum);
			}
			else if(message.startsWith("REJOIN")) {
				System.out.println("Client " + clientNum + " is Rejoining their Game");
				int gameID = Integer.parseInt(message.substring(7));
				synchronized(current) {
					for(GameHandler h : current) {
						if(h.getID() == gameID) {
							h.rejoin(client);
							break;
						}
					}
				}
			}
			else {
				System.out.println("Client " + clientNum + " is Looking for Game");
				new GameChooser().runChooser(start, client, message);
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}
