package gameBase;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.base.Moat;
import cards.extra.Champion;
import cards.seaside.Lighthouse;
import computerPlayer.CPUFactory;
import genericGame.AbstractPlayer;
import genericGame.BoardGame;


/**
 * Runs entire game for Dominion.
 * @author Nathaniel
 * @version 04-02-2015
 */
public class DominionGame extends BoardGame {

	private static final long serialVersionUID = 10000L;

	//Game Fields
	public final ArrayList<Player> players;	//contains the players in the current game
	public int gamePhase;					//records the phase (start, action, buy, end)
	public Board board;						//this is the game's board
	public CardFactory allCards;			//stores the list of all cards in the game
	public int numNPC;						//records the number of NPC in the game
	private transient DominionGUI gameGUI;	//this is the game's main GUI, used in pass and play
	public GameSetup setup;			        //holds game setup information
	private boolean extraTurn;				//records whether player gets a repeat turn
	private List<String> cpuTypes;			//holds the types of CPU in the game
	
	/**
	 * Initializes the game.
	 * @param setupInfo Card setup information for the game.
	 * @param options Setup options for the game.
	 */
	public DominionGame(GameSetup setupInfo, GameOptions options) {
		super(options.isOnline());
		setup = setupInfo;
		showGraphics = options.showGraphics();
		players = new ArrayList<Player>();
		allCards = new CardFactory();
		if(setup.getCards().size() != 10) {
			throw new RuntimeException("Incorrect number of supplies selected");
		}
		setNumPlayers(options.getNumPlayers());
		setGameName(setup.getGameName());
		numNPC = options.getNumNPC();		
		cpuTypes = options.getNPCTypes();
	}

	@Override
	public void startGame() {
		gameStarted = true;
		
		// Setup Board
		for(Card c : setup.getCards()) {
			c.passGame(this);
		}
		board = new Board(setup.getCards(), random, setup.useProsperity(), getNumPlayers());
		for(Supply s : board.getAllSupplies()) {
			s.getCard().passGame(this);
		}
		
		// Setup Players
		CPUFactory factory = new CPUFactory(this);
		int cpuIndex = 0;
		for(int i = 1; i <= getNumPlayers(); i++) {
			players.add(new Player(this, i, setup.useShelters()));
			boolean isCPU = i > getNumPlayers() - numNPC;
			if(isCPU) {
				Player current = players.get(players.size() - 1);
				current.setComputerPlayer(factory.getComputerPlayer(current, cpuTypes.get(cpuIndex++)));
			}
		} 
		
		// Setup GUI
		if(showGraphics) {
			if(isOnline()) {
				gameGUI = new DominionGUI(this, players.get(getClient().getPlayerNumber() - 1));
			}
			else {
				gameGUI = new DominionGUI(this, null); 
			}
		}
		
		log("Game " + getName() + " successfully set up");		
	}

	/**
	 * Ends a player's turn.
	 * Sets all games to correct turn position.
	 */
	public void endTurn() {
		Player endingPlayer = getCurrentPlayer();
		
		//Check the card ownership in decks is correct
		for(Player p : players) {
			for(Card c : p.deck.getDeck()) {
				if(c.getPlayer() == null) {
					throw new RuntimeException(c.getName() + " has no owner, in deck of "
							+ p.getPlayerName());
				}
				if(!c.getPlayer().equals(p)) {
					throw new RuntimeException(c.getName() + " owned by "
							+ c.getPlayer().getPlayerName() + ", in deck of " + p.getPlayerName());
				}
			}
		}

		//Run player-specific turn ending things
		getCurrentPlayer().endTurn();

		//Change whose turn it is
		if(!extraTurn) {
			nextTurn();
		}
		extraTurn = false;

		//Check if the game is over
		if(board.isGameOver()) {
			getClient().stopThreads();
			if(showGraphics) getGUI().showScores();
			return;
		}

		//Change Turn 
		if(showGraphics && !getCurrentPlayer().isComputerPlayer() 
				&& players.size() - numNPC > 1) {
			getGUI().turnNotify();
		}

		//Resets game phase
		gamePhase = 0;	

		//Starts the next person's turn
		getCurrentPlayer().startTurn();
		
		
		//Save changes to server
		if(showGraphics && getGUI().getMyPlayer().equals(endingPlayer)) {
			getClient().backupGame(this);
		}

		//Check the decks are correct between computers
		if(showGraphics && getGUI().getMyPlayer().equals(endingPlayer)) {
			for(Player p : players) {
				getClient().sendString("CHECK " + p.getPlayerNum() + " " + p.deck.toString());
			}
		}

	}

	@Override
	public DominionGUI getGUI() {
		return gameGUI;
	}
	
	/**
	 * Sets the GUI for this game.
	 * @param gui the new GUI.
	 */
	public void setGUI(DominionGUI gui) {
		gameGUI = gui;
	}

	@Override
	public ArrayList<AbstractPlayer> getPlayers() {
		ArrayList<AbstractPlayer> out = new ArrayList<AbstractPlayer>();
		out.addAll(players);
		return out;
	}

	@Override
	public Player getCurrentPlayer() {
		return players.get(getWhoseTurn() - 1);
	}

	/**
	 * Gets all players but the current player.
	 * @return ArrayList containing all Players but the current player.
	 */
	public ArrayList<Player> getOtherPlayers() {
		ArrayList<Player> output = new ArrayList<Player>();
		output.addAll(players);
		output.remove(getWhoseTurn() - 1);
		return output;
	}

	/**
	 * Gets the player to the right of the current player
	 * @return the Player object for the player to the right 
	 */
	public Player getRightPlayer() {
		if(getWhoseTurn() - 2 < 0) {
			return players.get(players.size() - 1);
		}
		else {
			return players.get(getWhoseTurn() - 2);
		}
	}

	/**
	 * Gets the player to the left of the current player
	 * @return the Player object for the player to the left 
	 */
	public Player getLeftPlayer() {
		if(getWhoseTurn() > players.size() - 1) {
			return players.get(0);
		}
		else {
			return players.get(getWhoseTurn());
		}
	}

	/**
	 * Gets a list of all players that should be attacked.
	 * @return ArrayList containing all players but 
	 * the current player and players with lighthouses or moats.
	 */
	public List<Player> getAttackedPlayers() {
		ArrayList<Player> output = new ArrayList<Player>();
		for(Player p : getOtherPlayers()) {
			if(!(p.deck.hand.contains(new Moat()) 
					|| p.deck.duration.contains(new Lighthouse()) 
					|| p.deck.duration.contains(new Champion()) 
					|| p.getPlayerNum() == getWhoseTurn())) {
				output.add(p);
			}
			ArrayList<Card> reactions = new ArrayList<Card>();
			for(Card c : p.deck.hand) {
				if(c.isReaction()) reactions.add(c);
			}
			for(Card c : reactions) {
				c.reactAttack();
			}
		}
		return output;
	}
	
	/**
	 * Can be called during cleanup to give the current player another turn.
	 */
	public void extraTurn() {
		extraTurn = true;
	}

}