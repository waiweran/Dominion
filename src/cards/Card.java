package cards;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Card class for the Dominion game.
 * Used to make all the cards in the game.
 * @author Nathaniel Brooke
 * @version 04-02-2015
 */
public abstract class Card implements Comparable<Card>, Cloneable, Serializable {

	private static final long serialVersionUID = 3000L;

	private DominionGame access;
	private Player player;

	private String name;
	private String type;
	private String game;
	private int victory;
	private int treasure;
	private int cost;
	private boolean potion;
	private boolean isAction;
	private boolean isAttack;
	private boolean isDuration;
	private boolean isReaction;
	private boolean isVictory;
	private boolean isTreasure;
	private boolean isTraveller;
	private boolean isReserve;

	/**
	 * Constructor for the Card class.
	 * Used to make most cards.
	 * @param n name of the card
	 * @param t type of card
	 * @param g game the card is from
	 * @param c cost of the card
	 */
	protected Card(String n, String t, String g, int c) {
		name = n;
		type = t;
		game = g;
		cost = c;
		victory = 0;
		treasure = 0;
		isAction = t.contains("Action");
		isAttack = t.contains("Attack");
		isDuration = t.contains("Duration");
		isReaction = t.contains("Reaction");
		isVictory = t.contains("Victory");
		isTreasure = t.contains("Treasure");
		isTraveller = t.contains("Traveller");
		isReserve = t.contains("Reserve");
	}

	/**
	 * Overloaded constructor for the Card class.
	 * Used to construct victory or treasure cards.
	 * @param n name of the card
	 * @param ty type of card
	 * @param g game the card is from
	 * @param c cost of the card
	 * @param v victory value of the card
	 * @param tr treasure value of the card
	 */
	protected Card(String n, String ty, String g, int c, int v, int tr) {
		name = n;
		type = ty;
		game = g;
		cost = c;
		victory = v;
		treasure = tr;
		isAction = ty.contains("Action");
		isAttack = ty.contains("Attack");
		isDuration = ty.contains("Duration");
		isReaction = ty.contains("Reaction");
		isVictory = ty.contains("Victory");
		isTreasure = ty.contains("Treasure");
		isTraveller = ty.contains("Traveller");
		isReserve = ty.contains("Reserve");
	}
	
	/**
	 * Sets the card to cost a potion.
	 */
	protected void setPotionCost() {
		potion = true;
	}

	/**
	 * Passes the DominionGame object to the card.
	 * Used so that actions can modify variables in DominionGame, Player, and Deck.
	 * @param g the current game
	 */
	public void passGame(DominionGame g) {
		access = g;
	}

	/**
	 * Passes the DominionGame object to the card.
	 * Used so that actions can modify variables in DominionGame, Player, and Deck.
	 * @param player the Card's player
	 */
	public void passPlayer(Player myPlayer) {
		player = myPlayer;
	}

	//Accessor methods for the Card class.
	public String getName() {return name;}
	public String getType() {return type;}
	public int getVictory() {return victory;}
	public int getTreasure() {return treasure;}
	public boolean isAction() {return isAction;}
	public boolean isAttack() {return isAttack;}
	public boolean isDuration() {return isDuration;}
	public boolean isReaction() {return isReaction;}
	public boolean isVictory() {return isVictory;}
	public boolean isTreasure() {return isTreasure;}
	public boolean isTraveller() {return isTraveller;}
	public boolean isReserve() {return isReserve;}
	
