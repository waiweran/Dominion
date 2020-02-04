package selectors;


import java.util.Arrays;
import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 * Displays a popup with a list of text options to choose from.
 * @author Nathaniel
 * @version 05-17-2018
 */
public class Selector {

	private boolean displaying;
	private DominionGame game;
	private Player player;

	/**
	 * Sets up the Option Pane.
	 * @param g the game.
	 */
	public Selector(DominionGame g) {
		displaying = false;
		game = g;
		player = game.getCurrentPlayer();
	}

	/**
	 * Sets the player this is displayed for.
	 * @param p the player to display to.
	 */
	public void setPlayer(Player p) {
		player = p;
	}

	/**
	 * Prompts the specified player to make a selection.
	 * @param message the message to display
	 * @param title the title of the window
	 * @param options the text of the buttons
	 * @return index of the button clicked
	 */
	public int showOptionDialog(Node message, String title, String... options) {

		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().choose(Arrays.asList(options), title);
		}
		Platform.runLater(() -> {
			if(!game.isOnline() || player.equals(game.getGUI().getMyPlayer())) {
				if(!game.isOnline() && !player.equals(game.getCurrentPlayer())) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Decision for " + player.getPlayerName());
					alert.setTitle("Dominion");
					alert.showAndWait();
				}
				HBox buttons = new HBox(10);
				for(int i = 0; i < options.length; i++) {
					Button btn = new Button(options[i]);
					int finali = i;
					btn.setOnAction(e -> game.getClient().sendString("SELECT " + finali));
					buttons.getChildren().add(btn);
				}
				buttons.setAlignment(Pos.CENTER_RIGHT);
				VBox root = new VBox(5);
				root.getChildren().addAll(message, buttons);
				root.setAlignment(Pos.CENTER_RIGHT);
				root.setPadding(new Insets(5, 5, 5, 5));
				game.getGUI().addOverlay(root, title);
				displaying = true;
			}
		});
		String input = game.getClient().getSelection();
		Platform.runLater(() -> {
			if(displaying) game.getGUI().removeOverlay();
		});
		return Integer.parseInt(input.substring(7));
	}

	/**
	 * Prompts the specified player to make a selection.
	 * @param card the card to display on the left
	 * @param message the message to display
	 * @param options the text of the buttons
	 * @return index of the button clicked
	 */
	public int showOptionDialog(Card card, Node message, String... options) {

		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().choose(Arrays.asList(options), card.getName());
		}
		Platform.runLater(() -> {
			if(!game.isOnline() || player.equals(game.getGUI().getMyPlayer())) {
				if(!game.isOnline() && !player.equals(game.getCurrentPlayer())) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Decision for " + player.getPlayerName());
					alert.setTitle("Dominion");
					alert.showAndWait();
				}

				HBox mainView = new HBox();
				VBox leftPane = new VBox();
				ImageView cardView = new ImageView(card.getImage().toURI().toString());
				cardView.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/8.5 - 25);
				cardView.setFitHeight(cardView.getFitWidth()/296.0*473.0);
				Button cardBtn = new Button();
				cardBtn.setGraphic(cardView);
				cardBtn.setBackground(new Background(new BackgroundFill(
						Color.BLACK, new CornerRadii(5), new Insets(0, 3, 0, 3))));
				leftPane.setPadding(new Insets(10, 10, 10, 10));
				leftPane.getChildren().add(cardBtn);
				mainView.getChildren().addAll(leftPane, message);
				HBox buttons = new HBox(10);
				for(int i = 0; i < options.length; i++) {
					Button btn = new Button(options[i]);
					int finali = i;
					btn.setOnAction(e -> game.getClient().sendString("SELECT " + finali));
					buttons.getChildren().add(btn);
				}
				buttons.setAlignment(Pos.CENTER_RIGHT);
				VBox root = new VBox(5);
				root.getChildren().addAll(mainView, buttons);
				root.setAlignment(Pos.CENTER_RIGHT);
				root.setPadding(new Insets(5, 5, 5, 5));
				game.getGUI().addOverlay(root,  card.getName());
				displaying = true;
			}
		});
		String input = game.getClient().getSelection();
		Platform.runLater(() -> {
			if(displaying) game.getGUI().removeOverlay();
		});
		return Integer.parseInt(input.substring(7));
	}



	/**
	 * Prompts a player to see if they want to use a reaction.
	 * @param player the player for which the message is intended
	 * @param card the card which can be used to react
	 * @return true if the player wants to react, false otherwise
	 */
	public boolean checkReact(Player player, Card card) {
		setPlayer(player);
		return 1 == showOptionDialog(card, new HBox(), "Do not React", "React");
	}

	/**
	 * Prompts a player to see if they want to exchange their card.
	 * @param old card to lose when exchanging
	 * @param exchange card to get when exchanging
	 * @param message informational text to display at the top of the pop-up
	 * @return true if the player wants to exchange, false otherwise.
	 */
	public boolean checkExchange(Card old, Card exchange, String message) {
		VBox mainView;
		if(player.isComputerPlayer()) {
			mainView = null; 
		}
		else {
			mainView = new VBox(5);
			mainView.setAlignment(Pos.CENTER);
			HBox cardSwap = new HBox(10);
			cardSwap.setAlignment(Pos.CENTER);
			Text arrow = new Text("-->");
			arrow.setFont(Font.font(20));
			arrow.setFill(Color.WHITE);
			old.passGame(game);
			exchange.passGame(game);
			ImageView oldView = new ImageView(old.getImage().toURI().toString());
			oldView.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/5 - 25);
			oldView.setFitHeight(oldView.getFitWidth()/296.0*473.0);
			ImageView exchView = new ImageView(exchange.getImage().toURI().toString());
			exchView.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/5 - 25);
			exchView.setFitHeight(exchView.getFitWidth()/296.0*473.0);
			cardSwap.getChildren().addAll(oldView,
					arrow, exchView);
			Text msg = new Text(message);
			msg.setFont(Font.font(18));
			msg.setFill(Color.WHITE);
			mainView.getChildren().addAll(msg, cardSwap);
		}

		return 1 == showOptionDialog(mainView, 
				old.getName(), "Do Not Exchange", "Exchange");
	}

	/**
	 * Shows an error dialog from the tavern mat.
	 * @param game the DominionGame.
	 * @param message the message to be displayed.
	 */
	public void showTavernError(String message) {
		
		if(player.isComputerPlayer()) {
			return;
		}
		
		Text msg = new Text(message);
		msg.setFont(Font.font(18));
		msg.setFill(Color.WHITE);
		showOptionDialog(msg, "Tavern", "Ok");
	}

	/**
	 * Display a dialog asking the player a question.
	 * @param card The card to display on the left.
	 * @param message The text of the question.
	 * @param options Options for answering the question.
	 * @return The index of the selected answer.
	 */
	public int showQuestionDialog(Card card, String message, String... options) {
		
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().choose(Arrays.asList(options), card.getName());
		}
		
		Text msg = new Text(message);
		msg.setFont(Font.font(18));
		msg.setFill(Color.WHITE);
		return showOptionDialog(card, msg, options);
	}

	/**
	 * Displays a question about a card for the player.
	 * @param card The card to display on the left.
	 * @param message The text of the question.
	 * @param display The card to display with the question.
	 * @param options The list of potential answers to the question.
	 * @return the index of the option the user chose.
	 */
	public int showCardDialog(Card card, String message, Card display, String... options) {
		
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().chooseForCard(display, 
					Arrays.asList(options), card.getName());
		}
		
		ImageView cardView = new ImageView(display.getImage().toURI().toString());
		cardView.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/9.5 - 25);
		cardView.setFitHeight(cardView.getFitWidth()/296.0*473.0);
		Button cardBtn = new Button();
		cardBtn.setGraphic(cardView);
		cardBtn.setBackground(new Background(new BackgroundFill(
				Color.BLACK, new CornerRadii(5), new Insets(0, 3, 0, 3))));
		VBox mainView = new VBox(10);
		Text msg = new Text(message);
		msg.setFont(Font.font(18));
		msg.setFill(Color.WHITE);
		if(!message.isEmpty()) mainView.getChildren().add(msg);
		mainView.getChildren().add(cardBtn);	
		return showOptionDialog(card, mainView, options);
	}

	/**
	 * Shows a dialog asking the user what to do with a list of cards.
	 * @param card The card to display on the left.
	 * @param message The text asking the user for a response.
	 * @param cards The list of cards to display with the message.
	 * @param options Options for responding to the dialog.
	 * @return The index of the selected response in options.
	 */
	public int showCardListDialog(Card card, String message, List<Card> cards, String... options) {
		
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().chooseForCards(cards, 
					Arrays.asList(options), card.getName());
		}
		
		Text msg = new Text(message);
		msg.setFont(Font.font(18));
		msg.setFill(Color.WHITE);
		HBox cardsPane = new HBox(10);
		for(Card c : cards) {
			cardsPane.getChildren().add(game.getGUI().getImage(c));
		}
		VBox display = new VBox(5);
		display.setAlignment(Pos.CENTER);
		display.getChildren().addAll(msg, cardsPane);
		return showOptionDialog(card, display, options);
	}

}