package selectors;

import java.util.ArrayList;
import java.util.function.Predicate;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import gameBase.Supply;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Runs the user selection of supplies in gameplay.
 * @author Nathaniel Brooke
 * @version 04-03-2015
 */
public class SupplySelector {

	private String message, title;
	private int maxCost, minCost;
	private boolean hasPotion, required;
	private Predicate<Card> typeSpecifier;

	private DominionGame game;
	private Player player;
	
	/**
	 * Constructor for the SupplySelection class.
	 * @param g the game.
	 * @param message Message to display.
	 * @param maxC the maximum cost
	 * @param minC the minimum cost
	 */
	public SupplySelector(DominionGame g, String title, String message, int minC, int maxC)	{
		maxCost = maxC;
		minCost = minC;
		required = true;
		hasPotion = false;
		typeSpecifier = e -> true;
		this.message = message;
		this.title = title;
		game = g;
		player = game.getCurrentPlayer();
	}
	
	/**
	 * Sets the player who is selecting
	 * @param p the player
	 */
	public void setPlayer(Player p) {
		player = p;
	}
	
	/**
	 * Sets whether the player must select a card.
	 * @param isMust
	 */
	public void setMust(boolean isMust) {
		required = isMust;
	}
	
	/**
	 * Adds a predicate that determines whether a card can be selected.
	 * @param selector the predicate.
	 */
	public void setCardSelector(Predicate<Card> selector) {
		typeSpecifier = selector;
	}
	
	/**
	 * Sets whether cards that cost a potion can be selected.
	 * @param potion true if they can, false if they can't.
	 */
	public void setPotion(boolean potion) {
		hasPotion = potion;
	}

	/**
	 * Sets up the game GUI for this selection.
	 */
	private void setupUI() {
		if(!game.isOnline() && !player.equals(game.getCurrentPlayer())) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Decision for " + player.getPlayerName());
			alert.setTitle("Dominion");
			alert.showAndWait();
		}
		game.getGUI().supplySelect(title + ": " + message,
				typeSpecifier, minCost, maxCost, hasPotion, required);
	}	

	/**
	 * Determines the card that was selected from the Supply.
	 * @return the card that was selected, taking it from the Supply.
	 */
	public Card getGainedCard() {
		ArrayList<Supply> choices = new ArrayList<Supply>();
		for(Supply s : game.board.getAllSupplies()) {
			if(s.getQuantity() > 0 
					&& typeSpecifier.test(s.getTopCard())
					&& s.getTopCard().getCost() <= maxCost
					&& s.getTopCard().getCost() >= minCost
					&& (!s.getTopCard().costsPotion() || hasPotion)
					&& s.getTopCard().canBeGained()) {
				choices.add(s);
			}
		}
		if(choices.isEmpty()) return null;
		
		if(player.isComputerPlayer()) {
			Supply s = player.getComputerPlayer().chooseGain(choices, required);
			if(s == null) return null;
			return s.takeCard();
		}
		
		if(!game.isOnline() || player.equals(game.getGUI().getMyPlayer())) {
			Platform.runLater(() -> setupUI());
		}
		String input = game.getClient().getSelection();
		Platform.runLater(() -> game.getGUI().resetSupplies());
		if(input.equals("SELECT CANCEL")) return null;
		Card c = game.board.findSupply(game.allCards.findCard(input.substring(7))).takeCard();
		return c;
	}

	/**
	 * Determines the card that was selected from the Supply.
	 * @return the card that was selected, without taking it from the Supply.
	 */
	public Supply getSelectedSupply() {
		ArrayList<Supply> choices = new ArrayList<Supply>();
		for(Supply s : game.board.getAllSupplies()) {
			if(s.getQuantity() > 0 
					&& typeSpecifier.test(s.getTopCard())
					&& s.getTopCard().getCost() <= maxCost
					&& s.getTopCard().getCost() >= minCost
					&& (!s.getTopCard().costsPotion() || hasPotion)
					&& s.getTopCard().canBeGained()) {
				choices.add(s);
			}
		}
		if(choices.isEmpty()) return null;
		
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().chooseSupply(choices, required, title);
		}

		if(!game.isOnline() || player.equals(game.getGUI().getMyPlayer())) {
			Platform.runLater(() -> setupUI());
		}
		String input = game.getClient().getSelection();
		Platform.runLater(() -> game.getGUI().resetSupplies());
		if(input.equals("SELECT CANCEL")) return null;
		return game.board.findSupply(game.allCards.findCard(input.substring(7)));
	}
	
}