package gameBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import cards.Card;
import cards.defaults.Copper;
import cards.defaults.Estate;
import cards.extra.Champion;
import cards.extra.Hovel;
import cards.extra.Necropolis;
import cards.extra.OvergrownEstate;
import computerPlayer.ComputerPlayer;
import genericGame.AbstractPlayer;
import javafx.application.Platform;

/**
 * Represents a single player in the Dominion game.
 * @author Nathaniel
 * @version 04-02-2015
 */
public class Player extends AbstractPlayer {

	private static final long serialVersionUID = 1000L;
	
	private transient Set<Observer> observers;	//List of all observers to notify when fields change

	private DominionGame access;		//gives access to DominionGame's fields

	private int actions;				//Number of Actions a player has (observed)
	private int buys;					//Number of buys a player has (observed)
	private int treasure;				//Amount a player has to spend (observed)
	public int potion;					//Number of potion cards a played
	public int extraVictory = 0;		//Number of victory tokens a player has
	public int pirateShip = 0;			//Pirate ship mat counter
	public int coinTokens = 0;			//Guilds coin tokens counter
	public int coppersmith = 0;			//Coppersmiths played this turn
	public int bridge = 0;				//Bridges played this turn
	public int quarry = 0;				//Quarries played this turn
	public List<Card> contraband;		//Cards that cannot be bought due to Contraband
	public Deck deck;					//The player's deck

	public List<Card> bought;			//Cards the player bought this turn
	public boolean buying = false; 		//Whether the player is currently buying a card
	
	private ComputerPlayer ai;			//For computer controlled play

	/**
	 * Constructor for the Player class.  
	 * Sets up for the game.
	 * @param num sets the player number.
	 */
	public Player(DominionGame game, int num, boolean shelter) {
		super(num, game.isOnline());
		observers = new HashSet<>();
		bought = new ArrayList<>();
		contraband = new ArrayList<>();
		access = game;
		startingSetup(shelter);
	}
	
	/**
	 * Sets the computer player that this player uses.
	 * @param computer the ComputerPlayer to use.
	 */
	public void setComputerPlayer(ComputerPlayer computer) {
		ai = computer;
		super.setComputerPlayer();
		setPlayerName(ai.getName() + "_" + getPlayerNum());
	}

	/**
	 * Sets up the deck for the game, with 7 Copper and 3 Estates.
	 * @param shelter true if shelters are used instead of estates.
	 */
	private void startingSetup(boolean shelter) {
		ArrayList<Card> startDeck = new ArrayList<Card>();
		while(startDeck.size() < 7) {
			startDeck.add(new Copper());
		}
		if(shelter) {
			startDeck.add(new OvergrownEstate());
			startDeck.add(new Hovel());
			startDeck.add(new Necropolis());
		}
		else {
			while(startDeck.size() < 10) {
				startDeck.add(new Estate());
			}
		}		
		deck = new Deck(access.random, startDeck, access, this);
		deck.cleanUp();
		actions = 1;
		buys = 1;
		treasure = 0;
		potion = 0;
	}

	/**
	 * Sets up the start of the player's turn.
	 * Clears storage of purchases and runs durations.
	 */
	public void startTurn() {
		bought.clear();
		deck.gained.clear();
		for(int i = deck.duration.size() - 1; i >= 0; i--) {
			deck.duration.get(i).durationAction();
		}
		notifyObservers();
		access.log(getPlayerName() + "'s turn started");
		if(ai != null) ai.takeTurn();
	}

	/**
	 * Plays a card from the player's hand.
	 * Plays any actions associated with that card.
	 * @param index of the card to be played.
	 * @return whether the card was played.
	 */
	public void playCard(int index) {
		Card c = deck.hand.get(index);
		if(c.isAction() && actions > 0 && access.gamePhase < 2) {
			access.gamePhase = 1;
			if(!deck.play.contains(new Champion())) actions--;
			deck.playCard(index);
			c.performAction();
			notifyObservers();
		}
		else if(c.isTreasure() && access.gamePhase < 3) {
			access.gamePhase = 1;
			treasure += c.getTreasure();
			deck.playCard(index);
			c.performAction();
			notifyObservers();
		}
	}
	
	/**
	 * Plays all the current player's treasures.
	 */
	public void playTreasures() {
		for(int i = deck.hand.size() - 1; i >= 0; i--) {
				if(deck.hand.get(i).isTreasure()) {
					playCard(i);
				}
				if(i > deck.hand.size()) {
					i = deck.hand.size() - 1;
				}
		}
		access.log(getPlayerName() + " played all treasures");
	}

	/**
	 * Buys the player a copy of a card.
	 * @param s the supply with the card to be bought.
	 * @return whether the card was bought.
	 */
	public boolean buyCard(Supply s) {
		boolean wasBought;
		if(s.getQuantity() < 1 || contraband.contains(s.getTopCard())) {
			return false;
		}
		Card c = s.getTopCard();
		c.passGame(access);
		buying = true;
		bought.add(c);
		if(buys > 0 && treasure - c.getCost() >= 0 && c.canBeGained()
				&& (!c.costsPotion() || potion > 0)) {
			treasure -= c.getCost();
			if(c.costsPotion()) potion--;
			buys--;
			s.takeCard();
			deck.gain(c);
			for(int i = 0; i < s.getEmbargo(); i++) {
				buying = false;
				deck.gain(access.board.getCurse().takeCard());
				buying = true;
			}
			access.gamePhase = 2;
			wasBought = true;
			access.log(getPlayerName() + " bought " + c.getName());
		}
		else {
			wasBought = false;
			bought.remove(bought.size() - 1);
		}
		if(buys < 1) {
			access.gamePhase = 3;
		}
		buying = false;
		notifyObservers();
		return wasBought;
	} 

	/**
	 * Performs the actions required at the end of a player's turn.
	 */
	public void endTurn() {
		contraband.clear();
		actions = 1;
		buys = 1;
		if(treasure > 0) treasure = 0;
		potion = 0;
		coppersmith = 0;
		bridge = 0;
		quarry = 0;
		deck.cleanUp();
		access.log(getPlayerName() + "'s Deck: " + deck.toString());
		access.log(getPlayerName() + "'s turn ended");
		notifyObservers();
	}

	/**
	 * returns the player's current score.
	 * includes value of victory cards in deck
	 * and any other victory points the player has.
	 * @return Player's score.
	 */
	public int getScore() {
		return deck.getScore() + extraVictory;
	}
	
	/**
	 * @return the computer player controlling this player, if they exist.
	 */
	public ComputerPlayer getComputerPlayer() {
		return ai;
	}
	
	/**
	 * @return the number of actions this player currently has.
	 */
	public int getActions() {
		return actions;
	}
	
	/**
	 * Adds actions to this player's counter
	 * @param num the number of actions to add
	 */
	public void addAction(int num) {
		actions += num;
		notifyObservers();
	}
	
	/**
	 * @return the number of buys this player has.
	 */
	public int getBuys() {
		return buys;
	}
	
	/**
	 * Gives the player an extra buy.
	 */
	public void addBuy() {
		buys++;
		notifyObservers();
	}
	
	/**
	 * @return The amount of money this player has to spend.
	 */
	public int getTreasure() {
		return treasure;
	}
	
	/**
	 * Adds the specified amount of money to this player's counter.
	 * @param amt the amount to add.
	 */
	public void addTreasure(int amt) {
		treasure += amt;
		notifyObservers();
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
	 * Notifies all observers.
	 */
	private void notifyObservers() {
		Platform.runLater(() -> {
			for(Observer o : observers) {
				o.update(null, null);
			}
		});
	}

}
