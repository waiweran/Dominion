package neuralnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class TrainingData implements Iterable<TrainingRow> {
	
	private List<TrainingRow> data;
	private int inWidth, outWidth;

	/**
	 * Initializes the TrainingData.
	 * @param inputWidth The number of entries in a line of input data.
	 * @param outputWidth The number of entries in a line of expected output data.
	 */
	public TrainingData(int inputWidth, int outputWidth) {
		data = new ArrayList<>();
		inWidth = inputWidth;
		outWidth = outputWidth;
	}
	
	/**
	 * Loads a new TrainingData from the given file.
	 * @param file to load TrainingData from.
	 * @throws FileNotFoundException 
	 */
	public TrainingData(File file) throws FileNotFoundException {
		this(-1, -1);
		load(file);
	}
	
	/**
	 * Adds a row to the Training Data.
	 * @param row to add.
	 */
	public void add(TrainingRow row) {
		if(row.getInputWidth() != inWidth || row.getOutputWidth() != outWidth) {
			throw new RuntimeException("Adding row of incorrect size to training data set");
		}
		data.add(row);
	}
	
	/**
	 * Add all entries in the given TrainingData to this one.
	 * @param newData the TrainingData to append to this.
	 */
	public void append(TrainingData newData) {
		if(newData.inWidth != inWidth || newData.outWidth != outWidth) {
			throw new RuntimeException("Adding row of incorrect size to training data set");
		}
		data.addAll(newData.data);
	}
	
	/**
	 * Gets the TrainingRow at the requested index.
	 * @param index 
	 * @return the selected TrainingRow.
	 */
	public TrainingRow get(int index) {
		return data.get(index);
	}
	
	/**
	 * @return the number of entries in an input row.
	 */
	public int getInputWidth() {
		return inWidth;
	}
	
	/**
	 * @return the number of entries in an expected output row.
	 */
	public int getOutputWidth() {
		return outWidth;
	}
	
	/**
	 * @return total number of rows in the data set.
	 */
	public int size() {
		return data.size();
	}
	
	/**
	 * Saves the training data to the given file.
	 * @param file The file to save to.
	 */
	public void save(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(inWidth + ", " + outWidth);
			for(TrainingRow row : data) {
				pw.print(row.getInput()[0]);
				for(int i = 1; i < row.getInputWidth(); i++) {
					pw.print(", " + row.getInput()[i]);
				}
				for(int i = 0; i < row.getOutputWidth(); i++) {
					pw.print(", " + row.getOutput()[i]);
				}
				pw.println();
			}
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Loads data from the given file.
	 * @param file The File to load from.
	 * @throws FileNotFoundException 
	 */
	public void load(File file) throws FileNotFoundException {
		Scanner in = new Scanner(file);
		String[] header = in.nextLine().split(", ");
		if(inWidth < 0 || outWidth < 0) {
			inWidth = Integer.parseInt(header[0]);
			outWidth = Integer.parseInt(header[1]);
		}
		if(inWidth != Integer.parseInt(header[0]) || 
				outWidth != Integer.parseInt(header[1])) {
			in.close();
			throw new RuntimeException("Input and output sizes do not match file");
		}
		while(in.hasNextLine()) {
			String[] line = in.nextLine().split(", ");
			int[] input = new int[inWidth];
			int[] output = new int[outWidth];
			for(int i = 0; i < inWidth; i++) {
				input[i] = Integer.parseInt(line[i]);
			}
			for(int i = 0; i < outWidth; i++) {
				output[i] = Integer.parseInt(line[inWidth + i]);
			}
			data.add(new TrainingRow(input, output));
		}
		in.close();
	}

	@Override
	public Iterator<TrainingRow> iterator() {
		return data.iterator();
	}
	
	@Override
	public String toString() {
		return data.toString();
	}

}
