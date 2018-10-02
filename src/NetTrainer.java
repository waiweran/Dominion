import java.io.File;

import neuralnet.DataProcessor;
import neuralnet.NetworkTrainer;

public class NetTrainer {

	public static void main(String[] args) {
		DataProcessor datap = new DataProcessor();
		NetworkTrainer nett = new NetworkTrainer();
		
		System.out.println("Loading data...");
		datap.importTrainingFiles();
		datap.loadOldData();
		datap.loadNewData();
		datap.saveData();
		datap.clearTrainingFiles();
		
		System.out.println("Training play net...");
		System.out.println("Data set size: " + datap.getPlayData().size());
		nett.makeNetwork(datap.getPlayData(), new File("Networks/play.nnet"));
		System.out.println("Training gain net...");
		System.out.println("Data set size: " + datap.getGainData().size());
		nett.makeNetwork(datap.getGainData(), new File("Networks/gain.nnet"));
		System.out.println("Complete");
	}

}
