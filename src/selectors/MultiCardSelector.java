package selectors;

import java.util.ArrayList;
import java.util.Collections;
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
 * @author Nathaniel
 * @version 04-03-2015
 */
public class MultiCardSelector {

	private List<Integer> selectionIndecies;
	private List<Card> selectFrom;
	private String message;
	private int numNeeded;
	private boolean isRequired;
	private Card title;
	
	private boolean displaying;
	
	private DominionGame game;
	private Player player;

	/**
	 * Constructor for the SelectionDialog class.  
	 * Selects from an ArrayList of cards.
	 * @param g the game.
	 * @param selection cards to select from.
	 * @param message Message to display.
	 * @param num number to select.
	 * @param isMax if the number listed must be selected. 
	 */
	public MultiCardSelector(DominionGame g, List<Card> selection, String message, Card label, int num, boolean isMust)	{
		this(g, g.getCurrentPlayer(), selection, message, label, num, isMust);
	}

	/**
	 * Constructor for the SelectionDialog class.  
	 * Selects from an ArrayList of cards.
	 * @param g the game.
	 * @param p the player deciding
	 * @param selection cards to select from.
	 * @param message Message to display.
	 * @param num number to select.
	 * @param isMax if the number listed must be selected. 
	 */
	public MultiCardSelector(DominionGame g, Player p, List<Card> selection, String message, Card label, int num, boolean isMust)	{
		selectFrom = selection;
		title = label;
		this.message = message;
		numNeeded = num;
		game = g;
		player = p;
		isRequired = isMust;
		selectionIndecies = new ArrayList<Integer>();
		displaying = false;

		if(isMust && selectFrom.size() - numNeeded < 0) {
			throw new RuntimeException(selectFrom.size()
					+ " options provided, " + numNeeded + " required");
		}
	}

	/**
	 * Handles clicks to cards being selected.
	 * Adds/removes them from selected list, changes background colors.
	 */
	private void cardClicked(Button btn, int index) {
		if(!selectionIndecies.contains(new Integer(index))) {
			setBackground(btn, Color.YELLOW);
			selectionIndecies.add(new Integer(index));
		}
		else {
			setBackground(btn, Color.BLACK);
			selectionIndecies.remove(new Integer(index));
		}
	}
	
	/**
	 * Handles clicks to the Done button.
	 */
	private void doneClicked() {
		if(selectionIndecies.size() == numNeeded
				|| !isRequired && selectionIndecies.size() < numNeeded) {
			Collections.sort(selectionIndecies);
			String selection = "";
			for(int i : selectionIndecies) {
				selection += i;
				selection += " ";
			}
			game.getClient().sendString("SELECT " + selection);
		}
	}

	/**
	 * Determines which cards were selected.
	 * Waits until Ok has been clicked and a proper number of cards had been selected.
	 * @return a list of indices of the cards selected.
	 */
	public List<Integer> getSelectedIndex() {
		if(selectFrom.isEmpty()) return new ArrayList<Integer>();
		if(player.isComputerPlayer()) {
			return player.getComputerPlayer().chooseCards(selectFrom, numNeeded, isRequired, title.getName());
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
		return getFromServer();
	}

	/**
	 * Gets the list of selected indices from the server.
	 * @return List of selected indices.
	 */
	private List<Integer> getFromServer() {
		ArrayList<Integer> output = new ArrayList<Integer>();
		String input = game.getClient().getSelection();
		Platform.runLater(() -> {
			if(displaying) game.getGUI().removeOverlay();
		});
		for(String s : input.substring(7).split(" ")) {
			try {
				output.add(Integer.parseInt(s));
			}
			catch(NumberFormatException e) {}
		}
		return output;
	}
	

	/**
	 * Creates the pane with the message.
	 * @return the top pane.
	 */
	private Pane getTopPane() {
		HBox topPane = new HBox(20);
		topPane.setAlignment(Pos.CENTER);
		Text msg = new Text(message);
		msg.setFont(Font.font(18));
		msg.setFill(Color.WHITE);
		Button okBtn = new Button("Done"); 
		okBtn.setOnAction(e -> doneClicked());
		topPane.getChildren().addAll(msg, okBtn);
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
			cardBtn.setOnAction(e -> cardClicked(cardBtn, index));
			setBackground(cardBtn, Color.BLACK);
			centerPane.getChildren().add(cardBtn);
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
