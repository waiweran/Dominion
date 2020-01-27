package machineLearning;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gameBase.DominionGame;
import gameBase.GameOptions;

public class ModelFactory implements Serializable {
	
	private static final long serialVersionUID = 4629690058531622695L;
	
	private List<GainModel> gainModels;
	private int index;
	private DominionGame access;
	private GameOptions opt;
	FastTrainer trainer;

	public ModelFactory(DominionGame game, GameOptions options) {
		gainModels = new ArrayList<>();
		index = 0;
		access = game;
		opt = options;
		trainer = null;
	}
	
	public GainModel getGainModel() {
		if(index >= gainModels.size()) {
			try {
				String filename = "Training/Game_" + access.setup.hashCode() + "/GainModel.txt";
				gainModels.add(new GainModel(access.board, new File(filename)));
			}
			catch(Exception e) {
				e.printStackTrace();
				GainModel model = new GainModel(access.board);
				trainer = new FastTrainer();
				GainModel tModel = trainer.trainModel(access.setup, opt, model);
				gainModels.add(tModel);
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
	
	public void close() {
		if(trainer != null) trainer.close();
	}

}
