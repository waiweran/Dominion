package computerPlayer;

import java.util.HashMap;
import java.util.Map;

import cards.Card;
import cards.adventures.Amulet;
import cards.adventures.Artificer;
import cards.adventures.BridgeTroll;
import cards.adventures.CaravanGuard;
import cards.adventures.DistantLands;
import cards.adventures.Dungeon;
import cards.adventures.Duplicate;
import cards.adventures.Gear;
import cards.adventures.Guide;
import cards.adventures.Hireling;
import cards.adventures.LostCity;
import cards.adventures.Magpie;
import cards.adventures.Messenger;
import cards.adventures.Page;
import cards.adventures.Port;
import cards.adventures.Ratcatcher;
import cards.adventures.Raze;
import cards.adventures.RoyalCarriage;
import cards.adventures.Storyteller;
import cards.adventures.Transmogrify;
import cards.adventures.WineMerchant;
import cards.base.Adventurer;
import cards.base.Bureaucrat;
import cards.base.Cellar;
import cards.base.Chancellor;
import cards.base.Chapel;
import cards.base.CouncilRoom;
import cards.base.Feast;
import cards.base.Festival;
import cards.base.Laboratory;
import cards.base.Library;
import cards.base.Market;
import cards.base.Militia;
import cards.base.Mine;
import cards.base.Moat;
import cards.base.Moneylender;
import cards.base.Remodel;
import cards.base.Smithy;
import cards.base.Spy;
import cards.base.Thief;
import cards.base.ThroneRoom;
import cards.base.Village;
import cards.base.Witch;
import cards.base.Woodcutter;
import cards.base.Workshop;
import cards.cornucopia.FarmingVillage;
import cards.cornucopia.FortuneTeller;
import cards.cornucopia.Hamlet;
import cards.cornucopia.Harvest;
import cards.cornucopia.HorseTraders;
import cards.cornucopia.HuntingParty;
import cards.cornucopia.Jester;
import cards.cornucopia.Menagerie;
import cards.cornucopia.Remake;
import cards.cornucopia.Tournament;
import cards.cornucopia.YoungWitch;
import cards.darkAges.Altar;
import cards.darkAges.Armory;
import cards.darkAges.BandOfMisfits;
import cards.darkAges.BanditCamp;
import cards.darkAges.Beggar;
import cards.darkAges.Catacombs;
import cards.darkAges.Count;
import cards.darkAges.Cultist;
import cards.darkAges.DameAnna;
import cards.darkAges.DameJosephine;
import cards.darkAges.DameMolly;
import cards.darkAges.DameNatalie;
import cards.darkAges.DameSylvia;
import cards.darkAges.DeathCart;
import cards.darkAges.Forager;
import cards.darkAges.Fortress;
import cards.darkAges.Graverobber;
import cards.darkAges.Hermit;
import cards.darkAges.HuntingGrounds;
import cards.darkAges.Ironmonger;
import cards.darkAges.JunkDealer;
import cards.darkAges.Knight;
import cards.darkAges.Marauder;
import cards.darkAges.Mystic;
import cards.darkAges.Pillage;
import cards.darkAges.PoorHouse;
import cards.darkAges.Procession;
import cards.darkAges.Rats;
import cards.darkAges.Rebuild;
import cards.darkAges.Rogue;
import cards.darkAges.Sage;
import cards.darkAges.Scavenger;
import cards.darkAges.SirBailey;
import cards.darkAges.SirDestry;
import cards.darkAges.SirMartin;
import cards.darkAges.SirMichael;
import cards.darkAges.SirVander;
import cards.darkAges.Squire;
import cards.darkAges.Storeroom;
import cards.darkAges.Urchin;
import cards.darkAges.Vagrant;
import cards.darkAges.WanderingMinstrel;
import cards.extra.AbandonedMine;
import cards.extra.BagOfGold;
import cards.extra.Champion;
import cards.extra.Followers;
import cards.extra.Hero;
import cards.extra.Madman;
import cards.extra.Mercenary;
import cards.extra.Necropolis;
import cards.extra.Princess;
import cards.extra.RuinedLibrary;
import cards.extra.RuinedMarket;
import cards.extra.RuinedVillage;
import cards.extra.Survivors;
import cards.extra.TreasureHunter;
import cards.extra.TrustySteed;
import cards.extra.Warrior;
import cards.guilds.Advisor;
import cards.guilds.Journeyman;
import cards.guilds.Soothsayer;
import cards.guilds.Taxman;
import cards.hinterlands.BorderVillage;
import cards.hinterlands.Cartographer;
import cards.hinterlands.Crossroads;
import cards.hinterlands.Develop;
import cards.hinterlands.Duchess;
import cards.hinterlands.Embassy;
import cards.hinterlands.Haggler;
import cards.hinterlands.Highway;
import cards.hinterlands.Inn;
import cards.hinterlands.JackOfAllTrades;
import cards.hinterlands.Mandarin;
import cards.hinterlands.Margrave;
import cards.hinterlands.NobleBrigand;
import cards.hinterlands.NomadCamp;
import cards.hinterlands.Oasis;
import cards.hinterlands.Oracle;
import cards.hinterlands.Scheme;
import cards.hinterlands.SpiceMerchant;
import cards.hinterlands.Stables;
import cards.hinterlands.Trader;
import cards.intrigue.Baron;
import cards.intrigue.Bridge;
import cards.intrigue.Conspirator;
import cards.intrigue.Coppersmith;
import cards.intrigue.Courtyard;
import cards.intrigue.GreatHall;
import cards.intrigue.Ironworks;
import cards.intrigue.Masquerade;
import cards.intrigue.MiningVillage;
import cards.intrigue.Minion;
import cards.intrigue.Nobles;
import cards.intrigue.Pawn;
import cards.intrigue.Saboteur;
import cards.intrigue.Scout;
import cards.intrigue.SecretChamber;
import cards.intrigue.ShantyTown;
import cards.intrigue.Steward;
import cards.intrigue.Swindler;
import cards.intrigue.Torturer;
import cards.intrigue.TradingPost;
import cards.intrigue.Tribute;
import cards.intrigue.Upgrade;
import cards.intrigue.WishingWell;
import cards.promo.Envoy;
import cards.promo.WalledVillage;
import cards.prosperity.Bishop;
import cards.prosperity.City;
import cards.prosperity.CountingHouse;
import cards.prosperity.Expand;
import cards.prosperity.Forge;
import cards.prosperity.Goons;
import cards.prosperity.GrandMarket;
import cards.prosperity.KingsCourt;
import cards.prosperity.Mint;
import cards.prosperity.Monument;
import cards.prosperity.Mountebank;
import cards.prosperity.Peddler;
import cards.prosperity.Rabble;
import cards.prosperity.TradeRoute;
import cards.prosperity.Vault;
import cards.prosperity.Watchtower;
import cards.prosperity.WorkersVillage;
import cards.seaside.Ambassador;
import cards.seaside.Bazaar;
import cards.seaside.Caravan;
import cards.seaside.Cutpurse;
import cards.seaside.Embargo;
import cards.seaside.Explorer;
import cards.seaside.FishingVillage;
import cards.seaside.GhostShip;
import cards.seaside.Haven;
import cards.seaside.Island;
import cards.seaside.Lighthouse;
import cards.seaside.Lookout;
import cards.seaside.MerchantShip;
import cards.seaside.NativeVillage;
import cards.seaside.Navigator;
import cards.seaside.Outpost;
import cards.seaside.PearlDiver;
import cards.seaside.PirateShip;
import cards.seaside.Salvager;
import cards.seaside.SeaHag;
import cards.seaside.Smugglers;
import cards.seaside.Tactician;
import cards.seaside.TreasureMap;
import cards.seaside.Treasury;
import cards.seaside.Warehouse;
import cards.seaside.Wharf;

