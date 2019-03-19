package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * A server for board games.
 * Sends whatever it received to all other players.
 * Also performs initial setup of game.
 * @author Nathaniel
 * @version 03-10-2018
 */
public class GameServer {

	private static final int PORT = 8901;
	
	private ServerSocket listener;
	private boolean stop;
	
	public GameServer() {
		stop = false;
	}

	/**
	 * Initializes the Server
	 * Repeatedly runs the client finder.
	 */
	public void runServer(boolean countThreads) {
		if(countThreads) runThreadCounter();
		while(!stop) {
			try {
				runClientFinder();
			}
			catch (Exception e) {
				e.printStackTrace();
				// Old one crashed, start another one
			}
		}
	}

	/**
	 * Generates a thread that counts and lists the other threads in the system.
	 */
	public void runThreadCounter() {
		Thread threadCounter = new Thread(() -> {
			while(true) {
				System.out.println(Thread.getAllStackTraces().keySet().size() + " Threads: " + 
						Thread.getAllStackTraces().keySet());
				if(stop) break;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {}
			}
		});
		threadCounter.start();
		threadCounter.setName("Thread Counter");
	}

	/**
	 * Finds clients and sets them up to find games to play.
	 * @throws IOException if something goes wrong with I/O
	 */
	public void runClientFinder() throws IOException {
		listener = new ServerSocket(PORT);
		ArrayList<GameHandler> startingGames = new ArrayList<>();
		ArrayList<GameHandler> currentGames = new ArrayList<>();
		System.out.println("Game Server is Running");
		try {
			for (int i = 0; true; i++) {
				new ClientHandler(startingGames, currentGames, listener.accept(), i);
			}
		} 
		catch (IOException e) {
			if(!stop) throw e;
		}
		finally {
			for(GameHandler handler : startingGames) {
				handler.cancelGame();
			}
			for(GameHandler handler : currentGames) {
				handler.cancelGame();
			}
			listener.close();
		}
	}
	
	public void stop() {
		stop = true;
		try {
			if(listener != null) listener.close();
		} catch (IOException e) {
			// ServerSocket closed, do nothing
		}
	}

	/**
	 * Runs the application. Pairs up clients that connect.
	 */
	public static void main(String[] args) {
		new GameServer().runServer(args.length > 0);
	}

}