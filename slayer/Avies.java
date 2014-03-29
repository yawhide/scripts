package scripts.slayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;

@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", version=0.24, name = "Yaw hide's Ava Killer", description="Local version")
public class Avies extends Script implements Painting, Pausing, MessageListening07{

	// loot
	int[] junk = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767, 117, 6963, 
			556, 829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180, 6965, 1969,
			6183, 6181, 6962, 449, 1197, 243, 314, 526 };

	int[] loot = { 9142, 1615, 1247, 1303, 1249, 1149, 2361, 2366, 1462, 985,
			987, 2363, 1617, 1213, 2363, 207, 1229, 5678, 9431, 561, 
			/* clue scroll id starts */
			2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 };

	int[] loot2 = { 1615, 1247, 1303, 1249, 1149, 2361, 2366, 1462, 985, 987,
			2363, 1617, 1213, 2363, 207, 1229, 5678, 9431, 561,
			/* clue scroll id starts */
			2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 };

	int[] alcLoot = { 1213, 1229, 5678, 9431 };
	
	HashMap<Integer, String> map = new HashMap<Integer, String>(loot.length);

	String[] names = { "Mithril bolts", "Dragonstone", "Rune spear",
			"Rune longsword", "Dragon spear", "Dragon med helm",
			"Adamantite bar", "Shield left half", "Nature talisman",
			"Tooth half of key", "Loop half of key", "Runite bar",
			"Uncut diamond", "Rune dagger", "Rune bar", "Herb",
			"Rune dagger(p)", "Rune dagger(p+)", "Runite limbs", "Nature rune",
			"Clue scroll (hard)" };
	
	// RSPaths
	private RSTile[] toFirstSlide = { new RSTile(2889, 3683, 0), new RSTile(2884, 3679, 0), new RSTile(2881, 3673, 0), new RSTile(2887, 3668, 0), new RSTile(2885, 3665, 0), new RSTile(2880, 3668, 0) };
	private RSTile[] beforePrayerRange = { new RSTile(2878, 3664, 0), new RSTile(2876, 3667, 0), new RSTile(2873, 3669, 0), new RSTile(2872, 3672, 0), new RSTile(2872, 3676, 0), new RSTile(2871, 3679, 0), new RSTile(2871, 3682, 0), new RSTile(2870, 3686, 0), new RSTile(2872, 3689, 0), new RSTile(2873, 3691, 0) };
	private RSTile[] beforeRock = { new RSTile(2876, 3693, 0), new RSTile(2879, 3695, 0), new RSTile(2881, 3698, 0), new RSTile(2883, 3701, 0), new RSTile(2887, 3702, 0), new RSTile(2891, 3703, 0), new RSTile(2895, 3703, 0), new RSTile(2897, 3701, 0), new RSTile(2899, 3702, 0), new RSTile(2899, 3705, 0), new RSTile(2899, 3708, 0), new RSTile(2899, 3711, 0), new RSTile(2899, 3713, 0) };
	private RSTile[] toGW = { new RSTile(2899, 3721, 0), new RSTile(2902, 3724, 0), new RSTile(2904, 3726, 0), new RSTile(2906, 3729, 0), new RSTile(2907, 3731, 0), new RSTile(2908, 3732, 0), new RSTile(2908, 3733, 0), new RSTile(2909, 3734, 0), new RSTile(2909, 3735, 0), new RSTile(2910, 3738, 0), new RSTile(2912, 3742, 0), new RSTile(2915, 3742, 0), new RSTile(2913, 3744, 0), new RSTile(2916, 3746, 0) };
	private RSTile[] toAvies = { new RSTile(2883, 5311, 2),
			new RSTile(2879, 5310, 2), new RSTile(2873, 5309, 2),
			new RSTile(2867, 5307, 2), new RSTile(2861, 5307, 2),
			new RSTile(2856, 5304, 2), new RSTile(2857, 5299, 2),
			new RSTile(2861, 5295, 2), new RSTile(2865, 5294, 2),
			new RSTile(2871, 5293, 2), new RSTile(2885, 5309, 2),
			new RSTile(2890, 5309, 2), new RSTile(2893, 5308, 2),
			new RSTile(2896, 5304, 2), new RSTile(2896, 5298, 2),
			new RSTile(2896, 5296, 2), new RSTile(2896, 5294, 2),
			new RSTile(2892, 5291, 2), new RSTile(2888, 5291, 2),
			new RSTile(2881, 5307, 2), new RSTile(2881, 5304, 2),
			new RSTile(2882, 5300, 2), new RSTile(2880, 5298, 2),
			new RSTile(2880, 5295, 2), new RSTile(2878, 5291, 2),
			new RSTile(2878, 5288, 2) };

	//Positionables
	Positionable firstSlide = new RSTile(2879, 3668, 0);
	String firstSlideAction = "Climb"; // object is Rocks
	Positionable bankT = new RSTile(3185, 3438, 0);
	Positionable gwCenter = new RSTile(2901, 5283, 2);
	Positionable holeT = new RSTile(2918, 3746, 0);
	Positionable randomEastT = new RSTile(2862, 5287, 2);
	Positionable randomWestT = new RSTile(2887, 5283, 2);
	
	//RSAreas
	private RSTile[] teleSpotPoly = { new RSTile(2879, 3667, 0), new RSTile(2880, 3667, 0), new RSTile(2886, 3662, 0), new RSTile(2890, 3662, 0), new RSTile(2894, 3665, 0), new RSTile(2898, 3668, 0), new RSTile(2903, 3674, 0), new RSTile(2903, 3683, 0), new RSTile(2902, 3685, 0), new RSTile(2897, 3690, 0), new RSTile(2895, 3691, 0), new RSTile(2891, 3691, 0), new RSTile(2889, 3690, 0), new RSTile(2885, 3691, 0), new RSTile(2879, 3685, 0), new RSTile(2878, 3679, 0), new RSTile(2877, 3678, 0), new RSTile(2877, 3671, 0), new RSTile(2878, 3668, 0) };
	private RSTile[] aroundFirstSlide = { new RSTile(2877, 3671, 0), new RSTile(2877, 3668, 0), new RSTile(2879, 3667, 0), new RSTile(2880, 3667, 0), new RSTile(2883, 3665, 0), new RSTile(2883, 3666, 0), new RSTile(2880, 3670, 0), new RSTile(2879, 3671, 0) };
	RSTile[] varrockA = {new RSTile(3176, 3448, 0), new RSTile(3255, 3386, 0) };
	RSTile[] CWA = {new RSTile(2436, 3095, 0), new RSTile(2443, 3082, 0)};
	RSTile[] AviesA = {new RSTile(2847, 5307, 2), new RSTile(2901, 5281, 2)};
		
	RSArea teleSpotArea = new RSArea(teleSpotPoly);
	RSArea aroundFirstSlideArea = new RSArea(aroundFirstSlide);
	RSArea varrockArea = new RSArea(varrockA[0], varrockA[1]);
	RSArea CWArea = new RSArea(CWA[0], CWA[1]);
	RSArea aviesArea = new RSArea(AviesA[0], AviesA[1]);
	
	// Variables
	final int[] avaIDs = { 5750, 5758, 5753,
			5700, 5708 , 5703}; //69, 71, 83
	int[] foodIDs; // , 385, 7946, 1897 };
	final int[] ppot = { 2434, 139, 141, 143 };
	final int[] rangepots = { 169, 2444, 171, 173 };
	final int NAT = 561;
	final int FIRE = 554;
	final int LAW = 563;
	final int FTAB = 8009;
	final int VTAB = 8007;
	final int HTAB = 8013;
	int mbolts = 9142;
	long antiban = System.currentTimeMillis();
	int startXp = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.RANGED);
	double version;
	int current_xp;
	final long start_time = System.currentTimeMillis();
	double XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
	double XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	int startLv = Skills.getActualLevel(SKILLS.RANGED)
			+ Skills.getActualLevel(SKILLS.HITPOINTS);
	final String[] type = { "Defence", "Ranged", "Hitpoints" };
	final SKILLS[] Names = { SKILLS.DEFENCE, SKILLS.RANGED, SKILLS.HITPOINTS, };
	final int[] XP = { Skills.getXP(SKILLS.DEFENCE),
			Skills.getXP(SKILLS.RANGED), Skills.getXP(SKILLS.HITPOINTS) };
	int[] startLvs = { Skills.getActualLevel(SKILLS.DEFENCE),
			Skills.getActualLevel(SKILLS.RANGED),
			Skills.getActualLevel(SKILLS.HITPOINTS) };
	boolean scriptStatus = true;
	int foodNum = 11;
	int boltsID;
	int ADDY = 2361;
	int COIN = 995;
	int RANARR = 207;
	
	int addybars, coins, ranarrs = 0;
	int addycount, coinscount, ranarrcount = 0;
	int addyIni, coinsIni, ranarrIni = 0;
	int addyprice = 2800;
	int ranarrprice = 6400;
	boolean moveRandom = false;
	boolean useHouse = true;
	boolean waitGUI = true;

	String fightStatus;
	
	@Override
	public void run() {
		
		boltsID = Equipment.getItem(SLOTS.ARROW).getID();
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			println("You must be at least 70 range to use this script");
			scriptStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			println("You must be at least 44 prayer to use this script");
			scriptStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 60 && Skills.getActualLevel(SKILLS.STRENGTH) < 60){
			println("You must be at least 60 agility or strength to use this script");
			scriptStatus = false;
		}
		if(Skills.getActualLevel(SKILLS.CONSTRUCTION) < 50){
			useHouse = false;
			println("You do not have 50 con for varrock portal focus, you must use varrock teletabs");
		}
		
		boolean devmode = false;
		
		if(devmode){
			foodNum = 9;
			foodIDs = new int[] {379};
			useHouse = true;
		}
		else{
			AvieGUI g = new AvieGUI();
			g.setVisible(true);
			while (waitGUI)
				sleep(500);
			g.setVisible(false);
		}
		
		if(!Combat.isAutoRetaliateOn()){
			Combat.setAutoRetaliate(true);
			sleep(500,600);
		}
		
		putMap();
		
		addyIni = Inventory.getCount(ADDY);
		coinsIni = Inventory.getCount(COIN);
		ranarrIni = Inventory.getCount(RANARR);
		addyprice = Zybez.getPrice("Adamantite bar");
		ranarrprice = Zybez.getPrice("Clean ranarr");
		
		sleep(250);
		
		Walking.setWalkingTimeout(5000L);
		Walking.setControlClick(true);
		
		
		while(scriptStatus){
			current_xp = Skills.getXP(SKILLS.RANGED) + Skills.getXP(SKILLS.HITPOINTS);
			XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
			XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
			Mouse.setSpeed(General.random(175,190));
			
			addybars = Inventory.getCount(ADDY) - addyIni;
			coins = Inventory.getCount(COIN) - coinsIni;
			ranarrs = Inventory.getCount(RANARR) - ranarrIni;
			
			if(Game.getRunEnergy() > 20) {
			    Options.setRunOn(true);
			}
			
			if(moveRandom){
				if(pos().distanceTo(randomEastT) > pos().distanceTo(randomWestT)){
					Walking.walkPath(Walking.generateStraightPath(randomEastT));
					waitIsMovin();
				}
				else{
					Walking.walkPath(Walking.generateStraightPath(randomWestT));
					waitIsMovin();
				}
				moveRandom = false;
			}
			else if(gotoAvies()){
				fight();
			}
		}
	}
	
	public void fight(){
		
		Walking.setControlClick(true);
		Walking.setWalkingTimeout(1500L);
		
		if(getHp() <= 30){
			HEAL();
		}
		else if(Inventory.isFull() || (Inventory.getAll().length > 26 && !isLoot())){
			println("Inv is full");
			ifFull();
		}
		else if(isLoot()){
			println("looting...");
			LOOT();
		}
		else if (Inventory.getCount(rangepots) > 0
				&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
						.getActualLevel(SKILLS.RANGED) + 2) {

			println(Skills.getCurrentLevel(SKILLS.RANGED) + "  "
					+ Skills.getActualLevel(SKILLS.RANGED));
			fightStatus = "Potting up";
			drinkPotion(rangepots);
			sleep(1000,1200);
			println("Potted up");
		}
		else if (Prayer.getCurrentPrayers().length > 0){
			PRAYERS[] p = Prayer.getCurrentPrayers();
			if(!Prayer.isTabOpen())
				GameTab.open(TABS.PRAYERS);
			sleep(300,500);
			for (int i = 0; i < p.length; i++) {
				Prayer.disable(p[0]);
				sleep(300,500);
			}
		}
//		else if (inCombat() && !underAttack().equals("Aviansie")){
//			Walking.walkPath(Walking.generateStraightPath(toAvies[toAvies.length-1]));
//			waitIsMovin();
//		}
		else if(isRanging()){
			if(Combat.getTargetEntity() != null && !Combat.getTargetEntity().getName().equals("Aviansie")){
				moveRandom = true;
			}	
			else
				waitForLoot();
		}
		else if (Inventory.find(alcLoot).length > 0){
			alc(alcLoot);
		}
		else{
			RSNPC[] avies = NPCs.findNearest(avaIDs);
			for(RSNPC a : avies){
				if(aviesArea.contains(a.getPosition())){
					if (a.isOnScreen()) {
						clickObject(a, "Attack");//Clicking.click(avies)) {
							fightStatus = "killing an avie";
							
							sleep(300, 500);
//							final RSNPC tmp_blkdrag = a;
//							Timing.waitCondition(new Condition() {
//								public boolean active() {
//									return Player.getAnimation() == 4230
//											|| inCombat()
//											|| tmp_blkdrag.isInCombat();
//								}
//							}, General.random(2200, 2400));
//						} 
//						else {
//							sleep(200, 400);
//						}
					} 
					else {
						println("running to closest avie");
						Walking.clickTileMM(a.getAnimablePosition(), 1);
						Camera.turnToTile(a.getPosition());
						sleep(500,600);
					}
					break;
				}
			}
		}
	}
	
	public boolean gotoAvies(){
		RSObject[] boulder = Objects.findNearest(7, "Boulder");
		RSObject[] hole = Objects.findNearest(7, "Hole");
	
		if (inArea(aviesArea) && pos().getPlane() == 2)
			return true;
		else if(!inArea(varrockArea) && Objects.findNearest(50, "Portal").length > 0){
			fightStatus = "in house...";
			RSObject[] alter = Objects.findNearest(20, "Altar");
			RSObject[] portal = Objects.findNearest(20, "Varrock Portal");
			RSObject[] portal2 = Objects.findNearest(30, "Portal");
			
			if(alter.length == 0 || portal.length == 0){
				fightStatus = "not inside house...";
				if(portal2.length > 0){
					if(portal2[0].isOnScreen()){
						if(portal2[0].click("Enter")){
							NPCChat.selectOption("Go to your house", true);
						}
					}
					else{
						Walking.walkPath(Walking.generateStraightPath(portal2[0].getPosition()));
						waitIsMovin();
					}
				}
				
				println("Could not determine where you are, are you outside of the house???");
				//scriptStatus = false;
			}
			
			else if (Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER)) {
				if(portal.length > 0){
					if (portal[0].isOnScreen()) {
						if (portal[0].click("Enter")) {
							sleep(500, 700);
						} else {
							sleep(500, 700);
						}
					} else {
						Positionable temp = new RSTile(portal[0].getPosition().getX(), portal[0].getPosition().getY()-2, 0);
						Walking.walkPath(Walking.generateStraightPath(temp));
						waitIsMovin();
					}
				}
			} else {
				if (alter.length > 0) {
					if (alter[0].isOnScreen()) {
						if (alter[0].click("Pray")) {
							sleep(500, 700);

						} else {
							sleep(500, 700);
						}
					} else {
						Walking.walkPath(Walking.generateStraightPath(alter[0].getPosition()));
						waitIsMovin();
					}
				}
			}
		}
		else if(inGW()){
			fightStatus = "walking to avies spot";
			Walking.walkPath(Walking.generateStraightPath(toAvies[toAvies.length-1]));
			//Walking.walkPath(toAvies);
			waitIsMovin();
		}
		else if (pos().distanceTo(toGW[toGW.length - 1]) <= 5) {
			fightStatus = "walking to GW hole";
			if (Walking.clickTileMS(holeT, 1)) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return inGW();
					}
				}, General.random(5000, 7000));
			}
		}
		else if (aroundPath(toGW)){
			fightStatus = "walking before the range trolls";
			Walking.walkPath(toGW);
			waitIsMovin();
		}
		else if (pos().distanceTo(beforeRock[beforeRock.length - 1]) <= 5) {
			fightStatus = "prayer off, walking to boulder";
			turnOffPrayerProtectionMissles();
			if (Clicking.click(Objects.findNearest(7, "Boulder"))) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return aroundPath(toGW);
					}
				}, General.random(7000, 8000));
			}
		}
		else if (inArea(aroundFirstSlideArea)){
			fightStatus = "near first slide";
			if(Clicking.click(Objects.findNearest(7, "Rocks"))){
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return aroundPath(beforePrayerRange);
					}
				}, General.random(4500, 5500));
			}
		}
		else if (inArea(teleSpotArea)){
			fightStatus = "walking to first slide";
			Walking.walkPath(toFirstSlide);
			waitIsMovin();
		}
		else if (aroundPath(beforeRock)){
			fightStatus = "walking by trolls";
			if(!Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES)){
				if(!Prayer.isTabOpen()){
					GameTab.open(TABS.PRAYERS);
					sleep(300,350);
				}
				Prayer.enable(PRAYERS.PROTECT_FROM_MISSILES);
				sleep(200,250);
			}
			Walking.walkPath(beforeRock);
			waitIsMovin();
		}
		else if (aroundPath(beforePrayerRange)){
			fightStatus = "walking before the range trolls";
			Walking.walkPath(beforePrayerRange);
			waitIsMovin();
		}
		else if (inArea(varrockArea)){
			fightStatus = "in varrock, banking...";
			if(!gotEquipment()){
				if (pos().distanceTo(bankT) <= 5){
					println("banking...");
					bank();
				}
				else{
					println("gonna web walk to bank");
					WebWalking.walkTo(bankT);
					waitIsMovin();
				}
			}
			else{
				teleToTroll();
			}
		}
		return false;
	}
	
	public boolean isLoot(){
		RSGroundItem[] Nests = GroundItems.findNearest(loot2);
		return Nests.length > 0 && aviesArea.contains(Nests[0].getPosition());
	}
		
	public void LOOT() {
		fightStatus = "looting";
		turnOffPrayerEagle();		
		RSGroundItem[] Nests = GroundItems.findNearest(loot);
		
		for(int i = 0; i < Nests.length; i++){
			Walking.setWalkingTimeout(1000L);
			if(Nests[i].getID() == 9142){
				if (Nests[i].getStack() > 9) {
					if (!Nests[i].isOnScreen()) {
						Walking.walkPath(Walking.generateStraightPath(Nests[i].getPosition()));
						Camera.turnToTile(Nests[i].getPosition());
						Camera.setCameraAngle(General.random(90, 100));
						waitIsMovin();
					}
					String str = map.get(Nests[i].getID());
					int tmpCount = lootCount(Nests[i].getID());
					if (Nests[i].click("Take " + str))
						waitForInv(Nests[i].getID(), tmpCount);
				}
			}
			else{
				if (!Nests[i].isOnScreen()) {
					Walking.walkPath(Walking.generateStraightPath(Nests[i].getPosition()));
					Camera.turnToTile(Nests[i].getPosition());
					Camera.setCameraAngle(General.random(90, 100));
					waitIsMovin();
				}
				String str = map.get(Nests[i].getID());
				int tmpCount = lootCount(Nests[i].getID());
				if(Nests[i].click("Take " + str))
					waitForInv(Nests[i].getID(), tmpCount);
			}
		}
	}
	
	public void bank(){
		if(Banking.openBankBooth()){
			if(Banking.isPinScreenOpen())
				Banking.inPin();
			else if (Banking.isBankScreenOpen()){
				
				addycount += addybars;
				coinscount += coins;
				ranarrcount += ranarrs;
				addyIni = 0;
				coinsIni = 0;
				ranarrIni = 0;
				
				Banking.depositAll();
				sleep(100,150);
				
				if(!useHouse){
					int pt = Skills.getActualLevel(SKILLS.PRAYER) / 3;
					int currP = Skills.getCurrentLevel(SKILLS.PRAYER);

					RSItem[] pPot;
					if (currP < pt * 2) {
						println("potting prayer, pt is: " + pt);

						Clicking.hover(Banking.find(ppot));
						Banking.withdraw(1, ppot);
						sleep(600, 640);
						Banking.close();
						sleep(200, 230);

						pPot = Inventory.find(ppot);
						if (pPot.length > 0) {
							do {
								pPot[0].click("Drink");
								sleep(1000, 1200);
							} while (Skills.getCurrentLevel(SKILLS.PRAYER) <= (pt * 2) - 2);
						}
						sleep(200, 250);
						if (!Banking.isBankScreenOpen()) {
							if (Banking.openBankBanker()) {
								sleep(200, 250);
								Banking.deposit(1, ppot);
								sleep(200, 250);
							}
						}
					}
					sleep(200, 300);
				}
				
				Clicking.hover(Banking.find(rangepots));
				Banking.withdraw(1, rangepots);
				sleep(600, 650);
				Clicking.hover(Banking.find(NAT));
				Banking.withdraw(10, NAT);
				sleep(600, 650);	
				Clicking.hover(Banking.find(FIRE));
				Banking.withdraw(52, FIRE);
				sleep(600, 650);
				Clicking.hover(Banking.find(LAW));
				Banking.withdraw(2, LAW);
				sleep(600, 650);
				Clicking.hover(Banking.find(foodIDs));
				Banking.withdraw(foodNum+((Combat.getMaxHP() - Combat.getHP()) / 12), foodIDs);
				sleep(600,650);
				
				if(useHouse){
					if(lootCountStack(HTAB) < 5){
						Clicking.hover(Banking.find(HTAB));
						Banking.withdraw(10, HTAB);
						sleep(500,650);
					}
				}
				else{
					if(lootCountStack(HTAB) < 5){
						Clicking.hover(Banking.find(VTAB));
						Banking.withdraw(10, VTAB);
						sleep(500,650);
					}
				}
				if(getStackBolts() < 500){
					Clicking.hover(Banking.find(boltsID));
					Banking.withdraw(1000, boltsID);
					sleep(600, 650);
				}
				
				if(Inventory.find(boltsID).length > 0){
					Inventory.find(boltsID)[0].click("Equip");
					sleep(500,600);
				}
				
				Banking.close();
				while(Inventory.find(foodIDs).length > foodNum){
					Inventory.find(foodIDs)[0].click("Eat");
					sleep(300);
				}
			}
		}
	}

	public void ifFull(){
		RSItem[] food = Inventory.find(foodIDs);
		RSItem[] bolts = Inventory.find(boltsID);
		if(Inventory.find(junk).length > 0 || food.length > 0 || bolts.length > 0){
			fightStatus = "dropping junk";
			if (food.length > 0){
				food[0].click("Eat");
				sleep(300,350);
			}
			else if (bolts.length > 0){
				bolts[0].click("Wield");
				sleep(300, 350);
			}
			Inventory.drop(junk);
		}
		else{
			emergTele();
		}
	}
	
	public boolean gotEquipment(){
		println(lootCountStack(NAT));
		if( ((!useHouse && lootCountStack(VTAB) > 0) || (useHouse && lootCountStack(HTAB) > 0))
				&& lootCountStack(NAT) >= 10 && lootCountStack(FIRE) >= 52
				&& lootCountStack(LAW) == 2 && Inventory.find(rangepots).length == 1
				&& Inventory.find(foodIDs).length == foodNum){
			return true;
		}
		return false;
	}

	public void gotoAviesSpot(){
		fightStatus = "going to avvieArea";
		Walking.setWalkingTimeout(1500L);
		Walking.walkPath(toAvies);
		waitIsMovin();
	}

	@Override
	public void onPaint(Graphics g) {
		Rectangle hideZone = new Rectangle(342, 369, 542, 500);
		Point p = Mouse.getPos();
		
		int xpGained = current_xp - startXp;
		long timeRan = System.currentTimeMillis() - start_time;
		
		double multiplier = timeRan / 3600000D;
		int xpPerHour = (int) (xpGained / multiplier);
					
		long timeRan2 = getRunningTime();
		double secondsRan = (int) (timeRan2/1000);
		double hoursRan = secondsRan/3600;
		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(Names[i])) {

				double xp_per_hour = Math.round(((Skills.getXP(Names[i]) - XP[i])) / hoursRan);
				if (!hideZone.contains(p)) {
					g.setColor(new Color(0, 0, 0));
					g.fillRect(2, 326 - x, 515, 12);

					g.setColor(new Color(0, 255, 0, 255));
					g.drawString(Names[i] + ": " + (int) xp_per_hour + " Xp/h",
							5, (337 - x));

					int x1 = 125, y1 = 327 - x;
					int CUR_LVL = Skills.getActualLevel(Names[i]);
					int NXT_LVL = (CUR_LVL + 1);
					int Percentage = Skills
							.getPercentToLevel(Names[i], NXT_LVL);
					double nextLv = Skills.getXPToLevel(Names[i], NXT_LVL);

					double hours = (nextLv / xp_per_hour);

					g.drawString("Curr lv: " + CUR_LVL + " ("
							+ (CUR_LVL - startLvs[i]) + ")  TTL " + (int) hours
							+ ":" + (int) ((hours - (int) hours) * 60)
							+ " hr:min  Exp to lv: " + (int) nextLv, 235,
							y1 + 10);

					g.setColor(new Color(255, 0, 0, 255));
					g.fillRect(x1, y1, 100, 10);
					g.setColor(new Color(0, 255, 0, 255));
					g.fillRect(x1, y1, Percentage, 10);

					g.setColor(new Color(0, 0, 0));
					g.drawString(Percentage + "% to " + NXT_LVL, x1 + 25,
							y1 + 9);

					x += 15;
				}
			}
		}
		
		int dh, db, ran;
		dh = addybars + addycount;
		db = coins + coinscount;
		ran = ranarrs + ranarrcount;
		g.setColor(Color.CYAN);
		RSGroundItem[] Nests = GroundItems.findNearest(loot2);
		
		Map<Integer, Integer> items = new HashMap<Integer, Integer>();
		RSItem[] item = Inventory.getAll();
		for(int i = 0; i < item.length; i++){
			int count = items.containsKey(item[i].getID()) ? items.get(item[i].getID()) : 0;
			items.put(item[i].getID(), count + 1);
		}
		Iterator it = items.entrySet().iterator();
		int k = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        g.drawString((map.get(pairs.getKey()) != null ? map.get(pairs.getKey()) : pairs.getKey()) 
	        		+ " = " + pairs.getValue(), 5, 100+k);
	        it.remove(); // avoids a ConcurrentModificationException
	        k+=15;
	    }
		
		for(int i = 0; i < Nests.length; i++){
			if(aviesArea.contains(Nests[i].getPosition())){
				RSTile t = Nests[i].getPosition();				
				drawTile(t, g, false);
			}
		}
		g.setColor(Color.RED);
		RSNPC[] avv = NPCs.findNearest(avaIDs);
		for(int i = 0; i < avv.length; i++){
			drawTile(avv[i].getPosition(), g, false);			
		}
		g.drawString(Combat.getTargetEntity().getName(), 5, 50);
		g.setColor(Color.GREEN);