/**
 * Stores information about cards for the AI to use
 * @author Nathaniel
 * @version 12-17-2019
 */
public class CardProperties {
	
	private Map<Card, Boolean> plusAction;

	/**
	 * Initializes the tables of card properties
	 */
	public CardProperties() {
		makeActionTable();
	}
	
	/**
	 * Determines whether an action card gives the player an additional action
	 */
	public boolean givesAction(Card c) {
		return plusAction.get(c);
	}

	
	private void makeActionTable() {
		plusAction = new HashMap<>();
		plusAction.put(new Adventurer(), false);
		plusAction.put(new Bureaucrat(), false);
		plusAction.put(new Cellar(), true);
		plusAction.put(new Chancellor(), false);
		plusAction.put(new Chapel(), false);
		plusAction.put(new CouncilRoom(), false);
		plusAction.put(new Feast(), false);
		plusAction.put(new Festival(), true);
		plusAction.put(new Laboratory(), true);
		plusAction.put(new Library(), false);
		plusAction.put(new Market(), true);
		plusAction.put(new Militia(), false);
		plusAction.put(new Mine(), false);
		plusAction.put(new Moat(), false);
		plusAction.put(new Moneylender(), false);
		plusAction.put(new Remodel(), false);
		plusAction.put(new Smithy(), false);
		plusAction.put(new Spy(), true);
		plusAction.put(new Thief(), false);
		plusAction.put(new ThroneRoom(), false);
		plusAction.put(new Village(), true);
		plusAction.put(new Witch(), false);
		plusAction.put(new Woodcutter(), false);
		plusAction.put(new Workshop(), false);

		//Intrigue
		plusAction.put(new Baron(), false);
		plusAction.put(new Bridge(), false);
		plusAction.put(new Conspirator(), false);
		plusAction.put(new Coppersmith(), false);
		plusAction.put(new Courtyard(), false);
		plusAction.put(new GreatHall(), true);
		plusAction.put(new Ironworks(), false);
		plusAction.put(new Masquerade(), false);
		plusAction.put(new MiningVillage(), true);
		plusAction.put(new Minion(), true);
		plusAction.put(new Nobles(), false);
		plusAction.put(new Pawn(), false);
		plusAction.put(new Saboteur(), false);
		plusAction.put(new Scout(), true);
		plusAction.put(new SecretChamber(), false);
		plusAction.put(new ShantyTown(), true);
		plusAction.put(new Steward(), false);
		plusAction.put(new Swindler(), false); 
		plusAction.put(new Torturer(), false);
		plusAction.put(new TradingPost(), false);
		plusAction.put(new Tribute(), false);
		plusAction.put(new Upgrade(), true);
		plusAction.put(new WishingWell(), true);

		//Seaside
		plusAction.put(new Ambassador(), false);
		plusAction.put(new Bazaar(), true);
		plusAction.put(new Caravan(), true);
		plusAction.put(new Cutpurse(), false);
		plusAction.put(new Embargo(), false);
		plusAction.put(new Explorer(), false);
		plusAction.put(new FishingVillage(), true);
		plusAction.put(new GhostShip(), false);
		plusAction.put(new Haven(), true);
		plusAction.put(new Island(), false);
		plusAction.put(new Lighthouse(), true);
		plusAction.put(new Lookout(), true);
		plusAction.put(new MerchantShip(), false);
		plusAction.put(new NativeVillage(), true);
		plusAction.put(new Navigator(), false);
		plusAction.put(new Outpost(), false);
		plusAction.put(new PearlDiver(), true);
		plusAction.put(new PirateShip(), false);
		plusAction.put(new Salvager(), false);
		plusAction.put(new SeaHag(), false);
		plusAction.put(new Smugglers(), false);
		plusAction.put(new Tactician(), false);
		plusAction.put(new TreasureMap(), false);
		plusAction.put(new Treasury(), true);
		plusAction.put(new Warehouse(), true);
		plusAction.put(new Wharf(), false);

		//Alchemy
		//plusAction.put(new Alchemist(), true); 		78	Potion
		//plusAction.put(new Apothecary(), true); 		79	Potion
		//plusAction.put(new Apprentice(), true); 		80*
		//plusAction.put(new Familiar(), true); 		81	Potion
		//plusAction.put(new Golem(), false); 			82	Potion
		//plusAction.put(new Herbalist(), false); 		83	
		//plusAction.put(new Posession(), false); 		85*	Potion
		//plusAction.put(new ScryingPool(), true); 		86	Potion
		//plusAction.put(new Transmute(), false); 		87	Potion
		//plusAction.put(new University(), true); 		88	Potion

		//Prosperity
		plusAction.put(new Bishop(), false);
		plusAction.put(new City(), true);
		plusAction.put(new CountingHouse(), false);
		plusAction.put(new Expand(), false);	
		plusAction.put(new Forge(), false);
		plusAction.put(new Goons(), false);
		plusAction.put(new GrandMarket(), true);
		plusAction.put(new KingsCourt(), false);
		plusAction.put(new Mint(), false);
		plusAction.put(new Monument(), false);
		plusAction.put(new Mountebank(), false);
		plusAction.put(new Peddler(), true);
		plusAction.put(new Rabble(), false);
		plusAction.put(new TradeRoute(), false);
		plusAction.put(new Vault(), false);
		plusAction.put(new Watchtower(), false);
		plusAction.put(new WorkersVillage(), true);

		//Cornucopia
		plusAction.put(new FarmingVillage(), true);
		plusAction.put(new FortuneTeller(), false);
		plusAction.put(new Hamlet(), true);
		plusAction.put(new Harvest(), false);
		plusAction.put(new HorseTraders(), false);
		plusAction.put(new HuntingParty(), true);
		plusAction.put(new Jester(), false);
		plusAction.put(new Menagerie(), true);
		plusAction.put(new Remake(), false);
		plusAction.put(new Tournament(), true);
		plusAction.put(new YoungWitch(), false);	//Not Fully Working

		//Hinterlands
		plusAction.put(new BorderVillage(), true);
		plusAction.put(new Cartographer(), true);
		plusAction.put(new Crossroads(), false);
		plusAction.put(new Develop(), false);
		plusAction.put(new Duchess(), false);
		plusAction.put(new Embassy(), false);
		plusAction.put(new Haggler(), false);
		plusAction.put(new Highway(), true);
		plusAction.put(new Inn(), true);
		plusAction.put(new JackOfAllTrades(), false);
		plusAction.put(new Mandarin(), false);
		plusAction.put(new Margrave(), true);
		plusAction.put(new NobleBrigand(), false);
		plusAction.put(new NomadCamp(), false);
		plusAction.put(new Oasis(), true);
		plusAction.put(new Oracle(), false);
		plusAction.put(new Scheme(), true);
		plusAction.put(new SpiceMerchant(), false);
		plusAction.put(new Stables(), false);
		plusAction.put(new Trader(), false);

		//Dark Ages
		plusAction.put(new Altar(), false);
		plusAction.put(new Armory(), false);
		plusAction.put(new BanditCamp(), true);
		plusAction.put(new BandOfMisfits(), false);	
		plusAction.put(new Beggar(), false);
		plusAction.put(new Catacombs(), false);
		plusAction.put(new Count(), false);
		plusAction.put(new Cultist(), false);
		plusAction.put(new DeathCart(), false);
		plusAction.put(new Forager(), true);	
		plusAction.put(new Fortress(), true);
		plusAction.put(new Graverobber(), false);
		plusAction.put(new Hermit(), false);
		plusAction.put(new HuntingGrounds(), false);
		plusAction.put(new Ironmonger(), true);
		plusAction.put(new JunkDealer(), true);
		plusAction.put(new Knight(), false);
		plusAction.put(new Marauder(), false);
		//plusAction.put(new MarketSquare(), true);		174*
		plusAction.put(new Mystic(), true);
		plusAction.put(new Pillage(), false);
		plusAction.put(new PoorHouse(), false);
		plusAction.put(new Procession(), false);
		plusAction.put(new Rats(), true);
		plusAction.put(new Rebuild(), true);
		plusAction.put(new Rogue(), false);
		plusAction.put(new Sage(), true);
		plusAction.put(new Scavenger(), false);
		plusAction.put(new Squire(), false);
		plusAction.put(new Storeroom(), false);
		plusAction.put(new Urchin(), true);
		plusAction.put(new Vagrant(), true);
		plusAction.put(new WanderingMinstrel(), true);	

		//Guilds
		plusAction.put(new Advisor(), true);
		//plusAction.put(new Baker(), true);			190	 Coin Tokens
		//plusAction.put(new Butcher(), false);			191	 Coin Tokens
		//plusAction.put(new CandlestickMaker(), true);	192	 Coin Tokens
		//plusAction.put(new Doctor(), false);			193	 Overpay
		//plusAction.put(new Herald(), true);			194	 Overpay
		plusAction.put(new Journeyman(), false);
		//plusAction.put(new MerchantGuild(), false);	197	 Coin Tokens
		//plusAction.put(new Plaza(), true);			198	 Coin Tokens
		plusAction.put(new Soothsayer(), false);
		//plusAction.put(new Stonemason(), false);		200	 Overpay
		plusAction.put(new Taxman(), false);

		//Adventures
		plusAction.put(new Amulet(), false);
		plusAction.put(new Artificer(), true);
		plusAction.put(new BridgeTroll(), false);
		plusAction.put(new CaravanGuard(), true);
		plusAction.put(new DistantLands(), false);
		plusAction.put(new Dungeon(), true);
		plusAction.put(new Duplicate(), false);
		plusAction.put(new Gear(), false);
		//plusAction.put(new Giant(), false);			211	 Journey Token
		plusAction.put(new Guide(), true);
		//plusAction.put(new HauntedWoods(), false);	213*
		plusAction.put(new Hireling(), false);
		plusAction.put(new LostCity(), true);
		plusAction.put(new Magpie(), true);
		plusAction.put(new Messenger(), false);
		//plusAction.put(new Miser(), false);			218*
		plusAction.put(new Page(), true);
		//plusAction.put(new Peasant(), false);			220  Upgrades
		plusAction.put(new Port(), true);
		//plusAction.put(new Ranger(), false);			222	 Journey Token
		plusAction.put(new Ratcatcher(), true);
		plusAction.put(new Raze(), true);
		plusAction.put(new RoyalCarriage(), true);
		plusAction.put(new Storyteller(), true);
		//plusAction.put(new SwampHag(), false);		228*
		plusAction.put(new Transmogrify(), true);
		plusAction.put(new WineMerchant(), false);

		//Promo
		//plusAction.put(new BlackMarket(), false);		232*
		plusAction.put(new Envoy(), false);
		//plusAction.put(new Governor(), true);			234
		//plusAction.put(new Prince(), false);			235*
		plusAction.put(new WalledVillage(), true);


		//Extra Cards
		plusAction.put(new Necropolis(), true);
		plusAction.put(new AbandonedMine(), false);
		plusAction.put(new RuinedLibrary(), false);
		plusAction.put(new RuinedMarket(), false);
		plusAction.put(new RuinedVillage(), true);
		plusAction.put(new Survivors(), false);
		plusAction.put(new Madman(), true);
		plusAction.put(new Mercenary(), false);
		plusAction.put(new DameAnna(), false);
		plusAction.put(new DameJosephine(), false);
		plusAction.put(new DameMolly(), true);
		plusAction.put(new DameNatalie(), false);
		plusAction.put(new DameSylvia(), false);
		plusAction.put(new SirBailey(), true);
		plusAction.put(new SirDestry(), false);
		plusAction.put(new SirMartin(), false);
		plusAction.put(new SirMichael(), false);
		plusAction.put(new SirVander(), false);
		plusAction.put(new BagOfGold(), true);
		plusAction.put(new Followers(), false);
		plusAction.put(new Princess(), false);
		plusAction.put(new TrustySteed(), false);
		plusAction.put(new TreasureHunter(), true);
		plusAction.put(new Warrior(), false);
		plusAction.put(new Hero(), false);
		plusAction.put(new Champion(), true);
		//plusAction.put(new Soldier(), false);			-42
		//plusAction.put(new Fugitive(), true);			-43
		//plusAction.put(new Disciple(), false);		-44
		//plusAction.put(new Teacher(), false);			-45

	}

}
