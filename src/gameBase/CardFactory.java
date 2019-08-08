package gameBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cards.Card;
import cards.adventures.Amulet;
import cards.adventures.Artificer;
import cards.adventures.BridgeTroll;
import cards.adventures.CaravanGuard;
import cards.adventures.CoinOfTheRealm;
import cards.adventures.DistantLands;
import cards.adventures.Dungeon;
import cards.adventures.Duplicate;
import cards.adventures.Gear;
import cards.adventures.Guide;
import cards.adventures.Hireling;
import cards.adventures.LostCity;
import cards.adventures.Magpie;
import cards.adventures.Page;
import cards.adventures.Port;
import cards.adventures.Ratcatcher;
import cards.adventures.Relic;
import cards.adventures.RoyalCarriage;
import cards.adventures.Transmogrify;
import cards.adventures.TreasureTrove;
import cards.adventures.WineMerchant;
import cards.alchemy.PhilosophersStone;
import cards.alchemy.Vineyard;
import cards.base.Adventurer;
import cards.base.Bureaucrat;
import cards.base.Cellar;
import cards.base.Chancellor;
import cards.base.Chapel;
import cards.base.CouncilRoom;
import cards.base.Feast;
import cards.base.Festival;
import cards.base.Gardens;
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
import cards.cornucopia.Fairgrounds;
import cards.cornucopia.FarmingVillage;
import cards.cornucopia.FortuneTeller;
import cards.cornucopia.Hamlet;
import cards.cornucopia.Harvest;
import cards.cornucopia.HornOfPlenty;
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
import cards.darkAges.Counterfeit;
import cards.darkAges.Cultist;
import cards.darkAges.DameAnna;
import cards.darkAges.DameJosephine;
import cards.darkAges.DameMolly;
import cards.darkAges.DameNatalie;
import cards.darkAges.DameSylvia;
import cards.darkAges.DeathCart;
import cards.darkAges.Feodum;
import cards.darkAges.Forager;
import cards.darkAges.Fortress;
import cards.darkAges.GenericKnight;
import cards.darkAges.Graverobber;
import cards.darkAges.Hermit;
import cards.darkAges.HuntingGrounds;
import cards.darkAges.Ironmonger;
import cards.darkAges.JunkDealer;
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
import cards.defaults.Colony;
import cards.defaults.Copper;
import cards.defaults.Curse;
import cards.defaults.Duchy;
import cards.defaults.Estate;
import cards.defaults.Gold;
import cards.defaults.Platinum;
import cards.defaults.Province;
import cards.defaults.Silver;
import cards.defaults.Trash;
import cards.extra.AbandonedMine;
import cards.extra.BagOfGold;
import cards.extra.Champion;
import cards.extra.Diadem;
import cards.extra.Followers;
import cards.extra.Hero;
import cards.extra.Hovel;
import cards.extra.Madman;
import cards.extra.Mercenary;
import cards.extra.Necropolis;
import cards.extra.OvergrownEstate;
import cards.extra.Potion;
import cards.extra.Princess;
import cards.extra.RuinedLibrary;
import cards.extra.RuinedMarket;
import cards.extra.RuinedVillage;
import cards.extra.Ruins;
import cards.extra.Spoils;
import cards.extra.Survivors;
import cards.extra.TreasureHunter;
import cards.extra.TrustySteed;
import cards.extra.Warrior;
import cards.guilds.Advisor;
import cards.guilds.Journeyman;
import cards.guilds.Soothsayer;
import cards.guilds.Taxman;
import cards.hinterlands.BorderVillage;
import cards.hinterlands.Cache;
import cards.hinterlands.Cartographer;
import cards.hinterlands.Crossroads;
import cards.hinterlands.Develop;
import cards.hinterlands.Duchess;
import cards.hinterlands.Embassy;
import cards.hinterlands.Farmland;
import cards.hinterlands.FoolsGold;
import cards.hinterlands.Haggler;
import cards.hinterlands.Highway;
import cards.hinterlands.IllGottenGains;
import cards.hinterlands.Inn;
import cards.hinterlands.JackOfAllTrades;
import cards.hinterlands.Mandarin;
import cards.hinterlands.Margrave;
import cards.hinterlands.NobleBrigand;
import cards.hinterlands.NomadCamp;
import cards.hinterlands.Oasis;
import cards.hinterlands.Oracle;
import cards.hinterlands.Scheme;
import cards.hinterlands.SilkRoad;
import cards.hinterlands.SpiceMerchant;
import cards.hinterlands.Stables;
import cards.hinterlands.Trader;
import cards.hinterlands.Tunnel;
import cards.intrigue.Baron;
import cards.intrigue.Bridge;
import cards.intrigue.Conspirator;
import cards.intrigue.Coppersmith;
import cards.intrigue.Courtyard;
import cards.intrigue.Duke;
import cards.intrigue.GreatHall;
import cards.intrigue.Harem;
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
import cards.prosperity.Bank;
import cards.prosperity.Bishop;
import cards.prosperity.City;
import cards.prosperity.Contraband;
import cards.prosperity.CountingHouse;
import cards.prosperity.Expand;
import cards.prosperity.Forge;
import cards.prosperity.Goons;
import cards.prosperity.GrandMarket;
import cards.prosperity.Hoard;
import cards.prosperity.KingsCourt;
import cards.prosperity.Loan;
import cards.prosperity.Mint;
import cards.prosperity.Monument;
import cards.prosperity.Mountebank;
import cards.prosperity.Peddler;
import cards.prosperity.Quarry;
import cards.prosperity.Rabble;
import cards.prosperity.RoyalSeal;
import cards.prosperity.Talisman;
import cards.prosperity.TradeRoute;
import cards.prosperity.Vault;
import cards.prosperity.Venture;
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
 * Creates cards from indecies or names.
 * @author Nathaniel Brooke
 * @version 03-11-2017
 */
