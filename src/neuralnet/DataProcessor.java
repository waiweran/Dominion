package neuralnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class DataProcessor {
	
	private static final String TRAINING_PATH = "Training/";
	private static final String PLAY_SAVE = "Networks/Play_Data.csv";
	private static final String GAIN_SAVE = "Networks/Gain_Data.csv";
	
	private Map<Long, Map<String, File>> files;
	private TrainingData playData, gainData;

	public DataProcessor() {
		files = new TreeMap<>();
		playData = null;
		gainData = null;
	}
	
	/**
	 * Maps and sorts all training files in the Training directory.
	 */
	public void importTrainingFiles() {
		File[] datafiles = new File(TRAINING_PATH).listFiles();
		for(int i = 0; i < datafiles.length; i++) {
			File file = datafiles[i];
			String filename = file.getName();
			String filetype = filename.substring(filename.indexOf("_") + 1);
			long filetime = Long.parseLong(filename.substring(0, filename.indexOf("_")));
			if(!files.containsKey(filetime)) {
				files.put(filetime, new TreeMap<>());
			}
			files.get(filetime).put(filetype, file);
		}
	}
	
	/**
	 * Loads previously processed data.
	 */
	public void loadOldData() {
		if(playData == null) {
			try {
				playData = new TrainingData(new File(PLAY_SAVE));
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
			}
		}
		else {
			try {
				playData.load(new File(PLAY_SAVE));
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
			}
		}
		if(gainData == null) {
			try {
				gainData = new TrainingData(new File(GAIN_SAVE));
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
			}
		}
		else {
			try {
				gainData.load(new File(GAIN_SAVE));
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Loads all new training data from output files.
	 */
	public void loadNewData() {
		for(long filetime : files.keySet()) {
			Map<String, File> fileset = files.get(filetime);	
			try {
				Scanner inScan = new Scanner(fileset.get("playin.txt"));
				Scanner outScan = new Scanner(fileset.get("playout.txt"));
				TrainingData play = makeTrainingData(inScan, outScan);				
				inScan.close();
				outScan.close();
				if(playData == null) {
					playData = play;
				}
				else {
					playData.append(play);
				}
				inScan = new Scanner(fileset.get("gainin.txt"));
				outScan = new Scanner(fileset.get("gainout.txt"));
				TrainingData gain = makeTrainingData(inScan, outScan);		
				inScan.close();
				outScan.close();
				if(gainData == null) {
					gainData = gain;
				}
				else {
					gainData.append(gain);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}

	private TrainingData makeTrainingData(Scanner inScan, Scanner outScan) {
		String[] inLine = inScan.nextLine().split(", ");
		String[] outLine = outScan.nextLine().split(", ");
		TrainingData output = new TrainingData(inLine.length, outLine.length);
		int[] inData = new int[inLine.length];
		int[] outData = new int[outLine.length];
		for(int i = 0; i < inLine.length; i++) {
			inData[i] = Integer.parseInt(inLine[i]);
		}
		for(int i = 0; i < outLine.length; i++) {
			outData[i] = Integer.parseInt(outLine[i]);
		}
		TrainingRow row = new TrainingRow(inData, outData);
		output.add(row);
		while(inScan.hasNextLine() && outScan.hasNextLine()) {
			inLine = inScan.nextLine().split(", ");
			outLine = outScan.nextLine().split(", ");
			inData = new int[inLine.length];
			outData = new int[outLine.length];
			for(int i = 0; i < inLine.length; i++) {
				inData[i] = Integer.parseInt(inLine[i]);
			}
			for(int i = 0; i < outLine.length; i++) {
				outData[i] = Integer.parseInt(outLine[i]);
			}
			row = new TrainingRow(inData, outData);
			output.add(row);					
		}
		return output;
	}
	
	/**
	 * Saves training data in an easy-to-use format.
	 */
	public void saveData() {
		playData.save(new File(PLAY_SAVE));
		gainData.save(new File(GAIN_SAVE));
	}
	
	/**
	 * Deletes all training files
	 */
	public void clearTrainingFiles() {
		File[] datafiles = new File(TRAINING_PATH).listFiles();
		for(int i = 0; i < datafiles.length; i++) {
			datafiles[i].delete();
		}
	}
	
	/**
	 * @return the loaded play data.
	 */
	public TrainingData getPlayData() {
		return playData;
	}
	
	/**
	 * @return the loaded gain data.
	 */
	public TrainingData getGainData() {
		return gainData;
	}

}
