package machineLearning;

import java.util.ArrayList;

import gameBase.GameOptions;
import gameBase.GameSetup;

public class FastTrainer {

	private static final boolean QUIET = true;
	private static final int EPOCHS = 100;
	
	private static final double ANNEAL_RATE = 1.0 - 5.0/EPOCHS;
	private static final double START_PERTURB_PROB = 0.5;
	private static final double START_PERTURB_MAG = 1;
	private static final double MIN_PERTURB_PROB = 0.01;
	private static final double MIN_PERTURB_MAG = 0.01;
	
	private GameRunner runner;
	private GainModel currentModel;
	private boolean stop;
	
	public FastTrainer() {
		runner = new GameRunner(false);
		stop = false;
	}
	
	public GainModel trainModel(GameSetup setup, GameOptions options, GainModel model) {
		currentModel = new GainModel(model);
		
		Thread trainer = new Thread(() -> trainModel(setup, options, EPOCHS, QUIET));
		trainer.setName("Trainer");
		trainer.start();

		return currentModel;
	}
	
	public void close() {
		stop = true;
	}
	
	/**
	 * Trains a model by playing it against itself.
	 * @param setup The GameSetup to train on.
	 * @param options the GameOptions to train on.
	 * @param epochs Number of epochs to train for.
	 * @param quiet Whether or not to print details of each game.
	 */
	private void trainModel(GameSetup setup, GameOptions options, int epochs, boolean quiet) {
		
		// Starting perturbation parameters
		double perturbProbability = START_PERTURB_PROB;
		float perturbMagnitude = (float)START_PERTURB_MAG;
		
		// Setup game options for training
		ArrayList<String> cpuTypes = new ArrayList<>();
		for(int i = 0; i < options.getNumPlayers(); i++) {
			cpuTypes.add("ML");
		}
		GameOptions simOptions = new GameOptions(false);
		simOptions.hideGraphics();
		simOptions.setNumPlayers(cpuTypes.size());
		simOptions.setNPC(cpuTypes);

		for(int epoch = 1; epoch <= epochs && !stop; epoch++) {
			System.out.printf("Epoch %d, p = %5.3f, mag = %5.3f: ", epoch, perturbProbability, perturbMagnitude);

			// Copy model to make starting set
			ArrayList<GainModel> models = new ArrayList<>();
			models.add(currentModel);
			while(models.size() < simOptions.getNumNPC()) {
				GainModel newModel = new GainModel(currentModel);
				newModel.perturbMain(perturbProbability, perturbMagnitude);
				newModel.perturbCross(perturbProbability*0.1, perturbMagnitude);
				models.add(newModel);
			}

			// Run the training
			int winner = runner.runGameSet(setup, simOptions, models, quiet);

			// Keep whoever won more
			if(winner >= 1) {
				System.out.println(" **** MODEL REPLACEMENT ****");
				currentModel.updateTo(models.get(winner));
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
	}


}
