package gameBase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import cards.Card;
import genericGame.ObservableList;

/**
 * Deck class for the Dominion game.  
 * Used to manage each player's deck of cards.
 * @author Nathaniel
 * @version 04-02-2015
 */
public class Deck implements Serializable {

	private static final long serialVersionUID = -2000L;

	private Player player;
	private DominionGame game;

	public ObservableList<Card> draw;					//the draw pile
	public ObservableList<Card> discard;				//the discard pile
	public ObservableList<Card> hand;					//the hand
	public ObservableList<Card> play;					//the play space
	public List<Card> duration;							//duration actions during their duration
	public List<Card> tavern;							//the tavern mat
	public HashMap<Card, List<Card>> reserve;			//cards held by other cards
	public ObservableList<Card> gained;					//all cards the player gained this turn

	public boolean minusOneCard;						//-1 card token for Adventures
	private Random random;								//Random number generator

	/**
	 * Constructor for the Deck class.
	 * @param d List of cards to be stored in the deck.
	 */
	public Deck(Random rand, List<Card> deck, DominionGame g, Player p) {
		for(Card c : deck) {
			c.passGame(g);
			c.passPlayer(p);
		}
		player = p;
		game = g;
		draw = new ObservableList<>();
		discard = new ObservableList<>(deck);
		hand = new ObservableList<>();
		play = new ObservableList<>();
		duration = new ObservableList<>();
		tavern = new ObservableList<>();
		reserve = new HashMap<>();	
		gained = new ObservableList<>();
		minusOneCard = false;
		random = rand;
		shuffle();
	}

	/**
	 * Deals a single card off the top(end) of the deck.
	 */
	public void deal() {
		if (draw.size() < 1) {
			shuffle();
			if (draw.size() < 1) {
				return;
			}
		}
		if(minusOneCard) {
			minusOneCard = false;
			return;
		}
		hand.add(draw.remove(draw.size() - 1));
	}

	/**
	 * Returns the card on top(end) of the discard pile.
	 * @return the top card of the discard pile.
	 */
	public Card getDiscardTop() {
		if(discard != null && discard.size() > 0) {
			return discard.get(discard.size() - 1);
		}
		else {
			return null;
		}
	}

	/**
	 * Moves a card from the hand into the play space.
	 * @param index determines which card is moved into the play space.
	 */
	public void playCard(int index) {
		game.log(player.getPlayerName() + " played " + hand.get(index).getName());
		play.add(hand.remove(index));
	}

	/**
	 * Discards a card from the hand.
	 * @param index determines which card is discarded.
	 */
	public void discardCard(int index) {
		discardCard(hand.remove(index));
		
	}
	
	/**
	 * Discards the given card.
	 * @param c the card to discard.
	 */
	public void discardCard(Card c) {
		game.log(player.getPlayerName() + " discarded " + c.getName());
		discard.add(c);
		c.discardAction();
	}
	
	/**
	 * Discards the given cards.
	 * @param cards the list of cards to discard.
	 */
	public void discardCards(List<Card> cards) {
		if(!cards.isEmpty()) {
			game.log(player.getPlayerName() + " discarded " + cards.get(cards.size() - 1).getName());
			for(Card c : cards) {
				discard.add(c);
				c.discardAction();
			}
		}
	}

	/**
	 * Puts a card on top of the deck.
	 * DOES NOT GAIN THE CARD!  
	 * @param c card to be put on top of the deck.
	 */
	public void topOfDeck(Card c) {
		draw.add(c);
		game.log(player.getPlayerName() + " moved  " + c.getName() + " to top of deck");
	}

	/**
	 * Removes the top card from the draw pile and returns it.
	 * @return the top card of the draw pile.
	 */
	public Card getDrawCard() {
		if (draw.size() < 1) {
			shuffle();
			if (draw.size() < 1) {
				return null;
			}
		}
		return draw.remove(draw.size() - 1);
	}
	
	/**
	 * Removes a specified number of cards from the draw pile and returns them.
	 * @param num the number of cards to draw.
	 * @return a List containing the cards in the order they were drawn.
	 */
	public List<Card> getDrawCards(int num) {
		ArrayList<Card> cards = new ArrayList<>();
			for(int i = 0; i < num; i++) {
				cards.add(getDrawCard());
			}
		return cards;
	}

	/**
	 * puts a card from the hand back on the draw pile.
	 * @param index index of the card to be put back.
	 */
	public void putBack(int index) {
		draw.add(hand.get(index));
		hand.remove(index);
	}

	/**
	 * gains a card, putting it on top of the discard pile.
	 * @param c the card to be gained.
	 */
	public void gain(Card c) {
		if(c == null) return;
		c = c.clone();
		c.passGame(game);
		c.passPlayer(player);
		discard.add(c);
		gained.add(c);
		c.gainAction();
		game.log(player.getPlayerName() + " gained " + c.getName());
		for(int i = hand.size() - 1; i >= 0; i--) {
			hand.get(i).reactGain(c);
		}
		if(player.equals(game.getCurrentPlayer())) {
			for(int i = play.size() - 1; i >= 0; i--) {
				play.get(i).respondGain(c);
			}
		}

	}

