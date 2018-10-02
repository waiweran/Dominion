package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import genericGame.BoardGame;

/**
 * Used for storing and reading from sockets for each player
 * @author Nathaniel
 * @version 03-10-2018
 */
public class Client {

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private GameHandler handler;
	private boolean gameStart, stop;
	private String name;
	private int myIndex;

	/**
	 * initializes the input and output streams in the socket.
	 */
	public Client(Socket clientSocket) {
		socket = clientSocket;
		stop = false;	
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			name = (String) input.readObject();
		} 
		catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the handler for the game the current player is trying to join
	 * @param game the handler
	 */
	public void setHandler(GameHandler game) {
		handler = game;
	}
	
	/**
	 * Gets the player's name.
	 * @return the player's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the player's index.
	 * @param index the new index.
	 */
	public void setIndex(int index) {
		myIndex = index;
	}
	
	/**
	 * Sends the given text to the client.
	 * @param text to send.
	 */
	public void println(String text) {
		try {
			output.writeObject(text);
			output.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Reads the next line of the client's input
	 * @return the text read.
	 */
	public String readLine() {
		try {
			return (String) input.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Writes the game to the client's output.
	 * @param game the BoardGame to write.
	 */
	public void writeGame(BoardGame game) {
		try {
			output.writeObject(game);
			output.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Starts the game for the client.
	 */
	public void start() {
		gameStart = true;
	}
	
	/**
	 * Kills the client listener thread.
	 */
	public void stop() {
		stop = true;
	}

	/**
	 * The run method of this thread.
	 * Watches Socket input and forwards all input to other players.
	 */
	public void listenClient() {
		try {
			while(!stop) {
				Object fromClient = input.readObject();
				if(fromClient instanceof String) {
					String message = (String) fromClient;
					if(gameStart) {
						handler.passMessage(myIndex, message);
					}
					else {
						if(message.startsWith("GAME CANCEL")) {
							handler.removePlayer(this);
							stop = true;
						}
						if(message.equals("START")) {
							handler.startGame();
						}
					}
				}
				else if(fromClient instanceof BoardGame) {
					handler.backup((BoardGame) fromClient);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			if(!gameStart) handler.removePlayer(this);
			else handler.evictPlayer(this);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// Do nothing, sockets not open
			}
		}
	}
}

