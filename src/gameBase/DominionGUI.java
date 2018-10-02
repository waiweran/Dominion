package gameBase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import cards.Card;
import cards.defaults.Trash;
import cards.prosperity.TradeRoute;
import genericGame.GameGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Main GUI for the Dominion Game.
 * Can be implemented in pass-and-play or as separate screens for each player.
 * @author Nathaniel
 * @version 06/02/2015
 */
public class DominionGUI extends GameGUI {	

	public final int cardHeight, cardWidth;						//Card dimensions

	private DominionGame game;									//The Game
	private Player myPlayer;									//Used when game run on multiple machines
	private FlowPane defArea, kingArea, hidArea;			    //Contain default, kingdom, hidden supplies
	private VBox exArea;										//Contain extra supplies
	private HBox playSpace;										//Contains played cards
	private VBox messageBox;									//Area for messages over playspace
	private VBox drawPile, discardPile, trashPile;				//Draw, discard, and trash pile tops
	private VBox dispNums;										//Displays actions, buys, and treasure for player.
	private Pane popupPane;										//Contains extra buttons not on board
	private Text playDescrip;									//Describes what the player just did.
	private ArrayList<Pane> players;							//Contains the playing icons for all players
	private HashMap<Card, Image> cardImgs;						//Map of pictures of cards in the game.
	private HBox hand;											//Contains the hand

	/**
	 * Constructor for class DominionGUI - establish the JFrame
	 * Loads the window.
	 */
	public DominionGUI(DominionGame g, Player player) {
		super(g.showGraphics());
		game = g;
		myPlayer = player;
		cardImgs = new HashMap<Card, Image>();
		players = new ArrayList<Pane>();

		cardHeight = (int)(Screen.getPrimary().getVisualBounds().getHeight())/5 - 27;
		cardWidth = (int)(cardHeight*296.0/473.0);

		if(!game.showGraphics()) return;
		
		getMainPane().setLeft(getLeftPane());
		getMainPane().setCenter(getCenterPane());
		getMainPane().setRight(getRightPane());
		getMainPane().setBottom(getBottomPane());
		
		setupUpdating();
		updatePlaySpace();
	}

	////++++ METHODS FOR UPDATING GUI ++++\\\\

	/**
	 * Notifies player that it is their turn.
	 */
	public void turnNotify() {
		Platform.runLater(() -> {
			updateHand();
			for(int i = 0; i < players.size(); i++) {
				Pane player = players.get(i);
				Text name = (Text)player.getChildren().get(0);
				if(i == game.getCurrentPlayer().getPlayerNum() - 1) {
					name.setStroke(Color.BLACK);
					name.setFill(Color.BLACK);
					player.setBackground(new Background(new BackgroundFill(
							Color.YELLOW, new CornerRadii(5), new Insets(5, 10, 5, 10))));
				}
				else {
					name.setStroke(Color.WHITE);
					name.setFill(Color.WHITE);
					player.setBackground(new Background(new BackgroundFill(
							Color.color(0, 0, 0, 0), new CornerRadii(5), new Insets(5, 10, 5, 10))));
				}
			}
			if(!game.isOnline()) {
				hand.setVisible(false);
				VBox box = new VBox(10);
				box.setPadding(new Insets(10, 10, 10, 10));
				box.setAlignment(Pos.CENTER_RIGHT);
				Text txt = new Text(game.getCurrentPlayer().getPlayerName() + "'s Turn");
				txt.setFont(Font.font(20));
				txt.setFill(Color.WHITE);
				Button btn = new Button("Ok");
				box.getChildren().addAll(txt, btn);
				addOverlay(box, "Dominion");
				btn.setOnAction(e -> {
					removeOverlay();
					hand.setVisible(true);
					updateHand();
					updateDiscard();
				});
			}
			else if(getMyPlayer() == game.getCurrentPlayer()) {
				VBox box = new VBox(10);
				box.setPadding(new Insets(10, 10, 10, 10));
				box.setAlignment(Pos.CENTER_RIGHT);
				Text txt = new Text("Take your Turn!");
				txt.setFont(Font.font(20));
				txt.setFill(Color.WHITE);
				Button btn = new Button("Ok");
				box.getChildren().addAll(txt, btn);
				addOverlay(box, "Dominion");
				btn.setOnAction(e -> {
					removeOverlay();
				});
			}
		});
	}
	