	/**
	 * gains a card, putting it on top of the player's deck.
	 * @param c the card to be gained.
	 */
	public void gainTopDeck(Card c) {
		if(c == null) return;
		c = c.clone();
		c.passGame(game);
		c.passPlayer(player);
		draw.add(c);
		gained.add(c);
		c.gainAction();
		game.log(player.getPlayerName() + " gained " + c.getName() + ", to top of deck");
		for(int i = hand.size() - 1; i >= 0; i--) {
			hand.get(i).reactGain(c);
		}
		if(player.equals(game.getCurrentPlayer())) {
			for(int i = play.size() - 1; i >= 0; i--) {
				play.get(i).respondGain(c);
			}
		}
	}
	/**
	 * gains a card, putting it on top of the player's deck.
	 * @param c the card to be gained.
	 */
	public void gainHand(Card c) {
		if(c == null) return;
		c = c.clone();
		c.passGame(game);
		c.passPlayer(player);
		hand.add(c);
		gained.add(c);
		c.gainAction();
		game.log(player.getPlayerName() + " gained " + c.getName() + ", to their hand");
		for(int i = hand.size() - 1; i >= 0; i--) {
			hand.get(i).reactGain(c);
		}
		if(player.equals(game.getCurrentPlayer())) {
			for(int i = play.size() - 1; i >= 0; i--) {
				play.get(i).respondGain(c);
			}
		}
	}
	
	/**
	 * Sends a card to the tavern mat.
	 * Removes the card from play.
	 * @param c the card.
	 */
	public void sendToTavern(Card c) {
		if(play.remove(c)) {
			tavern.add(c);
		}
		else {
			throw new RuntimeException(c.getName() + " could not be found in play space");
		}
	}
	
	/**
	 * Takes a card from the tavern mat and returns it to play.
	 * @param c the card.
	 */
	public void callFromTavern(Card c) {
		if(tavern.remove(c)) {
			play.add(c);
		}
		else {
			throw new RuntimeException(c.getName() + " could not be found on tavern mat");
		}
	}

	/**
	 * Clears hand and play space into discard.
	 * Sends any duration cards played into duration.
	 * Deals new hand of 5 cards.
	 */
	public void cleanUp() {
		discard.addAll(hand);
		hand.clear();
		for(int i = play.size() - 1; i >= 0; i--) {
			play.get(i).cleanupAction();
		}
		discard.addAll(duration);
		duration.clear();
		for(int i = play.size() - 1; i >= 0; i--) {
			if(play.get(i).isDuration()) {
				duration.add(play.remove(i));
			}
		}
		discard.addAll(play);
		play.clear();
		for(int i = 0; i < 5; i++) {
			deal();
		}
		sortHand();
	}

	/**
	 * Sorts the player's hand.
	 */
	private void sortHand() {
		try {
			Collections.sort(hand);
		}
		catch (NullPointerException e) {}
	}

	/**
	 * Determines the total value of the victory cards in the deck.
	 * Includes bonus from cards like Gardens.
	 * @return number of victory points, or score.
	 */
	public int getScore() {
		int output = 0;
		for(Card c : draw) {
			output += c.getVictory();
			output += c.gameEndAction();
		}
		for(Card c : hand) {
			output += c.getVictory();
			output += c.gameEndAction();
		}
		for(Card c : play) {
			output += c.getVictory();
			output += c.gameEndAction();
		}
		for(Card c : discard) {
			output += c.getVictory();
			output += c.gameEndAction();
		}
		for(Card c : duration) {
			output += c.getVictory();
			output += c.gameEndAction();
		}
		for(Card c : tavern) {
			output += c.getVictory();
			output += c.gameEndAction();
		}
		for(List<Card> a : reserve.values()) {
			for(Card c : a) {
				output += c.getVictory();
				output += c.gameEndAction();
			}
		}
		return output;
	}

	/**
	 * Returns a full list of all cards in the deck.
	 * @return the deck in an ArrayList.
	 */
	public ArrayList<Card> getDeck() {
		ArrayList<Card> output = new ArrayList<Card>();
		output.addAll(draw);
		output.addAll(hand);
		output.addAll(play);
		output.addAll(discard);
		output.addAll(duration);
		output.addAll(tavern);
		for(List<Card> a : reserve.values()) {
			output.addAll(a);
		}
		return output;
	}
	
	/**
	 * Determines the number of cards available to draw.
	 * @return the number of cards in the draw and discard piles.
	 */
	public int drawSize() {
		int size = 0;
		size += draw.size();
		size += discard.size();
		return size;
	}

	/**
	 * Shuffles the deck.
	 */
	private void shuffle() {
		draw.addAll(discard);
		discard.clear();
		Collections.shuffle(draw, random);
	}

	@Override
	public String toString() {
		//String out = "";
		//if(draw.size() > 0) out += "Draw: " + printCardList(draw);
		//if(discard.size() > 0) out += " Discard: " + printCardList(discard);
		//if(hand.size() > 0) out += " Hand: " + printCardList(hand);
		//if(play.size() > 0) out += " Play: " + printCardList(play);
		//if(duration.size() > 0) out += " Duration: " + printCardList(duration);
		//if(tavern.size() > 0) out += " Tavern: " + printCardList(tavern);
		//if(reserve.size() > 0) out += " Reserve: " + printCardList(reserve);
		//return out;

		return printCardList(getDeck());
	}

	/**
	 * Generates a String format list of cards in a particular ArrayList.
	 * Saves characters by listing numbers of identical cards.
	 * @param list the ArrayList representing the deck to be printed.
	 * @return the cards in the deck in a string format.
	 */
	private String printCardList(ArrayList<Card> list) {
		if(list == null || list.size() < 1) {
			return "";
		}
		TreeMap<Card, Integer> deck = new TreeMap<Card, Integer>();
		for(Card c : list) {
			if(!deck.containsKey(c)) {
				deck.put(c, 1);
			}
			else {
				deck.put(c, deck.get(c) + 1);
			}
		}
		String out = "";
		for(Card c : deck.keySet()) {
			out += deck.get(c) + " " + c.getName() + ", ";
		}
		return out.substring(0, out.length() - 2);
	}

}
