import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import computerPlayer.ComputerPlayer;
import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import gameBase.Player;
import genericGame.network.LocalConnection;

public class Trainer {
	
	private static final String FILENAME = "Saves/Base/First Game.dog";
	
	private volatile int index;
	private volatile int runners;
	private volatile Map<String, Integer> games;	

	public Trainer() {
		index = 0;
		runners = 0;
		games  = new HashMap<>();
	}
	
	public void runGames(int numRuns, List<String> cpuTypes, String filename, boolean save, boolean quiet) {
		
		//Run Multiple Test Games
		ArrayList<String> players = new ArrayList<>();
		int gameNum = 0;

		while(true) {
			
			synchronized(this) {
				if(index >= numRuns) break;
				gameNum = index++;				
			}
						
			try {
				//Setup Game
				GameSetup setup = new GameSetup(new File(filename));
				GameOptions gameOptions = new GameOptions(false);
				gameOptions.setNumPlayers(cpuTypes.size());
				gameOptions.setNPC(cpuTypes);
				gameOptions.hideGraphics();
				DominionGame game = new DominionGame(setup, gameOptions);
				DominionClient dc = new DominionClient();
				new LocalConnection(dc, game);

				try {
					
					//Run game simulation
					game.getCurrentPlayer().startTurn();

					// List players in games
					if(players.isEmpty()) {
						for(Player p : game.players) {
							players.add(p.getComputerPlayer().getName() + " " + p.getPlayerNum());
						}
					}
					
					//Determine and Print Scores
					TreeMap<Integer, Player> scoreFiles = new TreeMap<Integer, Player>();
					synchronized(this) {
						if(!quiet) System.out.print("Scores for game " + (gameNum + 1) + ": ");
						for(Player p : game.players) {
							scoreFiles.put(p.getScore(), p);
							if(!quiet) System.out.print(p.getScore() + " ");
						}
						if(!quiet) System.out.println();

						//Save Winner's Files
						Player winnerP = scoreFiles.lastEntry().getValue();
						ComputerPlayer winner = winnerP.getComputerPlayer();
						if(save && !winner.getName().equals("Big Money"))
							winner.saveData();

						//Determine who wins game
						String entryName = winner.getName() + " " + winnerP.getPlayerNum();
						if(!games.containsKey(entryName)) {
							games.put(entryName, new Integer(0));
						}
						games.put(entryName, games.get(entryName) + 1);
					}
				} 
				catch(Exception e) {
					synchronized(this) {
						index--;
					}
					if(game.getCurrentPlayer().deck.size() == 0) {
						System.err.println("Empty deck caused error:");
					}
					else {
						System.err.println("Draw cards available, error:");
					}
					e.printStackTrace();
				}
			}
			catch(Exception e) {
				synchronized(this) {
					runners--;
				}
				throw new RuntimeException(e);
			}
		}
		synchronized(this) {
			runners--;
			if(runners == 0) {

				// List players in games
				if(!quiet) {
					System.out.print("\nPlayers: ");
					for(String s : players) {
						System.out.print(s + ", ");
					}
					System.out.println();
				}

				//Print Success Rate
				for(String name : players) {
					int wins = 0;
					if(games.containsKey(name)) {
						wins = games.get(name);
					}
					System.out.println(name + " win rate: " + wins + "/" + numRuns);
				}	

			}
		}
	}


}
