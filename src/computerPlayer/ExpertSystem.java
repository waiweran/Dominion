package computerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import cards.Card;
import cards.defaults.Curse;
import cards.defaults.Estate;
import cards.defaults.Province;

/**
 * Expert system designed to play the First Game setup.
 * @author Nathaniel
 * @version 03-22-2019
 */
public class ExpertSystem {
	
	private CardProperties props;
	
	/**
	 * Initializes selection tables for the ExpertSystem.
	 */
	public ExpertSystem() {
		props = new CardProperties();
	}
	
	/**
	 * Chooses victory cards for the Cellar to discard.
	 * @param choices The list of cards the Cellar can discard.
	 * @return List of card indices to discard.
	 */
	public ArrayList<Integer> chooseCardsCellar(List<Card> choices) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int i = 0; i < choices.size(); i++) {
			if(choices.get(i).isVictory() || choices.get(i) instanceof Curse) out.add(i);
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
		HashSet<Integer> out = new HashSet<Integer>();
		// Eliminate curses and victory cards
		for(int i = 0; i < choices.size() && out.size() < num; i++) {
			Card card = choices.get(i);
			if(card instanceof Curse || card.isVictory() && !card.isAction() && !card.isTreasure()) {
				out.add(i);
			}
		}
		// Eliminate cheaper actions that do not give +1 action
		int mostExpensiveAction = -1;
		for(Card c : choices) {
			if(c.isAction() && c.getCost() > mostExpensiveAction && !props.givesAction(c)) {
				mostExpensiveAction = c.getCost();
			}
		}
		for(int j = 0; j < 30 && out.size() < num; j++){
			for(int i = 0; i < choices.size() && out.size() < num; i++) {
				Card c = choices.get(i);
				if(c.isAction() && c.getCost() < mostExpensiveAction && !props.givesAction(c)) {
					out.add(i);
				}
			}
		}
		// Eliminate cards by cost
		for(int j = 0; j < 30 && out.size() < num; j++){
			for(int i = 0; i < choices.size() && out.size() < num; i++) {
				if(choices.get(i).getCost() == j) {
					out.add(i);
				}
			}
		}
		// Finish and return list
		ArrayList<Integer> output = new ArrayList<>();
		output.addAll(out);
		if(num == out.size()) return output;
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
		// Choose curse or estate
		for(int i = 0; i < choices.size(); i++) {
			Card card = choices.get(i);
			if(card instanceof Curse || card instanceof Estate) {
				return i;
			}
		}
		// Choose non-victory card by cost
		for(int j = 1; j < 7; j++){
			for(int i = 0; i < choices.size(); i++) {
				if(!choices.get(i).isVictory() && choices.get(i).getCost() == j) {
					return i;
				}
			}
		}
		// Choose any non-Province card
		for(int i = 0; i < choices.size(); i++) {
			if(!(choices.get(i) instanceof Province)) {
				return i;
			}
		}
		// Choose first card
		if(choices.size() > 0) return 0;
		throw new RuntimeException("Unable to choose remodel card from empty list");
	}


	/**
	 * Selects an action to play from a list of available actions
	 * based on cost, and whether the action gives additional actions.
	 * @param options The list of action cards that can be played.
	 * @return The action card to play.
	 */
	public Card chooseAction(List<Card> options) {
		return Collections.max(options, (a, b) -> {
			if(props.givesAction(a) == props.givesAction(b)) {
				return a.getCost() - b.getCost();
			}
			else if(props.givesAction(a)) {
				return 1;
			}
			else {
				return -1;
			}
		});
	}
	
}
