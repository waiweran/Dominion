package genericGame.network;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Sets up the window describing how many players are in a game.
 * @author Nathaniel
 * @version 07-24-2015
 */
public class GameWaiter {
	
	private static final int WIDTH = 600;

	private Stage mainStage;
	private Pane centerPane;
	private OnlineConnection client;
	private int slots;
	private boolean first, displayingWindow;

	/**
	 * Constructor for the GameWaiter class.
	 * @param gc the GameClient to send start or cancel messages through.
	 * @param firstPlayer true if this player started the game.
	 */
	public GameWaiter(OnlineConnection gc, boolean firstPlayer) {
		mainStage = new Stage();
		mainStage.setOnCloseRequest(e -> closeWithoutStarting());
		client = gc;
		first = firstPlayer;
		displayingWindow = false;
	}

	/**
	 * Sets up the window for the GameWaiter.
	 * @param numSlots the number of player slots in the game.
	 */
	private void setupWindow(int numSlots) {
		slots = numSlots;
		BorderPane root = new BorderPane();
		root.setTop(getTopPane());
		root.setCenter(getCenterPane());
		Scene scene = new Scene(root, WIDTH + 10, 160);
		mainStage.setScene(scene);
		mainStage.show();
		displayingWindow = true;
	}

	/**
	 * Called by the GameClient when it receives a join message.
	 * @param message the message received.
	 */
	public void playerJoined(String message) {
		Platform.runLater(() -> {
			String[] players = message.split("\t");
			if(!displayingWindow) {
				setupWindow(Integer.parseInt(players[players.length - 1]));
			}
			centerPane.getChildren().clear();
			for(int i = 1; i < players.length - 1; i++) {
				StackPane stack = new StackPane();
				stack.setAlignment(Pos.CENTER);
				Rectangle rect = new Rectangle(WIDTH/slots - 10, WIDTH/6 - 10);
				Text entry = new Text(" " + players[i]);
				rect.setFill(Color.LAWNGREEN);
				stack.getChildren().addAll(rect, entry);
				stack.setMaxWidth(WIDTH/slots - 10);
				centerPane.getChildren().add(stack);
			}
		});
	}
	
	/**
	 * Closes the GameWaiter.
	 */
	public void close() {
		Platform.runLater(() -> mainStage.close());
	}

	/**
	 * Generates the top pane with the title and the start button.
	 * @return the top pane.
	 */
	private Pane getTopPane() {
		HBox pane = new HBox(20);
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(10, 0, 0, 0));
		pane.getChildren().add(new Text("Wait while other players join the game"));
		if(first) {
			Button startBtn = new Button("Start Game");
			startBtn.setOnAction(e -> client.sendString("START"));
			pane.getChildren().add(startBtn);
		}
		return pane;
	}

	/**
	 * Generates the center pane with the slots filling up.
	 * @return the center pane.
	 */
	private Pane getCenterPane() {
		centerPane = new HBox(10);
		centerPane.setPadding(new Insets(0, 0, 0, 10));
		return centerPane;			
	}


	/**
	 * Handles closing the window when the game hasn't started.
	 */
	private void closeWithoutStarting() {
		client.sendString("GAME CANCEL Player closed waiting window");
		mainStage.close();
	}

}