//		RSNPC[] o = NPCs.findNearest(avaIDs);
//		if(o.length > 0){
//			int x2 = (int) o[0].getModel().getEnclosedArea().getBounds()
//					.getCenterX();
//			int y2 = (int) o[0].getModel().getEnclosedArea().getBounds()
//					.getCenterY();
//			g.drawRect(x2, y2, 1, 1);
//			g.drawRect(x2-4, y2-4, 1, 1);
//			g.drawRect(x2+4, y2+4, 1, 1);
//		}
		//drawModel(Combat.getTargetEntity().getModel(), g, true);
		
		
		
		if (!hideZone.contains(p)) {
			g.setColor(new Color(60, 60, 60)); 
			g.fillRect(340, 370, 200, 150);

			g.setColor(Color.WHITE);
			g.drawString("Yawhide's BDK", 345, 385);
			g.drawString("Version :" + version + "   Curr world: " + Game.getCurrentWorld(), 345, 405);
			g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
			g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour
					+ "/h)", 345, 435);
			g.drawString("State: " + fightStatus, 345, 450);
			g.drawString("Addy bars: " + dh + "       GP: " + db/1000 + " K", 345, 465);
			g.drawString("Gp/hr: " + (int) ((dh*addyprice + db + ran*ranarrprice)/hoursRan/1000) + " K   Total: " + (int)(dh*addyprice + db + ran*ranarrprice)/1000 + " K", 345, 480);
			
		}
	}
	
	public void teleToTroll(){
		GameTab.open(TABS.MAGIC);
		sleep(200,250);
		Magic.selectSpell("Trollheim Teleport");
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return inArea(teleSpotArea);
			}
		}, General.random(3500, 4500));
	}
	
	public void CWTele(){
		RSItem[] ROD = Equipment.find(SLOTS.RING);
		
		GameTab.open(TABS.EQUIPMENT);
		sleep(200,250);
		if(ROD.length > 0){
			if(!inCombat()){
				if (ROD[0].click("Operate")) {
					sleep(500, 700);
					if (NPCChat.selectOption("Castle Wars Arena.", true)) {
						Timing.waitCondition(new Condition() {
							public boolean active() {
								return inArea(CWArea);
							}
						}, General.random(4500, 5500));
					}
				}
			}
			else{
				emergTele();
			}
		}
	}
	
	public RSTile pos(){ return Player.getPosition(); }
	public boolean isRanging() { return Player.getRSPlayer().getInteractingCharacter() != null; }
	public int distanceTo(RSTile t){ return Math.max(pos().getX()-t.getX(), pos().getY() - t.getY()); }
	public int getHp() { return Combat.getHPRatio(); }
	public boolean isFull() { return Inventory.isFull(); }
	public int lootCount(int ID) { return Inventory.find(ID).length; }
	public int lootCountStack(int ID) { return Inventory.getCount(ID); }
	public boolean isMovin() { return Player.isMoving();}
	public boolean inCombat() { return Player.getRSPlayer().isInCombat(); }
	public int getStackBolts() { return Equipment.getItem(SLOTS.ARROW).getID() == boltsID ? Equipment.getItem(SLOTS.ARROW).getStack() : 0;}
	
	public void putMap() {
		for (int i = 0; i < loot.length; i++) {
			if (i >= names.length) {
				map.put(loot[i], names[names.length - 1]);
			} else
				map.put(loot[i], names[i]);
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
	
	public Avies() {
		version = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
	}
	
	public void emergTele(){
		fightStatus = "tele tabbing";
		RSItem[] tab = Inventory.find(VTAB);
		if(useHouse)
			tab = Inventory.find(HTAB);
		GameTab.open(TABS.INVENTORY);
		sleep(100, 150);
		if(tab.length > 0){
			if(tab[0].click("Break")){
				sleep(General.random(5000,  6000));
			}
		}
		else{
			println("Out of tabs");
			scriptStatus = false;
		}
	}
	
	public void HEAL() {
		RSItem[] food = Inventory.find(foodIDs);
		fightStatus = "eating food";
		if(GameTab.getOpen() != TABS.INVENTORY){
			GameTab.open(TABS.INVENTORY); 
			sleep(100, 150);
		}
		
		if (food.length > 0) {
			if(food[0].click("Eat"))
				sleep(300, 550);
		}
		else
			emergTele();
	}
	
	
	public void prayerflick() {// RSNPC drag) {

		while (!moveRandom && getHp() > 30 && Skills.getCurrentLevel(SKILLS.PRAYER) > 5 && isRanging() && inAviesSpot() && !isLoot()) {
			fightStatus = "flicking now!";
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
			}
			Timer t = new Timer(1100L);
			do {
				if (Player.getAnimation() != 4230) {
					sleep(250, 300);
					Prayer.enable(PRAYERS.EAGLE_EYE);
					//Options.setQuickPrayersOn(true);
				} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)) {
					//Options.setQuickPrayersOn(false);
					Prayer.disable(PRAYERS.EAGLE_EYE);
					sleep(350, 400);
				} else {
					sleep(400, 450);
				}
			} while (t.getRemaining() > 0);
		}

		turnOffPrayerEagle();
	}
	
	public void waitForLoot(){
		fightStatus = "prayerflicking";
		if(!isLoot() && isRanging() && inAviesSpot() && getHp() > 30){
			prayerflick();
			//sleep(500, 600);
		}
		turnOffPrayerEagle();
	}
	
	public void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)){
			//Options.setQuickPrayersOn(false);
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			sleep(800,1000);
		}
	}
	
	public String underAttack(){
		RSCharacter[] mon = Combat.getAttackingEntities();
		if(mon.length > 0){
			return mon[0].getName();
		}
		return null;
	}
	
	public boolean inAviesSpot(){
		return inArea(aviesArea);// || inArea(neSSArea);//inArea(sSSArea) || 
	}
	
	public boolean inArea(RSArea a) {
		return a.contains(pos());
	}
	
	public void waitIsMovin(){ 
		for(int i = 0; i < 57; i++, sleep(30, 40)){
			if (!Player.isMoving())
				break;
		}
	}
	
	public void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if(potion[0].click("Drink"))
					General.sleep(1100, 1300);
			}
		}
	}
	
	public void turnOffPrayerProtectionMissles(){
		if (Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES)){
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
				sleep(500,650);
			}
			Prayer.disable(PRAYERS.PROTECT_FROM_MISSILES);
			sleep(800,1000);
		}
	}
	
	private void waitForInv(int lootID, int lootNum) {
		fightStatus = "waiting for loot";
		int k = 0;
		while (lootCount(lootID) == lootNum && k < 70
				&& Player.isMoving()) {
			sleep(80);
			k++;
		}
	}
	
	public boolean aroundPath(RSTile[] path){
		for(int i = 0; i < path.length; i++){
			if(pos().distanceTo(path[i]) <= 4){
				return true;
			}	
		}
		sleep(1000, 1500);
		return false;
	}
	
	public boolean inGW(){
		return pos().distanceTo(gwCenter) <= 50 && pos().getPlane() == 2;
	}
	
	public void alc(int[] loot){
		RSItem[] l = Inventory.find(loot);
		GameTab.open(TABS.MAGIC);
		sleep(200,250);
		Magic.selectSpell("High Level Alchemy");
		sleep(200,250);
		if(l[0].click("Cast"))
			sleep(1000,1200);
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		
	}

	@Override
	public void serverMessageReceived(String arg0) {
		if(arg0.equals("You need a higher slayer level to know how to wound this monster.")){
			moveRandom = true;
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		
	}
	
	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}
	
	private boolean checkActions(RSNPC object, String action) {
		if (object.getActions() != null) {
			for (String s : object.getActions())
				return s.contains(action);
		}
		return false;
	}

	public void clickObject(RSNPC a, String option) {
		RSNPC object = a;
		if (object != null) {
			int x = (int) object.getModel().getEnclosedArea().getBounds()
					.getCenterX();
			int y = (int) object.getModel().getEnclosedArea().getBounds()
					.getCenterY();
			Point p = new Point(x + General.random(-4, 4), y
					+ General.random(-4, 0));
			if (object.getPosition().isOnScreen()) {
				if(ChooseOption.isOpen()){
					ChooseOption.close();
				}
				Mouse.move(p);
//				if(ChooseOption.isOpen()){
//					ChooseOption.close();
//				}
				if (Game.getUptext().contains(option)
						&& (checkActions(object, option)) && Game.getUptext().contains("Aviansie")) {
					Mouse.click(1);
				}
				else if (!Game.getUptext().contains(option)) {
					Mouse.click(3);
					if (ChooseOption.isOpen()
							&& ChooseOption.isOptionValid(option))
						ChooseOption.select(option);
					if (ChooseOption.isOpen()
							&& !ChooseOption.isOptionValid(option)){
						ChooseOption.close();
						Camera.turnToTile(object.getPosition());
					}
				}
			} else {
				if (Player.getPosition().distanceTo(object.getPosition()) > 4)
					WebWalking.walkTo(object.getPosition());
				if (!object.getPosition().isOnScreen())
					Camera.turnToTile(object.getPosition());
				while (Player.isMoving()) {
					sleep(50, 80);
				}
			}
		}
    }
	
	public class AvieGUI extends javax.swing.JFrame {

	    /**
	     * Creates new form AvieGUI
	     */
	    public AvieGUI() {
	        initComponents();
	    }

	    /**
	     * This method is called from within the constructor to initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is always
	     * regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initComponents() {

	        jPanel1 = new javax.swing.JPanel();
	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        foodamount = new javax.swing.JTextField();
	        jLabel3 = new javax.swing.JLabel();
	        foodusing = new javax.swing.JComboBox();
	        jButton1 = new javax.swing.JButton();
	        jLabel4 = new javax.swing.JLabel();
	        telemethod = new javax.swing.JComboBox();

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
	        jLabel1.setText("Yaw hide's Avies Killer");

	        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel2.setText("Food Amount");

	        foodamount.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                foodamountActionPerformed(evt);
	            }
	        });

	        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel3.setText("Food Using");

	        foodusing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "lobster", "salmon", "trout", "shark" }));
	        foodusing.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                foodusingActionPerformed(evt);
	            }
	        });

	        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jButton1.setText("Start");
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel4.setText("Tele method");

	        telemethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "house", "varrock" }));
	        telemethod.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                telemethodActionPerformed(evt);
	            }
	        });

	        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addGap(105, 105, 105)
	                .addComponent(jButton1)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap(40, Short.MAX_VALUE)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jLabel1)
	                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                        .addGroup(jPanel1Layout.createSequentialGroup()
	                            .addComponent(jLabel4)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(telemethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGroup(jPanel1Layout.createSequentialGroup()
	                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addComponent(jLabel2)
	                                .addComponent(jLabel3))
	                            .addGap(63, 63, 63)
	                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addComponent(foodamount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addComponent(foodusing, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
	                .addContainerGap(52, Short.MAX_VALUE))
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jLabel1)
	                .addGap(33, 33, 33)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(foodamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel3)
	                    .addComponent(foodusing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel4)
	                    .addComponent(telemethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
	                .addComponent(jButton1))
	        );

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addContainerGap())
	        );

	        pack();
	    }// </editor-fold>                        

	    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String food = foodusing.getSelectedItem().toString();
	    	switch(food){
	    	case "lobster":
	    		foodIDs = new int[] {379}; break;
	    	case "trout":
	    		foodIDs = new int[] {333}; break;
	    	case "salmon":
	    		foodIDs = new int[] {329}; break;
	    	case "shark":
	    		foodIDs = new int[] {385}; break;
	    	}
	    	foodNum = Integer.parseInt(foodamount.getText());
	    	useHouse = telemethod.getSelectedItem().toString().equals("house") ? true : false;
	        waitGUI = false;
	    }                                       

	    private void foodamountActionPerformed(java.awt.event.ActionEvent evt) {                                           
	        // TODO add your handling code here:
	    }                                          

	    private void foodusingActionPerformed(java.awt.event.ActionEvent evt) {                                          
	        // TODO add your handling code here:
	    }                                         

	    private void telemethodActionPerformed(java.awt.event.ActionEvent evt) {                                           
	        // TODO add your handling code here:
	    }

	    // Variables declaration - do not modify                     
	    private javax.swing.JTextField foodamount;
	    private javax.swing.JComboBox foodusing;
	    private javax.swing.JButton jButton1;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JComboBox telemethod;
	    // End of variables declaration                   
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

}
