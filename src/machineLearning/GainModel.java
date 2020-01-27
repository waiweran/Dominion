package machineLearning;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import cards.defaults.Curse;
import gameBase.Board;
import gameBase.Supply;

/**
 * Trainable model designed to determine what card to gain
 * @author Nathaniel
 * @version 12-04-2019
 */
public class GainModel {

	private static final float TRAINING_FACTOR = 100;

	private int numSupplies;
	private float[][][] weights;
	
	private Random rand;

	/**
	 * Initializes a new game model.
	 * @param board The board the game model is based on.
	 */
	public GainModel(Board board) {
		rand = new Random();
		initializeWeights(board);
	}

	/**
	 * Initializes a new game model.
	 * @param board The board the game model is based on.
	 * @param file Location to preload weights from.
	 */
	public GainModel(Board board, File file) {
		rand = new Random();
		try {
			load(board, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Initializes a new game model by copying an existing one.
	 * @param other
	 */
	public GainModel(GainModel other) {
		rand = new Random();
		numSupplies = other.numSupplies;
		weights = new float[other.weights.length][other.weights[0].length][other.weights[0][0].length];
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				for(int k = 0; k < weights[i][j].length; k++) {
					weights[i][j][k] = other.weights[i][j][k];
				}	
			}
		}

	}
	
	/**
	 * Initializes a new game model by intermixing two existing models.
	 * Randomly chooses between parents when copying weights.
	 * Keeps all parameters for weight normals together (center, spread, magnitude).
	 * @param parent1
	 * @param parent2
	 */
	public GainModel(GainModel parent1, GainModel parent2) {
		rand = new Random();
		numSupplies = parent1.numSupplies;
		if(numSupplies != parent2.numSupplies) {
			throw new RuntimeException("Incompatible parents - different number of supplies");
		}
		weights = new float[parent1.weights.length][parent1.weights[0].length][parent1.weights[0][0].length];
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				if(rand.nextBoolean()) {
					for(int k = 0; k < weights[i][j].length; k++) {
						weights[i][j][k] = parent1.weights[i][j][k];
					}	
				}
				else {
					for(int k = 0; k < weights[i][j].length; k++) {
						weights[i][j][k] = parent2.weights[i][j][k];
					}	
				}
			}
		}

	}

	/**
	 * Chooses a supply pile to purchase from based on a trainable model.
	 * @param data Information about the current state of the supply piles.
	 * @return The index of the supply to purchase from, or out of bounds for no purchase.
	 */
	public int choose(SupplyData[] data, boolean optional) {
		float[] outputs = evaluateNet(data);
		int maxIndex = 0;
		float max = Float.NEGATIVE_INFINITY;
		for(int i = 0; i < numSupplies; i++) {
			if(data[i].canGain() && outputs[i] > max) {
				maxIndex = i;
				max = outputs[i];
			}
		}
		if(optional && outputs[numSupplies] > max) {
			return numSupplies;
		}
		return maxIndex;
	}

	/**
	 * Trains the supply pile purchase model.
	 * @param data Information about the current state of the supply piles.
	 * @param choice Index of the supply pile a card was purchased from.
	 */
	public void train(SupplyData[] data, int choice) {
		float[] outputs = evaluateNet(data);

		for(int i = 0; i < outputs.length; i++) {
			SupplyData current = data[i];
			for(int j = 0; j < data.length; j++) {
				if(data[j].canGain()) {
					float[] local = weights[i][j];
					// Add to value +/-1 if wrong, 0 if correct * distance between center and current value / training factor
					local[0] += (((j == choice)? 1 : 0) - outputs[j])*(current.getNumAvailable() - local[0])/TRAINING_FACTOR;
					// Multiply by 1 + (pos/neg based on choice * value of evaluated normal compared to 50% / training factor)
					local[1] *= (1 + ((j == choice)? 1 : -1)*(evaluateNormal(current.getNumAvailable(), local[0], local[1], 1) - 0.5)/TRAINING_FACTOR);
					// Add to value +/- 1 based on choice * value of evaluated normal compared to 50% / training factor
					local[2] += ((j == choice)? 1 : -1)*(evaluateNormal(current.getNumAvailable(), local[0], local[1], 1) - 0.5)/TRAINING_FACTOR;

					// Same math as above
					local[3] += (((j == choice)? 1 : 0) - outputs[j])*(current.getNumInDeck() - local[3])/TRAINING_FACTOR;
					local[4] *= (1 + ((j == choice)? 1 : -1)*(evaluateNormal(current.getNumInDeck(), local[3], local[4], 1) - 0.5)/TRAINING_FACTOR);
					local[5] += ((j == choice)? 1 : -1)*(evaluateNormal(current.getNumInDeck(), local[3], local[4], 1) - 0.5)/TRAINING_FACTOR;
				}
			}
		}
	}
	
	/**
	 * Perturb main weights for purchasing each card
	 * @param probability The probability that an individual value is adjusted
	 * @param magnitude A scaling factor for the magnitude of each weight adjustment
	 */
	public void perturbMain(double probability, float magnitude) {
		for(int i = 0; i < weights.length; i++) {
			if(rand.nextDouble() < probability)
				weights[i][i][0] += (float)rand.nextGaussian()*magnitude*0.1; // Center
			if(rand.nextDouble() < probability)
				weights[i][i][1] *= Math.pow(2, rand.nextGaussian()*magnitude*0.1); // Spread, uses exponent to equal probability of doubling and halving, etc.
			if(rand.nextDouble() < probability)
				weights[i][i][2] += (float)rand.nextGaussian()*magnitude; // Multiplier
			
			if(rand.nextDouble() < probability)
				weights[i][i][3] += (float)rand.nextGaussian()*magnitude*0.1; // Center
			if(rand.nextDouble() < probability) 
				weights[i][i][4] *= Math.pow(2, rand.nextGaussian()*magnitude*0.1); // Spread, uses exponent to equal probability of doubling and halving, etc.
			if(rand.nextDouble() < probability)
				weights[i][i][5] += (float)rand.nextGaussian()*magnitude; // Multiplier
		}
	}

	/**
	 * Perturb cross weights that link purchase probability to other cards in the supply and deck
	 * @param probability The probability that an individual value is adjusted
	 * @param magnitude A scaling factor for the magnitude of each weight adjustment
	 */
	public void perturbCross(double probability, float magnitude) {
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				if(i != j) {
					if(rand.nextDouble() < probability)
						weights[i][j][0] += (float)rand.nextGaussian()*magnitude; // Center
					if(rand.nextDouble() < probability)
						weights[i][j][1] *= Math.pow(2, rand.nextGaussian()*magnitude*0.1); // Spread, uses exponent to equal probability of doubling and halving, etc.
					if(rand.nextDouble() < probability)
						weights[i][j][2] += (float)rand.nextGaussian()*magnitude; // Multiplier

					if(rand.nextDouble() < probability)
						weights[i][j][3] += (float)rand.nextGaussian()*magnitude; // Center
					if(rand.nextDouble() < probability) 
						weights[i][j][4] *= Math.pow(2, rand.nextGaussian()*magnitude*0.1); // Spread, uses exponent to equal probability of doubling and halving, etc.
					if(rand.nextDouble() < probability)
						weights[i][j][5] += (float)rand.nextGaussian()*magnitude; // Multiplier
				}
			}
		}
	}

	/**
	 * Evaluates the neural net based on the parameters stored in the matrix.
	 * First applies a constant factor to each output.
	 * Next, for each supply, it applies two bell curves, regarding the number
	 * already owned and the number remaining in the Supply, to each output.
	 * @param data The input data on the current date of the supplies.
	 * @return An array of output values corresponding to the input supplies, higher is better.
	 * Final value in the array indicates support for buying nothing.
	 */
	public float[] evaluateNet(SupplyData[] data) {
		float[] outputs = new float[numSupplies + 1];
		for(int i = 0; i < numSupplies; i++) {
			SupplyData current = data[i];
			for(int j = 0; j < outputs.length; j++) {
				float[] local = weights[i][j];
				outputs[j] += evaluateNormal(current.getNumAvailable(), local[0], local[1], local[2]) +
						evaluateNormal(current.getNumInDeck(), local[3], local[4], local[5]);
			}
		}
		return outputs;
	}

	/**
	 * Evaluates a bell curve.
	 * @param value The value being evaluated.
	 * @param center The center of the bell curve.
	 * @param spread Factor determining how rapidly the curve declines in each direction.
	 * @param multiplier Factor determining the total height of the bell curve.
	 * @return The computed output of the bell curve for the given value.
	 */
	private float evaluateNormal(int value, float center, float spread, float multiplier) {
		return multiplier / (1 + (value - center)*(value - center)*spread);
	}
	
	/**
	 * Smart initialization of the weights in the model
	 * Starts with weighting based on cost, whether the player has 
	 * none of the card, and whether the supply is running out.
	 * @param board The board the game is based on.
	 */
	private void initializeWeights(Board board) {
		List<Supply> supplies = board.getAllSupplies();
		numSupplies = supplies.size();
		weights = new float[numSupplies][numSupplies + 1][6];
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				if(i == j) {
					// Push actions to scale down quickly with purchase
					if(supplies.get(i).getCard().isAction()) {
						weights[i][j][0] = 0;
						weights[i][j][1] = (float)1/(float)supplies.get(i).getQuantity();
						weights[i][j][2] = supplies.get(i).getCard().getCost();
						weights[i][j][3] = 0;
						weights[i][j][4] = (float)1/(float)supplies.get(i).getQuantity();
						weights[i][j][5] = supplies.get(i).getCard().getCost();						
					}
					// Push curse to negative value, minimize scale down
					else if(supplies.get(i).getCard() instanceof Curse) {
						weights[i][j][0] = 0;
						weights[i][j][1] = (float)0.0001;
						weights[i][j][2] = -10;
						weights[i][j][3] = 0;
						weights[i][j][4] = (float)0.0001;
						weights[i][j][5] = -10;					
					}
					// Push other cards (treasure, victory) to scale down slowly with purchase
					else {
						weights[i][j][0] = 0;
						weights[i][j][1] = (float)0.1/(float)supplies.get(i).getQuantity();
						weights[i][j][2] = supplies.get(i).getCard().getCost();
						weights[i][j][3] = 0;
						weights[i][j][4] = (float)0.1/(float)supplies.get(i).getQuantity();
						weights[i][j][5] = supplies.get(i).getCard().getCost();
					}
				}
				else {
					weights[i][j][0] = 0;
					weights[i][j][1] = (float)1;
					weights[i][j][2] = 0;
					weights[i][j][3] = 0;
					weights[i][j][4] = (float)1;
					weights[i][j][5] = 0;
				}
			}
		}
	}
	
	/**
	 * Creates a model and loads weights from a file.
	 * @param board The game board for which the model is being set up.
	 * @param file the location of the weights.
	 * @throws IOException if something goes wrong opening and reading the file.
	 */
	private void load(Board board, File file) throws IOException {
		List<Supply> supplies = board.getAllSupplies();
		numSupplies = supplies.size();
		Scanner in = new Scanner(file);
		int supplyCheck = in.nextInt();
		if(supplyCheck != numSupplies) {
			in.close();
			throw new IOException("Opened file with incorrect number of supplies");
		}
		weights = new float[numSupplies][numSupplies + 1][6];
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				for(int k = 0; k < weights[i][j].length; k++) {
					weights[i][j][k] = in.nextFloat();
				}	
			}
		}
		in.close();
	}
	
	/**
	 * Saves the weights in this model to a file.
	 * @param file The location to write the weights.
	 * @throws IOException if the file can't be written.
	 */
	public void save(File file) throws IOException {
		FileWriter fw = new FileWriter(file);
		PrintWriter pw = new PrintWriter(fw);
		pw.println(numSupplies);
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				for(int k = 0; k < weights[i][j].length; k++) {
					pw.println(weights[i][j][k]);
				}	
			}
		}
		pw.flush();
		fw.flush();
		pw.close();
		fw.close();
	}
	
	/**
	 * Updates this GainModel's weights to match another gain model.
	 * Used for on-the-fly updating of a model that's already in use.
	 * @param other
	 */
	public void updateTo(GainModel other) {
		if(numSupplies != other.numSupplies) {
			throw new RuntimeException("Cannot update to incompatible model");
		}
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				for(int k = 0; k < weights[i][j].length; k++) {
					weights[i][j][k] = other.weights[i][j][k];
				}
			}
		}
	}
	

}
