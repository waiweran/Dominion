package machineLearning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import gameBase.Player;
import genericGame.network.LocalConnection;

/**
 * Runs Dominion games to test and train computer players
 * @author Nathaniel
 * @version 01-27-2020
 */
public class GameRunner {
	
	private volatile int index;
	private volatile int runners;
	private volatile int[] games;	
	
	private boolean parallel;

	/**
	 * Initializes the GameRunner
	 * @param parallelize true to run games in parallel using 
	 * all available cores, false to run games in a single thread
	 */
	public GameRunner(boolean parallelize) {
		index = 0;
		runners = 0;
		games = new int[6];
		parallel = parallelize;
	}
	
	/**
	 * Simulates a set of games to determine which computer player is better.
	 * @param setup The GameSetup to simulate.
	 * @param options the GameOptions for simulation.
	 * @param models GainModels for the NPCs if needed.
	 * @param quiet whether results of each game are printed.
	 * @return The index of the NPC that was statistically better (p < 0.05) 
	 * or -1 if neither was significantly better after 100,000 games.
	 */
	public int runGameSet(GameSetup setup, GameOptions options, List<GainModel> models, boolean quiet) {
		index = 0;
		runners = 0;
		Arrays.fill(games, 0);
		
		boolean significant = true;
		runGamesInternal(setup, options, models, 10, quiet);
		if(games[0] > 1 && games[1] > 1) { // If both won more than 1 game (p > 0.05)
			runGamesInternal(setup, options, models, 100, quiet);
			if(games[0] > 41 && games[1] > 41) { // If both won more than 41 games (p > 0.05)
				runGamesInternal(setup, options, models, 1000, quiet);
				if(games[0] > 473 && games[1] > 473) { // If both won more than 473 games (p > 0.05)
					runGamesInternal(setup, options, models, 10000, quiet);
					if(games[0] > 4917 && games[1] > 4917) { // If both won more than 4917 games (p > 0.05)
						significant = false;
					}		
				}		
			}		
		}
		System.out.println("Result: " + games[0] + " to " + games[1]);

		// Assess results
		int output = -1;
		if(significant) {
			if(games[0] > games[1]) {
				output = 0;
			}
			else {
				output = 1;
			}
		}
				
		return output;
	}
	
	/**
	 * Simulates Dominion games for training.
	 * @param setup The GameSetup to simulate.
	 * @param options GameOptions for the simulation.
	 * @param models GainModels for NPCs that need them.
	 * @param numSimulations Total number of simulations to complete.
	 * @param quiet whether to print results of individual games.
	 * @return An array indicating how many games each player won.
	 */
	public int[] runGames(GameSetup setup, GameOptions options, List<GainModel> models, int numSimulations, boolean quiet) {
		index = 0;
		runners = 0;
		Arrays.fill(games, 0);
		runGamesInternal(setup, options, models, numSimulations, quiet);
		return Arrays.copyOf(games, options.getNumPlayers());
	}

	/**
	 * Manages game simulation for training on multiple threads.
	 * @param setup The GameSetup to simulate.
	 * @param options GameOptions for the simulation.
	 * @param models GainModels for NPCs that need them.
	 * @param numSimulations Total number of simulations to complete.
	 * @param quiet whether to print results of individual games.
	 */
	private void runGamesInternal(GameSetup setup, GameOptions options, List<GainModel> models, int numSimulations, boolean quiet) {	
		try {

			// Collect system information
			int cores = Runtime.getRuntime().availableProcessors();
			synchronized(this) {
				runners = cores;
			}

			// Print Header
			if(!quiet) {
				System.out.println(cores + " Cores Detected");
				System.out.println("Playing "  + setup.getGameName());
				System.out.print("Players: ");
				for(String s : options.getNPCTypes()) {
					System.out.print(s + ", ");
				}
				System.out.println("\n");
			}
			
			// Run Threads, then wait for them to complete
			if(parallel) {
				for(int i = 0; i < cores; i++) {
					Thread gameRunner = new Thread(() ->
					runGameThread(setup, options, models, numSimulations, quiet));
					gameRunner.setName("Game Runner " + i);
					gameRunner.start();
				}
				synchronized(this) {
					wait();
				}
			}
			
			// Just run the games in this thread
			else {
				runners = 1;
				runGameThread(setup, options, models, numSimulations, quiet);
			}
			
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Single thread running game simulations.
	 * @param setup The GameSetup for the simulation.
	 * @param gameOptions the GameOptions for the simulation.
	 * @param models GainModels to be used for any computer players requiring them.
	 * @param numRuns The total number of runs all threads should complete.
	 * @param quiet Whether to print information about the runs.
	 */
	private void runGameThread(GameSetup setup, GameOptions gameOptions, List<GainModel> models, int numRuns, boolean quiet) {

		//Run Multiple Test Games
		ArrayList<String> players = new ArrayList<>();
		HashMap<String, Integer> names = new HashMap<>();
		int gameNum = 0;

		while(true) {

			synchronized(this) {
				if(index >= numRuns) break;
				gameNum = index++;				
			}
			
			DominionGame game = new DominionGame(setup, gameOptions);
			try {
				
				//Setup Game
				game.models.setGainModels(models);
				DominionClient dc = new DominionClient();
				new LocalConnection(dc, game);


				//Run game simulation
				game.getCurrentPlayer().startTurn();

				// List players in games
				if(players.isEmpty()) {
					for(Player p : game.players) {
						players.add(p.getComputerPlayer().getName() + " " + p.getPlayerNum());
						names.put(p.getComputerPlayer().getName() + " " + p.getPlayerNum(), p.getPlayerNum());
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


					//Determine who wins game
					Player winnerP = scoreFiles.lastEntry().getValue();
					int entryNum = winnerP.getPlayerNum() - 1;
					games[entryNum] += 1;
				}
			} 
			catch(StackOverflowError e) {
				synchronized(this) {
					index--;
				}
				try {
					System.err.println("Stack Overflow:");
					for(Player p : game.players) {
						System.err.println("\t" + p.getPlayerName() + ": " + p.deck);
					}
				}
				catch(Exception e2) {
					e.printStackTrace();
				}
				
			}
			catch(Exception e) {
				synchronized(this) {
					index--;
				}
				try {
					if(game.getCurrentPlayer().deck.size() == 0) {
						System.err.println("Empty deck caused error:");
					}
					else {
						System.err.println("Draw cards available, error:");
					}
					System.err.println("Thread " + Thread.currentThread().getName());
					e.printStackTrace();
				}
				catch(Exception e2) {
					e.printStackTrace();
				}
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

					//Print Success Rate
					for(String name : players) {
						int playerNum = names.get(name);
						int wins = games[playerNum - 1];
						System.out.println(name + " win rate: " + wins + "/" + numRuns);
					}	
				}

				// Let the main thread resume
				if(parallel) {
					this.notify();
				}

			}
		}
	}


}
