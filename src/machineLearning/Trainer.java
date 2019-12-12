package machineLearning;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import gameBase.Player;
import genericGame.network.LocalConnection;

public class Trainer {

	private static final String FILENAME = "Saves/Dark Ages/Dark Carnival.dog";
	private static final boolean QUIET = true;

	private static final int EPOCHS = 100;
	private static final double ANNEAL_RATE = 0.99;
	private static final double START_PERTURB_PROB = 0.5;
	private static final double START_PERTURB_MAG = 1;
	private static final double MIN_PERTURB_PROB = 0.01;
	private static final double MIN_PERTURB_MAG = 0.01;
	

	private volatile int index;
	private volatile int runners;
	private volatile int[] games;	

	public Trainer() {
		index = 0;
		runners = 0;
		games = new int[6];
	}

	public void train(GameSetup setup, int epochs, boolean quiet) throws IOException {

		// Create game to get initial model
		ArrayList<String> cpuTypes = new ArrayList<>();
		cpuTypes.add("ML");
		GameOptions gameOptions = new GameOptions(false);
		gameOptions.setNumPlayers(cpuTypes.size());
		gameOptions.setNPC(cpuTypes);
		gameOptions.hideGraphics();
		DominionGame game = new DominionGame(setup, gameOptions);
		DominionClient dc = new DominionClient();
		new LocalConnection(dc, game);
		GainModel model = game.models.getGainModels().get(0);

		// Starting perturbation parameters
		double perturbProbability = START_PERTURB_PROB;
		float perturbMagnitude = (float)START_PERTURB_MAG;
		
		for(int epoch = 0; epoch < epochs; epoch++) {
			System.out.println("Epoch " + (epoch+1) + ": p = " + 
					perturbProbability + ", mag = " + perturbMagnitude);

			// Copy model to make starting pair
			ArrayList<GainModel> models = new ArrayList<>();
			models.add(model);
			models.add(new GainModel(model));
			model.perturb(perturbProbability, perturbMagnitude);

			// Run the training
			cpuTypes = new ArrayList<>();
			cpuTypes.add("ML");
			cpuTypes.add("ML");
			runTraining(10, cpuTypes, models, setup, quiet);
			if(games[0] > 1 && games[1] > 1) { // If both won more than 1 game (p > 0.05)
				runTraining(100, cpuTypes, models, setup, quiet);
				if(games[0] > 37 && games[1] > 37) { // If both won more than 37 games (p > 0.05)
					runTraining(1000, cpuTypes, models, setup, quiet);
					if(games[0] > 461 && games[1] > 461) { // If both won more than 461 games (p > 0.05)
						runTraining(10000, cpuTypes, models, setup, quiet);
//						if(games[0] > 4877 && games[1] > 4877) { // If both won more than 4877 games (p > 0.05)
//							runTraining(100000, cpuTypes, models, setup, quiet);
//						}		
					}		
				}		
			}

			// Keep whoever won more
			System.out.println("Result: " + games[0] + " to " + games[1]);
			if(games[0] < games[1]) {
				model = models.get(1);
			}
			
			// Update perturbation parameters
			if(perturbProbability > MIN_PERTURB_PROB) {
				perturbProbability *= ANNEAL_RATE;
			}
			else {
				perturbProbability = MIN_PERTURB_PROB;
			}
			if(perturbMagnitude > MIN_PERTURB_MAG) {
				perturbMagnitude *= ANNEAL_RATE;
			}
			else {
				perturbMagnitude = (float)MIN_PERTURB_MAG;
			}
			
			
			// Reset the running parameters
			index = 0;
			runners = 0;
			games = new int[6];

		}
		
		// Benchmark against Big Money
		ArrayList<GainModel> models = new ArrayList<>();
		models.add(model);
		cpuTypes = new ArrayList<>();
		cpuTypes.add("BigMoney");
		cpuTypes.add("ML");
		runTraining(10, cpuTypes, models, setup, quiet);
		if(games[0] > 1 && games[1] > 1) { // If both won more than 1 game (p > 0.05)
			runTraining(100, cpuTypes, models, setup, quiet);
			if(games[0] > 37 && games[1] > 37) { // If both won more than 37 games (p > 0.05)
				runTraining(1000, cpuTypes, models, setup, quiet);
				if(games[0] > 461 && games[1] > 461) { // If both won more than 461 games (p > 0.05)
					runTraining(10000, cpuTypes, models, setup, quiet);
					if(games[0] > 4877 && games[1] > 4877) { // If both won more than 4877 games (p > 0.05)
						runTraining(100000, cpuTypes, models, setup, quiet);
					}		
				}		
			}		
		}
		if(games[0] < games[1]) {
			System.out.println("Machine Learning is better!");
		}
		else {
			System.out.println("Big Money retains its throne");
		}
		System.out.println("Big Money: " + games[0] + ", Machine Learning: " + games[1]);
		
		// Save
		model.save(new File("Training/GainModel.txt"));

	}

	private void runTraining(int numSimulations, List<String> cpuTypes, List<GainModel> models, GameSetup setup, boolean quiet) {

		try {

			// Setup games
			GameOptions gameOptions = new GameOptions(false);
			gameOptions.setNumPlayers(cpuTypes.size());
			gameOptions.setNPC(cpuTypes);
			gameOptions.hideGraphics();

			// Collect system information
			int cores = Runtime.getRuntime().availableProcessors();
			synchronized(this) {
				runners = cores;
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
				runGames(setup, gameOptions, models, numSimulations, quiet));
				gameRunner.setName("Game Runner " + i);
				gameRunner.start();
			}

			// Wait for games to complete
			synchronized(this) {
				wait();
			}

		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void runGames(GameSetup setup, GameOptions gameOptions, List<GainModel> models, int numRuns, boolean quiet) {

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
				this.notify();

			}
		}
	}

	public static void main(String[] args) {
		try {
			new Trainer().train(new GameSetup(new File(FILENAME)), EPOCHS, QUIET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}