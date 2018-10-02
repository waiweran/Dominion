package neuralnet;

import java.util.Arrays;

public class TrainingRow {
	
	private int[] in, out;

	public TrainingRow(int[] input, int[] output) {
		in = input;
		out = output;
	}
	
	public int[] getInput() {
		return in;
	}
	
	public int[] getOutput() {
		return out;
	}
	
	public int getInputWidth() {
		return in.length;
	}

	public int getOutputWidth() {
		return out.length;
	}
	
	@Override
	public String toString() {
		return "{in:" + Arrays.toString(in) + ", out:" + Arrays.toString(out) + "}";
	}

}
