package neuralnet;

import java.io.File;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BinaryDeltaRule;

public class NetworkTrainer {
	
	public NetworkTrainer() {

	}
	
	/**
	 * Creates a Neural Network and trains it on the given data.
	 * @param trainingSet The training data set.
	 * @param savefile The file to save the network into.
	 * @return the network.
	 */
	public NeuralNetwork<?> makeNetwork(TrainingData trainingSet, File savefile) {
		NeuralNetwork<?> neuralNetwork = new Perceptron(
				trainingSet.getInputWidth(), trainingSet.getOutputWidth());
		DataSet data = makeTrainingSet(trainingSet);
		((BinaryDeltaRule) neuralNetwork.getLearningRule()).setMaxError(0.1);
		((BinaryDeltaRule) neuralNetwork.getLearningRule()).setMaxIterations(1000);
		neuralNetwork.learn(data);
		neuralNetwork.save(savefile.getAbsolutePath());
		return neuralNetwork;
	}
	
	/**
	 * Trains an existing Neural Network with the given data.
	 * @param neuralNetwork The network to train.
	 * @param trainingSet The new data to train on.
	 * @param savefile The File to save the trained network into.
	 */
	public void trainNetwork(NeuralNetwork<?> neuralNetwork, 
			TrainingData trainingSet, File savefile) {
		neuralNetwork.learn(makeTrainingSet(trainingSet));
		neuralNetwork.save(savefile.getAbsolutePath());
	}
	
	/**
	 * Converts the input TrainingData to a DataSet for training the net.
	 * @param data the input TrainingData
	 * @return the DataSet to train with.
	 */
	private DataSet makeTrainingSet(TrainingData data) {
		DataSet trainingSet = new DataSet(data.getInputWidth(), data.getOutputWidth());
		for(TrainingRow row : data) {
			double[] input = new double[row.getInputWidth()];
			for(int i = 0; i < input.length; i++) {
				input[i] = row.getInput()[i];
			}
			double[] output = new double[row.getOutputWidth()];
			for(int i = 0; i < output.length; i++) {
				output[i] = row.getOutput()[i];
			}
			trainingSet.addRow(new DataSetRow(input, output));
		}
		return trainingSet;
	}

}
