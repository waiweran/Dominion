package selectors;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cards.Card;
import gameBase.CardFactory;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Runs the user selection of cards for the Dominion game.
 * Lets user select 10 cards to play game with.
 * @author Nathaniel Brooke
 * @version 03-11-2017
 */
public class InputDialog {

	private Stage stage;
	private Text numSelected;
	private Button[] buttons;
	private boolean[] options;
	private CheckBox balance, prosperity, shelter, fancy;
	private ComboBox<Integer> players, npc;
	private int howMany;
	private TextField name;

	private CardFactory allCards;
	private GameOptions gameOptions;
	private Card bane;
	private DominionGame game;
	
	/**
	 * Initializes and displays the card selection.
	 * @param startingOptions Starting settings for the game.
	 */
	public InputDialog(GameOptions startingOptions) {
		allCards = new CardFactory();
		gameOptions = startingOptions;
		options = new boolean[256];
		initialize();
	}

	/**
	 * Initializes and displays the card selection.
	 * @param setup pre-loaded cards, preferences.
	 * @param startingOptions Starting settings for the game.
	 */
	public InputDialog(GameSetup setup, GameOptions startingOptions) {
		this(startingOptions);
		makeStartingSelections(setup);
	}
	
	/**
	 * Runs the selection GUI.
	 * @return the created game.
	 */
	public DominionGame makeSelections() {
		BorderPane root = new BorderPane();		
		root.setTop(getTopPane());
		root.setCenter(getCenterPane());
		Scene scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.showAndWait();
		return game;
	}

	/**
	 * Makes starting card selections based on the given game setup.
	 * @param setup Starting card selections.
	 */
	private void makeStartingSelections(GameSetup setup) {
		for(Card c : setup.getCards()) {
			int index = allCards.getCardNum(c);
			options[index] = true;
			setBackground(buttons[index], Color.ORANGE);
			howMany++;
		}
		numSelected.setText("   " + howMany + " of 10 Cards Selected     ");
		name.setText(setup.getGameName());
		balance.setSelected(setup.balanceCards());
		prosperity.setSelected(setup.useProsperity());
		shelter.setSelected(setup.useProsperity());
		fancy.setSelected(setup.useFancyBase());
		bane = setup.getBane();
	}
	
	/**
	 * Creates the DominionGame designated by the input.
	 * @param showGraphics Used to disable the GUI
	 */
	private void createGame() {
		ArrayList<Card> cards = new ArrayList<>();
		for(int i = 0; i < options.length; i++) {
			if(options[i]) {
				cards.add(allCards.getCardAt(i));
			}
		}
		String gameName = name.getText();
		if(gameName.isEmpty()) gameName = "Game";
		GameSetup setup = new GameSetup(gameName, cards, balance.isSelected(), 
				prosperity.isSelected(), fancy.isSelected(), shelter.isSelected());
		setup.setBane(bane);
		game = new DominionGame(setup, gameOptions);
	}
	
	/**
	 * Selects NPC types for all NPC players.
	 * @param num The total number of NPC players.
	 * @return A list of NPC types.
	 */
	private List<String> selectNPC(int num) {
		List<String> output = new ArrayList<>();
		for(int i = gameOptions.getNumNPC(); i < num; i++) {
			String[] options = {"Big Money", "Ratio", "Random", "AI"};
			HashMap<ButtonType, Integer> choices = new HashMap<>();
			Alert alert = new Alert(AlertType.NONE);
			alert.setHeaderText("Pick Computer Player Type");
			alert.setContentText("Choice for Player " + i);
			alert.setTitle("Dominion");
			alert.getButtonTypes().clear();
			for(int j = 0; j < options.length; j++) {
				ButtonType btn = new ButtonType(options[j]);
				choices.put(btn, j);
				alert.getButtonTypes().add(btn);
			}
			int choice = choices.get(alert.showAndWait().get());
			output.add(options[choice]);
		}
		return output;
	}

	/**
	 * Handles when OK button is clicked.
	 * Checks if selection complete, starts game.
	 */
	private void confirmSelection()	{	
		int i = 0;
		for(boolean b : options) {
			if(b) i++;
		}
		if(i == 10) {
			gameOptions.setNumPlayers(players.getValue());
			gameOptions.setNPC(selectNPC(npc.getValue()));
			createGame();
			stage.close();
		}
	}
	
	/**
	 * Randomizes cards selection.
	 * Randomizes playing with shelters, platinum, and colony.
	 */
	private void randomize() {	
		int random;
		while(howMany < 10) {
			random = (int) (Math.random()*(options.length - 6));
			if(!options[random] && exists[random]) {
				options[random] = true;
				setBackground(buttons[random], Color.ORANGE);
				howMany++;
			}
		}
		prosperity.setSelected(0 == (int) (Math.random()*2));
		shelter.setSelected(0 == (int) (Math.random()*2));
		numSelected.setText("   " + howMany + " of 10 Cards Selected     ");
	}
	
	/**
	 * Handles whenever a button in the main card menus is clicked.
	 * @param btn the button clicked.
	 */
	private void buttonClicked(Button btn) {
		for(int i = 0; i < buttons.length; i++) {
			if(btn == buttons[i]) {
				options[i] = !options[i];
				if(options[i]) {
					setBackground(btn, Color.ORANGE);
					howMany++;
					numSelected.setText("   " + howMany + " of 10 Cards Selected     ");
				}
				else {
					setBackground(btn, Color.WHITE);
					howMany--;
					numSelected.setText("   " + howMany + " of 10 Cards Selected     ");
				}
			}
		}
	}
	
