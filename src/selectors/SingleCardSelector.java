package selectors;

import java.util.List;

import cards.Card;
import gameBase.DominionGame;
import gameBase.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 * Runs the user selection of cards inside gameplay.
 * @author Nathaniel Brooke
 * @version 04-03-2015
 */
public class SingleCardSelector {

	private int selection;
	private List<Card> selectFrom;
	private String message;
	private boolean isRequired;
	private Card title;
	
	private boolean displaying;
	private ImageView topImage;

	private DominionGame game;
	private Player player;

	/**
	 * Constructor for the SingleCardSelector class.  
	 * Selects from an ArrayList of cards.
	 * @param g the game.
	 * @param selection cards to select from.
	 * @param message Message to display.
	 * @param num number to select.
	 * @param isMax if the number listed must be selected. 
	 */
	public SingleCardSelector(DominionGame g, List<Card> selection, String message, Card label, boolean isMust)	{
		this(g, g.getCurrentPlayer(), selection, message, label, isMust);
	}

	/**
	 * Constructor for the SingleCardSelector class.  
	 * Selects from an ArrayList of cards.
	 * @param g the game.
	 * @param p the player deciding.
	 * @param selection cards to select from.
	 * @param message Message to display.
	 * @param num number to select.
	 * @param isMax if the number listed must be selected. 
	 */
	public SingleCardSelector(DominionGame g, Player p, List<Card> selection, String message, Card label, boolean isMust)	{
		selectFrom = selection;
		title = label;
		this.message = message;
		game = g;
		player = p;
		isRequired = isMust;
		displaying = false;

		if(isMust && selectFrom.isEmpty()) {
			throw new RuntimeException("Empty list provided");
		}
	}
	
	/**
	 * Displays a card in the caption area of the selection window.
	 * @param c the card to display.
	 */
	public void displayCard(Card c) {
		c.passGame(game);
		topImage = new ImageView(c.getImage().toURI().toString());
	}

	/**
	 * Handles when card is clicked.
	 */
	private void cardClicked(int index) {
		selection = index;
		game.getClient().sendString("SELECT " + selection);
	}

	/**
	 * Determines which card was selected.
	 * @return index of the card selected.
	 */
	public int getSelectedIndex() {
		if(selectFrom.isEmpty()) return -1;
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().chooseCard(selectFrom, isRequired, title.getName());
		}
		Platform.runLater(() -> {
			if(!game.isOnline() && !player.equals(game.getCurrentPlayer())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Decision for " + player.getPlayerName());
				alert.setTitle("Dominion");
				alert.showAndWait();
			}
			if(!game.isOnline() || player.equals(game.getGUI().getMyPlayer())) {
				VBox center = new VBox(10);
				center.getChildren().addAll(getTopPane(), getCenterPane());
				center.setPadding(new Insets(10, 10, 10, 10));
				HBox root = new HBox(10);
				root.getChildren().addAll(getLeftPane(), center);
				game.getGUI().addOverlay(root, title.getName());
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
	 * Creates the pane with the message.
	 * @return the top pane.
	 */
	private Pane getTopPane() {
		VBox topPane = new VBox(5);
		topPane.setAlignment(Pos.CENTER);
		if(topImage != null) topPane.getChildren().add(topImage);
		Text msg = new Text(message);
		msg.setFont(Font.font(18));
		msg.setFill(Color.WHITE);
		topPane.getChildren().add(msg);
		return topPane;
	}
	
	/**
	 * Creates the pane with the card view.
	 * @return the left pane.
	 */
	private Pane getLeftPane() {
		VBox leftPane = new VBox();
		ImageView cardView = new ImageView(title.getImage().toURI().toString());
		cardView.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/8.5 - 25);
		cardView.setFitHeight(cardView.getFitWidth()/296.0*473.0);
		Button cardBtn = new Button();
		cardBtn.setGraphic(cardView);
		setBackground(cardBtn, Color.BLACK);
		leftPane.setPadding(new Insets(10, 10, 10, 10));
		leftPane.getChildren().add(cardBtn);
		return leftPane;
	}

	/**
	 * Sets up center panel with ArrayList of buttons, 
	 * and scroll-able panels of buttons representing each card.
	 */
	private Pane getCenterPane()	{
		HBox centerPane = new HBox(10);
		for(int i = 0; i < selectFrom.size(); i++) {
			int index = i;
			Button cardBtn = new Button();
			cardBtn.setGraphic(game.getGUI().getImage(selectFrom.get(i)));
			cardBtn.setOnAction(e -> cardClicked(index));
			setBackground(cardBtn, Color.BLACK);
			centerPane.getChildren().add(cardBtn);
		}
		if(!isRequired) {
			Button noneBtn = new Button("None");
			noneBtn.setOnAction(e -> cardClicked(-1));
			centerPane.getChildren().add(noneBtn);
		}
		return centerPane;
	}
		
	/**
	 * Sets the background of a button.
	 * @param input the button to set the background of.
	 * @param color the color of the background.
	 */
	private void setBackground (Button input, Color color) {
		if(input == null) return;
		input.setBackground(new Background(new BackgroundFill(
				color, new CornerRadii(5), new Insets(0, 3, 0, 3))));
	}
	
}
