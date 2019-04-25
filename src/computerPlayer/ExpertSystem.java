package computerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cards.Card;
import cards.base.Cellar;
import cards.base.Market;
import cards.base.Militia;
import cards.base.Mine;
import cards.base.Moat;
import cards.base.Remodel;
import cards.base.Smithy;
import cards.base.Village;
import cards.base.Woodcutter;
import cards.base.Workshop;
import cards.defaults.Copper;
import cards.defaults.Curse;
import cards.defaults.Duchy;
import cards.defaults.Estate;
import cards.defaults.Gold;
import cards.defaults.Province;
import cards.defaults.Silver;

/**
 * Expert system designed to play the First Game setup.
 * @author Nathaniel
 * @version 03-22-2019
 */
public class ExpertSystem {
	
	private Map<Card, Integer> actionMap, militiaMap, remodelMap;
	
	/**
	 * Initializes selection tables for the ExpertSystem.
	 */
	public ExpertSystem() {
		initializeActionMap();
		initializeMilitiaMap();
		initializeRemodelMap();
	}
	
	/**
	 * Initializes the expert map for action order.
	 * Actions with higher numbers are played first.
	 */
	private void initializeActionMap() {
		actionMap = new HashMap<>();
		actionMap.put(new Village(), 10);
		actionMap.put(new Market(), 10);
		actionMap.put(new Cellar(), 9);
		actionMap.put(new Mine(), 8);
		actionMap.put(new Smithy(), 7);
		actionMap.put(new Remodel(), 7);
		actionMap.put(new Militia(), 7);
		actionMap.put(new Workshop(), 2);
		actionMap.put(new Woodcutter(), 1);
		actionMap.put(new Moat(), 0);
	}
	
	/**
	 * Initializes the expert map for militia discard.
	 * Cards with higher numbers are discarded last.
	 */
	private void initializeMilitiaMap() {
		militiaMap = new HashMap<>();
		militiaMap.put(new Gold(), 20);
		militiaMap.put(new Silver(), 18);
		militiaMap.put(new Market(), 16);
		militiaMap.put(new Village(), 14);
		militiaMap.put(new Smithy(), 12);
		militiaMap.put(new Militia(), 10);
		militiaMap.put(new Mine(), 8);
		militiaMap.put(new Copper(), 6);
		militiaMap.put(new Remodel(), 4);
		militiaMap.put(new Workshop(), 4);
		militiaMap.put(new Woodcutter(), 4);
		militiaMap.put(new Moat(), 4);
		militiaMap.put(new Cellar(), 2);	
		militiaMap.put(new Estate(), 0);
		militiaMap.put(new Duchy(), 0);
		militiaMap.put(new Province(), 0);
		militiaMap.put(new Curse(), 0);
	}
	
	/**
	 * Initializes the expert map for remodel trashing.
	 * Cards with higher numbers are trashed first.
	 */
	private void initializeRemodelMap() {
		remodelMap = new HashMap<>();
		remodelMap.put(new Estate(), 20);
		remodelMap.put(new Workshop(), 18);
		remodelMap.put(new Woodcutter(), 18);
		remodelMap.put(new Cellar(), 16);
		remodelMap.put(new Remodel(), 14);
		remodelMap.put(new Curse(), 13);
		remodelMap.put(new Gold(), 12);
		remodelMap.put(new Silver(), 10);
		remodelMap.put(new Village(), 10);
		remodelMap.put(new Smithy(), 10);
		remodelMap.put(new Militia(), 10);
		remodelMap.put(new Moat(), 8);
		remodelMap.put(new Copper(), 6);
		remodelMap.put(new Duchy(), 4);
		remodelMap.put(new Mine(), 2);
		remodelMap.put(new Market(), 2);
		remodelMap.put(new Province(), 0);
	}
	
	/**
	 * Chooses victory cards for the Cellar to discard.
	 * @param choices The list of cards the Cellar can discard.
	 * @return List of card indices to discard.
	 */
	public ArrayList<Integer> chooseCardsCellar(List<Card> choices) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int i = 0; i < choices.size(); i++) {
			if(choices.get(i).isVictory()) out.add(i);
		}
		return out;
	}
		
	/**
	 * Chooses cards for the Militia to discard.
	 * @param choices The list of cards the Militia can discard.
	 * @param num The number of cards that must be discarded.
	 * @return List of card indices to discard.
	 */
	public ArrayList<Integer> chooseCardsMilitia(List<Card> choices, int num) {	
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int j = 0; j < 50 && num > out.size(); j++){
			for(int i = 0; i < choices.size() && num > out.size(); i++) {
				if(militiaMap.get(choices.get(i)) == j) out.add(i);
			}
		}
		Collections.sort(out);
		if(num == out.size()) return out;
		throw new RuntimeException("Could not select enough cards: " + num + ", " + choices);
	}
	
	/**
	 * Chooses a card for the Mine to trash.
	 * Chooses Copper or Silver if available.
	 * @param choices The list of cards that the Mine can trash.
	 * @return The index of the card to trash.
	 */
	public int chooseCardMine(List<Card> choices) {
		for(int i = 0; i < choices.size(); i++) {
			if(choices.get(i).getName().equals("Copper") 
					|| choices.get(i).getName().equals("Silver")) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Chooses a card for Remodel to trash.
	 * @param choices The list of cards that Remodel can trash.
	 * @return The index of the card to trash.
	 */
	public int chooseCardRemodel(List<Card> choices) {
		for(int j = 30; j >= 0; j--){
			for(int i = 0; i < choices.size(); i++) {
				if(remodelMap.get(choices.get(i)) == j) return i;
			}
		}
		throw new RuntimeException("Unable to choose remodel card from " + choices);
	}


	/**
	 * Selects an action to play from a list of available actions.
	 * Only works for actions in the First Game setup.
	 * @param options The list of action cards that can be played.
	 * @return The action card to play.
	 */
	public Card chooseAction(List<Card> options) {		
		Collections.sort(options, (a, b) -> actionMap.get(a) - actionMap.get(b));		
		return options.get(0);
	}
	
}
