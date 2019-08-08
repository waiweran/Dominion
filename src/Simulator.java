import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private volatile Map<String, Integer> games;	

	
	/**
	 * Main method of the Simulator class.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		int start = 0;
		boolean saveData = false;
		boolean beQuiet = false;
		if(args[start].equals("-s") || args[start].equals("--save")) {
			start++;
			saveData = true;
		}
		if(args[start].equals("-q") || args[start].equals("--quiet")) {
			start++;
			beQuiet = true;
		}
		boolean save = saveData;
		boolean quiet = beQuiet;
		int numSimulations = Integer.parseInt(args[start]);
		ArrayList<String> cpuTypes = new ArrayList<>();
		for(int i = start + 1; i < args.length; i++) {
			cpuTypes.add(args[i]);
		}
		Simulator sim = new Simulator();
		int cores = Runtime.getRuntime().availableProcessors();
		synchronized(sim) {
			sim.runners = cores;
		}
		
		// Print Header
		if(!quiet) {
			System.out.println(cores + " Cores Detected");
			System.out.println("Playing " + FILENAME);
			System.out.print("Players: ");
			for(String s : cpuTypes) {
				System.out.print(s + ", ");
			}
			System.out.println("\n");
		}
		
		// Run Threads
		for(int i = 0; i < cores; i++) {
			Thread gameRunner = new Thread(() ->
			sim.runGames(numSimulations, cpuTypes, FILENAME, save, quiet));
			gameRunner.setName("Game Runner " + i);
			gameRunner.start();
		}
	}
	
	/**
	 * Initializes the simulator.
	 */
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
	 * @param save True to save winning player moves to file.
	 * @param quiet True to suppress printing to standard out.
	 */
	private void runGames(int numRuns, List<String> cpuTypes, String filename, boolean save, boolean quiet) {
		
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
							players.add(p.getComputerPlayer().getName() + " " + p.getPlayerNum());
						}
					}
					
					//Determine and Print Scores
					TreeMap<Integer, Player> scoreFiles = new TreeMap<Integer, Player>();
					synchronized(this) {
						if(!quiet) System.out.print("Scores for game " + (gameNum + 1) + ": ");
						for(Player p : game.players) {
							scoreFiles.put(p.getScore(), p);
							if(!quiet) System.out.print(p.getScore() + " ");
						}
						if(!quiet) System.out.println();

						//Save Winner's Files
						Player winnerP = scoreFiles.lastEntry().getValue();
						ComputerPlayer winner = winnerP.getComputerPlayer();
						if(save && !winner.getName().equals("Big Money"))
							winner.saveData();

						//Determine who wins game
						String entryName = winner.getName() + " " + winnerP.getPlayerNum();
						if(!games.containsKey(entryName)) {
							games.put(entryName, new Integer(0));
						}
						games.put(entryName, games.get(entryName) + 1);
					}
				} 
				catch(Exception e) {
					synchronized(this) {
						index--;
					}
					if(game.getCurrentPlayer().deck.size() == 0) {
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
				if(!quiet) {
					System.out.print("\nPlayers: ");
					for(String s : players) {
						System.out.print(s + ", ");
					}
					System.out.println();
				}

				//Print Success Rate
				for(String name : players) {
					int wins = 0;
					if(games.containsKey(name)) {
						wins = games.get(name);
					}
					System.out.println(name + " win rate: " + wins + "/" + numRuns);
				}	

			}
		}
	}

}
