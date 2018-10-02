package genericGame;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Allows the player to select an online game to join.
 * @author Nathaniel Brooke
 * @version 03-11-2017
 */
public class GameChooser {

	private boolean displayingWindow;
	private Stage stage;
	private Pane centerPane;
	private OnlineConnection client;
	private ArrayList<String> games;

	/**
	 * Constructor for the GameChooser class.
	 * @param gc the GameClient to send game choice messages through.
	 */
	public GameChooser(OnlineConnection gc) {
		games = new ArrayList<>();
		displayingWindow = false;
		client = gc;
	}

	/**
	 * Updates the game listings that the player can select.
	 * @param listings the listings that the GameClient received from the server.
	 */
	public void updateListings(String listings) {
		Platform.runLater(() -> {
			games.clear();
			if(listings.equals("CANCEL")) {
				closeWithoutChoosing();
				return;
			}
			if(listings.equals("CONFIRM")) {
				stage.close();
				return;
			}
			for(String s : listings.split("\t")) {
				games.add(s);
			}
			if(!displayingWindow) {
				setupWindow();
			}
			else {
				updateCenterPane();
			}
		});
	}

	/**
	 * Sets up the GUI for the GameChooser.
	 */
	private void setupWindow() {
		stage = new Stage();
		stage.setOnCloseRequest(e -> closeWithoutChoosing());
		BorderPane root = new BorderPane();
		centerPane = new VBox(15);
		root.setTop(getTopPane());
		updateCenterPane();
		root.setCenter(centerPane);
		stage.setScene(new Scene(root, 600, 600));
		stage.show();
		displayingWindow = true;
	}

	/**
	 * Gets the top pane with the window label.
	 * @return the top pane.
	 */
	private Pane getTopPane() {
		FlowPane pane = new FlowPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(10, 10, 10, 10));
		Text title = new Text("Choose an Online Game to Join:");
		title.setFont(Font.font("Serif", 20));
		pane.getChildren().add(title);
		return pane;
	}

	/**
	 * Gets the center pane with the game listings.
	 * @return the center pane.
	 */
	private void updateCenterPane() {
		centerPane.getChildren().clear();
		centerPane.setPadding(new Insets(10, 10, 10, 10));
		for(int i = 0; i < games.size(); i++) {
			String s = games.get(i);
			if(s.length() > 4) {
				Text nameView = new Text(s.substring(0, s.length() - 4));
				nameView.setFont(Font.font("Serif", 16));
				FlowPane name = new FlowPane(nameView);
				name.setMaxWidth(300);
				name.setMinWidth(300);
				name.setAlignment(Pos.CENTER_LEFT);
				String[] gamesplit = s.split(" ");
				Text spots = new Text(gamesplit[gamesplit.length - 2] + " Slots");
				spots.setFont(Font.font("Serif", 16));
				Button join = new Button("Join");
				int index = Integer.parseInt(gamesplit[gamesplit.length - 1]);
				join.setOnAction(e -> buttonClicked(index));
				HBox entry = new HBox(95);
				entry.setAlignment(Pos.CENTER_RIGHT);
				entry.getChildren().addAll(name, spots, join);
				centerPane.getChildren().add(entry);
			}
		}
	}

	/**
	 * Handles join button click.
	 * @param index of the entry selected.
	 */
	private void buttonClicked(int index) {
		client.sendString("CHOOSE " + index);
	}

	/**
	 * Closes the window without choosing a game.
	 */
	private void closeWithoutChoosing() {
		client.sendString("CHOOSE CANCEL");
		stage.close();
	}
	
}

