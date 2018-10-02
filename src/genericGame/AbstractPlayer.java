package genericGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Represents a player for any board game.
 * @author Nathaniel
 * @version 08-11-2015
 */
public abstract class AbstractPlayer implements Serializable {

	private static final long serialVersionUID = 1111L;

	private int playerNumber;		//player 1, player 2, etc.
	private String playerName; 		//name of the player
	private int playerScore;		//Player's overall score
	private boolean onlineGame;		//If game is online
	private boolean isNPC;			//If this player is AI

	/**
	 * Creates a new abstract player object.
	 * Sets the player's number and default name.
	 * @param num the player number.
	 */
	public AbstractPlayer(int num, boolean online) {
		playerNumber = num;
		playerName = "Player " + num;
		playerScore = 0;
		onlineGame = online;
		isNPC = false;
		try {
			if(online) {
				Scanner in = new Scanner(new File("PlayerInfo.txt"));
				playerName = in.nextLine();
				playerScore = in.nextInt();
				in.close();
			}
		} catch(Exception e) {}
	}

	/**
	 * Accessor method for the player's number.
	 * @return the player's number.
	 */
	public int getPlayerNum() {
		return playerNumber;
	}

	/**
	 * Accessor method for the player's name.
	 * @return the player's name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Accessor method for the player's game score.
	 * @return the player's score.
	 */
	public int getPlayerScore() {
		return playerScore;
	}
	
	/**
	 * Sets this player to be a NPC.
	 */
	public void setComputerPlayer() {
		isNPC = true;
	}
	
	/**
	 * Accessor method for whether the player is an AI
	 * @return the player's AI status
	 */
	public boolean isComputerPlayer() {
		return isNPC;
	}

	/**
	 * Adds a new game score to the player's overall score.
	 * @param score the score to be added.
	 */
	public void addPlayerWin() {
		playerScore++;
		try {
			if(onlineGame) {
				PrintWriter pw = new PrintWriter(new FileWriter(new File("PlayerInfo.txt")));
				pw.println(playerName);
				pw.println(playerScore);
				pw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifier method for the player's name.
	 * @param name the player's new name.
	 */
	public void setPlayerName(String name) {
		playerName = name;
	}
	
	/**
	 * Saves the player's current name to file.
	 */
	public void savePlayerName() {
		try {
			if(onlineGame) {
				PrintWriter pw = new PrintWriter(new FileWriter(new File("PlayerInfo.txt")));
				pw.println(playerName);
				pw.println(playerScore);
				pw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object other) {
		try {
			return this.getPlayerNum() == ((AbstractPlayer)other).getPlayerNum();
		} catch(Exception e) {} 
		return false;
	}

	@Override
	public int hashCode() {
		return playerNumber;
	}

}
