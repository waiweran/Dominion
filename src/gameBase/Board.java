package gameBase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cards.Card;
import cards.adventures.Port;
import cards.darkAges.Knight;
import cards.darkAges.Rats;
import cards.defaults.Colony;
import cards.defaults.Copper;
import cards.defaults.Curse;
import cards.defaults.Duchy;
import cards.defaults.Estate;
import cards.defaults.Gold;
import cards.defaults.Platinum;
import cards.defaults.Province;
import cards.defaults.Silver;
import cards.defaults.Trash;
import cards.extra.BagOfGold;
import cards.extra.Diadem;
import cards.extra.Followers;
import cards.extra.Princess;
import cards.extra.TrustySteed;
import genericGame.ObservableList;

/**
 * Board class for the Dominion game
 * Builds the game board.
 * @author Nathaniel
 * @version 04-02-2015
 */
public class Board implements Serializable {

	private static final long serialVersionUID = -3776267726819210544L;

	public ObservableList<Card> trash;		//the trash pile
	public ArrayList<Supply> kingdomCards;	//kingdom card supplies
	public ArrayList<Supply> defaultCards;	//default card supplies
	public ArrayList<Supply> extraCards;	//cards required by a kingdom card
	public ArrayList<Card> prizes;			//stores the prizes
	private boolean prosperity;				//if colony and platinum is used

	/**
	 * Constructor for the Board class
	 * build the game board
	 * @param inputCards what kingdom cards to include
	 * @param p whether to use Prosperity or not
	 * @param players how many players there are
	 */
	public Board(List<Card> inputCards, Random random, boolean p, int players) {
		trash  = new ObservableList<>();
		trash.add(new Trash());
		prosperity = p;
		kingdomCards = new ArrayList<>(10);
		extraCards = new ArrayList<>();
		defaultCards = new ArrayList<>(9);
		for(Card c : inputCards) {
			if(c instanceof Knight) {
				kingdomCards.add(new ShuffledSupply(random, c, Knight.KNIGHTS, 10));
			}
			else if(c.isVictory()) {
				kingdomCards.add(new Supply(c, players*2+4));
			}
			else if(c instanceof Rats) {
				kingdomCards.add(new Supply(c, 20));
			}
			else if(c instanceof Port) {
				kingdomCards.add(new Supply(c, 12));
			}
			else {
				kingdomCards.add(new Supply(c, 10));
			}
			ArrayList<Supply> setupRequirement = c.setupRequirement();
			if (setupRequirement != null) {
				for(Supply s : setupRequirement) {
					if(findSupply(s.getCard()) == null) {
						extraCards.add(s);
					}
				}
			}
		}
		Collections.sort(kingdomCards);
		defaultCards.add(new Supply(new Copper(), 60 - 7*players));
		defaultCards.add(new Supply(new Silver(), 40));
		defaultCards.add(new Supply(new Gold(), 30));
		defaultCards.add(new Supply(new Estate(), players*2+4));
		defaultCards.add(new Supply(new Duchy(), players*2+4));
		defaultCards.add(new Supply(new Province(), players*2+4));
		defaultCards.add(new Supply(new Curse(), players*10));
		if(prosperity) {
			defaultCards.add(3, new Supply(new Platinum(), 12));
			defaultCards.add(7, new Supply(new Colony(), players*2+4));
		}
		prizes = new ArrayList<Card>();
		prizes.add(new BagOfGold());
		prizes.add(new Diadem());
		prizes.add(new Followers());
		prizes.add(new Princess());
		prizes.add(new TrustySteed());
	}
	
	/**
	 * Gets a list of all supplies in the game.
	 * @return List containing all supplies.
	 */
	public List<Supply> getAllSupplies() {
		ArrayList<Supply> output = new ArrayList<Supply>();
		output.addAll(defaultCards);
		output.addAll(extraCards);
		output.addAll(kingdomCards);
		return output;
	}