	/**
	 * Sets up observation of all fields displayed.
	 */
	public void setupUpdating() {
		if(getGame().isOnline()) {
			getMyPlayer().deck.hand.addObserver((o, arg) -> updateHand());
			getMyPlayer().deck.discard.addObserver((o, arg) -> updateDiscard());
		}
		else {
			for(Player p : getGame().players) {
				p.deck.hand.addObserver((o, arg) -> updateHand());
				p.deck.discard.addObserver((o, arg) -> updateDiscard());
			}
		}
		for(Player p : getGame().players) {
			p.deck.play.addObserver((o, arg) -> updatePlaySpace());
			p.deck.discard.addObserver((o, arg) -> updateOpponentDiscard(p));
			p.addObserver((o, arg) -> updateDisplayNumbers());
		}
		for(Supply s : game.board.defaultCards) {
			s.addObserver((o, arg) -> updateSupplies());
		}
		for(Supply s : game.board.extraCards) {
			s.addObserver((o, arg) -> updateSupplies());
		}
		for(Supply s : game.board.kingdomCards) {
			s.addObserver((o, arg) -> updateSupplies());
		}
		game.board.trash.addObserver((o, arg) -> updateTrash());
	}

	/**
	 * Updates the rendering of the player's hand.
	 */
	private void updateHand() {
		hand.getChildren().clear();
		for(int i = 0; i < getMyPlayer().deck.hand.size(); i++) {
			Button b = makeCardButton(getMyPlayer().deck.hand.get(i));
			hand.getChildren().add(b);
			int index = i;
			b.setOnMouseClicked(e -> {
				if(e.getButton().equals(MouseButton.SECONDARY)) {
					rightClicked(getMyPlayer().deck.hand.get(index));
				}
				else if(getMyPlayer() == game.getCurrentPlayer()) {
					handClicked(index);
				}
			});
		}
	}

	/**
	 * Updates the rendering of a player's play space.
	 */
	private void updatePlaySpace() {
		playSpace.getChildren().clear();
		
		// Show opponent's play space and gained cards
		Player p = game.getCurrentPlayer();
		if(getMyPlayer() != p) {
			HBox plays = new HBox(10);
			HBox gains = new HBox(10);
			playSpace.getChildren().addAll(plays, gains);
			for(Card c : p.deck.duration) {
				plays.getChildren().add(makeCardButton(c));
			}
			for(Card c : p.deck.play) {
				plays.getChildren().add(makeCardButton(c));
			}
			for(Card c : p.deck.gained) {
				plays.getChildren().add(makeCardButton(c));
			}
		}
		
		// Show my play space
		else {
			for(Card c : p.deck.duration) {
				playSpace.getChildren().add(makeCardButton(c));
			}
			for(Card c : p.deck.play) {
				playSpace.getChildren().add(makeCardButton(c));
			}
		}
	}

	/**
	 * Updates the rendering of a player's discard.
	 */
	private void updateDiscard() {
		discardPile.getChildren().clear();
		if(getMyPlayer().deck.getDiscardTop() != null) {
			discardPile.getChildren().add(makeCardButton(getMyPlayer().deck.getDiscardTop()));
		}
		else {
			Button disc = new Button();
			disc.setGraphic(getBlank());
			setBackground(disc);
			discardPile.getChildren().add(disc);
		}
	}

	/**
	 * Updates the rendering of an opponent's discard.
	 * @param opponent the opponent whose discard needs updating.
	 */
	private void updateOpponentDiscard(Player opponent) {
		Pane player = players.get(opponent.getPlayerNum() - 1);
		player.getChildren().remove(1);
		if(opponent.deck.getDiscardTop() != null) {
			player.getChildren().add(makeCardButton(opponent.deck.getDiscardTop()));
		}
		else {
			Button b = new Button();
			b.setGraphic(getBack());
			setBackground(b);
			player.getChildren().add(b);
		}
		((Text)player.getChildren().get(0)).setText(opponent.getPlayerName());
	}

	/**
	 * Updates rendering of trash.
	 */
	private void updateTrash() {
		trashPile.getChildren().clear();
		trashPile.getChildren().add(getImage(game.board.trash.get(game.board.trash.size() - 1)));
	}

