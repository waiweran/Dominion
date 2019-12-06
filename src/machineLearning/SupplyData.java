package machineLearning;

import cards.Card;
import gameBase.Supply;

public class SupplyData {
	
	private Card card;
	private int cost;
	private boolean isOption;
	private int numAvailable;
	private int inDeck;

	public SupplyData(Supply s, boolean canGain, int numInDeck) {
		card = s.getTopCard();
		cost = card.getCost();
		isOption = canGain;
		numAvailable = s.getQuantity();
		inDeck = numInDeck;
	}
	
	public boolean canGain() {
		return isOption;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getNumAvailable() {
		return numAvailable;
	}
	
	public int getNumInDeck() {
		return inDeck;
	}

}
