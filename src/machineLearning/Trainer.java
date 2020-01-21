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

	private static final String FILENAME = "Saves/Base/First Game.dog";
	private static final boolean QUIET = true;

	private static final int EPOCHS = 300;
	private static final double ANNEAL_RATE = 0.98;
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
		GameOptions options = new GameOptions(false);
		options.hideGraphics();
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);
		DominionGame game = new DominionGame(setup, options);
		DominionClient dc = new DominionClient();
		new LocalConnection(dc, game);
		GainModel startingModel = game.models.getGainModels().get(0);
		
		// Setup model saving directory
		String filepath = "Training/Game_" + setup.hashCode();
		File folder = new File(filepath);
		if(!folder.exists()) folder.mkdir();

		// Setup Council of Ten Round Robin
		ArrayList<GainModel> allModels = new ArrayList<>();
		GainModel[] councilOfTen = new GainModel[10];
		
		// Train 100 models based on the starting model
		for(int modelNum = 0; modelNum < 100; modelNum++) {
			System.out.println("Training Model " + (modelNum + 1));
			allModels.add(trainModel(setup, startingModel, epochs, quiet));
		}
		
		// Test the models against each other
		int[] scores = new int[allModels.size()];
		cpuTypes.clear();
		cpuTypes.add("ML");
		cpuTypes.add("ML");
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);
		for(int i = 0; i < allModels.size(); i++) {
			for(int j = i + 1; j < allModels.size(); j++) {
				ArrayList<GainModel> models = new ArrayList<>();
				models.add(allModels.get(i));
				models.add(allModels.get(j));
				System.out.print("Model " + (i+1) + " vs. Model " + (j+1) + ": ");
				int winner = runGameSet(setup, options, models, quiet);
				if(winner == 0) {
					scores[i] += 1;
					scores[j] -= 1;
				}
				else if(winner == 1) {
					scores[i] -= 1;
					scores[j] += 1;					
				}
			}
		}
		
		// Extract the 10 best models and put them on the council
		int[] topModels = new int[councilOfTen.length];
		int minTopModel = 0;
		for(int i = 0; i < topModels.length; i++) {
			topModels[i] = i;
			if(scores[topModels[i]] < scores[topModels[minTopModel]]) {
				minTopModel = i;
			}
		}
		for(int i = topModels.length; i < scores.length; i++) {
			if(scores[i] > scores[topModels[minTopModel]]) {
				topModels[minTopModel] = i;
				for(int j = 0; j < topModels.length; j++) {
					if(scores[topModels[j]] < scores[topModels[minTopModel]]) {
						minTopModel = j;
					}
				}
			}	
		}
		System.out.println("Top ten:");
		for(int i = 0; i < topModels.length; i++) {
			System.out.println("  Model " + (topModels[i] + 1) + " (" + scores[topModels[i]] + ")");
			councilOfTen[i] = allModels.get(topModels[i]);
			councilOfTen[i].save(new File(filepath + "/GainModel_C" + (i+1) + ".txt"));
		}
		
		for(int generation = 0; generation < 10; generation++) { // TODO increase number of generations
			System.out.println("******** GENERATION " + (generation + 1) + " ********");

			// Intermix the Council of Ten to create new set of models and train them
			allModels.clear();
			for(int i = 0; i < councilOfTen.length; i++) {
				for(int j = 0; j < councilOfTen.length; j++) {
					GainModel offspring = new GainModel(councilOfTen[i], councilOfTen[j]);
					System.out.println("Training Model " + allModels.size());
					offspring = trainModel(setup, offspring, epochs, quiet);
					allModels.add(offspring);
				}
			}

			// Test new models against the Council of Ten
			scores = new int[allModels.size()];
			cpuTypes.clear();
			cpuTypes.add("ML");
			cpuTypes.add("ML");
			options.setNumPlayers(cpuTypes.size());
			options.setNPC(cpuTypes);
			int[] councilScores = new int[councilOfTen.length];
			for(int i = 0; i < allModels.size(); i++) {
				for(int j = 0; j < councilOfTen.length; j++) {
					ArrayList<GainModel> models = new ArrayList<>();
					models.add(allModels.get(i));
					models.add(councilOfTen[j]);
					System.out.print("Model " + (i+1) + " vs. Council Member " + (j+1) + ": ");
					int winner = runGameSet(setup, options, models, quiet);
					if(winner == 0) {
						scores[i] += 1;
						councilScores[j] -= 1;
					}
					else if(winner == 1) {
						scores[i] -= 1;
						councilScores[j] += 1;					
					}				
				}
			}

			// Find n highest scoring models where n < min score of selected models > 4
			ArrayList<GainModel> replacements = new ArrayList<>();
			while(true) {
				int max = 0;
				for(int i = 1; i < allModels.size(); i++) {
					if(scores[i] > scores[max]) {
						max = i;
					}
				}
				if(replacements.size() >= councilOfTen.length
						|| scores[max] <= replacements.size() || scores[max] < 4) {
					break;
				}
				System.out.println("Model " + (max + 1) + " joining the council (" + scores[max] + ")");
				replacements.add(allModels.get(max));
				scores[max] = -1;
			}
			
			// Replace lowest scoring members of the Council of Ten with selected models
			for(int i = 0; i < replacements.size(); i++) {
				int min = 0;
				for(int j = 1; j < councilOfTen.length; j++) {
					if(councilScores[j] < councilScores[min]) {
						min = j;
					}
				}
				System.out.println("Council member " + (min + 1) + " leaving the council (" + councilScores[min] + ")");
				councilOfTen[min] = replacements.get(i);
				councilOfTen[min].save(new File(filepath + "/GainModel_C" + (min+1) + ".txt"));
				councilScores[min] = allModels.size();
			}

		}
	}

	/**
	 * Trains a model by playing it against itself.
	 * @param setup The GameSetup to train on.
	 * @param model The GainModel to train.
	 * @param epochs Number of epochs to train for.
	 * @param quiet Whether or not to print details of each game.
	 * @return A copy of the model that has been updated based on training
	 */
	private GainModel trainModel(GameSetup setup, GainModel model, int epochs, boolean quiet) {
		model = new GainModel(model);
		
		// Starting perturbation parameters
		double perturbProbability = START_PERTURB_PROB;
		float perturbMagnitude = (float)START_PERTURB_MAG;
		
		// Setup game options for training
		ArrayList<String> cpuTypes = new ArrayList<>();
		cpuTypes.add("ML");
		cpuTypes.add("ML");
		GameOptions options = new GameOptions(false);
		options.hideGraphics();
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);

		for(int epoch = 1; epoch <= epochs; epoch++) {
			System.out.printf("Epoch %d, p = %5.3f, mag = %5.3f: ", epoch, perturbProbability, perturbMagnitude);

			// Copy model to make starting pair
			ArrayList<GainModel> models = new ArrayList<>();
			GainModel newModel = new GainModel(model);
			newModel.perturb(perturbProbability, perturbMagnitude);
			models.add(model);
			models.add(newModel);

			// Run the training
			int winner = runGameSet(setup, options, models, quiet);

			// Keep whoever won more
			if(winner == 1) {
				System.out.println("**** MODEL REPLACEMENT ****");
				model = models.get(1);
			}

			// Update perturbation parameters
			perturbProbability *= ANNEAL_RATE;
			perturbMagnitude *= ANNEAL_RATE;
			if(perturbProbability < MIN_PERTURB_PROB) {
				perturbProbability = MIN_PERTURB_PROB;
			}
			if(perturbMagnitude < MIN_PERTURB_MAG) {
				perturbMagnitude = (float)MIN_PERTURB_MAG;
			}
		}
		

		// Benchmark against Big Money
		ArrayList<GainModel> models = new ArrayList<>();
		models.add(model);
		cpuTypes = new ArrayList<>();
		cpuTypes.add("BigMoney");
		cpuTypes.add("ML");
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);
		System.out.print("Big Money vs. Machine Learning: ");
		int winner = runGameSet(setup, options, models, quiet);
		if(winner == 1) {
			System.out.println("Machine Learning is better!");
		}
		else {
			System.out.println("Big Money retains its throne");
		}
		
		return model;
	}

	/**
	 * Simulates a set of games to determine which NPC is better.
	 * @param setup The GameSetup to simulate.
	 * @param options the GameOptions for simulation.
	 * @param models GainModels for the NPCs if needed.
	 * @param quiet whether results of each game are printed.
	 * @return The index of the NPC that was statistically better (p < 0.05) 
	 * or -1 if neither was significantly better after 100,000 games.
	 */
	private int runGameSet(GameSetup setup, GameOptions options, ArrayList<GainModel> models, boolean quiet) {
		boolean significant = true;
		runGames(setup, options, models, 10, quiet);
		if(games[0] > 1 && games[1] > 1) { // If both won more than 1 game (p > 0.05)
			runGames(setup, options, models, 100, quiet);
			if(games[0] > 41 && games[1] > 41) { // If both won more than 41 games (p > 0.05)
				runGames(setup, options, models, 1000, quiet);
				if(games[0] > 473 && games[1] > 473) { // If both won more than 473 games (p > 0.05)
					runGames(setup, options, models, 10000, quiet);
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
		
		// Reset the running parameters
		index = 0;
		runners = 0;
		games = new int[6];
		
		return output;
	}

	/**
	 * Simulates Dominion games for training on multiple threads.
	 * @param setup The GameSetup to simulate.
	 * @param gameOptions GameOptions for the simulation.
	 * @param models GainModels for NPCs that need them.
	 * @param numSimulations Total number of simulations to complete.
	 * @param quiet whether to print results of individual games.
	 */
	private void runGames(GameSetup setup, GameOptions gameOptions, List<GainModel> models, int numSimulations, boolean quiet) {

		try {

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
				for(String s : gameOptions.getNPCTypes()) {
					System.out.print(s + ", ");
				}
				System.out.println("\n");
			}

			// Run Threads
			for(int i = 0; i < cores; i++) {
				Thread gameRunner = new Thread(() ->
				runGameThread(setup, gameOptions, models, numSimulations, quiet));
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