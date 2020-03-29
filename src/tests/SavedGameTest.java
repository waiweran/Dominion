package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import gameBase.DominionClient;
import gameBase.DominionGame;
import gameBase.GameOptions;
import gameBase.GameSetup;
import genericGame.network.LocalConnection;


public class SavedGameTest {

	@Test
	public void testFirstGame() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Base/First Game.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testInteraction() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Base/Interaction.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testBigMoney() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Base/Big Money.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testSizeDistortion() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Base/Size Distortion.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testVillageSquare() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Base/Village Square.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testHandMadness() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Intrigue/Hand Madness.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testBestWishes() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Intrigue/Best Wishes.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testUnderlings() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Intrigue/Underlings.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testSecretSchemes() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Intrigue/Secret Schemes.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testDeconstruction() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Intrigue/Deconstruction.dog"));
		testGame(setup, options);
	}

	@Test
	public void testVictoryDance() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Intrigue/Victory Dance.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testBuriedTreasure() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Seaside/Buried Treasure.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testHighSeas() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Seaside/High Seas.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testShipwrecks() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Seaside/Shipwrecks.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testGiveAndTake() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Seaside/Give And Take.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testRepetition() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Seaside/Repetition.dog"));
		testGame(setup, options);
	}

	@Test
	public void testReachForTomorrow() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Seaside/Reach For Tomorrow.dog"));
		testGame(setup, options);
	}

	@Test
	public void testBeginners() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/Beginners.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testBigActions() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/Big Actions.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testBiggestMoney() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/Biggest Money.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testFriendlyInteractive() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/Friendly Interactive.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testLuckySeven() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/Lucky Seven.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testPathsToVictory() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/Paths to Victory.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testAllAlongTheWatchtower() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/All Along the Watchtower.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testTheGoodLife() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/The Good Life.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testTheKingsArmy() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Prosperity/The King's Army.dog"));
		testGame(setup, options);
	}
	
	@Test
	public void testBadOmens() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Cornucopia/Bad Omens.dog"));
		testGame(setup, options);
	}

	@Test
	public void testBountyOfTheHunt() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Cornucopia/Bounty of the Hunt.dog"));
		testGame(setup, options);
	}

	@Test
	public void testLastLaughs() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Cornucopia/Last Laughs.dog"));
		testGame(setup, options);
	}

	@Test
	public void testSmallVictories() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Cornucopia/Small Victories.dog"));
		testGame(setup, options);
	}

	@Test
	public void testTheJestersWorkshop() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Cornucopia/The Jester's Workshop.dog"));
		testGame(setup, options);
	}

	@Test
	public void testTheSpiceOfLife() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Cornucopia/The Spice of Life.dog"));
		testGame(setup, options);
	}

	@Test
	public void testIntroduction() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Introduction.dog"));
		testGame(setup, options);
	}

	@Test
	public void testAdventuresAbroad() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Adventures Abroad.dog"));
		testGame(setup, options);
	}

	@Test
	public void testBargains() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Bargains.dog"));
		testGame(setup, options);
	}

	@Test
	public void testBlueHarvest() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Blue Harvest.dog"));
		testGame(setup, options);
	}

	@Test
	public void testDiplomacy() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Diplomacy.dog"));
		testGame(setup, options);
	}

	@Test
	public void testFairTrades() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Fair Trades.dog"));
		testGame(setup, options);
	}

	@Test
	public void testGambits() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Gambits.dog"));
		testGame(setup, options);
	}

	@Test
	public void testHighwayRobbery() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Highway Robbery.dog"));
		testGame(setup, options);
	}

	@Test
	public void testInstantGratification() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Instant Gratification.dog"));
		testGame(setup, options);
	}

	@Test
	public void testMoneyForNothing() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Money for Nothing.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testSchemesAndDreams() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Schemes and Dreams.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testTheDukesBall() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/The Duke's Ball.dog"));
		testGame(setup, options);
	}

	@Test
	public void testTravellers() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Travellers.dog"));
		testGame(setup, options);
	}

	@Test
	public void testTravellingCircus() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Travelling Circus.dog"));
		testGame(setup, options);
	}

	@Test
	public void testTreasureTrove() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Treasure Trove.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testWineCountry() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Hinterlands/Wine Country.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testChivalryAndRevelry() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Chivalry and Revelry.dog"));
		testGame(setup, options);
	}

	@Test
	public void testDarkCarnival() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Dark Carnival.dog"));
		testGame(setup, options);
	}

	@Test
	public void testExpeditions() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Expeditions.dog"));
		testGame(setup, options);
	}

	@Test
	public void testFarFromHome() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Far From Home.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testGrimParade() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Grim Parade.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testHighAndLow() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/High and Low.dog"));
		testGame(setup, options);
	}

	@Test
	public void testHonorAmongThieves() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Honor Among Thieves.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testInfestations() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Infestations.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testInvasion() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Invasion.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testLamentations() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Lamentations.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testOneMansTrash() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/One Man's Trash.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testPeasants() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Peasants.dog"));
		testGame(setup, options);
	}

	@Test
	public void testPlayingChessWithDeath() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Playing Chess With Death.dog"));
		testGame(setup, options);
	}

	@Test
	public void testProphecy() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Prophecy.dog"));
		testGame(setup, options);
	}

	@Test
	public void testToTheVictor() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/To the Victor.dog"));
		testGame(setup, options);
	}

	@Test
	public void testWateryGraves() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Dark Ages/Watery Graves.dog"));
		testGame(setup, options);
	}
	
//	@Test TODO
//	public void testCemeteryPolka() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Cemetery Polka.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testExpertIntro() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Expert Intro.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testGentleIntro() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Gentle Intro.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testGiftsAndMathoms() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Adventures/Gifts and Mathoms.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testGroovyDecay() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Groovy Decay.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testJourneys() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Journeys.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testLastWillAndMonument() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Adventures/Last Will and Monument.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testLevelUp() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Level Up.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testMastersOfFinance() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Adventures/Masters of Finance.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testPrinceOfOrange() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Prince of Orange.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testQueenOfTan() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Queen of Tan.dog"));
//		testGame(setup, options);
//	}

	@Test
	public void testRoyaltyFactory() throws IOException {
		GameOptions options = prepGameOptions();
		GameSetup setup = new GameSetup(new File("Saves/Adventures/Royalty Factory.dog"));
		testGame(setup, options);
	}

//	@Test TODO
//	public void testSeacraftAndWitchcraft() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Seacraft and Witchcraft.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testSonOfSizeDistortion() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Son of Size Distortion.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testSpendthrift() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Spendthrift.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testTheHerosReturn() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/The Hero's Return.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testThinkBig() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Think Big.dog"));
//		testGame(setup, options);
//	}

//	@Test TODO
//	public void testTradersAndRaiders() throws IOException {
//		GameOptions options = prepGameOptions();
//		GameSetup setup = new GameSetup(new File("Saves/Adventures/Traders and Raiders.dog"));
//		testGame(setup, options);
//	}


	
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
	 * Runs a repeatable, local DominionGame simulation.
	 * @param setup the GameSetup describing the game.
	 * @param gameOptions the GameOptions describing the game.
	 */
	private void testGame(GameSetup setup, GameOptions gameOptions) {
		for(int i = 0; i < 100; i++) {
			DominionGame game = new DominionGame(setup, gameOptions);
			game.random.setSeed(i);
			DominionClient dc = new DominionClient();
			new LocalConnection(dc, game);
			game.getCurrentPlayer().startTurn();
		}
	} 
	
}
