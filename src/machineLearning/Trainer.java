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

	private static final int GENERATIONS = 50;
	private static final int EPOCHS = 50;
	private static final double ANNEAL_RATE = 0.98;
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

		// Setup Council of Ten Round Robin
		GainModel[] councilOfTen = new GainModel[10];
		Arrays.fill(councilOfTen, startingModel);
		ArrayList<GainModel> allModels = new ArrayList<>();
		ArrayList<String> cpuTypes = new ArrayList<>();
		cpuTypes.add("ML");
		cpuTypes.add("ML");
		int[] scores = new int[councilOfTen.length*councilOfTen.length];
		for(int generation = 0; generation < GENERATIONS; generation++) {
			System.out.println("******** GENERATION " + (generation + 1) + " ********");

			// Intermix the Council of Ten to create new set of models and train them
			allModels.clear();
			for(int i = 0; i < councilOfTen.length; i++) {
				for(int j = 0; j < councilOfTen.length; j++) {
					GainModel offspring = new GainModel(councilOfTen[i], councilOfTen[j]);
					System.out.println("Training Model " + (allModels.size() + 1));
					offspring = trainModel(setup, offspring, epochs, quiet);
					allModels.add(offspring);
				}
			}

			// Test new models against the Council of Ten
			Arrays.fill(scores, 0);
			options.setNumPlayers(cpuTypes.size());
			options.setNPC(cpuTypes);
			int[] councilScores = new int[councilOfTen.length];
			for(int i = 0; i < allModels.size(); i++) {
				for(int j = 0; j < councilOfTen.length; j++) {
					ArrayList<GainModel> models = new ArrayList<>();
					models.add(allModels.get(i));
					models.add(councilOfTen[j]);
					System.out.print("Model " + (i+1) + " vs. Council Member " + (j+1) + ": ");
					int winner = runner.runGameSet(setup, options, models, quiet);
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
				boolean beatBM = bigMoneyTest(setup, allModels.get(max), quiet);
				if(beatBM) {
					System.out.println("Model " + (max + 1) + " joining the council (" + scores[max] + ")");
					replacements.add(allModels.get(max));
				}
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
			newModel.perturbMain(perturbProbability, perturbMagnitude);
			newModel.perturbCross(perturbProbability*0.1, perturbMagnitude);
			models.add(model);
			models.add(newModel);

			// Run the training
			int winner = runner.runGameSet(setup, options, models, quiet);

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
		options.setNumPlayers(cpuTypes.size());
		options.setNPC(cpuTypes);
		System.out.print("Big Money vs. Machine Learning: ");
		int winner = runner.runGameSet(setup, options, models, quiet);
		if(winner == 1) {
			System.out.println("Machine Learning is better!");
		}
		else {
			System.out.println("Big Money retains its throne");
		}
		return winner == 1;
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