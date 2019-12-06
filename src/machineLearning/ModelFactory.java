package machineLearning;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gameBase.DominionGame;

public class ModelFactory {
	
	private List<GainModel> gainModels;
	private int index;
	private DominionGame access;

	public ModelFactory(DominionGame game) {
		gainModels = new ArrayList<>();
		index = 0;
		access = game;
	}
	
	public GainModel getGainModel() {
		if(index >= gainModels.size()) {
			try {
				File modelFile = new File("Training/GainModel.txt");
				gainModels.add(new GainModel(access.board, modelFile));
			}
			catch(Exception e) {
				e.printStackTrace();
				gainModels.add(new GainModel(access.board));
			}
		}
		return gainModels.get(index++);
	}
	
	public List<GainModel> getGainModels() {
		return Collections.unmodifiableList(gainModels);
	}
	
	public void setGainModels(List<GainModel> models) {
		gainModels = models;
	}

}