	/**
	 * Initializes required graphics fields.
	 */
	private void initialize() {
		buttons = new Button[options.length];
		for(int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button();
		}
		name = new TextField();
		players = new ComboBox<Integer>();
		npc = new ComboBox<Integer>();
		balance = new CheckBox("Balance Cards");
		prosperity = new CheckBox("Platinum and Colony");
		shelter = new CheckBox("Shelters");
		fancy = new CheckBox("Fancy Base Cards");
		numSelected = new Text("    " + howMany + " of 10 Cards Selected    ");
	}
	
	/**
	 * Creates the top pane of the GUI. 
	 * Contains the prosperity check box, the number of players selector, 
	 * and the card counter and confirm button.
	 * @return the top pane.
	 */
	private Pane getTopPane()	{
		FlowPane topPane = new FlowPane();
		topPane.setAlignment(Pos.CENTER);
		topPane.setPadding(new Insets(5, 5, 5, 5));
		name.setPromptText("Game Name");
		players.getItems().addAll(2, 3, 4, 5, 6);
		players.getSelectionModel().select(0);
		if(gameOptions.isOnline()) {
			topPane.getChildren().add(new Text("Game Name: "));
			topPane.getChildren().add(name);
			players.setValue(6);
		}
		else {
			topPane.getChildren().add(new Text("Players: "));
			topPane.getChildren().add(players);
		}
		topPane.getChildren().add(new Text("    NPC: "));
		npc.getItems().addAll(0, 1, 2, 3, 4, 5);
		npc.setValue(0);
		topPane.getChildren().add(npc);
		topPane.getChildren().add(new Text("    "));
		topPane.getChildren().add(balance);
		topPane.getChildren().add(new Text("    "));
		topPane.getChildren().add(prosperity);
		topPane.getChildren().add(new Text("    "));
		topPane.getChildren().add(shelter);
		topPane.getChildren().add(new Text("    "));
		topPane.getChildren().add(fancy);		
		topPane.getChildren().add(new Text("    "));
		Button randomizeBtn = new Button("Randomize");
		topPane.getChildren().add(randomizeBtn);
		randomizeBtn.setOnAction(e -> randomize());
		topPane.getChildren().add(numSelected);
		Button confirmBtn = new Button("Ok");
		topPane.getChildren().add(confirmBtn);
		confirmBtn.setOnAction(e -> confirmSelection());
		return topPane;
	}

	/**
	 * Sets up center panel with tabs based on expansion, 
	 * and scroll-able panels of buttons representing each card.
	 * @param things the card buttons, passed from DominionGame.
	 */
	private TabPane getCenterPane()	{
		double cardWidth = (Screen.getPrimary().getVisualBounds().getWidth())/5 - 25;
		double cardHeight = cardWidth/296.0*473.0;
		TabPane centerPane = new TabPane();
		centerPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		int i, k = 0;
		String[] files = {"Base", "Intrigue", "Seaside", "Alchemy", "Prosperity", 
				"Cornucopia", "Hinterlands", "Dark Ages", "Guilds", "Adventures", "Promo", "Events"};

		for(int j = 0; j < files.length; j++) {
			FlowPane temp = new FlowPane();
			temp.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
			File dir = new File("Images/Cards/" + files[j]);
			File[] pics = dir.listFiles();
			Arrays.sort(pics, (a, b) -> a.getName().compareTo(b.getName()));

			for(i = 0; i < pics.length; i++) {
				if(pics[i].getName().equals("knights")) 
					pics[i] = new File("Images/Cards/Dark Ages/knights/knightsrandomizer.jpg");
				Button btn = buttons[i + k];
				ImageView cardImg = new ImageView(pics[i].toURI().toString());
				cardImg.setFitWidth(cardWidth);
				cardImg.setFitHeight(cardHeight);
				btn.setGraphic(cardImg);
				temp.getChildren().add(btn);
				btn.setOnAction(e -> buttonClicked(btn));
				if(!exists[i + k]) setBackground(btn, Color.RED);
				buttons[i + k] = btn;
			}
			k  += i;
			ScrollPane scr = new ScrollPane(temp);
			centerPane.getTabs().add(new Tab(files[j], scr));
		}
		return centerPane;
	}
	
	private void setBackground(Button btn, Color color) {
		   btn.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	private static final boolean[] exists = {
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, // Base
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, // Intrigue
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, // Seaside
			false,false,false,false,false,false,true, false,false,false,false,true, // Alchemy
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, // Prosperity
			true, true, true, true, true, true, true, true, true, true, true, true, true, // Cornucopia
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, // Hinterlands
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false,true, true, true, true, true, true, true, true, true, true, true, true, true, true, // Dark Ages
			true, false,false,false,false,false,true, false,false,false,true, false,true, // Guilds
			true, true, true, true, true, true, true, true, true, false,true, false,true, true, true, true, false,true, false,true, false,true, true, true, true, true, false,true, true, true, // Adventures
			false,true, false,false,false,true, // Promo
			false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,// Adventures Events
		};

}
