package gameBase;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import cards.Card;
import cards.cornucopia.YoungWitch;

/**
 * Stores the card setup of a game.
 * @author Nathaniel Brooke
 * @version 02-27-2017
 */
public class GameSetup implements Serializable {
	
	private static final long serialVersionUID = 7665847874573372720L;
	
	private String name;
	private ArrayList<Card> myCards;
	private Card bane;
	private boolean prosp, bal, shel, fancy;

	public GameSetup(String gameName, ArrayList<Card> cards, boolean balanced, 
			boolean prosperity, boolean fancyBase, boolean shelter) {
		name = gameName;
		myCards = cards;
		prosp = prosperity;
		bal = balanced;
		shel = shelter;
		fancy = fancyBase;
		if(myCards.size() != 10) {
			throw new RuntimeException("Incorrect number of cards specified for game: " 
					+ myCards.size());
		}
	}
	
	public GameSetup(File setupFile) throws IOException {
		myCards = new ArrayList<Card>();
		load(setupFile);
		if(myCards.size() != 10) {
			throw new RuntimeException("Incorrect number of cards specified for game: " 
					+ myCards.size());
		}
	}
	
	public void setBane(Card c) {
		bane = c;
	}
	
	public boolean hasBane() {
		return bane != null;
	}
	
	public Card getBane() {
		return bane;
	}
	
	public ArrayList<Card> getCards() {
		return myCards;
	}
	
	public boolean useProsperity() {
		return prosp;
	}
	
	public boolean balanceCards() {
		return bal;
	}
	
	public boolean useShelters() {
		return shel;
	}
	
	public boolean useFancyBase() {
		return fancy;
	}
	
	public String getGameName() {
		return name;
	}
	
	public void setGameName(String newName) {
		name = newName;
	}
	
	/**
	 * Saves the game's setup in Saves.
	 */
	public void save(String name) {
		try {
			PrintStream ps = new PrintStream(new File("Saves/" + name + ".dog"));
			for(Card c : myCards) {
				ps.println(c.getName());
			}
			ps.print((bal)? "t " : "f ");
			ps.print((prosp)? "t " : "f ");
			ps.print((fancy)? "t " : "f ");
			ps.print((shel)? "t" : "f");
			ps.println();
			ps.print("Bane: " + bane.getName());
			ps.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void load(File file) throws IOException {
		Scanner setup = new Scanner(file);
		CardFactory cardMaker = new CardFactory();
		while(myCards.size() < 10) {
			String cardName = setup.nextLine();
			Card c = cardMaker.findCard(cardName);
			if(c == null) {
				setup.close();
				throw new IOException("Card " + cardName + " in file not found");
			}
			myCards.add(c);
		}
		bal = (setup.next().equals("t"));
		prosp = (setup.next().equals("t"));
		fancy = (setup.next().equals("t"));
		shel = (setup.next().equals("t"));
		if(myCards.contains(new YoungWitch())) {
			String baneName = "";
			while(true) {
				baneName = setup.nextLine();
				if(baneName.startsWith("Bane: ")) break;
			}
			bane = cardMaker.findCard(baneName.substring(6));
		}
		setup.close();
		name = file.getName().replace(".dog", "");
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof GameSetup) {
			GameSetup o = (GameSetup)other;
			return myCards.equals(o.myCards) && bane.equals(o.bane) 
					&& prosp == o.prosp && bal == o.bal && shel == o.shel;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		HashSet<Card> set = new HashSet<>();
		set.addAll(myCards);
		return set.hashCode()*31 + (bane==null?0:bane.hashCode()*17) + (prosp? 13:0) + (bal?7:0) + (shel?5:0);
	}


}
