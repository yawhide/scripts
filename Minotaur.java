package scripts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Screen;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Pausing;
import org.tribot.api2007.ext.Doors;

import scripts.easyslayer.Timer;
import scripts.easyslayer.YawhideHelper;


@ScriptManifest(authors = { "Yaw hide" }, version = 1.14, category = "Combat", name = "MinotaurSlayer")
public class Minotaur extends Script implements Painting, Ending, Pausing{
	
	//food Ids 
	//int[] foodID = {333, 379};
	int[] foodID = { 333, 329, 379, 361, 7946, 1897 };
	int[] junk = { 1539, 9003, 229, 1623, 1355, 440, 7767, 117,
			6963, 829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180,
			6965, 1969, 6183, 6181, 1237, 1155, 1454, 882, 526 };
	
	int[] loot = {563, 560, 561, 562, 564, 565, 566, 7937, 816, 1747, 536, 9142, 868, 563,
			1615, 1319, 1373, 1247, 1303, 1249, 1123, 1149, 1201,
			1186, 1113, 1079, 892, 565, 560, 561, 563, 2361, 2366, 443, 
			985, 987, 2363, 1617, 1619, 574,
			/**** herbs ****/
			207, 257, 208, 258, 2481, 2482, 2485,
			2486, 209, 210, 211, 212, 213, 214, 217, 218, 219, 220,
			3049, 3050, 3051, 3052,
			/****seeds ****/
			5295, 5302, 5286, 5289, 5288, 
			5289, 5290, 5296, 5297, 5298,
			5299, 5300, 5301, 5303, 5304,
			5321,
			/****stackables ****/
			554, 557, 556, 555, 884//, 437, 439
	};
	
	String[] names = {"Law rune", "Death rune", "Nature rune", "Chaos rune", "Cosmic rune", 
			"Blood rune", "Soul rune", "Pure essence", "Adamant dart(p)",
			"Black dragonhide", "Dragon bones", "Mithril bolts",
			"Rune knife", "Law rune", "Dragonstone",
			"Rune 2h sword", "Rune battleaxe",
			"Rune spear", "Rune longsword", "Dragon spear",
			"Adamant platebody", "Dragon med helm", 
			"Rune kiteshield", "Rune sq shield", "Rune chainbody",
			"Rune platelegs", "Rune arrow", "Blood rune", "Death rune",
			"Nature rune", "Law rune", "Adamantite bar", "Shield left half",
			"Silver ore", "Tooth half of key", "Loop half of key",
			"Runite bar", "Uncut diamond", "Uncut ruby", "Air orb",
			
			/**** herbs ****/
			"Herb", "Herb", "Herb", "Herb", "Herb", "Herb",
			"Herb", "Herb", "Herb", "Herb", "Herb", "Herb",	"Herb", "Herb", "Herb", "Herb", "Herb", "Herb",
			"Herb", "Herb", "Herb", "Herb",
			/****seeds ****/
			"Ranarr seed", "Lantadyme seed", "Curry tree seed", "Pineapple seed", "Papaya tree seed", 
			"Palm tree seed", "Calquat tree seed", "Toadflax seed", "Irit seed", "Avantoe seed",
			"Kwuarm seed", "Snapdragon seed", "Cadantine seed", "Dwarf weed seed", "Torstol seed",
			"Watermelon seed",
			/****stackables ****/
			"Fire rune", "Earth rune", "Air rune", "Water rune", "Iron arrow"//, "Copper ore", "Tin ore"
	}; 
	
	HashMap<Integer, String> map = new HashMap<Integer, String>(loot.length);
	
	//slayer items
	int slayGem = 4155;
	int COINS = 995;
	int VTAB = 8007;
	int FTAB = 8009;
	int CTAB = 8010;
	int LTAB = 8008;
	int ATAB = 8011;
	int EARMUFFS = 4166;
	int TINDER = 590;
	int SALT = 4161;
	int SPINY = 4551;
	int LAW = 563;
	int EARTH = 557;
	int AIR = 556;
	int WATER = 555;
	int FIRE = 554;
	int[] WATERSKIN = {1823, 1825, 1827, 1829};
	int COOLER = 6696;
	int ECTO = 4251;
	int rope = 954;
	int SHANTY = 1854;
	
	//kalphites 40exp
	
	RSTile shantyBankTile = new RSTile(3309, 3120, 0);
	
	//antiban stuff
	private long antiban = System.currentTimeMillis();
	//private int[] evilchicken = { 2465, 2467 };
	//private int swarm = 407;
	
