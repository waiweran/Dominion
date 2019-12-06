package computerPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;

/**
 * Class representing a computer controlled player.
 * @author Nathaniel
 * @version 05-04-2017
 */
public abstract class ComputerPlayer {
	
	protected Player player;
	protected DataWriter dataOut;
	protected DominionGame access;
	private String name;

	/**
	 * Initializes the computer player.
	 * @param cpuName the name of this computer player type.
	 * @param pComputer The Player this plays for.
	 * @param game the main DominionGame.
	 */
	public ComputerPlayer(String cpuName, Player pComputer, DominionGame game) {
		name = cpuName;
		player = pComputer;
		access = game;
		dataOut = new DataWriter(pComputer, game);
	}
	
	/**
	 * Runs the computer player's turn.
	 */
	public final void takeTurn() {
		if(access.getCurrentPlayer() == player) {
			playActions();
			playTreasures();
			buyCards();
		}
	}
	
	/**
	 * Chooses a card to gain.
	 * Used by SupplySelector.
	 * @param options List of supplies available to gain from.
	 * @param required Whether choice is required or optional.
	 * @return The chosen supply.
	 */
	public abstract Supply chooseGain(List<Supply> options, boolean required);
	
	/**
	 * Chooses a supply for a non-gain purpose.
	 * @param options The list of available supplies.
	 * @param required Whether choice is required or optional.
	 * @param choiceName The name of the card causing the selection.
	 * @return The selected supply.
	 */
	public abstract Supply chooseSupply(List<Supply> options, boolean required, String choiceName);
	
	/**
	 * Chooses a number of cards from a given list.
	 * Used with MultiCardSelector
	 * @param choices The list of cards to choose from.
	 * @param num The number of cards to choose.
	 * @param required Whether the number of cards selected is required or maximum.
	 * @param choiceName The name of the card making the choice.
	 * @return List of selected card indices.
	 */
	public abstract ArrayList<Integer> chooseCards(List<Card> choices, 
			int num, boolean required, String choiceName);
	
	/**
	 * Chooses a card from a given list.
	 * Used with SingleCardSelector
	 * @param choices The list of cards to choose from.
	 * @param required Whether choice is required or optional.
	 * @param choiceName The name of the card making the choice.
	 * @return Selected card index.
	 */
	public abstract int chooseCard(List<Card> choices, boolean required, String choiceName);


	/**
	 * Choose one from an array of options.
	 * Used by Selector
	 * @param options the choices.
	 * @param choiceName string describing the choice.
	 * @return the index of the option chosen.
	 */
	public abstract int choose(List<String> options, String choiceName);

	/**
	 * Names any card in the game.
	 * Used by TextInput.
	 * @param messenger The card played to elicit the choice.
	 * @return The named card.
	 */
	public abstract Card enterCard(Card messenger);
	
	/**
	 * Saves the gain and play data and targets for this computer player.
	 */
	public void saveData() {
		dataOut.saveData();
	}
	
	/**
	 * Closes the computer player at the end of a game.
	 */
	public void close() {
		// Do nothing here
	}
		
	/**
	 * Plays action cards for the player by making a series of choosePlay() calls.
	 */
	private void playActions() {
		ArrayList<Card> actions = new ArrayList<Card>();
		ArrayList<Integer> acIndex = new ArrayList<Integer>();
		while(true) {
			actions.clear();
			acIndex.clear();
			for(int i = 0; i < player.deck.hand.size(); i++) {
				if(i < player.deck.hand.size() && player.deck.hand.get(i).isAction()) {
					actions.add(player.deck.hand.get(i));
					acIndex.add(i);
				}
			}
			if(actions.isEmpty() || player.getActions() == 0) break;
			dataOut.writePlayData(actions);
			Card play = chooseAction(actions);
			dataOut.writePlayTarget(play);
			if (play == null) break;
			player.playCard(acIndex.get(actions.indexOf(play)));
		}		
	}	
	
	/**
	 * Plays treasure cards for the player by making a series of chooseTreasure() calls.
	 */
	private void playTreasures() {
		ArrayList<Card> treasures = new ArrayList<Card>();
		ArrayList<Integer> treIndex = new ArrayList<Integer>();
		while(true) {
			treasures.clear();
			treIndex.clear();
			for(int i = 0; i < player.deck.hand.size(); i++) {
				if(i < player.deck.hand.size() && player.deck.hand.get(i).isTreasure()) {
					treasures.add(player.deck.hand.get(i));
					treIndex.add(i);
				}
			}
			if(treasures.isEmpty()) break;
			// Note that play data output for treasures has been removed
			Card play = chooseTreasure(treasures);
			if (play == null) break;
			player.playCard(treIndex.get(treasures.indexOf(play)));
		}		

	}
	
	/**
	 * Buys cards as specified by the computer player by making a series of chooseBuy() calls.
	 */
	private void buyCards() {
		while(true) {	
			// List Supplies that can be bought from
			ArrayList<Supply> choices = new ArrayList<Supply>();
			for(Supply supp : access.board.getAllSupplies()) {
				if(supp.getTopCard().getCost() <= player.getTreasure() 
						&& (!supp.getTopCard().costsPotion() || player.potion > 0)
						&& supp.getTopCard().canBeGained()
						&& supp.isEmpty() == 0) {
					choices.add(supp);
				}
			}
			dataOut.writeGainData(choices);
			
			// Pick one
			Supply s = chooseBuy(choices);
			
			// No Purchase if Null
			if(s == null) {
				dataOut.writeGainTarget(null);
				access.endTurn();
				return;
			}
			
			// Buy the Card
			dataOut.writeGainTarget(s.getTopCard());
			if(player.buyCard(s)) {
				if(access.gamePhase > 2) {
					access.endTurn();
					return;
				}
			}
		}		
	}	
	
	/**
	 * Chooses a card to play from an array of actions.
	 * @param options the cards available to play.
	 * @return the chosen card.
	 */
	protected abstract Card chooseAction(List<Card> options);
	
	/**
	 * CHooses a card to play from an array of treasures.
	 * @param options the cards available to play.
	 * @return the chosen card.
	 */
	protected abstract Card chooseTreasure(List<Card> options);
	
	/**
	 * Chooses a card to buy.
	 * @param options List of supplies available to buy from.
	 * @return The chosen supply.
	 */
	protected abstract Supply chooseBuy(List<Supply> options);
	
	/**
	 * @return the name of the computer player type.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Comparator class that sorts from high cost to low cost.
	 * @author Nathaniel Brooke
	 * @version 6-19-2016
	 */
	protected class CostComparator implements Comparator<Supply> {

		@Override
		public int compare(Supply o1, Supply o2) {
			return o2.getCard().getCost() - o1.getCard().getCost();
		}

	}
	
}
