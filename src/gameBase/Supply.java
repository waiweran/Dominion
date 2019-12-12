package gameBase;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

import cards.Card;

/**
 * Supply pile for the Dominion game.
 * @author Nathaniel
 * @version 04-02-2015
 */
public class Supply implements Comparable<Supply>, Serializable {

	private static final long serialVersionUID = -5818509276218412963L;
	
	private Card card;
	private int quantity;
	private int embargo;
	private boolean gaveTradeToken;
	private transient Set<Observer> observers;

	/**
	 * Constructor for the Supply class.
	 * @param c type of card in this supply pile.
	 * @param num number of cards initially in this supply pile.
	 */
	public Supply(Card c, int num) {
		card = c;
		quantity = num;
		embargo = 0;
		gaveTradeToken = false;
		observers = new HashSet<>();
	}
	
	/**
	 * Takes one card from this supply pile.
	 * Transfers Trade Route coin token to Trade Route Mat.
	 * @return the card taken.
	 */
	public Card takeCard() {
		if(quantity > 0) {
			if(card.isVictory()) {
				gaveTradeToken = true;
			}
			quantity--;
			notifyObservers();
			return card;
		}
		return null;
	}
	
	/**
	 * Puts one card back into this supply.
	 * @param c the card to put back.
	 */
	public void putOneBack(Card c) {
		quantity++;
		notifyObservers();
	}
	
	/**
	 * Determines whether this pile is empty.
	 * @return 1 if empty, 0 if not empty.
	 * This is used to determine when 3 piles are empty.
	 * When sum of isEmptys >= 3, game should end. 
	 */
	public int isEmpty() {
		if(quantity > 0) return 0;
		else return 1;
	}
	
	/**
	 * Places an embargo on the supply.
	 */
	public void embargo() {
		embargo++;
		notifyObservers();
	}
	
	/**
	 * Accessor method for the number of cards remaining.
	 * @return number of cards in supply.
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Accessor method for the card type of the supply.
	 * @return the card contained in the supply.
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * Accessor method for the top card of the supply.
	 * @return the card top card of the supply.
	 */
	public Card getTopCard() {
		return card;
	}

	/**
	 * Accessor method for the embargo status of the supply.
	 * @return number of embargo tokens on supply.  
	 */
	public int getEmbargo() {
		return embargo;
	}
	
	/**
	 * Accessor method for the trade tokens on the supply
	 * @return whether this supply has contributed a trade token to the mat.
	 */
	public boolean gaveToken() {
		return gaveTradeToken;
	}

	@Override
	public int compareTo(Supply other) {
		if(this.getCard().getCost() == other.getCard().getCost()) {
			return this.getCard().compareTo(other.getCard());
		}
		else {
			return this.getCard().getCost() - other.getCard().getCost();
		}
	}
	
	@Override
	public String toString() {
		return (quantity + "_" + card.getName());
	}
	
	/**
	 * Adds an Observer to the observable list.
	 * @param o the Observer to add.
	 */
	public void addObserver(Observer o) {
		if(observers == null) observers = new HashSet<>();
		observers.add(o);
	}
	
	/**
	 * Clears the list of observers observing this.
	 */
	public void clearObservers() {
		observers.clear();
	}
	
	/**
	 * Passes the main game to all cards in the supply.
	 */
	public void passGameToCards(DominionGame g) {
		card.passGame(g);
	}
	
	/**
	 * Notifies all observers.
	 */
	protected void notifyObservers() {
		for(Observer o : observers) {
			o.update(null, null);
		}
	}
	
}