	// Slayer teleport items
	int[] gamesNecklace = {3853, 3855, 3857, 3859, 3861, 3863, 3865, 3867}; // goes from 8 to 1
	int[] rod = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};
	int[] antiPoison = {2448, 181, 183, 185, 193, 2446, 175, 177, 179}; // start with super 4-1
	int[] lightsources = {4531, 4539, 4550};
	int[] depositAllExcept = { 3853, 3855, 3857, 3859, 3861, 3863, 3865, slayGem, VTAB, FIRE, LAW, AIR};
	
	
	String lastServerMessage;
	int numLeftToKill;
	String currTask;
	boolean script = true;
	boolean isLooting = true;
	
	@Override
	public void run() {
		
		
		onStart();
		
		currTask = "minotaurs";
		
		if (isAtTask()){
			println("I am at my task");
			FIGHT();
		}
		else{
			println("I am NOT at my task");
			GOTO_TASK();
		}
		
		while(script){
			currTask = "minotaurs";
			current_xp = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.STRENGTH) 
			+ Skills.getXP(SKILLS.ATTACK) + Skills.getXP(SKILLS.DEFENCE) + Skills.getXP(SKILLS.RANGED);
			current_xpSlayer = Skills.getXP(SKILLS.SLAYER);
			XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
			XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
			XpToLVstr = Skills.getXPToNextLevel(SKILLS.STRENGTH);
			XpToLVatt = Skills.getXPToNextLevel(SKILLS.ATTACK);
			XpToLVdef = Skills.getXPToNextLevel(SKILLS.DEFENCE);
			XpToLVslayer = Skills.getXPToNextLevel(SKILLS.SLAYER);
			
			currLv = Skills.getActualLevel(SKILLS.SLAYER);
			if(currLv > startLv){
				lvsGained = Math.abs(currLv - startLv);
			}
			else if(Inventory.find(junk).length > 0){
				Inventory.drop(junk);
				sleep(200,300);
			}
			else if(Inventory.find(foodID).length == 0 && getHp() < 30){
				if(inArea(varrockWBankArea[0], varrockWBankArea[1], pos())){
					
				}
				else if(inArea(varrockArea[0], varrockArea[1], pos())){
					WebWalking.walkToBank();
				}
				else{
					emergTele();
					script = false;
				}
			}
			else if(getHp() < 30){
				eatFood();
			}
			else if (isAtTask()){
				FIGHT();
			}
			else{
				GOTO_TASK();
			}
			
			sleep(50,100);
		}
		
	}
	
	 
	
	public boolean waitGUI = true;
	public boolean LootEverythingIncludingNonStacks = true;
	public boolean LootOnlyStacksAndHighValueDrops = false;
	public boolean LootOnlyHighValueDrops = false;
	public boolean DoNotLoot = false;
	public boolean UseRunesToTele = false;
	public boolean PureMode = false;
	
	//TODO onStart
	public void onStart(){
			
		Mouse.setSpeed(General.random(130, 140));
		sleep(200,300);
		Walking.setControlClick(true);
		Walking.setWalkingTimeout(5000L);
		putMap();
		sleep(200, 250);
		tin = Inventory.getCount(439);
		copper = Inventory.getCount(437);
		ess = Inventory.getCount(7937);
		ironArr = Inventory.getCount(884);
		sleep(100,140);
	}
	
	public void useGameNeck(){
		state="Using games Neck";
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		sleep(200,300);
		
		RSItem[] gamesNeck = Inventory.find(gamesNecklace);
		if (gamesNeck.length > 0){
			
			if (gamesNeck[0].click("Rub")){
				sleep(3500,4750);
				RSInterface gamebox = Interfaces.get(230, 1);
				if (gamebox != null){
					gamebox.click("Continue");
					sleep(5200,5250);
				}
			}
		}
	}
	//TODO useROD
	public void useROD(){
		state = "Using ROD";
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		sleep(200,300);
		
		RSItem[] ROD = Inventory.find(rod);
		
		if (ROD.length > 0){
			
			if (ROD[0].click("Rub")){
				sleep(2500,2750);
				RSInterface ringbox = Interfaces.get(230, 1);
				if (ringbox != null){
					
					ringbox.click("Continue");
					sleep(4200,4250);
				}
			}
		}
	}
	
	// area RSTiles for gotoTurael
	RSTile[] gamesNeckTeleSpot = {new RSTile(2203, 4942, 0), new RSTile(2214, 4936, 0)};
	RSTile gamesNeckStairsObj = new RSTile(2207, 4935, 0);
	
	RSTile[] middleStairs = {new RSTile(2204, 4938, 1), new RSTile(2209, 4933, 1)};
	RSTile middleStairsObj = new RSTile(2205, 4935, 1);
	
	RSTile[] mainCastle = {new RSTile(2892, 3569, 0), new RSTile(2904, 3558, 0)};
	RSTile innerDoorsTile1 = new RSTile(2898, 3558, 0);
	RSTile innerDoorsTile2 = new RSTile(2898, 3561, 0);
			/*
	private RSTile[] toTuraelPath = { new RSTile(2899, 3553, 0), new RSTile(2903, 3546, 0), 
			new RSTile(2913, 3546, 0), new RSTile(2921, 3542, 0), new RSTile(2928, 3536, 0) };
	*/
	RSTile stairAtGameNeckTele =  new RSTile(2207, 4935, 0);
	
	public void gotoTurael(){
		state = "Going to Turael";
		RSItem[] gamesNeck = Inventory.find(gamesNecklace);
		RSObject[] GAMESNECKSTAIROBJ = Objects.getAt(stairAtGameNeckTele);
		Mouse.setSpeed(General.random(130,150));
		
		if(inArea(gamesNeckTeleSpot[0], gamesNeckTeleSpot[1], pos())){
						
			if (GAMESNECKSTAIROBJ.length > 0){
				if(GAMESNECKSTAIROBJ[0].isOnScreen()){
					if(clickOBJ(GAMESNECKSTAIROBJ[0], "Climb-up"))
						waitIsMovin();
				}
				else
					Walking.blindWalkTo(gamesNeckStairsObj);
			}
				
		}
		else if (inArea(middleStairs[0], middleStairs[1], pos())){
			RSObject[] MIDDLESTAIRSOBJ = Objects.getAt(middleStairsObj);
			if (MIDDLESTAIRSOBJ.length > 0 && clickOBJ(MIDDLESTAIRSOBJ[0], "Climb-up"))
				waitIsMovin();
		}
		else if (inArea(mainCastle[0], mainCastle[1], pos())){
			
			if(openDoor(innerDoorsTile1)){ 
				WebWalking.walkTo(turaelT);
				waitIsMovin();
			}
			else{
				Walking.walkTo(innerDoorsTile2);
				waitIsMovin();
			}
		}
		else{
			if (gamesNeck.length > 0){
				
				if (!inCombat()){
					println("Going to rub games Necklace");
					useGameNeck();
				}
			}
		}
	}
		
	
	// goto zombie task, uses similarities in toSkeletons method
	private RSTile[] toZombies = { new RSTile(3105, 9909, 0), new RSTile(3115, 9909, 0), 
			new RSTile(3126, 9909, 0), new RSTile(3131, 9911, 0), new RSTile(3139, 9907, 0), 
			new RSTile(3141, 9900, 0), new RSTile(3139, 9889, 0), new RSTile(3147, 9883, 0) };
	
	
	
	RSTile zombieMiddle = new RSTile(3145, 9886, 0);	

	
	// goto lumby bank
	//private RSTile[] toStairs1F = { new RSTile(3221, 3218, 0), new RSTile(3214, 3214, 0), new RSTile(3209, 3211, 0) };
	RSTile toStaiRSTile = new RSTile(3204, 3207, 0);
	RSTile toStairs1FTile = new RSTile(3204, 3207, 1);
	RSTile toStairs2FTile = new RSTile(3204, 3207, 2);
	RSTile lumbyBankTile = new RSTile(3208, 3218, 2);
	
	RSTile[] lumby2FArea = { new RSTile(3205, 3228, 2), new RSTile(3215, 3208, 2) };
	RSTile[] lumby1FArea = { new RSTile(3205, 3228, 1), new RSTile(3215, 3208, 1) };
	RSTile[] lumbyArea = { new RSTile(3205, 3228, 0), new RSTile(3225, 3208, 0) };
	
	
	// goto varrock bank after teleporting
	private RSTile[] toVWBankTeleport = { new RSTile(3212, 3423, 0), new RSTile(3202, 3428, 0), 
			new RSTile(3190, 3429, 0), new RSTile(3184, 3436, 0) };
	RSTile[] varrockWBankArea = {new RSTile(3185, 3433, 0), new RSTile(3180, 3441, 0)};
	
	public void gotoVWBankTelespot(){
		
		Walking.walkPath(toVWBankTeleport);
	}
	
	
	RSTile kalphiteMiddle = new RSTile(3506, 9522, 2);
	RSTile edgeLadderTile = new RSTile(3097, 9867, 0);
	private RSTile[] toGate = { new RSTile(3096, 9867, 0), new RSTile(3096, 9877, 0), 
			new RSTile(3095, 9887, 0), new RSTile(3095, 9895, 0), new RSTile(3096, 9904, 0),
			new RSTile(3102, 9909, 0)};
	private RSTile[] toSkeletons = { new RSTile(3105, 9909, 0), new RSTile(3115, 9909, 0), 
			new RSTile(3126, 9909, 0), new RSTile(3131, 9911, 0) };
	RSTile edgeDungeonGateTile = new RSTile(3103, 9909, 0);
	RSTile[] toEdgeGateArea = {new RSTile(3094, 9915, 0), new RSTile(3103, 9867, 0)};
	RSTile[] afterEdgeGateArea = {new RSTile(3104, 9917, 0), new RSTile(3151,9870, 0)};
	RSTile[] skeletonArea = {new RSTile(3104, 9917, 0), new RSTile(3141, 9141, 0)};
	RSTile skeletonMiddle = new RSTile(3131, 9911, 0);	
	RSTile[] zombieArea = {new RSTile(3136, 9910, 0), new RSTile(3151, 9876, 0)};
	RSTile beforeIceFiends = new RSTile(3031, 3473, 0);

	
	/***** killing da monstas *****/
	//TODO FIGHT
	public void FIGHT(){
		
		state = "Fight";
		
			if (minoArea.contains(pos())) {
				Mouse.setSpeed(General.random(130, 140));
				if(isLooting && GroundItems.findNearest(loot).length > 0 && checkLootInArea(GroundItems.findNearest(loot)[0]))
					LOOT();
				else{
					attackInactiveMonster("Minotaur");
				}
				Mouse.setSpeed(General.random(130, 140));
			} 
			else {
				Walking.walkPath(Walking.generateStraightPath(minotaurT));
				waitIsMovin();
			}
		
		
	}
	

	//TODO LOOT()
		public void LOOT(){
			RSGroundItem[] Nests = GroundItems.findNearest(loot);
			
			if (!Inventory.isFull() && Nests.length > 0) {
				if(checkLootInArea(Nests[0])){
					if (!Nests[0].isOnScreen()) {
						Walking.clickTileMM(Nests[0].getPosition(), 1);
						sleep(100, 150);
						Camera.turnToTile(Nests[0].getPosition());
						sleep(200, 300);
						waitIsMovin();
					}
					String str = map.get(Nests[0].getID());
					Nests[0].click("Take " + str);
					waitForInv(Nests[0].getID());
				}
			}
		}
	
	/***** City Areas *****/
	RSTile[] alkharidArea = {new RSTile(3265, 3322, 0), new RSTile(3324, 3117, 0) };
	RSTile[] desertArea = {new RSTile(3223, 3115, 0), new RSTile(3430, 3030, 0) };
	
	
	RSTile bansheesT = new RSTile(3442, 3545, 0); //673
	RSTile[] bansheeArea = { new RSTile(3431, 3567, 0), new RSTile(3453, 3531, 0)};
	RSTile[] slayerTowerArea = {new RSTile(3405, 3579, 0), new RSTile(3453, 3532, 0)};
	
	RSTile slayerTowerEntranceT = new RSTile(3428, 3535, 0);
	Positionable slayerTowerEntranceP = slayerTowerEntranceT;
	RSTile crawlingHandsT = new RSTile(3417, 3557, 0); //714
	
	RSTile[] betweenCanafisEctoArea = {new RSTile(3475, 3537, 0), new RSTile(3665, 3470, 0)};
	RSTile[] canafisArea = {new RSTile(3470, 3510, 0), new RSTile(3520, 3466, 0)};
	RSTile[] betweenCanafisSlayerTowerArea = {new RSTile(3400, 3537, 0), new RSTile(3475, 3470, 0)};
	RSTile[] slayerTowerDoorArea = {new RSTile(3426, 3535, 0), new RSTile(3431, 3533, 0)};
	RSTile[] awkwardBansheeSq = {new RSTile(3430, 3546, 0), new RSTile(3436, 3541, 0)};
	RSTile[] awkwardBansheeSqPath = { new RSTile(3429, 3543, 0), new RSTile(3428, 3551, 0), new RSTile(3422, 3556, 0), 
				new RSTile(3414, 3557, 0), new RSTile(3412, 3563, 0), new RSTile(3412, 3571, 0) };
	
	
	RSTile lumbySwampT = new RSTile(3169, 3173, 0);
	RSTile[] toSlimes = { new RSTile(3165, 9573, 0), new RSTile(3156, 9573, 0), 
			new RSTile(3148, 9571, 0), new RSTile(3149, 9565, 0), new RSTile(3153, 9556, 0), 
			new RSTile(3153, 9554, 0) };
	RSTile caveSlimeT = new RSTile(3153, 9554, 0);
	RSTile[] toLumbySwampArea = {new RSTile(3160, 3223, 0), new RSTile(3248, 3165, 0)};
	RSTile[] swampArea = {new RSTile(3144, 9578, 0), new RSTile(3170, 9543, 0)};
	RSTile[] caveSlimeArea = {new RSTile(9147, 9554, 0), new RSTile(9546, 9543, 0)};
	RSTile[] caveSlimeArea2 = {new RSTile(3145, 9567, 0), new RSTile(3163, 9551, 0)};
	
	RSTile lumbySwampEntranceT = new RSTile(3169, 3172, 0);
	RSTile afterLumbySwampEntranceT = new RSTile(3169, 9571, 0);
	RSTile[] toCaveSlimeT = {new RSTile(3165, 9573, 0), new RSTile(3157, 9573, 0), 
			new RSTile(3147, 9567, 0), new RSTile(3152, 9561, 0), new RSTile(3153, 9554, 0)};
	RSTile nearedgeTrapdoorTile = new RSTile(3094, 3471, 0);
	
	RSTile[] caveBugArea = {new RSTile(3144, 9583, 0), new RSTile(3160, 9568, 0)};
	RSTile[] toCaveBug = {new RSTile(3165, 9573, 0), new RSTile(3157, 9573, 0), new RSTile(3151, 9573, 0)};
	
	RSTile renekSlayerT = new RSTile(2796, 3614, 0);
	RSTile afterCaveEntrance = new RSTile(2807, 10001, 0);
	int CaveEntrance = 2292; // Enter
	RSTile caveEntranceT = new RSTile(2796, 3615, 0);
	RSTile renekSlayCave = new RSTile(2797, 3614, 0);
	RSTile[] toCCinside = { new RSTile(2804, 10001, 0), new RSTile(2798, 9997, 0), new RSTile(2792, 9996, 0),
			new RSTile(2788, 9996, 0) };
	RSTile[] toRenekSlayCaveArea = {new RSTile(2633, 3650, 0), new RSTile(2810, 3480, 0)};
	RSTile[] toRenekSlayCave = { new RSTile(2752, 3477, 0), new RSTile(2746, 3479, 0), new RSTile(2740, 3480, 0), 
			new RSTile(2731, 3484, 0), new RSTile(2724, 3483, 0), new RSTile(2715, 3484, 0), 
			new RSTile(2706, 3484, 0), new RSTile(2699, 3484, 0), new RSTile(2692, 3484, 0), 
			new RSTile(2687, 3486, 0), new RSTile(2687, 3492, 0), new RSTile(2687, 3499, 0), 
			new RSTile(2687, 3504, 0), new RSTile(2687, 3509, 0), new RSTile(2686, 3514, 0), 
			new RSTile(2684, 3518, 0), new RSTile(2682, 3523, 0), new RSTile(2681, 3528, 0), 
			new RSTile(2679, 3533, 0), new RSTile(2678, 3538, 0), new RSTile(2678, 3546, 0), 
			new RSTile(2675, 3550, 0), new RSTile(2673, 3554, 0), new RSTile(2668, 3557, 0), 
			new RSTile(2662, 3559, 0), new RSTile(2660, 3565, 0), new RSTile(2658, 3570, 0), 
			new RSTile(2654, 3576, 0), new RSTile(2653, 3582, 0), new RSTile(2653, 3588, 0), 
			new RSTile(2654, 3597, 0), new RSTile(2655, 3603, 0), new RSTile(2659, 3607, 0), 
			new RSTile(2666, 3607, 0), new RSTile(2670, 3607, 0), new RSTile(2677, 3609, 0), 
			new RSTile(2685, 3609, 0), new RSTile(2691, 3609, 0), new RSTile(2699, 3608, 0), 
			new RSTile(2708, 3608, 0), new RSTile(2716, 3607, 0), new RSTile(2725, 3606, 0), 
			new RSTile(2733, 3606, 0), new RSTile(2739, 3603, 0), new RSTile(2747, 3601, 0), 
			new RSTile(2754, 3599, 0), new RSTile(2761, 3598, 0), new RSTile(2770, 3595, 0), 
			new RSTile(2778, 3595, 0), new RSTile(2786, 3597, 0), new RSTile(2789, 3602, 0), 
			new RSTile(2792, 3609, 0), new RSTile(2795, 3615, 0) };
	RSTile[] caveCrawlerArea = {new RSTile(2775, 10006, 0), new RSTile(2810, 9985, 0)};
	
	
	
	RSTile caveCrawlerT = new RSTile(2788, 9995, 0);
	String[] wereWolves = {"Yuri", "Zoja", "Lev", "Svetlana", "Boris", "Irina", "Sofiya", 
			"Yadviga", "Nikita", "Fidelio", "Joseph", "Georgy"};
	RSTile wereWolvesT = new RSTile(3494, 3489, 0);
	RSTile duelAreaGateCloseT = new RSTile(3312, 3234, 0); //3197
	RSTile scorpionT = new RSTile(3298, 3194, 0);

	
	RSTile shantyPassT = new RSTile(3303, 3117, 0);
	RSTile[] afterShantyPassArea = {new RSTile(3297, 3115, 0), new RSTile(3315, 3106, 0)};
	RSTile bankChestShantyT = new RSTile(3308, 3120, 0); //id is 2693
	
	RSTile camelotTeleSpot = new RSTile(2757, 3476, 0);
	RSTile lumbyTeleT = new RSTile(3223, 3219, 0);
	RSTile varrockTeleT = new RSTile(3211, 3424, 0);
	RSTile ardougneTeleT = new RSTile(2661, 3303, 0);
	
	RSTile[] ectoTeleArea = {new RSTile(3651, 3529, 0), new RSTile(3667, 3510, 0)};
	
	
	RSTile[] rodTeleSpotArea = {new RSTile(3312, 3239, 0), new RSTile(3318, 3231, 0)};
	RSTile rodGateTile = new RSTile(3312, 3234, 0);
	
	
	RSTile[] toKalphite = { new RSTile(3304, 3113, 0), new RSTile(3297, 3107, 0), 
			new RSTile(3290, 3107, 0), new RSTile(3282, 3106, 0), new RSTile(3272, 3107, 0), 
			new RSTile(3262, 3108, 0), new RSTile(3256, 3108, 0), new RSTile(3248, 3108, 0), 
			new RSTile(3239, 3105, 0), new RSTile(3232, 3103, 0), new RSTile(3227, 3107, 0) };
	RSTile tunnelTile = new RSTile(3227, 3108, 0);
	RSTile inTunnel = new RSTile(3484, 9510, 2);
	
	RSTile[] kalphiteTunnel = {new RSTile(3475, 9530, 2), new RSTile(3515, 9506, 2) };
	RSTile[] toKalphitePart2 = { new RSTile(3488, 9510, 2), new RSTile(3495, 9509, 2),
			new RSTile(3498, 9515, 2), new RSTile(3503, 9520, 2), new RSTile(3507, 9522, 2) };
	
	// desert lizards
	RSTile[] toDesertLizards = { new RSTile(3305, 3111, 0), new RSTile(3308, 3107, 0), 
			new RSTile(3310, 3101,0), new RSTile(3315, 3098, 0), new RSTile(3322, 3094, 0), 
			new RSTile(3330, 3092, 0), new RSTile(3338, 3090, 0), new RSTile(3345, 3089, 0), 
			new RSTile(3352, 3086, 0), new RSTile(3360, 3083, 0), new RSTile(3366, 3082, 0), 
			new RSTile(3373, 3082, 0), new RSTile(3382, 3081, 0), new RSTile(3388, 3076, 0),
			new RSTile(3392, 3069, 0), new RSTile(3398, 3066, 0), new RSTile(3405, 3062, 0) };
	
	RSTile manHoleT = new RSTile(3237, 3457, 0);
	RSTile afterManHoleT = new RSTile(3237, 3457, 0);
	RSTile manHole = new RSTile(3237, 3458, 0);
	RSTile ratsMiddle =  new RSTile(3237, 9867, 0);
	
	RSTile dwarfMiddle = new RSTile(3011, 3451, 0);
	RSTile[] dwarfArea = { new RSTile(3989, 3475, 0), new RSTile(3070, 3415, 0)}; 
	RSTile[] trickyDwarf = { new RSTile(3021, 3455, 0), new RSTile(3025, 3445, 0)}; 
	RSTile[] dwarfAttackArea = { new RSTile(3003, 3462, 0), new RSTile(3020, 3442, 0)}; 
	RSTile[] trickyDwarf1 =  {new RSTile(3014, 3452, 0), new RSTile(3014, 3454, 0), new RSTile(3010, 3452, 0), 
			new RSTile(3010, 3449, 0), new RSTile(3008, 3449, 0), new RSTile(3008, 3454, 0) };
	RSTile[] trickyDwarf2 = { new RSTile(3017, 3454, 0), new RSTile(3017, 3452, 0), new RSTile(3021, 3452, 0), 
			new RSTile(3021, 3449, 0), new RSTile(3023, 3450, 0), new RSTile(3023, 3454, 0) };
	
	RSArea trickyDwarf1Area = new RSArea(trickyDwarf1);
	RSArea trickyDwarf2Area = new RSArea(trickyDwarf2);
	
	RSTile faladorTeleT = new RSTile(2965, 3376, 0);
	
	RSTile lowWallT = new RSTile(2935, 3355, 0);
	RSTile lowWall = new RSTile(2938, 3355, 0);
	RSTile afterLowWall = new RSTile(2934, 3355, 0);
	RSTile tavernlyLadder = new RSTile(2884, 3396, 0);
	RSTile tavernlyLadderT = new RSTile(2884, 3397, 0);
	RSTile afterTavernlyLadderT = new RSTile(2884, 9798, 0);
	RSTile[] toGhost = { new RSTile(2884, 9799, 0), new RSTile(2884, 9806, 0), 
			new RSTile(2884, 9814, 0), new RSTile(2884, 9820, 0), new RSTile(2884, 9825, 0), 
			new RSTile(2884, 9834, 0), new RSTile(2884, 9840, 0), new RSTile(2888, 9846, 0), 
			new RSTile(2894, 9848, 0), new RSTile(2898, 9849, 0) };
	RSTile ghostMiddle = new RSTile(2898, 9849, 0);
	
	RSTile[] toBat = { new RSTile(2884, 9799, 0), new RSTile(2884, 9806, 0), 
			new RSTile(2884, 9814, 0), new RSTile(2884, 9820, 0), new RSTile(2884, 9825, 0), 
			new RSTile(2884, 9834, 0), new RSTile(2884, 9840, 0), new RSTile(2888, 9846, 0), 
			new RSTile(2894, 9848, 0), new RSTile(2898, 9849, 0), new RSTile(2904, 9849, 0), 
			new RSTile(2911, 9849, 0), new RSTile(2914, 9841, 0), new RSTile(2914, 9835, 0), 
			new RSTile(2915, 9833, 0), new RSTile(2914, 9828) };
	RSTile batMiddle = new RSTile(2915, 9833, 0);
	RSTile[] toTavernlyDungPath = { new RSTile(2928, 3357, 0), new RSTile(2925, 3363, 0), new RSTile(2921, 3370, 0), 
			new RSTile(2914, 3373, 0), new RSTile(2907, 3377, 0), new RSTile(2901, 3381, 0), 
			new RSTile(2895, 3386, 0), new RSTile(2892, 3391, 0), new RSTile(2885, 3392, 0), 
			new RSTile(2884, 3396, 0) };
	RSTile[] toTavLadderDown = {new RSTile(2877, 3403, 0), new RSTile(2935, 3346, 0)};
	RSTile[] insideTavBatArea = {new RSTile(2880, 9853, 0), new RSTile(2931, 9794, 0)};
	RSTile[] insideTavGhostArea = {new RSTile(2880, 9853, 0), new RSTile(2924, 9794, 0)};
	
	
	RSTile bearsMiddle = new RSTile(2699, 3333, 0);
	
	RSTile dogGate = new RSTile(2636, 3307, 0);
	RSTile dogsMiddle = new RSTile(2635, 3321, 0);
	RSTile[] dogArea1 = {new RSTile(2626, 3315, 0), new RSTile(2642, 3308, 0)};
	RSTile[] dogArea2 = {new RSTile(2624, 3331, 0), new RSTile(2642, 3315, 0)};
	RSTile[] ardyArea = {new RSTile(2602, 3340), new RSTile(2690, 3265, 0) };
	
	
	RSTile[] toWolfArea = { new RSTile(2735, 3495, 0), new RSTile(2866, 3428, 0)};
	RSTile wolfMiddle = new RSTile(2847, 3481, 0);
	RSTile[] wolfArea = { new RSTile(2837, 3491, 0), new RSTile(2850, 3468, 0)};
	
	RSTile varrockBankT = new RSTile(3183, 3437, 0);
	RSTile[] burthorpeArea = {new RSTile(2893, 3566, 0), new RSTile(2937, 3530, 0)};
	RSTile turaelT = new RSTile(2931, 3536, 0);
	RSTile[] varrockArea = {new RSTile(3176, 3448, 0), new RSTile(3255, 3386, 0) };
	RSTile[] toStrongholdArea = {new RSTile(3074, 3440, 0), new RSTile(3180, 3408, 0) };
	RSTile strongHoldHole = new RSTile(3081, 3420, 0);
	RSTile teleCenterT = new RSTile(1863, 5238, 0);
	RSTile[] afterPortal = {new RSTile(1905, 5229, 0), new RSTile(1916, 5210, 0) };
	RSTile[] afterPortal2 = {new RSTile(1911, 5209, 0), new RSTile(1912, 5207, 0) };
	RSTile[] afterPortal3 = {new RSTile(1907, 5206, 0), new RSTile(1912, 5202, 0) };
	RSTile[] afterPortal4 = {new RSTile(1904, 5204, 0), new RSTile(1906, 5203, 0) };
	RSTile[] afterPortal5 = {new RSTile(1894, 5213, 0), new RSTile(1896, 5212, 0) };
	RSTile[] afterPortal6 = {new RSTile(1879, 5189, 0), new RSTile(1881, 5188, 0) };
	
	RSTile strongHoldDoor = new RSTile(1911, 5209, 0);
	RSTile strongHoldDoor2 = new RSTile(1911, 5206, 0);
	RSTile strongHoldDoor3 = new RSTile(1907, 5204, 0);
	RSTile strongHoldDoor4 = new RSTile(1904, 5204, 0);
	RSTile strongHoldDoor5 = new RSTile(1897, 5212, 0);
	RSTile strongHoldDoor6 = new RSTile(1882, 5188, 0);
	RSTile minotaurT = new RSTile(1898, 5196, 0);
	
	
	RSTile[] minotaurArea = { new RSTile(1903, 5195, 0), new RSTile(1908, 5192, 0),
			new RSTile(1908, 5186, 0), new RSTile(1887, 5185, 0),
			new RSTile(1886, 5193, 0), new RSTile(1889, 5197, 0),
			new RSTile(1893, 5200, 0), new RSTile(1899, 5200, 0),
			new RSTile(1899, 5212, 0), new RSTile(1903, 5212, 0)};
	RSArea minoArea = new RSArea(minotaurArea);
	
	
	RSTile[] pathToDoor1 = {new RSTile(1912, 5215,0), new RSTile(1912, 5210, 0)};
	RSTile afterStrongHoldHole = new RSTile(1859, 5243, 0);
	
	RSTile goblinDoor = new RSTile(3246, 3244, 0);
	RSTile goblinMiddle = new RSTile(3249, 3240, 0);
	RSTile[] goblinArea = {new RSTile(3239, 3254, 0), new RSTile(3267, 3220, 0)};
	RSTile[] goblinHut = {new RSTile(3243, 3248, 0), new RSTile(3248, 3244, 0)};
	
	RSTile iceFiendsMiddle = new RSTile(3004, 3474, 0);
	RSTile[] toIceFieldsArea = {iceFiendsMiddle, toStrongholdArea[1] };
	RSTile[] beforeToIceFiendsArea = {new RSTile(3002, 3488, 0), new RSTile(3033, 3462, 0) };
	RSTile[] beforeDwarfArea = {new RSTile(3000, 3485, 0), new RSTile(3080, 3419, 0) };
	
	RSTile[] edgeTrapDoorArea = {new RSTile(3050, 3486, 0), varrockArea[0] };
	RSTile edgeTrapdoorTile = new RSTile(3097, 3468, 0);
	
	RSTile cowGate = new RSTile(2924, 3292, 0);
	//private RSTile[] fromGateToCows = { new RSTile(3256, 3266, 0), new RSTile(3257, 3274, 0), 
	//		new RSTile(3256, 3279, 0), new RSTile(3254, 3287, 0) };
	RSTile cowsMiddle = new RSTile(2925, 3283, 0);
	
	RSTile inBoat = new RSTile(2956, 3143, 1);
	RSTile afterPortBoat = new RSTile(2958, 3146, 0);
	RSTile monkeyT = new RSTile(2878, 3158, 0);
	RSTile portBoatT = new RSTile(3027, 3217, 0);
	RSTile[] toMonkeyArea = {new RSTile(2861, 3180, 0), new RSTile(2959, 3138, 0) };
	RSTile[] fallyArea = {new RSTile(2936, 3390, 0), new RSTile(3030, 3323, 0) };
	RSTile[] toBoatFromFally = {fallyArea[0], new RSTile(3032, 3203, 0) };
	RSTile[] toMonkey = { new RSTile(2952, 3146, 0), new RSTile(2946, 3146, 0), new RSTile(2939, 3146, 0), 
			new RSTile(2934, 3149, 0), new RSTile(2927, 3151, 0), new RSTile(2920, 3150, 0), 
			new RSTile(2913, 3151, 0), new RSTile(2909, 3153, 0), new RSTile(2904, 3154, 0), 
			new RSTile(2899, 3156, 0), new RSTile(2894, 3155, 0), new RSTile(2888, 3153, 0), 
			new RSTile(2883, 3155, 0), new RSTile(2879, 3158, 0) };
	
	RSTile[] toGStrongholdArea = {new RSTile(2447, 3388, 0), ardyArea[1]};
	RSTile strongholdDoorT = new RSTile(2461, 3380, 0);
	RSTile afterStrongholdDoorT = new RSTile(2461, 3387, 0);
	
	RSTile birdMiddle = new RSTile(2378, 3433, 0);
	RSTile[] toBirds = { new RSTile(2459, 3393, 0), new RSTile(2453, 3396, 0), new RSTile(2447, 3397, 0), 
			new RSTile(2441, 3398, 0), new RSTile(2433, 3400, 0), new RSTile(2428, 3404, 0), 
			new RSTile(2424, 3410, 0), new RSTile(2418, 3416, 0), new RSTile(2412, 3420, 0), 
			new RSTile(2408, 3426, 0), new RSTile(2402, 3428, 0), new RSTile(2396, 3428, 0),
			new RSTile(2388, 3427, 0), new RSTile(2382, 3429, 0), new RSTile(2378, 3433, 0) };
	RSTile[] gnomeArea = {new RSTile(2373, 3450, 0), new RSTile(2467, 3383, 0)};
	RSTile birdGateS = new RSTile(2380, 3425, 0);
	RSTile birdGateN = new RSTile(2384, 3436, 0);
	
	RSTile[] bearArea = {new RSTile(2602, 3350, 0), new RSTile(2730, 3265, 0) };
	
	//TODO GOTO_TASK
	public boolean GOTO_TASK(){
		state="Going to task";
		Mouse.setSpeed(General.random(130, 140));
		sleep(100,150);
		
		if (currTask.equals( "minotaurs")){ 
			
			if(pos().distanceTo(strongHoldHole) <=5){
				clickRSObjectAt(strongHoldHole, "Climb-down");
			}
			else if(pos().distanceTo(teleCenterT) <=4){
				clickRSObjectAt(teleCenterT, "Use");
				sleep(1000,1200);
			}
			else if (pos().distanceTo(afterStrongHoldHole) <=3){
				Walking.blindWalkTo(teleCenterT);
				waitIsMovin();
			}
			else if (inArea(afterPortal4[0], afterPortal4[1], pos())){
				clickRSObjectAt(strongHoldDoor4, "Open");
				if(chatup())
					chatting();
			}
			else if (inArea(afterPortal3[0], afterPortal3[1], pos())){
				clickRSObjectAt(strongHoldDoor3, "Open");
				if(chatup())
					chatting();				
			}
			else if (inArea(afterPortal2[0], afterPortal2[1], pos())){
				clickRSObjectAt(strongHoldDoor2, "Open");
				if(chatup())
					chatting();
			}
			else if (inArea(afterPortal5[0], afterPortal5[1], pos())){
				clickRSObjectAt(strongHoldDoor5, "Open");
				if(chatup())
					chatting();
			}
			else if (inArea(afterPortal6[0], afterPortal6[1], pos())){
				clickRSObjectAt(strongHoldDoor6, "Open");
				if(chatup())
					chatting();
			}
			else if (pos().distanceTo(strongHoldDoor) <=5){
				clickRSObjectAt(strongHoldDoor, "Open");
				if(chatup())
					chatting();
			}
			
			else if (inArea(afterPortal[0], afterPortal[1], pos())){
				
				Walking.walkPath(pathToDoor1);
				waitIsMovin();
			}
			else if(inArea(toStrongholdArea[0], toStrongholdArea[1], pos()) 
					|| inArea(varrockArea[0], varrockArea[1], pos())){
				WebWalking.walkTo(strongHoldHole);
				waitIsMovin();				
			}
			else if (minoArea.contains(pos()))
				return true;
			
			else
				Walking.walkPath(Walking.generateStraightPath(minotaurT));
		}
		
		return false;
	}
	//TODO isAtTask
	public boolean isAtTask(){
		state = "Check for task area";
		if (currTask.equals( "minotaurs")){ 
			if(minoArea.contains(pos()))
				return true;
		}
		return false;
	}

	boolean useTabV;
	boolean useTabF;
	boolean useTabC;
	boolean useTabL;
	boolean useTabA;
	boolean useTabs;
	
	
	//TODO useTeleport
	public boolean useTeleport(int option){
		state = "Teleporting...";
		RSItem[] vtab = Inventory.find(VTAB);
		RSItem[] ftab = Inventory.find(FTAB);
		RSItem[] ctab = Inventory.find(CTAB);
		RSItem[] ltab = Inventory.find(LTAB);
		RSItem[] atab = Inventory.find(ATAB);
		RSItem[] ecto = Inventory.find(ECTO);
		RSItem[] ROD = Inventory.find(rod);
		RSItem[] games = Inventory.find(gamesNecklace);
		
		int tabOrRune = 1; 
		
		switch (option) {
		case 0:
			if (useTabV) tabOrRune = 0;
			if (tabOrRune == 0) {
				if (vtab.length > 0) {
					if (vtab[0].click("Break")) {
						sleep(3000, 4000);
						return true;
					}
				}
			} 
			else {
				if (YawhideHelper.hasReqTab(useTabs, "VARROCK")){//haveRune(1) && haveRune(2) && haveRune(3)) {
					
					if(GameTab.getOpen() != TABS.MAGIC)
						GameTab.open(TABS.MAGIC);
					sleep(200, 300);
					Magic.selectSpell("Varrock Teleport");
					/*RSInterface vtele = Interfaces.get(192, 15);
					if (vtele.click("Cast Varrock Teleport"))
						sleep(3000, 3500);*/
					sleep(3000, 3500);
					return true;
				}
			}
			break;
		case 1:
			if (useTabF) tabOrRune = 0;
			if (tabOrRune == 0) {
				if (ftab.length > 0) {
					if (ftab[0].click("Break")) {
						sleep(3000, 4000);
						return true;
					}
				}
			} 
			else {
				if (YawhideHelper.hasReqTab(useTabs, "FALADOR")){//haveRune(1) && haveRune(2) && haveRune(4)) {
					GameTab.open(org.tribot.api2007.GameTab.TABS.MAGIC);
					sleep(200, 300);
					Magic.selectSpell("Falador Teleport");/*
					RSInterface ftele = Interfaces.get(192, 21);
					if (ftele.click("Cast Falador Teleport"))
						sleep(3000, 3500);*/
					sleep(3000, 3500);
					return true;
				}
			}
			break;
		case 2:
			if (useTabC) tabOrRune = 0;
			if (tabOrRune == 0) {
				if (ctab.length > 0) {
					if (ctab[0].click("Break")) {
						sleep(3000, 4000);
						return true;
					}
				}
			} 
			else {
				if (YawhideHelper.hasReqTab(useTabs, "CAMELOT")){//haveRune(1) && haveRune(2)) {
					GameTab.open(org.tribot.api2007.GameTab.TABS.MAGIC);
					sleep(200, 300);
					Magic.selectSpell("Camelot Teleport");/*
					RSInterface ctele = Interfaces.get(192, 26);
					if (ctele.click("Cast Camelot Teleport"))
						sleep(3000, 3500);*/
					sleep(3000, 3500);
					return true;
				}
			}
			break;
		case 3:
			if (useTabL) tabOrRune = 0;
			if (tabOrRune == 0) {
				if (ltab.length > 0) {
					if (ltab[0].click("Break")) {
						sleep(3000, 4000);
						return true;
					}
				}
			} 
			else {
				if (YawhideHelper.hasReqTab(useTabs, "LUMBRIDGE")){//haveRune(1) && haveRune(2) && haveRune(0)) {
					GameTab.open(org.tribot.api2007.GameTab.TABS.MAGIC);
					sleep(200, 300);
					Magic.selectSpell("Lumbridge Teleport");/*
					RSInterface ltele = Interfaces.get(192, 18);
					if (ltele.click("Cast Lumbridge Teleport"))
						sleep(3000, 3500);*/
					sleep(3000, 3500);
					return true;
				}
			}
			break;
		case 4:
			if (useTabA) tabOrRune = 0;
			if (tabOrRune == 0) {
				if (atab.length > 0) {
					if (atab[0].click("Break")) {
						sleep(3000, 4000);
						return true;
					}
				}
			} 
			else {
				if (YawhideHelper.hasReqTab(useTabs, "ARDOUGNE")){//haveRune(1) && haveRune(4)) {
					GameTab.open(org.tribot.api2007.GameTab.TABS.MAGIC);
					sleep(200, 300);
					Magic.selectSpell("Ardougne Teleport");/*
					RSInterface atele = Interfaces.get(192, 32);
					if (atele.click("Cast Ardougne Teleport"))
						sleep(3000, 3500);*/
					sleep(3000, 3500);
					return true;
				}
			}
			break;
		case 5:
			if (ecto.length > 0) {
				if (ecto[0].click("Empty")) {
					sleep(7000, 8000);
					waitIsMovin();
					return true;
				}
			}
			break;
		case 6:
			if (ROD.length > 0) {
				useROD();
				return true;
			}
			break;
		case 7:
			if (games.length > 0) {
				useGameNeck();
				return true;
			}
			break;
		}
		return false;
	}
	
	//TODO checkLootInArea
	public boolean checkLootInArea(RSGroundItem loot){
		if(loot.getID() == 884 && loot.getStack() < 5)
			return false;
		RSTile loots = loot.getPosition();
		
		 if (currTask.equals("minotaurs")) {
			if (minoArea.contains(loots))
				return true;
		}


		return false;
	}
	
	
	
	//TODO openDoor
	public boolean openDoor(Positionable doorTile){
		RSObject door = Doors.getDoorAt(doorTile);
		if(door != null){
			if(door.isOnScreen()){
				if(Doors.handleDoor(door,  true)){
					waitIsMovin();
					sleep(1000,1200);
					return true;
				}
			}
			else{
				Walking.walkTo(doorTile);
				Camera.turnToTile(doorTile);
				waitIsMovin();
				sleep(200,300);
				return false;
			}
		}
		return true;
	}
	
	public int isNotFighting(RSNPC[] npc){
		for (int i = 0; i < 57; i++, sleep(30, 40)) {
			if (npc.length > 0 && !npc[0].isInteractingWithMe() && !inCombat() && npc[0].getCombatCycle() <= 0) {
				return 0;
			}
			else if (npc.length > 1 && !npc[1].isInteractingWithMe() && !inCombat() && npc[1].getCombatCycle() <= 0) {
				return 1;
			}
		}
		return -1;
	}

	public void	useAnti(){
		RSItem[] antiP = Inventory.find(antiPoison);
		if(antiP.length > 0){
			if(antiP[0].click("Drink")){
				println("Just drank a dose of antipoison");
				sleep(500,1000);
			}
		}
		
	}
	
	public boolean isFull() {
		// Mouse.setSpeed(General.random(150, 160));
		sleep(5, 7);
		return Inventory.isFull();
	}

	public void cameraDrag() {
		Camera.setCameraRotation(General.random(88, 92));
	}

	public void cameraPortal() {
		Camera.setCameraRotation(General.random(178, 182));
	}

	public void cameraBank() {
		Camera.setCameraRotation(General.random(0, 2));
	}

	public boolean inCombat() {
		sleep(5, 7);
		return Player.getRSPlayer().isInCombat();
	}

	public int getHp() {
		//double x = Skills.getCurrentLevel(SKILLS.HITPOINTS);
		//double y = Skills.getActualLevel(SKILLS.HITPOINTS);
		//return (int) ((x / y) * 100);
		return Combat.getHPRatio();
	}

	public RSTile pos() {
		return Player.getPosition();
	}

	public boolean inArea(RSTile nw, RSTile se, RSTile pos) {

		int posX = pos.getPosition().getX();
		int posY = pos.getPosition().getY();
		int posP = pos.getPosition().getPlane();
		int nwX = nw.getPosition().getX();
		int nwY = nw.getPosition().getY();
		int seX = se.getPosition().getX();
		int seY = se.getPosition().getY();
		int nwP = nw.getPosition().getPlane();
		int seP = se.getPosition().getPlane();		

		if (posX >= nwX && posX <= seX && posY >= seY && posY <= nwY
				&& posP == nwP && posP == seP) {
			return true;
		} else
			return false;
	}

	public void checkXP() {

		GameTab.open(org.tribot.api2007.GameTab.TABS.STATS);
		sleep(80, 100);
		Mouse.moveBox(557, 307, 600, 321);
		sleep(1700, 2200);
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		sleep(80, 100);

	}

	public void checkFriends() {

		GameTab.open(org.tribot.api2007.GameTab.TABS.FRIENDS);
		sleep(80, 100);
		Mouse.moveBox(616, 240, 664, 261);
		sleep(1700, 2200);
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		sleep(80, 100);
	}

	private void ANTIBAN() {

		int x = General.random(1, 2);
		if (x == 1)
			checkXP();
		else
			checkFriends();
		antiban = System.currentTimeMillis();
	}

	private boolean clickNPC(RSNPC npc, String option) {
		//Mouse.setSpeed(General.random(100,120));
		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			loc = npc.getPosition();
			Mouse.move(Projection.tileToScreen(loc, 10));
			if (Game.isUptext(option)) {
				Mouse.click(1);
				return true;
			} else {
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
		}
		return false;
	}

	
	private boolean clickOBJ(RSObject npc, String option) {
		Camera.turnToTile(npc.getPosition());
		// Mouse.setSpeed(170);
		if (npc.isOnScreen()) {
			Point[] points = npc.getModel().getAllVisiblePoints();

			if (points.length > 0) {
				Mouse.move(points[points.length / 2]);
				// Mouse.move(new Point(points[0].x, points[0].y));
				if (Game.isUptext(option)) {
					Mouse.click(1);
					return true;
				} else {
					Mouse.click(3);
					if (ChooseOption.isOpen()) {
						ChooseOption.select(option);
					}
				}
			}
		}
		return false;
	}

	public void drawModel(RSModel model, Graphics g, boolean fill) {
		if (model.getAllVisiblePoints().length != 0) {
			if (fill) {
				// fill triangles
				for (Polygon p : model.getTriangles()) {
					g.fillPolygon(p);
				}
			} else {
				// draw triangles
				for (Polygon p : model.getTriangles()) {
					g.drawPolygon(p);
				}
			}
		}
	}
	
	public void drawPath(RSTile[] path, Graphics g, boolean minimap) {
		Point[] points = new Point[path.length];
		if (minimap) {
			// Drawing on the other whatever
			for (int i = 0; i < path.length - 1; i++) {
				Point tilePoint = Projection.tileToMinimap(path[i]);
				if (Projection.isInMinimap(tilePoint)) {
					if (i < path.length - 1) {
						Point nextPoint = Projection.tileToMinimap(path[i + 1]);
						if (Projection.isInMinimap(nextPoint)) {
							g.drawLine(tilePoint.x, tilePoint.y, nextPoint.x,
									nextPoint.y);
						}
					}
					g.fillOval(tilePoint.x, tilePoint.y, 3, 3);
				}
			}
		} else {
			for (int i = 0; i < path.length - 1; i++) {
				if (path[i].getPosition().isOnScreen()) {
					if (i > 0) { // could just start index
						Point currentTile = Projection.tileToScreen(path[i], 0);
						Point lastTile = Projection
								.tileToScreen(path[i - 1], 0);
						g.drawLine(currentTile.x, currentTile.y, lastTile.x,
								lastTile.y);
					}
					//g.setColor("#FF0000");
					drawTile(path[i], g, false);
				}
			}
		}
	}
	
	public void drawTile(RSTile tile, Graphics g, boolean fill) {
		if (tile.getPosition().isOnScreen()) {
			if (fill) {
				g.fillPolygon(Projection.getTileBoundsPoly(tile, 0));
			} else {
				g.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
			}
		}
	}
	
	public boolean isMovin() {

		return Player.isMoving();
	}
	
	public void waitIsMovin(){
		
		for(int i = 0; i < 57; i++, sleep(30, 40)){
			if (!isMovin())
				break;
		}
	}

	public boolean gotFood() {

		return Inventory.getCount(foodID) > 0;
	}

	public int lootCount(int ID) {

		return Inventory.getCount(ID);
	}

	// improved waiting for loot
	private void waitForInv(int lootID) {
		int k = 0;

		while (k < 200	&& Player.isMoving()) {
			sleep(80);
			k++;
		}
	}

	public boolean clickModel(RSModel model, String option, boolean rightClick)

	{

		Point[] points = model.getAllVisiblePoints();
		int length = points.length;
		if (length != 0) {
			Point p = points[//General.random(0, length - 1)];
			                 length / 2];
			Mouse.move(p);
			{
				String top = org.tribot.api2007.Game.getUptext();
				if (top.contains(option)) {
					Mouse.click(p, 0);
					return true;
				} 
				else if (rightClick) {
					Mouse.click(3);
					scripts.easyslayer.Timer t = new Timer(500);
					while (t.isRunning() && !ChooseOption.isOpen())
						sleep(50, 100);
					if (!ChooseOption.isOpen())
						return false;
					if (ChooseOption.select(option))
						return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean distanceFromTile(RSTile tile, int distance){
		return pos().distanceTo(tile) <= distance;
	}
	
	public void runAndTurnCamera(RSNPC mob){
		Walking.setWalkingTimeout(500L);
		Walking.clickTileMM(mob.getPosition(),  1);
		Camera.turnToTile(mob.getPosition());
	}
	
	public boolean mobNotOnScreen(RSNPC mob){
		return !mob.isOnScreen();
	}
	
	
	public void emergTele(){
		RSItem[] vtab = Inventory.find(VTAB);
		RSItem[] air = Inventory.find(AIR);
		RSItem[] fire = Inventory.find(FIRE);
		RSItem[] law = Inventory.find(LAW);
		
		if (vtab.length > 0){
			println("going to varrock using runes");
			vtab[0].click("Break");
			sleep(5000, 6000);
		}
		else if(air.length > 0 && fire.length > 0 && law.length > 0){
			if(GameTab.getOpen() != TABS.MAGIC)
				GameTab.open(TABS.MAGIC);
			sleep(200, 300);
			Magic.selectSpell("Varrock Teleport");
			sleep(3000, 3500);
		}
		else{
			println("No more Varrock teletabs or runes!!!! stopping script to prevent death ");
			script = false;
		}
		sleep(4000,5000);
	}
	
	private void chatting()
    {
        String ops[] = {
            "Don't give him my password.", "Don't tell them anything and inform Jagex through the game website.", "Nobody", "Game Inbox on the RuneScape website.", "The birthday of a famous person or event.", "Talk to any banker in RuneScape.", "Nowhere.", "Recovering your account if it is stolen.", "Use the 'Recover a Lost Password' section on the RuneScape website.", "Only on the RuneScape website.", 
            "Memorable", "No", "Politely tell them no and then use the 'Report Abuse' button.", "No, it might steal my password.", "Don't tell them anything and click the 'Report Abuse' button.", "Don't give them the information and send an 'Abuse report'.", "Every couple of months", "Virus scan my computer then change my password and recoveries.", "No.", "To help me recover my password if I forget it or it is stolen."
        };
        if(NPCChat.getClickContinueInterface() != null)
            NPCChat.clickContinue(true);
        sleep(1000,1200);
        if(Interfaces.get(230, 0) != null || Interfaces.get(228, 0) != null)
        {
            String ans[] = NPCChat.getOptions();
            if(ans.length > 0)
            {
                for(int a = 0; a < ans.length; a++)
                {
                    for(int i = 0; i < ops.length; i++)
                        if(ans[a].toString().equals(ops[i]))
                            NPCChat.selectOption(ops[i], true);

                }

            }
        }
        sleep(1000,1200);
        if(NPCChat.getClickContinueInterface() != null)
            NPCChat.clickContinue(true);
        sleep(5000,6000);
    }
		
	private boolean chatup()  {
		println("chat is up to check if we are a bot");
		return Interfaces.get(242, 1) != null || Interfaces.get(244, 1) != null || Interfaces.get(243, 1) != null || Interfaces.get(230, 0) != null || Interfaces.get(228, 0) != null;
	}

	private boolean modsnear(){
		RSPlayer allPlayers[] = Players.getAll();
	        if(allPlayers.length > 0)
	        {
	            RSPlayer arsplayer[];
	            int k = (arsplayer = allPlayers).length;
	            for(int j = 0; j < k; j++)
	            {
	                RSPlayer i = arsplayer[j];
	                if(i != null && i.getName() != null && i.getName().contains("Mod "))
	                    return true;
	            }

	            return false;
	        } else
	        {
	            return false;
	        }
	    }
	 private int specialPercent()   {
	      return Game.getSetting(300) / 10;
	       
	    }
	 
	    private boolean specialSelected() {
	        return Game.getSetting(301) == 1;
	    }

	    private void selectspecial()
	    {
	        GameTab.open(TABS.COMBAT);
	        sleep(1000);
	        if(GameTab.getOpen() == TABS.COMBAT)
	        {
	            Mouse.clickBox(573, 412, 711, 432, 1);
	            sleep(1000);
	        }
	    }
	    
	    /**** brian methods ****/
	    //TODO getClosesetInactiveNPC
	    public RSNPC getClosestInactiveNPC(String id) {
	    	RSNPC[] npcs = NPCs.findNearest(id);
	    	if(npcs.length > 0)	{
	    		for(int x = 0; x < npcs.length; x++){
	    			if(npcs[x].isInteractingWithMe() && npcs[x].isValid()){ // not sure if this fixes anything
	    				println("already in combat, we'll just choose the monster interacting with me");
	    				return npcs[x];
	    			}
	    			else if(!npcs[x].isInCombat() && PathFinding.isTileWalkable(npcs[x].getAnimablePosition()))
	    				return npcs[x];
	    		}
	    	}
	    	return null;
	    }


	    public void attackInactiveMonster(String id){
	    	
	    	final RSNPC monster = getClosestInactiveNPC(id);
	    	if(monster != null)	{
	    		if (monster.isOnScreen()){
	    			//if(DynamicClicking.clickRSModel(monster.getModel(), "Attack")){
	    			//if(clickModel(monster.getModel(), "Attack", false)){
	    			if(specialPercent() > 99){
						selectspecial();
					}
	    			if(monster.click("Attack")){
	    				
	    				Timing.waitCondition(new Condition() {
	    					public boolean active() {
	    						return monster.isInteractingWithMe();
	    					}
	    				}, General.random(4000, 4500));
	    				
	    				waitTillDead(monster);
	    				sleep(100,120);
	    			}
	    		}
	    		else if (monster.getPosition().distanceTo(Player.getPosition()) <= 4){
	    			Camera.turnToTile(monster.getPosition());
	    			sleep(2000,2500);
	    		}
	    		else{
	    			Walking.blindWalkTo(monster.getPosition());
	    			sleep(2000,2500);
	    		}
	    	}
	    }
	    
	  //TODO waitTillDead
		public void waitTillDead(RSNPC npc){
			boolean hi = false;
			for (int i = 0; i < 800; i++, sleep(30, 40)) {
				if(getHp() < 30)
					eatFood();
				else if (npc == null || Player.getRSPlayer().getInteractingCharacter() == null || !npc.isInteractingWithMe() ){// || npc[0].getCombatCycle() > 0)) {
					hi = true;
					break;
				}
			}
			if (!hi)
				println("timeout");
			println("done attacking");
		}
	    
	    public boolean needsToEat(int percToEat)
	    {
	    	double currLevel = Skills.getCurrentLevel(SKILLS.HITPOINTS);
	    	int hitpointsLevel = Skills.getActualLevel(SKILLS.HITPOINTS);
	    	double decimal = currLevel / (double)(hitpointsLevel);
	    	int percent = (int)(decimal * 100);
	    	if (percent <= percToEat)
	    		return true;
	    	return false;
	    }

	    public void eatFood(int[] foodIds)
	    {
	    	RSItem[] food = Inventory.find(foodIds);
	    	if(food.length > 0)
	    	{
	    		food[0].click("Eat");
	    		sleep(400, 800);
	    	}
	    }
	    public void clickRSObject(int distance, int id, String action)
	    {
	    	boolean successful = false;
	    	while(!successful)
	    	{
	    		RSObject[] objects = Objects.find(distance, id);
	    		if (objects.length > 0)
	    		{
	    			int r = Projection.angleToTile(objects[0].getAnimablePosition());
	    			Camera.setCameraRotation(r + General.random(0, 90));
	    			sleep(600, 800);
	    			successful = DynamicClicking.clickRSObject(objects[0], action);
	    			waitIsMovin();
	    			sleep(1800, 2400);
	    			
	    		}
	    	}
	    }

	    	
	    public void clickRSObjectAt(RSTile tile, String action)
	    {
	    	boolean successful = false;
	    	while(!successful)
	    	{
	    		RSObject[] objects = Objects.getAt(tile);
	    		if(objects.length > 0)
	    		{
	    			int r = Projection.angleToTile(objects[0].getAnimablePosition());
	    			Camera.setCameraRotation(r + General.random(0, 80));
	    			sleep(600, 800);
	    			successful = DynamicClicking.clickRSObject(objects[0], action);
	    			sleep(1800, 2400);
	    		}
	    	}
	    }
	    
	    public boolean checkForHelm(int option){
			RSItem[] helm = Interfaces.get(387, 28).getItems();
			int helmID = 0;
			if(PureMode && option == 0) return true;
			else if(option == 0) // spiny
				helmID = SPINY;
			else if (option == 1) //earmuffs
				helmID = EARMUFFS;
			for (int i = 0; i < helm.length; i++) { // looping threw items equiped
				if (helm[i].getID() == helmID) {
					return true;
				}
			}
			return false;
		}
	    
	    
	    public boolean equipHelm(int option){
	    	
	    	RSItem[] helm0 = Inventory.find(SPINY);
	    	RSItem[] helm1 = Inventory.find(EARMUFFS);
			if(PureMode && option == 0) return true;
	    	else if(option == 0){ // spiny
				if(helm0.length > 0)
					if(helm0[0].click("Wear"))
						return true;
			}
			else if (option == 1){ //earmuffs
				if(helm1.length > 0)
					if(helm1[0].click("Wear"))
						return true;
			}
			
			
			return false;
	    }
	    
	    //START: Code generated using Enfilade's Easel
	    private final Color color1 = new Color(255, 255, 255);
	    private final Color color2 = new Color(0, 0, 0);
	    private final Color color3 = new Color(0, 204, 0);
	    private final Color color4 = new Color(0, 0, 255);
	    private final Color color5 = new Color(102, 102, 0);
	    private final Color color6 = new Color(255, 0, 0);
	    private final Color color7 = new Color(0, 0, 0);

	    private final BasicStroke stroke1 = new BasicStroke(1);
	    private final BasicStroke stroke2 = new BasicStroke(3);

	    private final Font font1 = new Font("Arial", 3, 24);
	    private final Font font2 = new Font("Arial", 0, 14);
	    public final int[] XP = {Skills.getXP(SKILLS.ATTACK), Skills.getXP(SKILLS.STRENGTH), 
	    		Skills.getXP(SKILLS.DEFENCE), Skills.getXP(SKILLS.RANGED), 
	    		Skills.getXP(SKILLS.PRAYER), Skills.getXP(SKILLS.MAGIC), 
	    		Skills.getXP(SKILLS.HITPOINTS), Skills.getXP(SKILLS.SLAYER)};
		public final SKILLS[] Names = {SKILLS.ATTACK, SKILLS.STRENGTH, 
				SKILLS.DEFENCE, SKILLS.RANGED, 
				SKILLS.PRAYER, SKILLS.MAGIC, 
				SKILLS.HITPOINTS, SKILLS.SLAYER};
		public int[] startLvs = {Skills.getActualLevel(SKILLS.ATTACK), Skills.getActualLevel(SKILLS.STRENGTH), 
				Skills.getActualLevel(SKILLS.DEFENCE), Skills.getActualLevel(SKILLS.RANGED), 
				Skills.getActualLevel(SKILLS.PRAYER), Skills.getActualLevel(SKILLS.MAGIC), 
				Skills.getActualLevel(SKILLS.HITPOINTS), Skills.getActualLevel(SKILLS.SLAYER), 
		};
	    
	    int tin,copper,ess,ironArr;
	    //TODO paint
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;

		g.setColor(color1);
		g.fillRect(339, 415, 154, 57);
		g.setStroke(stroke1);
		g.drawRect(339, 415, 154, 57);

		g.setColor(color7);

		timeRan = System.currentTimeMillis() - start_time;

		g.setFont(font2);
		g.setColor(color4);
		g.drawString("Time running " + Timing.msToString(timeRan), 345, 455);
		g.setColor(color7);

		g.setFont(new Font("Arial", 0, 11));

		long timeRan = getRunningTime();

		double secondsRan = (int) (timeRan / 1000);
		double hoursRan = secondsRan / 3600;

		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(Names[i])) {

				double xp_per_hour = Math.round(((Skills.getXP(Names[i]) - XP[i])) / hoursRan);

				g.setColor(new Color(0, 0, 0));
				g.fillRect(2, 326 - x, 515, 12);

				g.setColor(new Color(0, 255, 0, 255));
				g.drawString(Names[i] + ": " + (int) xp_per_hour + " Xp/h", 5, (337 - x));

				int x1 = 125, y1 = 327 - x;
				int CUR_LVL = Skills.getActualLevel(Names[i]);
				int NXT_LVL = (CUR_LVL + 1);
				int Percentage = Skills.getPercentToLevel(Names[i], NXT_LVL);
				double nextLv = Skills.getXPToLevel(Names[i], NXT_LVL);
				
				double hours = (nextLv / xp_per_hour);

				g.drawString("Curr lv: " + CUR_LVL + " (" 
						+ (CUR_LVL - startLvs[i]) + ")  TTL " + 
						(int) hours + ":" + (int) ((hours - (int) hours) * 60)
						+ " hr:min  Exp to lv: " + (int) nextLv
				, 235, y1 + 10);
				
				g.setColor(new Color(255, 0, 0, 255));
				g.fillRect(x1, y1, 100, 10);
				g.setColor(new Color(0, 255, 0, 255));
				g.fillRect(x1, y1, Percentage, 10);

				g.setColor(new Color(0, 0, 0));
				g.drawString(Percentage + "% to " + NXT_LVL, x1 + 25, y1 + 9);

				x += 15;
			}
		}
		
		int ironArrNum = Inventory.find(884).length > 0 ? (Inventory.find(884)[0].getStack() - ironArr) : 0;
		int essNum = Inventory.find(7937).length > 0 ? (Inventory.find(7937)[0].getStack() - ess) : 0;
		int tinNum = Inventory.find(439).length > 0 ? (Inventory.find(439)[0].getStack() - tin) : 0;
		int copperNum = Inventory.find(437).length > 0 ? (Inventory.find(437)[0].getStack() - copper) : 0;
		double cash_hr = (ironArrNum*20 + essNum*25 + tinNum*90 + copperNum*90)/hoursRan;
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 90, 130, 60);
		g.setColor(new Color(255, 255, 255));
		g.drawString("Iron arrows: " + ironArrNum, 2, 100);
		g.drawString("Pure ess: " + essNum, 2, 110);
		g.drawString("Copper ore: " + copperNum, 2, 120);
		g.drawString("Tin ore: " + tinNum, 2, 130);
		g.drawString("Cash/hr: " + Math.round(cash_hr), 2, 140);
	}
	    //END: Code generated using Enfilade's Easel
	   
	    
		@Override
		public void onPaint(Graphics arg0) {
			if (showPaint) {
				onRepaint(arg0);
			}
		}
		// ALL PAINT STUFF
		
		int startXp = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.STRENGTH) 
				+ Skills.getXP(SKILLS.ATTACK) + Skills.getXP(SKILLS.DEFENCE) + Skills.getXP(SKILLS.RANGED);
		int startXpSlayer = Skills.getXP(SKILLS.SLAYER);
		
		double version;
		int current_xp = 0;
		int current_xpSlayer = 0;
		final long start_time = System.currentTimeMillis();
		
		double XpToLVslayer = Skills.getXPToNextLevel(SKILLS.SLAYER);
		double XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
		double XpToLVatt = Skills.getXPToNextLevel(SKILLS.ATTACK);
		double XpToLVstr = Skills.getXPToNextLevel(SKILLS.STRENGTH);
		double XpToLVdef = Skills.getXPToNextLevel(SKILLS.DEFENCE);//"Defense");
		double XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
		long timeRan = System.currentTimeMillis() - start_time;
		int xpGained = current_xp - startXp;
		int xpGainedSlayer = current_xpSlayer - startXpSlayer;
		int lvsGained = 0;
		int startLv = Skills.getActualLevel(SKILLS.SLAYER);
		int currLv = Skills.getActualLevel(SKILLS.SLAYER);
		double multiplier = timeRan / 3600000D;
		int xpPerHour = (int) (xpGained / multiplier);
		int xpPerHourSlay = (int) (xpGainedSlayer / multiplier);
		double show_xp_to_lvrange = ((XpToLVrange / xpPerHour) * 60);
		double show_xp_to_lvstr = ((XpToLVstr / xpPerHour) * 60);
		double show_xp_to_lvatt = ((XpToLVatt / xpPerHour) * 60);
		double show_xp_to_lvdef = ((XpToLVdef / xpPerHour) * 60);
		double show_xp_to_lvhp = ((XpToLVhp / xpPerHour) * 60);
		double show_xp_to_lvslayer = ((XpToLVslayer / xpPerHourSlay) * 60);
		int timeToLv;
		
		
		
		String state = "null";
		boolean showPaint = true;
		Rectangle hideZone = new Rectangle(488, 348, 509, 368);


		public Minotaur() {
			version = ((ScriptManifest) getClass().getAnnotation(
					ScriptManifest.class)).version();
		}

	

	public void putMap() {
		for (int i = 0; i < loot.length; i++) {
			map.put(loot[i], names[i]);
		}
	}
	
	public void eatFood(){
		RSItem [] food = Inventory.find(foodID);
		if(food.length > 0){
			food[0].click("Eat");
			sleep(600,700);
		}
		else{
			println("You are out of food in inventory, lets bank!");
			emergTele();
			sleep(600,700);
		}
		
	}
	             

	/****dynamic sig stuff ****/
	// Initialize variable
	
	
	@Override
	public void onEnd() {
		//Send gained stuff to the database.
		
	}
	
	public static boolean isAttacking() {
		return Combat.getAttackingEntities().length > 0;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	
}