public class CardFactory implements Serializable {
	
	private static final long serialVersionUID = 7579100486856870428L;
	
	public ArrayList<Card> allCards;

	/**
	 * Initializes the CardFactory.
	 */
	public CardFactory() {
		initializeCardList();
	}
	
	/**
	 * Gets a card at the specified index.
	 * @param index the index of the card.
	 * @return the Card at the index.
	 */
	public Card getCardAt(int index) {
		return allCards.get(index);
	}
	
	/**
	 * Gets the index of the given card.
	 * @param c the Card to find the index of.
	 * @return the index of that card.
	 */
	public int getCardNum(Card c) {
		return allCards.indexOf(c);
	}
	
	/**
	 * Gets the total number of different Dominion cards.
	 * @return total number of cards.
	 */
	public int getNumCards() {
		return allCards.size();
	}
	
	/**
	 * Gets the list of all cards.
	 * @return Unmodifiable list of all cards in the game.
	 */
	public List<Card> getAllCards() {
		return Collections.unmodifiableList(allCards);
	}
	
	public Card findCard(String name) {
		for(Card c : allCards) {
			if(c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Initializes a list of every card in the game.
	 */
	private void initializeCardList() {
		allCards = new ArrayList<Card>(271);

		//Base Set
		allCards.add(new Adventurer());
		allCards.add(new Bureaucrat());
		allCards.add(new Cellar());
		allCards.add(new Chancellor());
		allCards.add(new Chapel());
		allCards.add(new CouncilRoom());
		allCards.add(new Feast());
		allCards.add(new Festival());
		allCards.add(new Gardens());
		allCards.add(new Laboratory());
		allCards.add(new Library());
		allCards.add(new Market());
		allCards.add(new Militia());
		allCards.add(new Mine());
		allCards.add(new Moat());
		allCards.add(new Moneylender());
		allCards.add(new Remodel());
		allCards.add(new Smithy());
		allCards.add(new Spy());
		allCards.add(new Thief());
		allCards.add(new ThroneRoom());
		allCards.add(new Village());
		allCards.add(new Witch());
		allCards.add(new Woodcutter());
		allCards.add(new Workshop());

		//Intrigue
		allCards.add(new Baron());
		allCards.add(new Bridge());
		allCards.add(new Conspirator());
		allCards.add(new Coppersmith());
		allCards.add(new Courtyard());
		allCards.add(new Duke());
		allCards.add(new GreatHall());
		allCards.add(new Harem());
		allCards.add(new Ironworks());
		allCards.add(new Masquerade());
		allCards.add(new MiningVillage());
		allCards.add(new Minion());
		allCards.add(new Nobles());
		allCards.add(new Pawn());
		allCards.add(new Saboteur());
		allCards.add(new Scout());
		allCards.add(new SecretChamber());
		allCards.add(new ShantyTown());
		allCards.add(new Steward());
		allCards.add(new Swindler()); 
		allCards.add(new Torturer());
		allCards.add(new TradingPost());
		allCards.add(new Tribute());
		allCards.add(new Upgrade());
		allCards.add(new WishingWell());

		//Seaside
		allCards.add(new Ambassador());
		allCards.add(new Bazaar());
		allCards.add(new Caravan());
		allCards.add(new Cutpurse());
		allCards.add(new Embargo());
		allCards.add(new Explorer());
		allCards.add(new FishingVillage());
		allCards.add(new GhostShip());
		allCards.add(new Haven());
		allCards.add(new Island());
		allCards.add(new Lighthouse());
		allCards.add(new Lookout());
		allCards.add(new MerchantShip());
		allCards.add(new NativeVillage());
		allCards.add(new Navigator());
		allCards.add(new Outpost());
		allCards.add(new PearlDiver());
		allCards.add(new PirateShip());
		allCards.add(new Salvager());
		allCards.add(new SeaHag());
		allCards.add(new Smugglers());
		allCards.add(new Tactician());
		allCards.add(new TreasureMap());
		allCards.add(new Treasury());
		allCards.add(new Warehouse());
		allCards.add(new Wharf());

		//Alchemy
		allCards.add(new Trash());		//allCards.add(new Alchemist()); 		78	Potion
		allCards.add(new Trash());		//allCards.add(new Apothecary()); 		79	Potion
		allCards.add(new Trash());		//allCards.add(new Apprentice()); 		80*
		allCards.add(new Trash());		//allCards.add(new Familiar()); 		81	Potion
		allCards.add(new Trash());		//allCards.add(new Golem()); 			82	Potion
		allCards.add(new Trash());		//allCards.add(new Herbalist()); 		83	
		allCards.add(new PhilosophersStone());
		allCards.add(new Trash());		//allCards.add(new Posession()); 		85*	Potion
		allCards.add(new Trash());		//allCards.add(new ScryingPool()); 		86	Potion
		allCards.add(new Trash());		//allCards.add(new Transmute()); 		87	Potion
		allCards.add(new Trash());		//allCards.add(new University()); 		88	Potion
		allCards.add(new Vineyard());

		//Prosperity
		allCards.add(new Bank());
		allCards.add(new Bishop());
		allCards.add(new City());
		allCards.add(new Contraband());	
		allCards.add(new CountingHouse());
		allCards.add(new Expand());	
		allCards.add(new Forge());
		allCards.add(new Goons());
		allCards.add(new GrandMarket());
		allCards.add(new Hoard());
		allCards.add(new KingsCourt());
		allCards.add(new Loan());
		allCards.add(new Mint());
		allCards.add(new Monument());
		allCards.add(new Mountebank());
		allCards.add(new Peddler());
		allCards.add(new Quarry());
		allCards.add(new Rabble());
		allCards.add(new RoyalSeal());
		allCards.add(new Talisman());
		allCards.add(new TradeRoute());
		allCards.add(new Vault());
		allCards.add(new Venture());
		allCards.add(new Watchtower());
		allCards.add(new WorkersVillage());

		//Cornucopia
		allCards.add(new Fairgrounds());
		allCards.add(new FarmingVillage());
		allCards.add(new FortuneTeller());
		allCards.add(new Hamlet());
		allCards.add(new Harvest());
		allCards.add(new HornOfPlenty());
		allCards.add(new HorseTraders());
		allCards.add(new HuntingParty());
		allCards.add(new Jester());
		allCards.add(new Menagerie());
		allCards.add(new Remake());
		allCards.add(new Tournament());
		allCards.add(new YoungWitch());	//Not Fully Working

		//Hinterlands
		allCards.add(new BorderVillage());
		allCards.add(new Cache());
		allCards.add(new Cartographer());
		allCards.add(new Crossroads());
		allCards.add(new Develop());
		allCards.add(new Duchess());
		allCards.add(new Embassy());
		allCards.add(new Farmland());
		allCards.add(new FoolsGold());	//Not Fully Working						136*
		allCards.add(new Haggler());
		allCards.add(new Highway());
		allCards.add(new IllGottenGains());	
		allCards.add(new Inn());
		allCards.add(new JackOfAllTrades());
		allCards.add(new Mandarin());
		allCards.add(new Margrave());
		allCards.add(new NobleBrigand());
		allCards.add(new NomadCamp());	//Not Fully Working						145*
		allCards.add(new Oasis());
		allCards.add(new Oracle());
		allCards.add(new Scheme());
		allCards.add(new SilkRoad());
		allCards.add(new SpiceMerchant());
		allCards.add(new Stables());
		allCards.add(new Trader());
		allCards.add(new Tunnel());

		//Dark Ages
		allCards.add(new Altar());
		allCards.add(new Armory());
		allCards.add(new BanditCamp());
		allCards.add(new BandOfMisfits());	
		allCards.add(new Beggar());
		allCards.add(new Catacombs());
		allCards.add(new Count());
		allCards.add(new Counterfeit());
		allCards.add(new Cultist());
		allCards.add(new DeathCart());
		allCards.add(new Feodum());
		allCards.add(new Forager());	
		allCards.add(new Fortress());
		allCards.add(new Graverobber());
		allCards.add(new Hermit());
		allCards.add(new HuntingGrounds());
		allCards.add(new Ironmonger());
		allCards.add(new JunkDealer());
		allCards.add(new GenericKnight());
		allCards.add(new Marauder());
		allCards.add(new Trash());		//allCards.add(new MarketSquare());		174*
		allCards.add(new Mystic());
		allCards.add(new Pillage());
		allCards.add(new PoorHouse());
		allCards.add(new Procession());
		allCards.add(new Rats());
		allCards.add(new Rebuild());
		allCards.add(new Rogue());
		allCards.add(new Sage());
		allCards.add(new Scavenger());
		allCards.add(new Squire());
		allCards.add(new Storeroom());
		allCards.add(new Urchin());
		allCards.add(new Vagrant());
		allCards.add(new WanderingMinstrel());	

		//Guilds
		allCards.add(new Advisor());
		allCards.add(new Trash());		//allCards.add(new Baker());			190	 Coin Tokens
		allCards.add(new Trash());		//allCards.add(new Butcher());			191	 Coin Tokens
		allCards.add(new Trash());		//allCards.add(new CandlestickMaker());	192	 Coin Tokens
		allCards.add(new Trash());		//allCards.add(new Doctor());			193	 Overpay
		allCards.add(new Trash());		//allCards.add(new Herald());			194	 Overpay
		allCards.add(new Journeyman());
		allCards.add(new Trash());		//allCards.add(new Masterpiece());		196	 Overpay
		allCards.add(new Trash());		//allCards.add(new MerchantGuild());	197	 Coin Tokens
		allCards.add(new Trash());		//allCards.add(new Plaza());			198	 Coin Tokens
		allCards.add(new Soothsayer());
		allCards.add(new Trash());		//allCards.add(new Stonemason());		200	 Overpay
		allCards.add(new Taxman());

		//Adventures
		allCards.add(new Amulet());
		allCards.add(new Artificer());
		allCards.add(new BridgeTroll());
		allCards.add(new CaravanGuard());
		allCards.add(new CoinOfTheRealm());
		allCards.add(new DistantLands());
		allCards.add(new Dungeon());
		allCards.add(new Duplicate());
		allCards.add(new Gear());
		allCards.add(new Trash());		//allCards.add(new Giant());			211	 Journey Token
		allCards.add(new Guide());
		allCards.add(new Trash());		//allCards.add(new HauntedWoods());		213*
		allCards.add(new Hireling());
		allCards.add(new LostCity());
		allCards.add(new Magpie());
		allCards.add(new Trash());		//allCards.add(new Messenger());		217
		allCards.add(new Trash());		//allCards.add(new Miser());			218*
		allCards.add(new Page());
		allCards.add(new Trash());		//allCards.add(new Peasant());			220  Upgrades
		allCards.add(new Port());
		allCards.add(new Trash());		//allCards.add(new Ranger());			222	 Journey Token
		allCards.add(new Ratcatcher());
		allCards.add(new Trash());		//allCards.add(new Raze());				224
		allCards.add(new Relic());
		allCards.add(new RoyalCarriage());
		allCards.add(new Trash());		//allCards.add(new Storyteller());		227
		allCards.add(new Trash());		//allCards.add(new SwampHag());			228*
		allCards.add(new Transmogrify());
		allCards.add(new TreasureTrove());
		allCards.add(new WineMerchant());

		//Promo
		allCards.add(new Trash());		//allCards.add(new BlackMarket());		232*
		allCards.add(new Envoy());
		allCards.add(new Trash());		//allCards.add(new Governor());			234
		allCards.add(new Trash());		//allCards.add(new Prince());			235*
		allCards.add(new Trash());		//allCards.add(new Stash());			236*
		allCards.add(new WalledVillage());

		//Events do not go here

		//Default Cards
		allCards.add(new Copper());
		allCards.add(new Silver());
		allCards.add(new Gold());
		allCards.add(new Platinum());
		allCards.add(new Estate());
		allCards.add(new Duchy());
		allCards.add(new Province());
		allCards.add(new Colony());
		allCards.add(new Curse());

		//Extra Cards
		allCards.add(new Spoils());
		allCards.add(new Potion());
		allCards.add(new OvergrownEstate());
		allCards.add(new Necropolis());
		allCards.add(new Hovel());
		allCards.add(new AbandonedMine());
		allCards.add(new RuinedLibrary());
		allCards.add(new RuinedMarket());
		allCards.add(new RuinedVillage());
		allCards.add(new Survivors());
		allCards.add(new Ruins());
		allCards.add(new Madman());
		allCards.add(new Mercenary());
		allCards.add(new DameAnna());
		allCards.add(new DameJosephine());
		allCards.add(new DameMolly());
		allCards.add(new DameNatalie());
		allCards.add(new DameSylvia());
		allCards.add(new SirBailey());
		allCards.add(new SirDestry());
		allCards.add(new SirMartin());
		allCards.add(new SirMichael());
		allCards.add(new SirVander());
		allCards.add(new BagOfGold());
		allCards.add(new Diadem());
		allCards.add(new Followers());
		allCards.add(new Princess());
		allCards.add(new TrustySteed());
		allCards.add(new TreasureHunter());
		allCards.add(new Warrior());
		allCards.add(new Hero());
		allCards.add(new Champion());
		allCards.add(new Trash());		//allCards.add(new Soldier());			-42
		allCards.add(new Trash());		//allCards.add(new Fugitive());			-43
		allCards.add(new Trash());		//allCards.add(new Disciple());			-44
		allCards.add(new Trash());		//allCards.add(new Teacher());			-45
	}

}
