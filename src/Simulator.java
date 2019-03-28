import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import computerPlayer.ComputerPlayer;
import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import gameBase.Player;
import genericGame.network.LocalConnection;

/**
 * Simulates Dominion games with the GUI off and automated card selection.
 * @author Nathaniel
 * @version 8-02-2016
 */
public class Simulator {
	
	private static final String FILENAME = "Saves/Base/First Game.dog";
	
	private volatile int index;
	private volatile int runners;
	private volatile HashMap<String, Integer> games;	

	
	/**
	 * Main method of the Simulator class.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		int numSimulations = Integer.parseInt(args[0]);
		ArrayList<String> cpuTypes = new ArrayList<>();
		for(int i = 1; i < args.length; i++) {
			cpuTypes.add(args[i]);
		}
		Simulator sim = new Simulator();
		int cores = 1;//Runtime.getRuntime().availableProcessors();
		synchronized(sim) {
			sim.runners = cores;
		}
		
		// Print Header
		System.out.println(cores + " Cores Detected");
		System.out.println("Playing " + FILENAME);
		System.out.print("Players: ");
		for(String s : cpuTypes) {
			System.out.print(s + ", ");
		}
		System.out.println("\n");
		
		// Run Threads
		for(int i = 0; i < cores; i++) {
			Thread gameRunner = new Thread(() ->
			sim.runGames(numSimulations, cpuTypes, FILENAME));
			gameRunner.setName("Game Runner " + i);
			gameRunner.start();
		}
	}
	
	public Simulator() {
		index = 0;
		runners = 0;
		games  = new HashMap<>();
	}

	/**
	 * Runs simulations of the specified game.
	 * @param numRuns The number of simulations to run.
	 * @param cpuTypes The types of CPU to simulate with.
	 * @param filename The game setup file to simulate.
	 */
	private void runGames(int numRuns, List<String> cpuTypes, String filename) {
		
		//Run Multiple Test Games
		ArrayList<String> players = new ArrayList<>();
		int gameNum = 0;

		while(true) {
			
			synchronized(this) {
				if(index >= numRuns) break;
				gameNum = index++;				
			}
						
			try {
				//Setup Game
				GameSetup setup = new GameSetup(new File(filename));
				GameOptions gameOptions = new GameOptions(false);
				gameOptions.setNumPlayers(cpuTypes.size());
				gameOptions.setNPC(cpuTypes);
				gameOptions.hideGraphics();
				DominionGame game = new DominionGame(setup, gameOptions);
				DominionClient dc = new DominionClient();
				new LocalConnection(dc, game);

				try {
					
					//Run game simulation
					game.getCurrentPlayer().startTurn();

					// List players in games
					if(players.isEmpty()) {
						for(Player p : game.players) {
							players.add(p.getComputerPlayer().getName());
						}
					}
					
					//Determine and Print Scores
					TreeMap<Integer, Player> scoreFiles = new TreeMap<Integer, Player>();
					synchronized(this) {
						System.out.print("Scores for game " + (gameNum + 1) + ": ");
						for(Player p : game.players) {
							scoreFiles.put(p.getScore(), p);
							System.out.print(p.getScore() + " ");
						}
						System.out.println();

						//Save Winner's Files
						ComputerPlayer winner = scoreFiles.lastEntry().getValue().getComputerPlayer();
						if(!winner.getName().equals("Big Money"))
							winner.saveData();

						//Determine who wins game
						if(!games.containsKey(winner.getName())) {
							games.put(winner.getName(), new Integer(0));
						}
						games.put(winner.getName(), games.get(winner.getName()) + 1);
					}
				} 
				catch(Exception e) {
					synchronized(this) {
						index--;
					}
					if(game.getCurrentPlayer().deck.drawSize() == 0) {
						System.err.println("Empty deck caused error:");
					}
					else {
						System.err.println("Draw cards available, error:");
					}
					e.printStackTrace();
				}
			}
			catch(Exception e) {
				synchronized(this) {
					runners--;
				}
				throw new RuntimeException(e);
			}
		}
		
		synchronized(this) {
			runners--;
			if(runners == 0) {

				// List players in games
				System.out.print("\nPlayers: ");
				for(String s : players) {
					System.out.print(s + ", ");
				}
				System.out.println();

				//Print Success Rate
				for(String winName : games.keySet()) {
					System.out.println(winName + " win rate: " + games.get(winName) + "/" + numRuns);
				}	

			}
		}
	}

}
