package gameBase;

import cards.Card;
import genericGame.BoardGame;
import genericGame.GameClient;

/**
 * A client for board game servers.
 * Contains the DGP (Dominion Game Protocol).
 * Here are the strings that are sent:
 *
 *  Client -> Server		Server -> Client		Explanation of use
 *  ----------------       	----------------		----------------
 *  NEW TURN               	NEW TURN				When "End Turn" is clicked
 *  HAND <n> (card #)		HAND <n> (card #)		When card from hand is clicked, "Play Treasures"
 *  SUPPLY <text>			SUPPLY <text>			When a supply is clicked
 *  TAVERN <text>			TAVERN <text>			When a tavern mat card is clicked
 *  CHECK <n>				CHECK <n>				When the system checks states are still aligned
 *  							
 */
public class DominionClient extends GameClient {

	private DominionGame game;

	@Override
	public DominionGame getGame() {
		return game;
	}
	
	@Override
	public void loadGame(BoardGame input) {
		game = (DominionGame) input;
		game.setGameClient(this);
		game.startGame();
	}

	@Override
	protected void monitorGame(String response) {
		if (response.equals("NEW TURN")) {
			game.endTurn();
		} 
		else if (response.startsWith("HAND")) {
			if(response.charAt(5) == 'T') {
				game.getCurrentPlayer().playTreasures();
			}
			else {
				int index = Integer.parseInt(response.substring(5)); 
				game.getCurrentPlayer().playCard(index);
			}
		} 
		else if (response.startsWith("SUPPLY")) {
			String suppName = response.substring(7); 
			if(game.getCurrentPlayer().buyCard(game.board.findSupply(game.allCards.findCard(suppName)))) {
				if(game.gamePhase > 2) {
					game.endTurn();
				}
			}
		} 
		else if (response.startsWith("TAVERN")) {
			String cardName = response.substring(7);
			Card tavernCard = null;
			for(Card c : game.getCurrentPlayer().deck.tavern) {
				if(c.getName().equals(cardName)) {
					tavernCard = c;
				}
			}
			tavernCard.tavernAction();
		}
		else if (response.startsWith("CHECK")) {
			String localDeck = getGame().players.get(
					Integer.parseInt(response.substring(6, 7)) - 1).deck.toString();
			if(!localDeck.equals(response.substring(8))) {
				System.err.println("Deck Mismatch");
				sendString("RESET");
			}
		}
	}
	
	@Override
	public void resetGame(BoardGame newGame) {
		try {
			DominionGUI gui = game.getGUI();
			int playerIndex = game.players.indexOf(gui.getMyPlayer());
			game = (DominionGame) newGame;
			game.setGameClient(this);
			game.setGUI(gui);
			gui.setGame(game, game.players.get(playerIndex));
			gui.setupUpdating();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}		
	}

}