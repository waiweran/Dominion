package selectors;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * Provides a text dialog for users to enter stuff.
 * @author Nathaniel
 * @version 05-17-2018
 */
public class TextInput {
	
	private DominionGame game;
	private Player player;
	
	/**
	 * Sets up the dialog.
	 * @param g the game.
	 */
	public TextInput(DominionGame g) {
		game = g;
		player = game.getCurrentPlayer();
	}
	
	/**
	 * Sets the player that this dialog is displayed for.
	 * @param p The player to display for.
	 */
	public void setPlayer(Player p) {
		player = p;
	}
	
	/**
	 * Gets player to enter the name of any card.
	 * @param card A card to display in the message pop-up.
	 * @param message The message to display in the pop-up.
	 * @return the named card.
	 */
	public Card getEnteredCard(Card card, String message) {
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().enterCard(card);
		}
		Card cardIn = null;
		while(cardIn == null) {
			cardIn = game.allCards.findCard(getEnteredText(card, message));
		}
		return cardIn;
	}

	/**
	 * Gets text entered by the player given a card and message to display.
	 * @param card the card to display.
	 * @param message the message to display.
	 * @return The text entered by the player.
	 */
	private String getEnteredText(Card card, String message) {

		Platform.runLater(() -> {
			if(!game.isOnline() || player.equals(game.getGUI().getMyPlayer())) {
				if(!game.isOnline() && !player.equals(game.getCurrentPlayer())) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Decision for " + player.getPlayerName());
					alert.setTitle("Dominion");
					alert.showAndWait();
				}
				TextInputDialog textIn = new TextInputDialog();
				textIn.setContentText(message);
				textIn.setTitle(card.getName());
				textIn.setHeaderText("");
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
				textIn.setGraphic(leftPane);
				String s = textIn.showAndWait().get();
				game.getClient().sendString("SELECT " + s);
			}
		});
		String input = game.getClient().getSelection();
		return input.substring(7);
	}

}