	/**
	 * Updates rendering of supplies for the player.
	 */
	private void updateSupplies() {
		defArea.getChildren().clear();
		exArea.getChildren().clear();
		kingArea.getChildren().clear();
		hidArea.getChildren().clear();
		
		// Default Supply Piles
		for(Supply s : game.board.defaultCards) {
			defArea.getChildren().add(makeSupply(s, true, e -> supplyClicked(s))); 
		}
		if(game.setup.useProsperity()) {
			defArea.setMaxWidth(cardWidth*6);

			defArea.getChildren().addAll(makeSpacer(), makeSpacer());
		}
		else {
			defArea.setMaxWidth(cardWidth*5);
			defArea.getChildren().add(makeSpacer());
		}
		StackPane trash = makeSpacer();
		trash.getChildren().add(trashPile);
		defArea.getChildren().add(trash);

		// Extra Supply Piles
		if(game.board.extraCards != null) {
			
			// Trade Route Mat
			int matNum = 0;
			if(game.board.findSupply(new TradeRoute()) != null) {
				matNum = 1;
				String name = "Images/Mats/traderoute" + game.board.getTradeRouteMat() + ".png";
				ImageView mat = new ImageView(new File(name).toURI().toString());
				mat.setFitHeight(cardHeight);		
				mat.setFitWidth(cardHeight);	
				HBox matPane = new HBox(mat);
				matPane.setPadding(new Insets(5, 0, 0, 0));
				exArea.getChildren().add(matPane);
			}
			
			// Put gain-able piles in middle, non-gain-able piles in more menu
			if(game.board.extraCards.size() + matNum > 3) {
				for(Supply s : game.board.extraCards) {
					Pane supply = makeSupply(s, true, e -> supplyClicked(s));
					if(s.getCard().canBeGained()) {
						exArea.getChildren().add(supply);
					}
					else {
						hidArea.getChildren().add(supply);
					}
				}
			}
			
			// Put all piles in the middle
			else {
				for(Supply s : game.board.extraCards) {
					exArea.getChildren().add(makeSupply(s, true, e -> supplyClicked(s)));
				}
			}
		}

		// Kingdom Supply Piles
		for(Supply s : game.board.kingdomCards) {
			kingArea.getChildren().add(makeSupply(s, true, e -> supplyClicked(s))); 
		}
		kingArea.setMaxWidth(cardWidth*6);
	}
	
	/**
	 * Updates the actions, buys, and treasure display numbers.
	 */
	private void updateDisplayNumbers() {
		if(!game.showGraphics()) return;
		dispNums.getChildren().clear();
		Player p = game.getCurrentPlayer();
		Text actions = new Text("Actions: " + p.getActions());
		actions.setStroke(Color.WHITE);
		actions.setFill(Color.WHITE);
		Text buys = new Text("Buys: " + p.getBuys());
		buys.setStroke(Color.WHITE);
		buys.setFill(Color.WHITE);
		Text treasure = new Text("Treasure: " + p.getTreasure());
		treasure.setStroke(Color.WHITE);
		treasure.setFill(Color.WHITE);
		dispNums.getChildren().addAll(actions, buys, treasure);
	}

	////++++ METHODS FOR USER INPUT ++++\\\\	