	/**
	 * Performs the intended trashing action of a card, and adds it to the trash.
	 * Make sure that the card is removed in the call of trashCard.
	 * @param c the card being sent to the trash.
	 */
	public void trashCard(Card c) {
		trash.add(c);
		if(c != null) c.trashAction();
	}

	/**
	 * Finds a supply in the board based on the card in the supply.
	 * @param c the card that the supply holds.
	 * @return the supply holding that card.
	 */
	public Supply findSupply(Card c) {
		for(Supply s : kingdomCards) {
			if(s.getCard().getClass().isInstance(c)) {
				return s;
			}
		}
		if(extraCards != null) for(Supply s : extraCards) {
			if(s.getCard().getClass().isInstance(c)) {
				return s;
			}
		}
		for(Supply s : defaultCards) {
			if(s.getCard().getClass().isInstance(c)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Determines if enough piles are empty to end the game.
	 * @return whether or not the game has ended.
	 */
	public boolean isGameOver() {
		int numEmpty = 0;
		for(Supply s : kingdomCards) {
			numEmpty += s.isEmpty();
		}
		for(Supply s : extraCards) {
			if(s.getCard().canBeGained()) {
				numEmpty += s.isEmpty();
			}
		}
		for(Supply s : defaultCards) {
			numEmpty += s.isEmpty();
			if(prosperity && s.getCard() instanceof Colony
					|| !prosperity && s.getCard() instanceof Province) {
				numEmpty += s.isEmpty() * 3;
			}
		}
		return numEmpty >= 3;
	}

	/**
	 * Accessor method for the trade route mat.
	 * Counts supplies that have given a coin token to the trade route mat.
	 * @return the number of coin tokens on the trade route mat.
	 */
	public int getTradeRouteMat() {
		int numTokens = 0;
		for(Supply s : kingdomCards) {
			if(s.gaveToken()) {
				numTokens++;
			}
		}
		if(extraCards != null) for(Supply s : extraCards) {
			if(s.gaveToken()) {
				numTokens++;
			}
		}
		for(Supply s : defaultCards) {
			if(s.gaveToken()) {
				numTokens++;
			}
		}
		return numTokens;
	}
	
	/**
	 * @return the Copper supply pile
	 */
	public Supply getCopper() {
		return defaultCards.get(0);
	}
	
	/**
	 * @return the Silver supply pile
	 */
	public Supply getSilver() {
		return defaultCards.get(1);
	}
	
	/**
	 * @return the Gold supply pile
	 */
	public Supply getGold() {
		return defaultCards.get(2);
	}
	
	/**
	 * @return the Estate supply pile
	 */
	public Supply getEstate() {
		return defaultCards.get(prosperity? 4 : 3);
	}
	
	/**
	 * @return the Duchy supply pile
	 */
	public Supply getDuchy() {
		return defaultCards.get(prosperity? 5 : 4);
	}
	
	/**
	 * @return the Province supply pile
	 */
	public Supply getProvince() {
		return defaultCards.get(prosperity? 6 : 5);
	}
	
	/**
	 * @return the Curse supply pile
	 */
	public Supply getCurse() {
		return defaultCards.get(prosperity? 8 : 6);
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("{");
		output.append("D:[");
		boolean hasPrev = false;
		for(Supply s : defaultCards) {
			if(hasPrev) output.append(", ");
			output.append(s.toString());
			hasPrev = true;
		}
		hasPrev = false;
		output.append("], E:[");
		for(Supply s : extraCards) {
			if(hasPrev) output.append(", ");
			output.append(s.toString());
			hasPrev = true;
		}
		hasPrev = false;
		output.append("], K:[");
		for(Supply s : kingdomCards) {
			if(hasPrev) output.append(", ");
			output.append(s.toString());
			hasPrev = true;
		}
		output.append("]");
		output.append("}");
		return output.toString();
	}
}