package genericGame;

import java.io.File;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * GUI for a BoardGame.
 * @author Nathaniel Brooke
 * @version 03/16/2017
 */
public abstract class GameGUI {
	
	private double width, height;				//Screen dimensions
	private Stage stage;						//Main window stage
	private BorderPane mainPane;				//Main pane to display game board
	private StackPane root;						//Root pane of the window.
	private TextArea logText;					//The window that stores the Game Log text
	private Pane gameLog;						//Panel with the game log in it
	private TextField chatType;					//Chat typing area
	private TextArea chatWindow;				//Chat text window	
	private boolean scoreShown;					//Whether the score has been displayed
	
	
	/**
	 * Constructor for the GUI.
	 */
	public GameGUI() {
		scoreShown = false;
		Platform.runLater(() -> {
			width = Screen.getPrimary().getVisualBounds().getWidth();
			height = Screen.getPrimary().getVisualBounds().getHeight();
			makeGameLog();
			stage = new Stage();
			root = new StackPane();
			mainPane = new BorderPane();
			setBackground();        
			root.getChildren().addAll(mainPane);		
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setOnCloseRequest(e -> onWindowClose(e));
			stage.show();
		});
	}

	/**
	 * Generates the game log and its display pane.
	 */
	private void makeGameLog() {
		gameLog = new Pane();
		logText = new TextArea();
		logText.setMinWidth(width/2);
		logText.setMinHeight(height/1.3);
		logText.setEditable(false);
		ScrollPane scroll = new ScrollPane(logText);
		scroll.setMinWidth(width/2);
		scroll.setMinHeight(height/1.3);
		gameLog.getChildren().add(scroll);
		gameLog.setMinWidth(width/2);
		gameLog.setMinHeight(height/1.3);
	}
	
	/**
	 * Sets the background in the window to one of the pictures in the Backgrounds folder.
	 */
	private void setBackground() {
		File[] imgs;
		imgs = new File("Images/Backgrounds").listFiles();
		int i = (int)(Math.random()*(imgs.length));
		ImageView background = new ImageView(imgs[i].toURI().toString());
		background.fitHeightProperty().bind(stage.heightProperty());
		background.fitWidthProperty().bind(stage.widthProperty());
		root.getChildren().add(0, background);
	}
	
	/**
	 * Generates the chat pane.
	 * @param width of the pane.
	 * @param height of the pane.
	 * @return the chat pane.
	 */
	protected Pane makeChat(double width, double height) {
		VBox chatPane = new VBox(15);
		chatWindow = new TextArea();
		chatWindow.setEditable(false);
		chatType = new TextField();
		chatType.setOnKeyPressed( e -> {
			if(e.getCode().equals(KeyCode.ENTER)) {
				if(getGame().isOnline()) {
					getGame().getClient().sendString("MESSAGE " + getMyPlayer().getPlayerName()
							+ ": " + chatType.getText() + "\n");
				}
				else {
					chatWindow.appendText(chatType.getText() + "\n");
				}
				chatType.setText("");
			}

		});
		chatPane.getChildren().addAll(chatWindow, chatType);
		chatPane.setMaxSize(width, height);
		return chatPane;
	}
	
	public void addToChat(String message) {
		Platform.runLater(() -> chatWindow.appendText(message + "\n"));
	}
	
	/**
	 * Adds an entry to the game log.
	 * @param entry the entry to be added.
	 */
	public void updateLog(String entry) {
		String text = System.currentTimeMillis() + ": " + entry + "\n";
		Platform.runLater(() -> logText.appendText(text));
	}
	
	/**
	 * Gets the game log pane.
	 * @return Pane holding the game log.
	 */
	protected Pane getGameLog() {
		return gameLog;
	}
	
	/**
	 * gets the main window pane.
	 * @return BorderPane to put main window into.
	 */
	protected BorderPane getMainPane() {
		return mainPane;
	}
	
	/**
	 * Adds an overlay to the GUI window.
	 * @param overlay
	 */
	public void addOverlay(Pane overlay, String title) {
		StackPane sp = new StackPane();
		HBox hbox = new HBox(sp);
		VBox vbox = new VBox(hbox);
		Pane bkgd = new Pane();
		VBox mainv = new VBox(5);
		bkgd.setBackground(new Background(new BackgroundFill(
				Color.GRAY, new CornerRadii(5), null)));
		bkgd.setOpacity(0.8);
		mainv.setPadding(new Insets(5, 10, 10, 10));
		Text titleTxt = new Text(title);
		titleTxt.setFont(Font.font(14));
		titleTxt.setFill(Color.WHITE);
		titleTxt.setStroke(Color.WHITE);
		mainv.getChildren().addAll(titleTxt, overlay);
		sp.getChildren().addAll(bkgd, mainv);
		hbox.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);
		root.getChildren().get(root.getChildren().size() - 1).setOpacity(0.8);
		root.getChildren().get(root.getChildren().size() - 1).setMouseTransparent(true);
		root.getChildren().add(vbox);
	}
	
	/**
	 * Removes the uppermost overlay from the GUI window.
	 */
	public void removeOverlay() {
		if(root.getChildren().size() > 2) {
			root.getChildren().remove(root.getChildren().size() - 1);
			root.getChildren().get(root.getChildren().size() - 1).setOpacity(1);
			root.getChildren().get(root.getChildren().size() - 1).setMouseTransparent(false);
		}
	}
	
	/**
	 * Accessor method for the game stored in a subclass.
	 * @return the game.
	 */
	public abstract BoardGame getGame();
		
	/**
	 * Accessor method for the player in the current GUI window.
	 * @return the player.
	 */
	public abstract AbstractPlayer getMyPlayer();
	
	/**
	 * Loads the score display into the GUI.
	 */
	protected abstract Pane loadScorePane();
	
	/**
	 * Displays score of all players.
	 */
	public void showScores() {
		Platform.runLater(() -> this.addOverlay(loadScorePane(), "Game End"));
		scoreShown = true;
	}

	/**
	 * Handles closing the game window.
	 */
	private void onWindowClose(WindowEvent e) {
		if(!scoreShown) {
			getGame().getClient().sendString("GAME END");
			showScores();
			e.consume();
		}
	}

}