	/**
	 * Calculates the cost of the card.
	 * @return the cost of the card.
	 */
	public int getCost() {
		DominionGame gm = getGame();
		try {
			int calcCost = cost - gm.getCurrentPlayer().bridge;
			if(isAction) {
				calcCost -= getGame().getCurrentPlayer().quarry * 2;
			}
			if(calcCost > 0) {
				return calcCost;
			}
			else {
				return 0;
			}
		} catch(IndexOutOfBoundsException e) {
			// If the player list is not initialized
			return cost;
		} catch(NullPointerException e) {
			// Not sure what causes this error, but waiting makes it go away
			// Must be caused by some satanic concurrency issue
			if(gm != null) {
				try {
					Thread.sleep(50);
					return getCost();
				} catch (InterruptedException e1) {
					e.printStackTrace();
				}
			}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Determines whether a card costs a potion.
	 * @return true if card costs potion, false otherwise.
	 */
	public boolean costsPotion() {
		return potion;
	}
	
	/**
	 * Gets the player that owns this card.
	 * @return the player.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the main game.
	 * @return the game.
	 */
	protected DominionGame getGame() {
		return access;
	}

	/**
	 * Accessor for the image associated with this card.
	 * @param fancy whether or not the special edition base cards are used.
	 * @return the File of the image of this card.
	 */
	public File getImage() {
		String filename = name.toLowerCase();
		String temp;
		int i = 0;
		while(filename.contains(" ")) {
			temp = filename.substring(0, filename.indexOf(" "));
			i = filename.indexOf(" ") + 1;
			if(i < filename.length()) temp += filename.substring(i);
			filename = temp;
		}
		i = 0;
		while(filename.contains("'")) {
			temp = filename.substring(0, filename.indexOf("'"));
			i = filename.indexOf("'") + 1;
			if(i < filename.length()) temp += filename.substring(i);
			filename = temp;
		}
		i = 0;
		while(filename.contains("-")) {
			temp = filename.substring(0, filename.indexOf("-"));
			i = filename.indexOf("-") + 1;
			if(i < filename.length()) temp += filename.substring(i);
			filename = temp;
		}
		if(access.setup.useFancyBase() && ("Default Cards".equals(game) || "potion".equals(filename))) {
			filename += " (1)";
		}
		return new File("Images/Cards/" + game + "/" + filename + ".jpg");
	}

	@Override
	public int compareTo(Card other) {
		if(other == null) {
			return 1;
		}
		int comp1 = this.getType().compareTo(other.getType());
		if(comp1 != 0) return comp1;
		int comp2 = this.getName().compareTo(other.getName());
		if(comp2 == 0) return 0;
		if(this.getName().equals("Bank")) {
			return -1;
		}
		else if(other.getName().equals("Bank")) {
			return 1;
		}
		else if(this.getName().equals("Contraband")) {
			return 1;
		}
		else if(other.getName().equals("Contraband")) {
			return -1;
		}
		return comp2;
	}

	@Override 
	public boolean equals(Object other) {
		if(other instanceof Card) {
			return this.getName().equals(((Card)other).getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public Card clone() {
		try {
			return (Card) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}

	/**
	 * returns any supplies necessary for this card's use.
	 * Default behavior: return null.
	 * @return an ArrayList of the required Supply objects.
	 */
	public ArrayList<Supply> setupRequirement() {
		return null;
	}

	/**
	 * Performs the action specific to the card.
	 * Default behavior: do nothing.
	 */
	public void performAction() {}

	/**
	 * Determines whether the card can be gained.
	 * without performing the card's gaining action.
	 * Default behavor: return true.
	 * @return whether or not the card can be gained.
	 */
	public boolean canBeGained() {
		return true;
	}
	
	/**
	 * Performs any action required when a card is gained.
	 * Also determines whether the card can be gained at this time.
	 * Default behavior: do nothing.
	 * @return whether or not the card can be gained at this time.
	 */
	public void gainAction() {}

	/**
	 * Performs any addition of victory points at the end of the game.
	 * Default behavior: return 0.
	 * @return number of victory points added.
	 */
	public int gameEndAction() {
		return 0;
	}

	/**
	 * Performs reaction of card in hand while player attacked.
	 * Default behavior: do nothing.
	 */
	public void reactAttack() {}
	
	/**
	 * Performs reaction of card in hand when player gains.
	 * Default behavior: do nothing.
	 * @param c the card that was gained
	 */
	public void reactGain(Card c) {}
	
	/**
	 * Performs response of played card to a gain.
	 * Default behavior: do nothing.
	 * @param c the card that was gained
	 */
	public void respondGain(Card c) {}
	
	/**
	 * Performs action of this card when called from tavern mat.
	 * Default behavior: do nothing.
	 */
	public void tavernAction() {}

	/**
	 * Performs an action specific to the card when it is trashed.
	 * Default behavior: do nothing.
	 */
	public void trashAction() {}

	/**
	 * Performs the card's action at the start of the player's next turn;
	 * Default behavior: do nothing.
	 */
	public void durationAction() {}

	/**
	 * Performs an action specific to the card when it is discarded from play in cleanup.
	 * Default behavior: do nothing.
	 */
	public void cleanupAction() {}
	
	/**
	 * Performs an action specific to the card when it is discarded from play outside of cleanup.
	 * Default behavior: do nothing.
	 */
	public void discardAction() {}

}