	/**
	 * Displays a large version of a card, upon right click
	 * @param c the card right clicked
	 */
	private void rightClicked(Card c) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Dominion");
		ImageView content = new ImageView(c.getImage().toURI().toString());
		content.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/5 - 25);
		content.setFitHeight(content.getFitWidth()/296.0*473.0);
		alert.setGraphic(content);
		alert.showAndWait();
	}
	
	/**
	 * Handles when a card in the hand is clicked.
	 * @param index of the card within the hand.
	 */
	private void handClicked(int index)  {
		game.getClient().sendString("HAND " + index);
	}
	
	/**
	 * Handles when a supply pile button is clicked.
	 * @param s the supply.
	 */
	private void supplyClicked(Supply s) {
		game.getClient().sendString("SUPPLY " + s.getCard().getName());
	}
	
	/**
	 * Handles when a card on the tavern mat is clicked.
	 * @param c the card on the tavern mat.
	 */
	private void tavernClicked(Card c) {
		removeOverlay();
		game.getClient().sendString("TAVERN " + c.getName());
	}


	////++++ PRIVATE METHODS FOR ESTABLISHING UI ++++\\\\

	/**
	 * Creates the left pane of the GUI.
	 * Contains other players' materials.
	 * @return the left pane.
	 */
	private Pane getLeftPane()	{
		VBox players1 = new VBox(30);
		players1.setAlignment(Pos.TOP_CENTER);
		players1.setPrefWidth(cardHeight);
		for(int i = 0; i < (int)(game.getNumPlayers()/2.0 + 0.5); i++) {
			VBox player = new VBox(5);
			player.setAlignment(Pos.TOP_CENTER);
			player.setPadding(new Insets(10, 10, 10, 10));
			Text name = new Text(game.players.get(i).getPlayerName());
			if(i == 0) {
				name.setStroke(Color.BLACK);
				player.setBackground(new Background(new BackgroundFill(
						Color.YELLOW, new CornerRadii(5), new Insets(5, 10, 5, 10))));

			}
			else {
				name.setStroke(Color.WHITE);
				name.setFill(Color.WHITE);
			}
			Button b = new Button();
			b.setGraphic(getBack());
			setBackground(b);
			player.getChildren().addAll(name, b);
			players.add(player);
			players1.getChildren().add(player);
		}
		return players1;
	}

	/**
	 * Creates the right pane of the GUI.
	 * Contains other players' materials.
	 * @return the right pane.
	 */
	private Pane getRightPane()	{
		VBox players1 = new VBox(30);
		players1.setAlignment(Pos.TOP_CENTER);
		players1.setPrefWidth(cardHeight);
		for(int i = (int)(game.getNumPlayers()/2.0 + 0.5); i < game.getNumPlayers(); i++) {
			VBox player = new VBox(5);
			player.setAlignment(Pos.TOP_CENTER);
			player.setPadding(new Insets(10, 10, 10, 10));
			Text name = new Text(game.players.get(i).getPlayerName());
			name.setStroke(Color.WHITE);
			name.setFill(Color.WHITE);
			Button b = new Button();
			b.setGraphic(getBack());
			setBackground(b);
			player.getChildren().addAll(name, b);
			players.add(player);
			players1.getChildren().add(player);
		}
		return players1;
	}
	
	/**
	 * Sets up the center panel of the GUI.
	 * Contains the supply piles and play space.
	 * @return the center panel.
	 */
	private Pane getCenterPane()	{
		BorderPane centerPane = new BorderPane();
		popupPane = new FlowPane();
		
		// Default Supply Piles
		defArea = new FlowPane();
		for(Supply s : game.board.defaultCards) {
			defArea.getChildren().add(makeSupply(s, true, e -> supplyClicked(s))); 
		}
		if(game.setup.useProsperity()) {
			defArea.setPrefWrapLength((cardWidth + 30)*4);
			defArea.getChildren().addAll(makeSpacer(), makeSpacer());
		}
		else {
			defArea.setPrefWrapLength((cardWidth + 30)*3);
			defArea.getChildren().add(makeSpacer());
		}
		StackPane trash = makeSpacer();
		trashPile = new VBox();
		trashPile.setAlignment(Pos.CENTER);
		trashPile.getChildren().add(makeCardButton(
				game.board.trash.get(game.board.trash.size() - 1)));
		trash.getChildren().add(trashPile);
		defArea.getChildren().add(trash);
		centerPane.setLeft(defArea);

		// Extra Supply Piles
		exArea = new VBox();
		exArea.setAlignment(Pos.TOP_CENTER);
		hidArea = new FlowPane();

		if(game.board.extraCards != null) {
			
			// Trade Route Mat
			int matNum = 0;
			if(game.board.findSupply(new TradeRoute()) != null) {
				matNum = 1;
				ImageView mat = new ImageView(new File(
						"Images/Mats/traderoute0.png").toURI().toString());
				mat.setFitHeight(cardHeight);		
				mat.setFitWidth(cardHeight);
				StackPane matPane = new StackPane();
				matPane.getChildren().addAll(makeSpacer(), mat);
				exArea.getChildren().add(matPane);
			}
			
			// Put gain-able piles in middle, non-gain-able piles in more menu
			if(game.board.extraCards.size() + matNum > 3) {
				for(Supply s : game.board.extraCards) {
					Pane supply = makeSupply(s, true, e -> supplyClicked(s));
					if(s.getCard().canBeGained()) {
						exArea.getChildren().add(supply);
					}
					else {
						hidArea.getChildren().add(supply);
					}
				}
			}
			
			// Put all piles in the middle
			else {
				for(Supply s : game.board.extraCards) {
					exArea.getChildren().add(makeSupply(s, true, e -> supplyClicked(s)));
				}
			}
		}
		HBox spacerBox = new HBox(exArea);
		spacerBox.setAlignment(Pos.TOP_CENTER);
		centerPane.setCenter(spacerBox);
		popupPane.getChildren().add(hidArea);

		// Kingdom Supply Piles
		kingArea = new FlowPane();
		for(Supply s : game.board.kingdomCards) {
			kingArea.getChildren().add(makeSupply(s, true, e -> supplyClicked(s))); 
		}
		kingArea.setPrefWrapLength((cardWidth + 30)*4);
		centerPane.setRight(kingArea);

		// Informational Numbers
		BorderPane playArea = new BorderPane();
		dispNums = new VBox(25);
		updateDisplayNumbers();
		StackPane spacer = makeSpacer();
		spacer.getChildren().add(dispNums);
		playArea.setLeft(spacer);

		// Play Description Text
		playDescrip = new Text("Welcome! Game Started");
		playDescrip.setStroke(Color.WHITE);
		playDescrip.setFill(Color.WHITE);
		HBox descripPane = new HBox(playDescrip);
		descripPane.setAlignment(Pos.CENTER);
		playArea.setTop(descripPane);	
		
		// Play Space
		playSpace = new HBox(10);
		playSpace.setAlignment(Pos.CENTER);
		playArea.setCenter(playSpace);
		StackPane msgOverlay = new StackPane();
		messageBox = new VBox();
		messageBox.setAlignment(Pos.CENTER);
		messageBox.setMouseTransparent(true);
		msgOverlay.getChildren().addAll(playArea, messageBox);
		centerPane.setBottom(msgOverlay);

		return centerPane;
	}



	/**
	 * Creates the bottom pane of the GUI.
	 * Contains the player's settings, draw pile, hand, discard, and chat.
	 * @return the bottom pane.
	 */
	private Pane getBottomPane()	{
		BorderPane bottomPane = new BorderPane();
		HBox bottomLeft = new HBox(10);
		HBox bottomRight = new HBox(10);
		bottomLeft.setAlignment(Pos.CENTER);
		bottomRight.setAlignment(Pos.CENTER);
		
		// Settings and More Buttons
		VBox controls = new VBox(30);
		controls.setAlignment(Pos.CENTER);
		Button btnMore = new Button("More");
		btnMore.setOnAction(e -> generateMoreWindow());
		controls.getChildren().add(btnMore);
		Button btnSettings = new Button();
		btnSettings.setGraphic(new ImageView(new File("Images/settings.png").toURI().toString()));
		btnSettings.setOnAction(e -> showSettings());
		controls.getChildren().add(btnSettings);
		bottomLeft.getChildren().add(controls);

		// Draw Pile
		Button draw = new Button();
		draw.setGraphic(getBack());
		setBackground(draw);
		drawPile = new VBox(draw);
		drawPile.setAlignment(Pos.CENTER);
		bottomLeft.getChildren().add(drawPile);
		
		// Hand
		hand = new HBox(4);
		hand.setAlignment(Pos.CENTER);
		updateHand();
		bottomPane.setCenter(hand);
		
		// Discard Pile
		Button disc = new Button();
		disc.setGraphic(getBlank());
		setBackground(disc);
		discardPile = new VBox(disc);
		discardPile.setAlignment(Pos.CENTER);
		bottomRight.getChildren().add(discardPile);
		
		// Play Treasures, End Turn buttons
		VBox actions = new VBox(30);
		actions.setAlignment(Pos.CENTER);
		Button btnPlayTreasures = new Button("Play Treasures"); 
		btnPlayTreasures.setOnAction(e -> { 
			if(getMyPlayer() == game.getCurrentPlayer()) {
				game.getClient().sendString("HAND T");
			}
		});
		actions.getChildren().add(btnPlayTreasures);
		Button btnEndTurn = new Button("End Turn");
		btnEndTurn.setOnAction(e -> {
			if(getMyPlayer() == game.getCurrentPlayer()) {
				game.getClient().sendString("NEW TURN");
			}
		});
		actions.getChildren().add(btnEndTurn);
		bottomRight.getChildren().add(actions);
		
		// Chat
		bottomRight.getChildren().add(this.makeChat(cardHeight, cardHeight));

		bottomPane.setLeft(bottomLeft);
		bottomPane.setRight(bottomRight);
		bottomPane.setPadding(new Insets(0, 10, 5, 10));
		return bottomPane;
	}

	private void showSettings() {
		String[] options = {"Save Game Setup", "View Game Log", "Set Player Name"};
		HashMap<ButtonType, Integer> choices = new HashMap<>();
		Alert alert = new Alert(AlertType.NONE);
		alert.setHeaderText("Settings");
		alert.setTitle("Settings");
		alert.getButtonTypes().clear();
		for(int i = 0; i < options.length; i++) {
			ButtonType btn = new ButtonType(options[i]);
			choices.put(btn, i);
			alert.getButtonTypes().add(btn);
		}
		switch(choices.get(alert.showAndWait().get())) {
				case 0 : {
					game.setup.save(game.getName());
					break;
				}
				case 1 : {
					Stage gamelog = new Stage();
					gamelog.setScene(new Scene(new VBox(getGameLog())));
					gamelog.show();
					break;
				}
				case 2 : {
					TextInputDialog textIn = new TextInputDialog();
					textIn.setHeaderText("Player Name");
					textIn.setContentText("Enter new player name");
					Optional<String> name = textIn.showAndWait();
					if(name.isPresent()) {
						getMyPlayer().setPlayerName(name.get());
						getMyPlayer().savePlayerName();
						game.getClient().sendString("NAME " + getMyPlayer().getPlayerNum() + " " + name.get());
					}
					break;
				}

		}
	}

	private void generateMoreWindow() {

		if(popupPane.getChildren().size() == 2) {
			popupPane.getChildren().remove(1);
		}

		FlowPane tavernArea = new FlowPane();

		TreeMap<Card, Integer> temp = new TreeMap<Card, Integer>();
		for(Card c : getMyPlayer().deck.tavern) {
			if(!temp.containsKey(c)) {
				temp.put(c, 1);
			}
			else {
				temp.put(c, temp.get(c) + 1);
			}
		}
		
		for(Card c : temp.keySet()) {
			StackPane tavernPile = makeSupply(new Supply(c, temp.get(c)), true, e -> tavernClicked(c));
			if(tavernPile.getChildren().size() == 3) {
				tavernPile.getChildren().remove(3); //remove coin overlay
			}
			tavernArea.getChildren().add(tavernPile);
		}

		popupPane.getChildren().add(tavernArea);
		VBox closeBox = new VBox(5);
		Button closeBtn = new Button("Close");
		closeBtn.setOnAction(e -> removeOverlay());
		closeBox.getChildren().addAll(popupPane, closeBtn);
		closeBox.setAlignment(Pos.TOP_RIGHT);
		closeBox.setPadding(new Insets(0, 0, 3, 0));
		addOverlay(closeBox, "More");
	}

	/**
	 * Generates a card button that can be viewed enlarged when hovered over.
	 * @param c the card to make a button for.
	 * @return the button.
	 */
	private Button makeCardButton(Card c) {
		Button b = new Button();
		setBackground(b);
		b.setGraphic(getImage(c));
		b.setTooltip(new Tooltip());
		ImageView content = new ImageView(c.getImage().toURI().toString());
		content.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/5 - 25);
		content.setFitHeight(content.getFitWidth()/296.0*473.0);
		b.getTooltip().setGraphic(content);
		return b;
	}

	/**
	 * Creates a StackPane representing a supply pile.
	 * @param c the card the pile represents.
	 * @param numCards the number of cards in the pile.
	 * @param event Consumer triggered when the pile is clicked.
	 * @return the StackPane.
	 */
	private StackPane makeSupply(Supply s, boolean enabled, Consumer<MouseEvent> event) {
		StackPane supply = new StackPane();
		supply.setPadding(new Insets(5, 2, 2, 5));
		supply.setAlignment(Pos.CENTER);
		Button btn = new Button();
		setBackground(btn);
		btn.setOnMouseClicked(e -> {
			if(getMyPlayer() == game.getCurrentPlayer()) {
				event.accept(e);
			}
		});
		if(s.getQuantity() > 0) {
			if(enabled) {
				btn.setGraphic(getImage(s.getTopCard()));
			}
			else {
				ImageView img = getImage(s.getTopCard());
				img.setOpacity(0.3);
				btn.setGraphic(img);
			}
			ImageView content = new ImageView(s.getTopCard().getImage().toURI().toString());
			content.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/5 - 25);
			content.setFitHeight(content.getFitWidth()/296.0*473.0);
			btn.setTooltip(new Tooltip());
			btn.getTooltip().setGraphic(content);
		}
		else {
			btn.setGraphic(getImage(s.getCard()));
			btn.setOpacity(0.3);
			ImageView content = new ImageView(s.getCard().getImage().toURI().toString());
			content.setFitWidth((Screen.getPrimary().getVisualBounds().getWidth())/5 - 25);
			content.setFitHeight(content.getFitWidth()/296.0*473.0);
			btn.setTooltip(new Tooltip());
			btn.getTooltip().setGraphic(content);
		}
		Text num = new Text(Integer.toString(s.getQuantity()));
		num.setStroke(Color.WHITE);
		num.setFill(Color.WHITE);
		HBox numPane = new HBox(num);
		numPane.setPadding(new Insets(0, 5, 2, 0));
		numPane.setAlignment(Pos.BOTTOM_RIGHT);
		numPane.setMouseTransparent(true);
		supply.getChildren().addAll(btn, numPane);
		if(s.getCard().isVictory() && !s.gaveToken() 
				&& game.board.findSupply(new TradeRoute()) != null) {
			ImageView coins = getCoins();
			coins.setMouseTransparent(true);
			supply.getChildren().add(coins);
		}
		if(s.getEmbargo() > 0) {
			ImageView embargo = getEmbargo(s.getEmbargo());
			embargo.setMouseTransparent(true);
			supply.getChildren().add(embargo);
		}
		return supply;
	}
	
	/**
	 * Makes a StackPane of the size of a card or supply button.
	 * @return the StackPane.
	 */
	private StackPane makeSpacer() {
		StackPane trash = new StackPane();
		trash.setAlignment(Pos.CENTER);
		StackPane spacer = makeSupply(new Supply(new Trash(), 0), true, null);
		spacer.setOpacity(0);
		spacer.setMouseTransparent(true);
		trash.getChildren().add(spacer);
		return trash;
	}

	////++++ IMAGE ACCESSING METHODS ++++\\\\

	/**
	 * Gets an ImageView with the card's image, correctly sized.
	 * Uses HashMap storing the Images for fast re-retrieval.
	 * @param c the card.
	 * @return the ImageView of the card.
	 */
	public ImageView getImage(Card c) {
		c.passGame(getGame());
		Image img;
		if(cardImgs.containsKey(c)) {
			img = cardImgs.get(c);
		}
		else {
			img = new Image(c.getImage().toURI().toString());
			cardImgs.put(c, img);
		}
		ImageView view = new ImageView(img);
		view.setFitHeight(cardHeight);		
		view.setFitWidth(cardWidth);
		return view;
	}

	/**
	 * Gets the coin overlay image, correctly sized.
	 * @return the ImageIcon of the coin overlay.
	 */
	private ImageView getCoins() {
		return makeImage("Images/Tokens/coinoverlay.png");
	}

	/**
	 * Gets the embargo overlay image, correctly sized.
	 * @param num the number of embargo tokens.
	 * @return the ImageView of the overlay.
	 */
	private ImageView getEmbargo(int num) {
		return makeImage("Images/Tokens/embargo" + num + "overlay.png");
	}

	/**
	 * Gets the blank card image, correctly sized.
	 * @return the ImageView of a blank card.
	 */
	private ImageView getBlank() {
		return makeImage("Images/Cards/Card Backs/blank.jpg");
	}

	/**
	 * Gets the back of a card image, correctly sized.
	 * @return the ImageView of a blank card.
	 */
	private ImageView getBack() {
		return makeImage("Images/Cards/Card Backs/back.jpg");
	}
	
	/**
	 * Generates a card shaped ImageView from an image file.
	 * @param filename the location of the image file.
	 * @return the ImageView, in the shape of a card.
	 */
	public ImageView makeImage(String filename) {
		ImageView img = new ImageView(new File(filename).toURI().toString());
		img.setFitHeight(cardHeight);		
		img.setFitWidth(cardWidth);
		return img;
	}
	
	/**
	 * Sets the background of the given button to black.
	 * @param input button to change background of.
	 */
	private void setBackground (Button input) {
		input.setBackground(new Background(new BackgroundFill(
				Color.BLACK, new CornerRadii(5), new Insets(0, 3, 0, 3))));
	}
	
	@Override
	public void updateLog(String entry) {
		if(!game.showGraphics()) return;
		Platform.runLater(() -> playDescrip.setText(entry));
		super.updateLog(entry);
	}

	@Override
	public DominionGame getGame() {
		return game;
	}
	
	/**
	 * Fills in the fields required to update the GUI to work with a new game backend.
	 * @param newGame the new game object.
	 * @param newPlayer My new player object.
	 */
	public void setGame(DominionGame newGame, Player newPlayer) {
		game = newGame;
		myPlayer = newPlayer;
	}

	@Override
	public Player getMyPlayer() {
		return (game.isOnline())? myPlayer : game.getCurrentPlayer();
	}
	
	@Override
	protected Pane loadScorePane() {
		return new ScoreDisplay(getGame()).displayScores();
	}
	
	/**
	 * Updates the supply piles for supply selection
	 * @param typeSpecifier A predicate for determining allowed cards
	 * @param minCost minimum card cost.
	 * @param maxCost maximum card cost.
	 * @param hasPotion true if player has potion to spend.
	 */
	public void supplySelect(String message, Predicate<Card> typeSpecifier, 
			int minCost, int maxCost, boolean hasPotion, boolean isMust) {
		defArea.getChildren().clear();
		exArea.getChildren().clear();
		kingArea.getChildren().clear();
		hidArea.getChildren().clear();
		
		// Default Supply Piles
		for(Supply s : game.board.defaultCards) {
			boolean enabled =  typeSpecifier.test(s.getTopCard())
					&& s.getTopCard().getCost() <= maxCost
					&& s.getTopCard().getCost() >= minCost
					&& (!s.getTopCard().costsPotion() || hasPotion)
					&& s.getTopCard().canBeGained();
			defArea.getChildren().add(makeSupply(s, enabled, e -> supplySelectClicked(s))); 
		}
		Button noneBtn = new Button("None");
		noneBtn.setOnAction(e -> {
			game.getClient().sendString("SELECT CANCEL");
			updateSupplies();
		});
		StackPane spacer = makeSpacer();
		if(!isMust) spacer.getChildren().add(noneBtn);
		defArea.getChildren().add(spacer);
		if(game.setup.useProsperity()) {
			defArea.setMaxWidth(cardWidth*6);
			defArea.getChildren().addAll(makeSpacer());
		}
		else {
			defArea.setMaxWidth(cardWidth*5);
		}
		StackPane trash = makeSpacer();
		trash.getChildren().add(trashPile);
		defArea.getChildren().add(trash);

		// Extra Supply Piles
		if(game.board.extraCards != null) {
			
			// Trade Route Mat
			int matNum = 0;
			if(game.board.findSupply(new TradeRoute()) != null) {
				matNum = 1;
				String name = "Images/Mats/traderoute" + game.board.getTradeRouteMat() + ".png";
				ImageView mat = new ImageView(new File(name).toURI().toString());
				mat.setFitHeight(cardHeight);		
				mat.setFitWidth(cardHeight);	
				HBox matPane = new HBox(mat);
				matPane.setPadding(new Insets(5, 0, 0, 0));
				exArea.getChildren().add(matPane);
			}
			
			// Put gain-able piles in middle, non-gain-able piles in more menu
			if(game.board.extraCards.size() + matNum > 3) {
				for(Supply s : game.board.extraCards) {
					boolean enabled =  typeSpecifier.test(s.getTopCard())
							&& s.getTopCard().getCost() <= maxCost
							&& s.getTopCard().getCost() >= minCost
							&& (!s.getTopCard().costsPotion() || hasPotion);
					Pane supply = makeSupply(s, enabled, e -> supplySelectClicked(s));
					if(s.getCard().canBeGained()) {
						exArea.getChildren().add(supply);
					}
					else {
						hidArea.getChildren().add(supply);
					}
				}
			}
			
			// Put all piles in the middle
			else {
				for(Supply s : game.board.extraCards) {
					boolean enabled =  typeSpecifier.test(s.getTopCard())
							&& s.getTopCard().getCost() <= maxCost
							&& s.getTopCard().getCost() >= minCost
							&& (!s.getTopCard().costsPotion() || hasPotion);
					exArea.getChildren().add(makeSupply(s, enabled, e -> supplySelectClicked(s)));
				}
			}			
		}

		// Kingdom Supply Piles
		for(Supply s : game.board.kingdomCards) {
			boolean enabled =  typeSpecifier.test(s.getTopCard())
					&& s.getTopCard().getCost() <= maxCost
					&& s.getTopCard().getCost() >= minCost
					&& (!s.getTopCard().costsPotion() || hasPotion);
			kingArea.getChildren().add(makeSupply(s, enabled, e -> supplySelectClicked(s))); 
		}
		kingArea.setMaxWidth(cardWidth*6);
		
		// Put message over playspace
		Text msg = new Text(message);
		msg.setFill(Color.WHITE);	
		msg.setStroke(Color.WHITE);
		msg.setFont(Font.font(20));
		playSpace.setOpacity(0.6);
		messageBox.getChildren().add(msg);
		
		// Disable hand buttons
		hand.setMouseTransparent(true);
	}
	
	/**
	 * Resets the board when supply selection finished.
	 */
	public void resetSupplies() {
		updateSupplies();
		messageBox.getChildren().clear();
		playSpace.setOpacity(1);
		hand.setMouseTransparent(false);
	}
	
	/**
	 * Used for when a supply under selection mode is clicked.
	 * @param s The supply clicked.
	 */
	private void supplySelectClicked(Supply s) {
		game.getClient().sendString("SELECT " + s.getCard().getName());
	}


}