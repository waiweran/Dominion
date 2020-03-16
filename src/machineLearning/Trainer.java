package machineLearning;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;

public class Trainer {

	private static final String FILENAME = "Saves/Base/First Game.dog";
	private static final boolean QUIET = true;

	private static final int GENERATIONS = 100;
	private static final int EPOCHS = 100;
	private static final double ANNEAL_RATE = 0.96;
	private static final double START_PERTURB_PROB = 0.5;
	private static final double START_PERTURB_MAG = 1;
	private static final double MIN_PERTURB_PROB = 0.01;
	private static final double MIN_PERTURB_MAG = 0.01;
	
	private GameRunner runner;

	public Trainer() {
		runner = new GameRunner(true);
	}

	public void train(GameSetup setup, int epochs, boolean quiet) throws IOException {

		// Create game to get initial model
		GameOptions options = new GameOptions(false);
		options.hideGraphics();
		options.setNumPlayers(1);
		DominionGame game = new DominionGame(setup, options);
		game.startGame();
		GainModel startingModel = game.models.getBasicGainModel();
		
		// Setup model saving directory
		String filepath = "Training/Game_" + setup.hashCode();
		File folder = new File(filepath);
		if(!folder.exists()) folder.mkdir();

		// Setup Elite Ten Round Robin
		GainModel[] eliteTen = new GainModel[10];
		Arrays.fill(eliteTen, startingModel);
		for(int i = 0; i < eliteTen.length; i++) {
			File eliteFile = new File("Training/Game_" + setup.hashCode()
					+ "/GainModel_C" + (i + 1) + ".txt");
			if(eliteFile.isFile()) {
				eliteTen[i] = new GainModel(game.board, eliteFile);
			}
		}
		ArrayList<GainModel> allModels = new ArrayList<>();
		ArrayList<String> cpuTypes = new ArrayList<>();
		cpuTypes.add("ML");
		cpuTypes.add("ML");
		int[] scores = new int[eliteTen.length*eliteTen.length];
		for(int generation = 0; generation < GENERATIONS; generation++) {
			System.out.println("******** GENERATION " + (generation + 1) + " ********");

			// Intermix the Elite Ten to create new set of models and train them
			allModels.clear();
			for(int i = 0; i < eliteTen.length; i++) {
				for(int j = 0; j < eliteTen.length; j++) {
					GainModel offspring = new GainModel(eliteTen[i], eliteTen[j]);
					System.out.println("Training Model " + (allModels.size() + 1));
					offspring = trainModel(setup, offspring, epochs, quiet);
					allModels.add(offspring);
				}
			}

			// Test new models against the Elite Ten
			Arrays.fill(scores, 0);
			options.setNumPlayers(cpuTypes.size());
			options.setNPC(cpuTypes);
			int[] eliteScores = new int[eliteTen.length];
			for(int i = 0; i < allModels.size(); i++) {
				for(int j = 0; j < eliteTen.length; j++) {
					ArrayList<GainModel> models = new ArrayList<>();
					models.add(eliteTen[j]);
					models.add(allModels.get(i));
					System.out.print("Elite Ten Seat " + (j+1) + " vs. Model " + (i+1) + ": ");
					int winner = runner.runGameSet(setup, options, models, quiet);
					if(winner == 1) {
						System.out.print(" **V**");
						scores[i] += 1;
						eliteScores[j] -= 1;
					}
					else if(winner == 0) {
						scores[i] -= 1;
						eliteScores[j] += 1;					
					}	
					System.out.println();
				}
			}

			// Find n highest scoring models where n < min score of selected models > 0
			ArrayList<Integer> replacements = new ArrayList<>();
			ArrayList<Integer> potentialReplacements = new ArrayList<>();
			for(int wins = eliteTen.length; wins > 0; wins--) {
				for(int i = 0; i < allModels.size(); i++) {
					if(scores[i] == wins) {
						System.out.print("Big Money vs. Model " + (i+1) + " (" + wins + "): ");
						if(bigMoneyTest(setup, allModels.get(i), quiet)) {
							potentialReplacements.add(i);
						}
					}
				}
				if(potentialReplacements.size() + replacements.size() <= wins) {
					replacements.addAll(potentialReplacements);
				}
				else if(potentialReplacements.size() + replacements.size() > wins) {
					for(int i = 0; true; i++) {
						if(i + 1 >= potentialReplacements.size()) {
							i = 0;
						}
						ArrayList<GainModel> models = new ArrayList<>();
						models.add(allModels.get(potentialReplacements.get(i)));
						models.add(allModels.get(potentialReplacements.get(i + 1)));
						System.out.print("Model " + (potentialReplacements.get(i) + 1) 
								+ " vs. Model " + (potentialReplacements.get(i+1) + 1) + ": ");
						int winner = runner.runGameSet(setup, options, models, quiet);
						System.out.println();
						if(winner == 0) {
							potentialReplacements.remove(i + 1);
						}
						else if(winner == 1) {
							potentialReplacements.remove(i);
						}
						if(potentialReplacements.size() + replacements.size() <= wins) {
							replacements.addAll(potentialReplacements);
							break;
						}
					}
				}
				potentialReplacements.clear();
				if(replacements.size() >= wins - 1) {
					for(int val : replacements) {
						System.out.println("Model " + (val+1) + " joining the Elite Ten");
					}
					break;
				}
			}
			
			// Replace lowest scoring members of the Elite Ten with selected models
			for(int i = 0; i < replacements.size(); i++) {
				int min = 0;
				for(int j = 1; j < eliteTen.length; j++) {
					if(eliteScores[j] < eliteScores[min]) {
						min = j;
					}
				}
				System.out.println("Elite Ten member " + (min + 1) + " leaving (" + eliteScores[min] + ")");
				eliteTen[min] = allModels.get(replacements.get(i));
				eliteTen[min].save(new File(filepath + "/GainModel_C" + (min+1) + ".txt"));
				eliteScores[min] = allModels.size();
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
			newModel.perturbMain(perturbProbability, perturbMagnitude);
			newModel.perturbCross(perturbProbability*0.1, perturbMagnitude);
			models.add(model);
			models.add(newModel);

			// Run the training
			int winner = runner.runGameSet(setup, options, models, quiet);

			// Keep whoever won more
			if(winner == 1) {
				System.out.println(" **** MODEL REPLACEMENT ****");
				model = models.get(1);
			}
			else {
				System.out.println();
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
				
		return model;
	}

	/**
	 * Benchmarks the given model against a Big Money player.
	 * @param setup The GameSetup to train on.
	 * @param model The GainModel to train.
	 * @param quiet Whether or not to print details of each game.
	 * @return true if the machine learning model is better, false otherwise.
	 */
	private boolean bigMoneyTest(GameSetup setup, GainModel model, boolean quiet) {
		ArrayList<GainModel> models = new ArrayList<>();
		models.add(model);
		ArrayList<String> cpuTypes = new ArrayList<>();
		cpuTypes.add("BigMoney");
		cpuTypes.add("ML");
		GameOptions options = new GameOptions(false);
		options.hideGraphics();
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);
		int winner = runner.runGameSet(setup, options, models, quiet);
		if(winner == 1) {
			System.out.println(" **** Machine Learning is better! ****");
		}
		else {
			System.out.println(" **** Big Money retains its throne ****");
		}
		return winner == 1;
	}
	
	
	public void firstPlayerTest(GameSetup setup, int numSimulations, boolean quiet) {
		
		// Get Models
		GameOptions options = new GameOptions(false);
		options.hideGraphics();
		options.setNumPlayers(1);
		DominionGame game = new DominionGame(setup, options);
		game.startGame();
		GainModel startingModel = game.models.getGainModel();
		ArrayList<GainModel> models = new ArrayList<>();
		models.add(startingModel);
		models.add(startingModel);

		// Run games
		ArrayList<String> cpuTypes = new ArrayList<>();
		cpuTypes.add("Random");
		cpuTypes.add("Random");
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);
		System.out.print("Random Model P1 vs Random Model P2: ");
		int[] games = runner.runGames(setup, options, models, numSimulations, quiet);
		System.out.print("Result: " + games[0]);
		for(int i = 1; i < options.getNumPlayers(); i++) {
			System.out.print(" to " + games[i]);
		}	
		System.out.println();
	}


	public static void main(String[] args) {
		try {
			new Trainer().train(new GameSetup(new File(FILENAME)), EPOCHS, QUIET);
			//new Trainer().firstPlayerTest(new GameSetup(new File(FILENAME)), 100000, QUIET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}