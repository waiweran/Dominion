
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;

import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import genericGame.GameSecurity;
import genericGame.network.LocalConnection;
import genericGame.network.OnlineConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import selectors.InputDialog;
import server.GameServer;

/**
 * Runs the user's Dominion game.
 * Lets the user select the game mode.
 * Controls whether the program is running.
 * @author Nathaniel Brooke
 * @version 03-11-2017
 */
public class Launcher extends Application {
	
	private static final File LOGO_FILE = new File("Images/dominion_logo.png");

	private GameSetup opened;
	private String name;
	private Text fileText;
	
	/**
	 * Initializes information, 
	 */
	public Launcher() {		
		loadPlayerName();
		setupErrorLog();
		GameSecurity.confirmLocation();
		GameSecurity.confirmReadMeAgree();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Dominion Launcher");
		BorderPane root = new BorderPane();
		root.setTop(getTopPane());
		root.setCenter(getCenterPane());
		Scene mainScene = new Scene(root, 800, 280);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	/**
	 * Loads the player's name from the PlayerInfo file.
	 */
	private void loadPlayerName() {
		try {
			Scanner in = new Scanner(new File("PlayerInfo.txt"));
			name = in.nextLine();
			in.close();
		} catch (FileNotFoundException e) {
			name = "New Player";
			e.printStackTrace();
		}
	}

	/**
	 * Creates an error log file.
	 * Only does this if program run as jar.
	 */
	private void setupErrorLog() {
		if(!Launcher.class.getResource("Launcher.class").toString().startsWith("file")) {
			try {
				System.setErr(new PrintStream("errorLog.txt"));
				System.err.println("Error Log:\n");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * Creates a new game on this computer.
	 */
	private void newGameComputer() {
		try {
			DominionClient dc = new DominionClient();
			new LocalConnection(dc, makeGame(false));
		}
		catch(NullPointerException e) {
			// Do nothing, selection cancelled
		}
	}



	/**
	 * Creates a new game and LAN server.
	 */
	private void newGameLan() {	
		Alert serverAlert = new Alert(AlertType.INFORMATION);
		GameServer gs = new GameServer();
		Thread server = new Thread(() -> {
			try {
				gs.runClientFinder();
			} catch (IOException e) {
				Platform.runLater(() -> {
					serverAlert.close();
					Alert alert = new Alert(AlertType.ERROR, "Network Error");
					alert.setHeaderText("Local Server Crashed");
					alert.showAndWait();
				});
			}
		});
		server.setName("Local Server Main");
		server.start();
		try {
			serverAlert.setTitle("Local Server");
			serverAlert.setHeaderText("Local Server IP Address: " + InetAddress.getLocalHost());
			serverAlert.setContentText("Local server runs as long as this message is open");
			serverAlert.getButtonTypes().clear();
			serverAlert.getButtonTypes().add(ButtonType.CLOSE);
			DominionClient dc = new DominionClient();
			new OnlineConnection(dc, "localhost", name, makeGame(true));
			serverAlert.showAndWait();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, "Network Error");
			alert.setHeaderText("Server Not Found");
			alert.show();
		} catch (NullPointerException e) {
			// Do nothing, selection cancelled
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverAlert.close();
			gs.stop();
		}
	}

	/**
	 * Opens a game on a LAN server.
	 */
	private void openGameLan() {	
		try {
			TextInputDialog ipaddress = new TextInputDialog();
			ipaddress.setHeaderText("Enter Local Server IP Address");
			ipaddress.setContentText("IP Address: ");
			String addr = ipaddress.showAndWait().get();
			DominionClient dc = new DominionClient();
			new OnlineConnection(dc, addr, name);
		} catch (NoSuchElementException e) {
			// No IP address entered, do nothing
		} catch (ConnectException e1) {
			Alert alert = new Alert(AlertType.ERROR, "Network Error");
			alert.setHeaderText("Server not Found");
			alert.showAndWait();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}

	/**
	 * Creates a new game through the server online.
	 */
	private void newGameOnline() {	
		try {
			DominionClient dc = new DominionClient();
			new OnlineConnection(dc, OnlineConnection.SERVER, name, makeGame(true));
		} catch (ConnectException e) {
			Alert alert = new Alert(AlertType.ERROR, "Network Error");
			alert.setHeaderText("Server not Found");
			alert.showAndWait();
		} catch (NullPointerException e) {
			// Do nothing, selection cancelled
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens a game on the online server.
	 */
	private void openGameOnline() {	
		try {
			DominionClient dc = new DominionClient();
			new OnlineConnection(dc, OnlineConnection.SERVER, name);
		} catch (ConnectException e) {
			Alert alert = new Alert(AlertType.ERROR, "Network Error");
			alert.setHeaderText("Server not Found");
			alert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Lets the user open a saved game setup.
	 */
	private void openGameSetup() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File("Saves"));
		chooser.getExtensionFilters().add(new ExtensionFilter("Dominion Files", "*.dog"));
		try {
			File gameFile = chooser.showOpenDialog(null);
			if(gameFile != null) {
				opened = new GameSetup(gameFile);
				fileText.setText("Open " + opened.getGameName());
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, "File Error");
			alert.setHeaderText("File could not be opened");
			alert.showAndWait();
		}
	}
	
	/**
	 * Makes a DominionGame.
	 * @return freshly made, not yet started DominionGame.
	 */
	private DominionGame makeGame(boolean online) {
		InputDialog in;
		if(opened == null) {
			in = new InputDialog(new GameOptions(online));
		}
		else {
			in = new InputDialog(opened, new GameOptions(online));
			opened = null;
		}
		DominionGame game = in.makeSelections();
		return game;
	}

	/**
	 * Creates the top pane of the GUI.
	 * Contains Dominion logo.
	 * @return the top pane.
	 */
	private Pane getTopPane()	{
		VBox topPane = new VBox();
		topPane.setAlignment(Pos.CENTER);
		fileText = new Text("New Game");
		topPane.getChildren().addAll(new ImageView(LOGO_FILE.toURI().toString()), fileText);
		topPane.setPadding(new Insets(15, 0, 0, 0));
		return topPane;
	}

	/**
	 * Sets up the center pane with buttons to select game mode.
	 * @return the center pane.
	 */
	private VBox getCenterPane()	{
		VBox center = new VBox(15);
		center.setAlignment(Pos.CENTER);
		Button newCompBtn = makeButton("New Game on This Computer");
		newCompBtn.setOnAction(e -> newGameComputer());
		Button newLanBtn = makeButton("New LAN Game");
		newLanBtn.setOnAction(e -> newGameLan());
		Button newOnlineBtn = makeButton("New Online Game");
		newOnlineBtn.setOnAction(e -> newGameOnline());
		Button openCompBtn = makeButton("Open Saved Game Setup");
		openCompBtn.setOnAction(e -> openGameSetup());
		Button openLanBtn = makeButton("Open Existing LAN Game");
		openLanBtn.setOnAction(e -> openGameLan());
		Button openOnlineBtn = makeButton("Open Online Game");
		openOnlineBtn.setOnAction(e -> openGameOnline());
		HBox topRow = new HBox(20);
		topRow.setAlignment(Pos.CENTER);		
		topRow.getChildren().addAll(newCompBtn, newLanBtn, newOnlineBtn);
		HBox bottomRow = new HBox(20);
		bottomRow.setAlignment(Pos.CENTER);
		bottomRow.getChildren().addAll(openCompBtn, openLanBtn, openOnlineBtn);
		center.getChildren().addAll(topRow,  bottomRow);
		return center;
	}
	
	/**
	 * Makes a button of the correct size.
	 * @param text to display on the button.
	 * @return the button.
	 */
	private Button makeButton(String text) {
		Button btn = new Button(text);
		btn.setPrefWidth(240);
		btn.setPrefHeight(50);
		return btn;
	}
	
	/**
	 * Main method of the Launcher class.  sets up and runs the game.
	 * @param args command line arguments if needed.  Not used.  
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
