package scripts.slayer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tribot.api2007.Prayer;
import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;


@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", name = "Yaw hide's BDK", version = 0.63, description="Local version")
public class BDK extends Script implements Painting, Pausing {
	  
	// loot
	int[] junk = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767, 117,
			6963, 554, 556, 829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180,
			6965, 1969, 6183, 6181, 6962, 449, 1197, 243 };

	int[] loot = { 1369, 816, 
			9142, 1751, 536,  
			868, 563,1615, 
			1247, 1303, 1249, 
			1123, 1149,  
			561, 563, 2361, 2366, 
			1462, 985, 987, 
			2363, 1617, 1619,
			1161, 1213, 2363, 1642, 207, 209, 211, 213, 215, 217, 219, 2485, 3049, 3051,
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
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049};
	
	int[] loot2 = { 1369, 816, 
			1751, 536, 
			868, 563,1615, 
			1247, 1303, 1249, 
			1123, 1149,
			561, 563, 2361, 2366, 
			1462, 985, 987, 
			2363, 1617, 1619, 
			1161, 1213, 2363, 1642, 207, 209, 211, 213, 215, 217, 219, 2485, 3049, 3051,
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
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049};

	
	HashMap<Integer, String> map = new HashMap<Integer, String>(loot.length);
	
	String[] names = { "Mithril battleaxe", "Adamant dart(p)",
			"Mithril bolts", "Blue dragonhide", "Dragon bones", 
			"Rune knife", "Law rune", "Dragonstone",
			"Rune spear", "Rune longsword", "Dragon spear",
			"Adamant platebody", "Dragon med helm",
			"Nature rune", "Law rune", "Adamantite bar", "Shield left half",
			"Nature talisman", "Tooth half of key", "Loop half of key",
			"Runite bar", "Uncut diamond", "Uncut ruby", 
			"Adamant full helm", "Rune dagger", "Rune bar", "Nature talisman"
			, "Herb", "Herb", "Herb", "Herb", "Herb", "Herb"
			, "Herb", "Herb", "Herb", "Herb", "Clue scroll (hard)"};

	public void putMap() {
		for (int i = 0; i < loot.length; i++) {
			if (i >= names.length) {
				map.put(loot[i], names[names.length - 1]);
			} else
				map.put(loot[i], names[i]);
		}
	}
	
	// RSTile for paths
	RSTile[] toTavernlyDungPath = { new RSTile(2928, 3357, 0),
			new RSTile(2925, 3363, 0), new RSTile(2921, 3370, 0),
			new RSTile(2914, 3373, 0), new RSTile(2907, 3377, 0),
			new RSTile(2901, 3381, 0), new RSTile(2895, 3386, 0),
			new RSTile(2892, 3391, 0), new RSTile(2885, 3392, 0),
			new RSTile(2884, 3396, 0) };
	
	// RSTiles
	RSTile[] toTavLadderDown = { new RSTile(2877, 3403, 0), 
			new RSTile(2935, 3346, 0) };
	RSTile[] fallyA = {new RSTile(2936, 3390, 0), new RSTile(3030, 3323, 0) };
	RSTile[] bankA = {new RSTile(2949, 3368, 0), new RSTile(2943, 3368, 0), new RSTile(2943, 3371, 0),
			new RSTile(2949, 3371, 0)};
	RSTile[] lowWallA = {new RSTile(2936, 3358, 0), new RSTile(2940, 3358, 0), new RSTile(2940, 3352, 0),
			new RSTile(2936, 3352, 0)};
	RSTile[] tavLadderA = {new RSTile(2881, 3400, 0), new RSTile(2888, 3393, 0)};
	RSTile[] afterTavernlyLadderA = {new RSTile(2881, 9801, 0), new RSTile(2887, 9794, 0)};
	RSTile[] blueDragA = { new RSTile(2889, 9787, 0), new RSTile(2889, 9813, 0),
			new RSTile(2924, 9812, 0), new RSTile(2924, 9787, 0) };
	RSTile[] sSSA = {new RSTile(2893, 9792, 0), new RSTile(2894, 9791, 0)};
	RSTile[] nwSSA = {new RSTile(2900, 9809, 0), new RSTile(2902, 9810, 0)};
	RSTile[] neSSA = {new RSTile(2903, 9808, 0), new RSTile(2904, 9808, 0), 
			new RSTile(2904, 9810, 0), new RSTile(2900, 9810, 0),
			new RSTile(2900, 9809, 0), new RSTile(2903, 9809, 0)};
	
	//Positionables
	Positionable bankT = new RSTile(2946, 3369, 0);
	Positionable faladorTeleT = new RSTile(2965, 3376, 0);
	Positionable lowWallT = new RSTile(2935, 3355, 0);
	Positionable lowWall = new RSTile(2938, 3355, 0);
	Positionable afterLowWall = new RSTile(2934, 3355, 0);
	Positionable tavernlyLadder = new RSTile(2884, 3396, 0);
	Positionable tavernlyLadderT = new RSTile(2884, 3397, 0);
	Positionable afterTavernlyLadderT = new RSTile(2884, 9798, 0);
	
	Positionable sSS = new RSTile(2894, 9792, 0);
	Positionable clickSSS = new RSTile(2894, 9791, 0);
	
	RSTile neSS = new RSTile(2903, 9808, 0);
	Positionable clickNESS = new RSTile(2903, 9808, 0);
	
	//RSAreas
	RSArea fallyArea = new RSArea(fallyA[0], fallyA[1]);
	RSArea toTavLadderDownArea = new RSArea(toTavLadderDown[0], toTavLadderDown[1]);
	RSArea bankArea = new RSArea(bankA);
	RSArea lowWallArea = new RSArea(lowWallA);
	RSArea tavladderArea = new RSArea(tavLadderA[0], tavLadderA[1]);
	RSArea afterTavernlyLadderArea = new RSArea(afterTavernlyLadderA[0], afterTavernlyLadderA[1]);
	RSArea blueDragArea = new RSArea(blueDragA);
	RSArea sSSArea = new RSArea(sSSA[0], sSSA[1]);
	RSArea nwSSArea = new RSArea(nwSSA[0], nwSSA[1]);
	RSArea neSSArea = new RSArea(neSSA);
	
	
	// Variables
	final int[] dragIDs = {2964, 2961, 250, 247, 335, 332, 288, 285};
	final int[] dragWithBabyArr = {2964, 2961, 2938, 2939, 2937, 250, 247, 249, 224, 225, 223, 308, 309, 310, 334, 335, 332, 
			288, 285, 262, 261,263 };
	final int drag247 = 285; // east drag
	int[] foodIDs  = { };//, 385, 7946, 1897 };
	int[] ppot = { 2434, 139, 141, 143 }; 
	int[] rangepots = { 169, 2444, 171, 173 };
	int boltsID = 9142;
	long antiban = System.currentTimeMillis();
	int startXp = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.RANGED);
	double version;
	int current_xp;
	final long start_time = System.currentTimeMillis();
	double XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
	double XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	int startLv = Skills.getActualLevel(SKILLS.RANGED) + Skills.getActualLevel(SKILLS.HITPOINTS);
	final String[] type = {"Defence", "Ranged", "Hitpoints" };
	final SKILLS[] Names = {SKILLS.DEFENCE, SKILLS.RANGED, SKILLS.HITPOINTS, };
	final int[] XP = { Skills.getXP(SKILLS.DEFENCE), Skills.getXP(SKILLS.RANGED), 
			Skills.getXP(SKILLS.HITPOINTS) };
	int[] startLvs = { 	Skills.getActualLevel(SKILLS.DEFENCE), Skills.getActualLevel(SKILLS.RANGED), 
			Skills.getActualLevel(SKILLS.HITPOINTS) };
	boolean scriptStatus = true;
	int foodNum = 2;
	int dragS = 249;
	int dragNW = 247;
	int dragNE = 250;
	
	int dhides,dbones = 0;
	int dhidecount, dbonecount = 0;
	int dhidesIni, dbonesIni = 0;
	int dhideprice = 1775;
	int dboneprice = 1800;
	int mithprice;
	int runedaggerprice;
	int natprice;
	boolean waitGUI = true;
	
	String fightStatus;
	
	@Override
	public void run() {
		
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			println("You must be at least 70 range to use this script");
			scriptStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			println("You must be at least 44 prayer to use this script");
			scriptStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 70){
			println("You must be at least 70 agility to use this script");
			scriptStatus = false;
		}
		
		boolean devmode = false;
		
		if(devmode){
			foodNum = 2;
			foodIDs = new int[] {379};
		}
		else{
			AvieGUI g = new AvieGUI();
			g.setVisible(true);
			while (waitGUI)
				sleep(500);
			g.setVisible(false);
		}
		
		putMap();
		
		dhidesIni = Inventory.getCount(536);
		dbonesIni = Inventory.getCount(1751);
		dhideprice = Zybez.getPrice("Blue dragonhide");
		dboneprice = Zybez.getPrice("Dragon bones");
		sleep(250);
		Mouse.setSpeed(General.random(175,190));
		Walking.setWalkingTimeout(5000L);
		General.useAntiBanCompliance(true);
		
		while(scriptStatus){
			current_xp = Skills.getXP(SKILLS.RANGED) + Skills.getXP(SKILLS.HITPOINTS);
			XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
			XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
			
			
			dhides = Inventory.getCount(536) - dhidesIni;
			dbones = Inventory.getCount(1751) - dbonesIni;
			
			if(Inventory.find("Falador teleport").length > 0 && Inventory.find("Falador teleport")[0].getStack() == 1){
				println("Ran out of ftabs...");
				emergTele();
				scriptStatus = false;
			}
			
			if(Equipment.getItem(SLOTS.ARROW).getStack() < 100){
				println("Ran out of bolts");
				emergTele();
				scriptStatus = false;
			}
			
			if(Game.getRunEnergy() > 50) {
			    Options.setRunOn(true);
			}
			
			if(gotoDrag()){
				fight();
			}
		}
	}
	
	public boolean gotoDrag(){
		if (inArea(blueDragArea))
			return true;
		else if (inArea(lowWallArea)){
			fightStatus = "doing low wall";
			RSObject[] LOWWALL = Objects.getAt(lowWallT);
			if (LOWWALL.length > 0){
				if(LOWWALL[0].click("Climb-over")){
					sleep(5000,6000);
				}
			}
		}
		else if (inArea(fallyArea)){
			Walking.setWalkingTimeout(1000L);
			if(Inventory.find(foodIDs).length == foodNum && Inventory.find("Falador teleport").length > 0
					&& Inventory.find(loot).length == 0 && Inventory.find(rangepots).length > 0) {
				fightStatus = "walking to low wall";
				WebWalking.walkTo(lowWall);
				waitIsMovin();
			}
			else if(inArea(bankArea)){
				fightStatus = "banking";
				bank();
			}
			else{
				fightStatus = "walking to bank";
				WebWalking.walkTo(bankT);
				waitIsMovin();
			}
		}
		else if (inArea(tavladderArea)){
			fightStatus = "clicking down ladder";
			RSObject[] TAVLAD = Objects.getAt(tavernlyLadderT);
			if (TAVLAD.length > 0){
				if(TAVLAD[0].isOnScreen()){
					if(TAVLAD[0].click("Climb-down")){
						sleep(4000,5000);
					}
				}
				else{
					fightStatus = "walking to down ladder";
					Walking.clickTileMM(TAVLAD[0].getPosition(), 1);
					waitIsMovin();
				}
			}
		}
		else if (inArea(toTavLadderDownArea)){
			fightStatus = "walking to tavernly";
			Walking.walkPath(toTavernlyDungPath);
			waitIsMovin();
		}
		else if (inArea(afterTavernlyLadderArea)){
			fightStatus = "doing pipe";
			RSObject[] pipe = Objects.findNearest(7, "Obstacle pipe");
			if(pipe.length > 0){
				if(pipe[0].isOnScreen()){
					if(pipe[0].click("Squeeze-through")){
						Timing.waitCondition(new Condition() {
							;
							@Override
	    					public boolean active() {
	    						return inArea(blueDragArea);
	    					}
	    				}, General.random(5000, 6000));
					}
				}
				else{
					Walking.clickTileMM(pipe[0].getPosition(), 1);
					waitIsMovin();
				}
			}
		}
		else{
			println("we are somewhere unsupported, gonna web walk to bank");
			WebWalking.walkTo(bankT);
			waitIsMovin();
		}
		return false;
	}
	
	public void fight(){
		RSNPC[] drag = NPCs.findNearest(dragIDs);
		RSNPC[] dragWithBaby = NPCs.findNearest(dragWithBabyArr);
		Walking.setWalkingTimeout(1500L);
		
		if(Inventory.isFull() || (Inventory.getAll().length == 27 && !isLoot())){
			println("Inv is full");
			ifFull();
		}
		else if(isLoot()){
			println("looting...");
			LOOT();
		}
		else if (inCombat() && getHp() > 50){
			println("under attack somewhere");
			fightStatus = "underattack";
			if (dragWithBaby.length > 0){
				for(RSNPC d : dragWithBaby){
					if(d.isInteractingWithMe()){
						if(!inSafeSpot()){
							println("under attack, not in safe spot");
							gotoSafeSpot();
						}
						else if(isRanging()){
							println("under attack, is ranging");
							sleep(1000,1200);
						}
						else{
							println("attack baby dragon");
							if(d.getPosition().isOnScreen()){
								if (clickNPC(d, "Attack")) {
									final RSNPC tmp_blkdrag = d;
									Timing.waitCondition(new Condition() {
										public boolean active() {
											return Player.getAnimation() == 4230
													|| inCombat()
													|| tmp_blkdrag.isInCombat()
													|| !inSafeSpot();
										}
									}, General.random(2200, 2400));
									for (int i = 0; i < 57; i++, sleep(30, 40)) {
										if (!inSafeSpot()) {
											println("attacked the dragon, running to safety!");
											gotoSafeSpot();
											break;
										}
									}
								}
							}
							else{
								Camera.turnToTile(d.getPosition());
								Camera.setCameraAngle(General.random(50, 70));
							}
						}
						break;
					}
					else if(!inSafeSpot()){
						println("under attack, not in safe spot");
						gotoSafeSpot();
					}
				}
			}
			else{
				gotoSafeSpot();
				waitIsMovin();
			}
		}
		else if (inSafeSpot()){
			if (NPCChat.clickContinue(true)){
				println("clicking to continue");
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
			else if(isRanging()){
				waitForLoot();
				
				//sleep(2000,4000);
			}
			else{
				if(drag.length > 0){
					if(Camera.getCameraRotation() < 100 || Camera.getCameraRotation() > 270){
						Camera.setCameraRotation(General.random(140, 180));
					}
					RSNPC d = drag[0];
					if (!d.isInCombat() || (d.isInCombat() && d.isInteractingWithMe()) || d.isInteractingWithMe()) {
						println("d not in combat, or is interacting with me or is in combat and interacting with me");
						fightStatus = "attacking dragon " + d.getID();
						if(d.getPosition().isOnScreen()){
							if (clickNPC(d, "Attack")) {
								final RSNPC tmp_blkdrag = d;
								Timing.waitCondition(new Condition() {
									public boolean active() {
										return Player.getAnimation() == 4230
												|| inCombat()
												|| tmp_blkdrag.isInCombat()
												|| !inSafeSpot();
									}
								}, General.random(2200, 2400));
								for (int i = 0; i < 57; i++, sleep(30, 40)) {
									if (!inSafeSpot()) {
										println("attacked the dragon, running to safety!");
										gotoSafeSpot();
										break;
									}
								}
							}
							else{
								sleep(500,700);
							}
						}
						else {
							println("turning to face dragon");
							Camera.turnToTile(d.getPosition());
							Camera.setCameraAngle(General.random(50, 70));
						}
					}
					else if (drag.length > 1){
						d = drag[1];
						if (!d.isInCombat() || (d.isInCombat() && d.isInteractingWithMe()) || d.isInteractingWithMe()) {
							println("d not in combat, or is interacting with me or is in combat and interacting with me");
							fightStatus = "attacking dragon " + d.getID();
							if(d.getPosition().isOnScreen()){
								if (clickNPC(d, "Attack")) {
									final RSNPC tmp_blkdrag = d;
									Timing.waitCondition(new Condition() {
										public boolean active() {
											return Player.getAnimation() == 4230
													|| inCombat()
													|| tmp_blkdrag.isInCombat()
													|| !inSafeSpot();
										}
									}, General.random(2200, 2400));
									for (int i = 0; i < 57; i++, sleep(30, 40)) {
										if (!inSafeSpot()) {
											println("attacked the dragon, running to safety!");
											gotoSafeSpot();
											break;
										}
									}
								}
								else{
									sleep(500,700);
								}
							}
							else {
								println("turning to face dragon");
								Camera.turnToTile(d.getPosition());
								Camera.setCameraAngle(General.random(50, 70));
							}
						}
					}
				}
			}
		}
		else if (!inSafeSpot()){
			println("end case, not in safespot");
			gotoSafeSpot();
		}
		else{
			println("doing nothing...");
			sleep(1000);
			//println("else case...");
			//gotoSafeSpot();
		}
	}
	
	public boolean isLoot(){
		RSGroundItem[] Nests = GroundItems.findNearest(loot2);
		return Nests.length > 0 && blueDragArea.contains(Nests[0].getPosition());
	}
		
	public void LOOT() {
		fightStatus = "looting";
		
		turnOffPrayer();
		
		
		RSGroundItem[] Nests = GroundItems.findNearest(loot);
		if (getHp() <= 50) {
			if (Inventory.getCount(foodIDs) == 0) {
				emergTele();
			} 
			else
				HEAL();
		}
		
		for(int i = 0; i < Nests.length; i++){
			if(Nests[i].getID() == 9142){
				if (Nests[i].getStack() > 9) {
					if (!Nests[i].isOnScreen()) {
						Walking.clickTileMM(Nests[i].getPosition(), 1);
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
					Walking.clickTileMM(Nests[i].getPosition(), 1);
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
		println("banking...");
		
		if(Banking.openBankBooth()){
			if(Banking.isPinScreenOpen())
				Banking.inPin();
			else if (Banking.isBankScreenOpen()){
				
				dhidecount += dhides;
				dbonecount += dbones;
				dhidesIni = 0;
				dbonesIni = 0;
				
				Banking.depositAllExcept("Falador teleport");
				sleep(100,150);
				
				
				int pt = Skills.getActualLevel(SKILLS.PRAYER) / 3;
				int currP = Skills.getCurrentLevel(SKILLS.PRAYER);
				
				RSItem[] pPot;
				if( currP < pt * 2){
					println("potting prayer, pt is: "+pt);
					
					Banking.withdraw(1, ppot);
					sleep(600,640);
					Banking.close();
					sleep(200,230);
					
					pPot = Inventory.find(ppot);
					if(pPot.length > 0){
						do{
							pPot[0].click("Drink");
							sleep(1000,1200);
						}while(Skills.getCurrentLevel(SKILLS.PRAYER) <= (pt*2)-2);
					}
					sleep(200,250);
					if (!Banking.isBankScreenOpen()) {
						if (Banking.openBankBanker()) {
							sleep(200, 250);
							Banking.deposit(1, ppot);
							sleep(200,250);
						}
					}
				}
				sleep(200,300);
				
				if(Banking.find(foodIDs).length == 0 || 
						(Inventory.find("Falador teleport").length == 0 && 
						Banking.find("Falador teleport").length == 0)){
					println("Ran out of food");
					scriptStatus = false;
				}
				
				Banking.withdraw(1, rangepots);
				sleep(600, 650);
				
				Banking.withdraw(foodNum+((Combat.getMaxHP() - Combat.getHP()) / 7), foodIDs);
				sleep(100,150);
				if(Inventory.find("Falador teleport").length == 0){
					Banking.withdraw(10, "Falador teleport");
					sleep(100,150);
				}
				Banking.close();
				dbonesIni = 0; 
				dhidesIni = 0;
				while(Inventory.find(foodIDs).length > foodNum){
					Inventory.find(foodIDs)[0].click("Eat");
					sleep(300);
				}
			}
		}
	}
	
	@Override
	public void onPaint(Graphics g) {
		
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
		
		int dh, db;
		dh = dhides + dhidecount;
		db = dbones + dbonecount;
		g.setColor(new Color(60, 60, 60));
		g.fillRect(340, 370, 200, 150);

		g.setColor(Color.WHITE);
		g.drawString("Yawhide's BDK", 345, 385);
		g.drawString("Version :" + version + "   Curr world: " + Game.getCurrentWorld(), 345, 405);
		g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
		g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour
				+ "/h)", 345, 435);
		g.drawString("State: " + fightStatus, 345, 450);
		g.drawString("Dbones:" + dh + "       Dhide: " + db, 345, 465);
		g.drawString("Gp/hr: " + (int) ((dh*dhideprice + db*dboneprice)/hoursRan/1000) + " K   Total: " + (int)(dh*dhideprice + db*dboneprice)/1000, 345, 480);
		//g.drawString("Ini: " + dhidesIni + " " + dbonesIni + "  Reg: " + dhides + " " + dbones, 345, 495);
		g.drawString("In safespot? " + inSafeSpot(), 345, 495);
		g.setColor(Color.CYAN);
		for(int i = 0; i < Combat.getAttackingEntities().length;i++){
			RSCharacter tmp = Combat.getAttackingEntities()[i];
			drawTile(new RSTile(tmp.getPosition().getX()-1, tmp.getPosition().getY()-1), g, false);
			g.setColor(Color.GREEN);
			drawTile(new RSTile(tmp.getPosition().getX(), tmp.getPosition().getY()-1), g, false);
		}
		g.setColor(Color.RED);
		for(int i = 0; i < neSSA.length; i++){
			drawTile(neSSA[i], g, false);
		}
			
	}
	
	public RSTile pos(){ return Player.getPosition(); }
	public boolean isRanging() { return Player.getRSPlayer().getInteractingCharacter() != null; }
	public int distanceTo(RSTile t){ return Math.max(pos().getX()-t.getX(), pos().getY() - t.getY()); }
	public int getHp() { return Combat.getHPRatio(); }
	public boolean isFull() { return Inventory.isFull(); }
	public int lootCount(int ID) { return Inventory.find(ID).length; }
	public boolean isMovin() { return Player.isMoving();}
	public boolean inCombat() { return Player.getRSPlayer().isInCombat(); }
	
	public void ifFull(){
		RSItem[] coin = Inventory.find("Coins");
		RSItem[] food = Inventory.find(foodIDs);
		RSItem[] bolts = Inventory.find("Mithril bolts");
		if(Inventory.find(junk).length > 0 || food.length > 0 || coin.length > 0 || bolts.length > 0){
			fightStatus = "dropping junk";
			
			if(coin.length > 0 && coin[0].getStack() < 1500){
				Inventory.drop(coin[0].getID());
				sleep(150,200);
			}
			else if (food.length > 0){
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

	public void gotoSafeSpot(){
		fightStatus = "going to safe spot";
		Walking.setWalkingTimeout(1500L);
		if(neSS.isOnScreen()){
			println("clicking on screen");
			Walking.clickTileMS(clickNESS, 1);
		}
		else if (pos().distanceTo(clickNESS) >= 7){
			println("generating a path");
			Walking.walkPath(Walking.generateStraightPath(clickNESS)); //safeSpotPath);//
		}
		else{
			println("clicking minimap");
			Walking.clickTileMM(clickNESS, 1);
		}	
		println("clicked, now gonna wait till I am not moving anymore");
		waitIsMovin();
	}
	
	public void waitForLoot(){
		fightStatus = "prayerflicking";
		while(!isLoot() && isRanging() && inSafeSpot() && !isMovin()){
			
			prayerflick();
		}
		turnOffPrayer();
	}
	
	public String underAttack(){
		RSCharacter[] mon = Combat.getAttackingEntities();
		if(mon.length > 0){
			return mon[0].getName();
		}
		return null;
	}
	
	public boolean inSafeSpot(){
		return inArea(neSSArea);// || inArea(neSSArea);//inArea(sSSArea) || 
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
	
	public void prayerflick(){//RSNPC drag) {
		
		while (getHp() > 30 &&	Prayer.getPrayerPoints() > 5 && isRanging() && inSafeSpot() && !isLoot()) {
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
		turnOffPrayer();
	}
	
	public void turnOffPrayer(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)){
			//Options.setQuickPrayersOn(false);
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			sleep(800,1000);
		}
	}
	
	public int getStackBolts() {
		return Equipment.getItem(SLOTS.ARROW).getID() == boltsID ? Equipment.getItem(SLOTS.ARROW).getStack() : 0;
	}
	
	public void emergTele(){
		fightStatus = "tele tabbing";
		RSItem[] tab = Inventory.find("Falador teleport");
		GameTab.open(TABS.INVENTORY);
		sleep(100, 150);
		if(tab.length > 0){
			if(tab[0].click("Break")){
				sleep(General.random(4000,  5000));
			}
		}
		else{
			println("Out of f tabs");
			scriptStatus = false;
		}
	}
	
	public void HEAL() {
		fightStatus = "eating food";
		GameTab.open(TABS.INVENTORY); sleep(100, 150);
		RSItem[] food = Inventory.find(foodIDs);
		if (food.length > 0) {
			if(food[0].click("Eat"))
				sleep(300, 550);
		}
		else
			emergTele();
	}
	
	private void waitForInv(int lootID, int lootNum) {
		fightStatus = "waiting for loot";
		int k = 0;
		while (lootCount(lootID) == lootNum && k < 200
				&& Player.isMoving()) {
			sleep(80);
			k++;
		}
		if (lootCount(lootID) > lootNum) {
			if (lootID == 1751)
				dhides++;
			else if (lootID == 536)
				dbones++;
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
	
	public BDK() {
		version = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
	}

	@Override
	public void onPause() {
		if(inArea(blueDragArea)){
			if(!inSafeSpot())
				gotoSafeSpot();
		}
		turnOffPrayer();
	}

	@Override
	public void onResume() {
		if(!inSafeSpot())
			gotoSafeSpot();
	}
	
	private boolean clickNPC(RSNPC npc, String option) {

		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			if(npc.getID() == drag247){
				loc = new RSTile(npc.getPosition().getX()-1, npc.getPosition().getY()-1);
			}
			else{
				loc = new RSTile(npc.getPosition().getX(), npc.getPosition().getY()-1);
			}
			Mouse.move(Projection.tileToScreen(loc, 10));
			if(Game.isUptext("Walk here / 2 more options")){
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
			else if (Game.isUptext(option)) {
				Mouse.click(1);
				waitForDrag(npc);
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
	
	public void waitForDrag(RSNPC drag) {

		while (drag.isInCombat() && inSafeSpot() && !inCombat())
			sleep(1000, 1300);
	}
	
	public void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if(Clicking.click("Drink", potion[0])){
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return Skills.getCurrentLevel(SKILLS.RANGED) > Skills.getActualLevel(SKILLS.RANGED);
						}
					}, General.random(1000, 1200));
				}
			}
		}
	}

	public void waitForItem(int[] i) {
		final int[] item = i;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return Inventory.getCount(item) > 0;
			}
		}, General.random(500, 1000));
	}

	public void waitForItem(int i) {
		final int item = i;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return Inventory.getCount(item) > 0;
			}
		}, General.random(500, 1000));
	}

	public void waitForTab(TABS t) {
		final TABS tab = t;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return GameTab.getOpen() == tab;
			}
		}, General.random(500, 1000));
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
	        jLabel1.setText("Yaw hide's BDK");

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

	        telemethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "falador" }));
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
	    	//useHouse = telemethod.getSelectedItem().toString().equals("house") ? true : false;
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
	
}
