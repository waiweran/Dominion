package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import genericGame.network.OnlineConnection;
import server.GameServer;

public class OnlineGameTest {

	@Test
	public void test() throws Exception {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Base/First Game.dog"));
		testGame(setup, options);
	}
	
	private GameOptions prepGameOptions() {
		ArrayList<String> npcList = new ArrayList<>();
		npcList.add("Random");
		npcList.add("Random");
		npcList.add("Random");
		npcList.add("Big Money");
		GameOptions options = new GameOptions(true);
		options.hideGraphics();
		options.setNumPlayers(4);
		options.setNPC(npcList);
		return options;
	}
	
	/**
	 * Runs a repeatable, online DominionGame simulation.
	 * @param setup the GameSetup describing the game.
	 * @param gameOptions the GameOptions describing the game.
	 * @throws Exception 
	 */
	private void testGame(GameSetup setup, GameOptions gameOptions) throws Exception {
		startServer();	
		for(int i = 0; i < 100; i++) {
			DominionGame game = new DominionGame(setup, gameOptions);
			game.random.setSeed(i);
			DominionClient dc = new DominionClient();
			new OnlineConnection(dc, "localhost", "Player 1", game);
			dc.sendString("START");
			dc.sendString("NEW TURN");
		}
	}

	private void startServer() {
		GameServer gs = new GameServer();
		Thread server = new Thread(() -> {
			try {
				gs.runClientFinder();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		server.setName("Local Server Main");
		server.start();
	} 


}
