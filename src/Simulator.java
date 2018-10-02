

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import computerPlayer.ComputerPlayer;
import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import gameBase.Player;
import genericGame.LocalConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import selectors.InputDialog;

/**
 * Simulates Dominion games with the GUI off and automated card selection.
 * @author Nathaniel
 * @version 8-02-2016
 */
public class Simulator extends Application {
	
	private static final String FILENAME = "Saves/Base/First Game.dog";
	
	private static String[] arguments;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		int numSimulations = Integer.parseInt(arguments[0]);
		ArrayList<String> cpuTypes = new ArrayList<>();
		for(int i = 1; i < arguments.length; i++) {
			cpuTypes.add(arguments[i]);
		}
		Thread gameManager = new Thread(() -> runGames(numSimulations, cpuTypes, FILENAME));
		gameManager.setName("Game Manager");
		gameManager.start();
	}

	/**
	 * Runs computer played games of Dominion without graphics.
	 * @param num the number of games to run.
	 * @param filename the name of the game file to use.
	 * If no game file provided, randomly generated games used
	 */
	private void runGames(int numRuns, List<String> cpuTypes, String filename) {
		
		//Run Multiple Test Games
		System.out.println("Playing " + filename + "\n");
		HashMap<String, Integer> games = new HashMap<>();	
		ArrayList<String> players = new ArrayList<>();
		int numGames = 0;
		for(int i = 0; i < numRuns; i++) {
			
			try {
				//Setup Game
				GameSetup setup = new GameSetup(new File(filename));
				DominionGame game = new InputDialog(setup, new GameOptions(false)).autoSelection(cpuTypes);				
				DominionClient dc = new DominionClient();
				LocalConnection lc = new LocalConnection(dc, game);
				dc.passConnection(lc);

				//Start Game
				Platform.runLater(() -> game.getCurrentPlayer().startTurn());
				
				//Wait for game to end
				while(!game.board.isGameOver()) Thread.sleep(10);
								
				try {
					// List players in games
					if(players.isEmpty()) {
						System.out.print("Players: ");
						for(Player p : game.players) {
							players.add(p.getComputerPlayer().getName());
							System.out.print(p.getComputerPlayer().getName() + ", ");
						}
						System.out.println("\n");
					}
					
					//Determine and Print Scores
					TreeMap<Integer, Player> scoreFiles = new TreeMap<Integer, Player>();
					System.out.print("Scores for game " + (i + 1) + ": ");
					for(Player p : game.players) {
						scoreFiles.put(p.getScore(), p);
						System.out.print(p.getScore() + " ");
					}
					System.out.println();

					//Save Winner's Files
					ComputerPlayer winner = scoreFiles.lastEntry().getValue().getComputerPlayer();
					if(!winner.getName().equals("Big Money"))
						winner.saveData();

					//Determine who wins game
					if(!games.containsKey(winner.getName())) {
						games.put(winner.getName(), new Integer(0));
					}
					games.put(winner.getName(), games.get(winner.getName()) + 1);
					numGames++;
					
				} 
				catch(Exception e) {
					if(game.getCurrentPlayer().deck.drawSize() == 0) {
						System.err.println("Empty deck caused error:");
					}
					else {
						System.err.println("Draw cards available, error:");
					}
					e.printStackTrace();
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		// List players in games
		System.out.print("\nPlayers: ");
		for(String s : players) {
			System.out.print(s + ", ");
		}
		System.out.println("\n");

		//Print Success Rate
		System.out.println();
		for(String winName : games.keySet()) {
			System.out.println(winName + " win rate: " + games.get(winName) + "/" + numGames);
		}		
		System.exit(0);
	}
	
	/**
	 * Main method of the Simulator class.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		arguments = args;
		launch(args);
	}


}
