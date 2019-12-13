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
	private float[] individualWeights;
	private float[][][] crossWeights;

	/**
	 * Initializes a new game model.
	 * @param board The board the game model is based on.
	 */
	public GainModel(Board board) {
		initializeWeights(board);
	}

	/**
	 * Initializes a new game model.
	 * @param board The board the game model is based on.
	 * @param file Location to preload weights from.
	 */
	public GainModel(Board board, File file) {
		try {
			load(board, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Initializes a new game model by copying an existing one
	 * @param other
	 */
	public GainModel(GainModel other) {
		numSupplies = other.numSupplies;
		individualWeights = new float[other.individualWeights.length];
		crossWeights = new float[other.crossWeights.length][other.crossWeights[0].length][other.crossWeights[0][0].length];
		for(int i = 0; i < individualWeights.length; i++) {
			individualWeights[i] = other.individualWeights[i];
		}
		for(int i = 0; i < crossWeights.length; i++) {
			for(int j = 0; j < crossWeights[i].length; j++) {
				for(int k = 0; k < crossWeights[i][j].length; k++) {
					crossWeights[i][j][k] = other.crossWeights[i][j][k];
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
			individualWeights[i] += (((i == choice)? 1 : 0) - outputs[i])/TRAINING_FACTOR;
		}

		for(int i = 0; i < outputs.length; i++) {
			SupplyData current = data[i];
			for(int j = 0; j < data.length; j++) {
				if(data[j].canGain()) {
					float[] local = crossWeights[i][j];
					local[0] += (((j == choice)? 1 : 0) - outputs[j])*(current.getNumAvailable() - local[0])/TRAINING_FACTOR;
					local[1] *= ((1 - 0.5/TRAINING_FACTOR) + (choice*2 - 1)*evaluateNormal(current.getNumAvailable(), local[0], local[1], 1)/TRAINING_FACTOR);
					local[2] += (choice*2 - 1)*(evaluateNormal(current.getNumAvailable(), local[0], local[1], 1) - 0.5)/TRAINING_FACTOR;
					local[3] += (((j == choice)? 1 : 0) - outputs[j])*(current.getNumInDeck() - local[3])/TRAINING_FACTOR;
					local[4] *= ((1 - 0.5/TRAINING_FACTOR) + (choice*2 - 1)*evaluateNormal(current.getNumInDeck(), local[3], local[4], 1)/TRAINING_FACTOR);
					local[5] += (choice*2 - 1)*(evaluateNormal(current.getNumInDeck(), local[3], local[4], 1) - 0.5)/TRAINING_FACTOR;
				}
			}
		}
	}
	
	/**
	 * Preturbs the weights in the model for training
	 */
	public void perturb(double probability, float magnitude) {
		Random rand = new Random();
		for(int i = 0; i < individualWeights.length; i++) {
			if(rand.nextDouble() < probability)
				individualWeights[i] += (float)rand.nextGaussian()*magnitude;
		}
		for(int i = 0; i < crossWeights.length; i++) {
			for(int j = 0; j < crossWeights[i].length; j++) {
				if(rand.nextDouble() < probability)
					crossWeights[i][j][0] += (float)rand.nextGaussian()*magnitude;
				if(rand.nextDouble() < probability)
					crossWeights[i][j][1] += (float)rand.nextGaussian()*magnitude;
				if(rand.nextDouble() < probability) {
					float multiplier = (float)rand.nextGaussian()*magnitude;
					if(multiplier != 0)
						crossWeights[i][j][2] *= multiplier;
				}
				if(rand.nextDouble() < probability)
					crossWeights[i][j][3] += (float)rand.nextGaussian()*magnitude;
				if(rand.nextDouble() < probability) {
					float multiplier = (float)rand.nextGaussian()*magnitude;
					if(multiplier != 0)
						crossWeights[i][j][4] *= multiplier;
				}
				if(rand.nextDouble() < probability)
					crossWeights[i][j][5] += (float)rand.nextGaussian()*magnitude;
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
		for(int i = 0; i < outputs.length; i++) {
			outputs[i] += individualWeights[i];
		}
		for(int i = 0; i < numSupplies; i++) {
			SupplyData current = data[i];
			for(int j = 0; j < outputs.length; j++) {
				float[] local = crossWeights[i][j];
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
		individualWeights = new float[numSupplies + 1];
		crossWeights = new float[numSupplies][numSupplies + 1][6];
		individualWeights[0] = 1;
		for(int i = 0; i < numSupplies; i++) {
			if(supplies.get(i).getCard() instanceof Curse) {
				individualWeights[i] = -10;
			}
			else {
				individualWeights[i] = supplies.get(i).getCard().getCost();
			}
		}
		individualWeights[numSupplies] = 1;
		for(int i = 0; i < crossWeights.length; i++) {
			for(int j = 0; j < crossWeights[i].length; j++) {
				if(i == j) {
					crossWeights[i][j][0] = 0;
					crossWeights[i][j][1] = (float)0.1;
					crossWeights[i][j][2] = supplies.get(i).getCard().getCost();
					crossWeights[i][j][3] = 0;
					crossWeights[i][j][4] = (float)0.1;
					crossWeights[i][j][5] = supplies.get(i).getCard().getCost();
				}
				else {
					crossWeights[i][j][1] = (float)1;
					crossWeights[i][j][4] = (float)1;
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
		individualWeights = new float[numSupplies + 1];
		crossWeights = new float[numSupplies][numSupplies + 1][6];
		individualWeights[0] = 1;
		for(int i = 0; i < individualWeights.length; i++) {
			individualWeights[i] = in.nextFloat();
		}
		for(int i = 0; i < crossWeights.length; i++) {
			for(int j = 0; j < crossWeights[i].length; j++) {
				for(int k = 0; k < crossWeights[i][j].length; k++) {
					crossWeights[i][j][k] = in.nextFloat();
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
		for(int i = 0; i < individualWeights.length; i++) {
			pw.println(individualWeights[i]);
		}
		for(int i = 0; i < crossWeights.length; i++) {
			for(int j = 0; j < crossWeights[i].length; j++) {
				for(int k = 0; k < crossWeights[i][j].length; k++) {
					pw.println(crossWeights[i][j][k]);
				}	
			}
		}
		pw.flush();
		fw.flush();
		pw.close();
		fw.close();
	}
	

}
