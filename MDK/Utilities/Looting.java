package scripts.MDK.Utilities;

import java.util.HashMap;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;

import scripts.MDK.Data.Constants;

public class Looting {

	public static int[] LOOT_IDS = { 11335, 11286, Constants.DIAMOND_E_BOLT, Constants.RUBY_E_BOLT,
			Constants.ADDY_BOLT, 11338, 1615, 1631, 1149, 1249, 2366, 1319, 1201, 985,
			987, 1373, 1163, 1147, 566, 565, 561, 1601, 1617, 2363, 1432, 1247,
			868, 9144 },

	BAD_LOOT = { 443, 1462, 892, 811, 817, },

	CONDITIONAL_LOOT = { 385, 11497, 11499, 11465, 11467, 11465, 11467 },

	CLUE = { 2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 },

	JUNK = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767, 117, 6963, 554, 556,
			829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180, 6965, 1969, 6183,
			6181, 6962, 865, 41 };

	public static String[] LOOT_NAMES = { "Dragon full helm", "Draconic visage",
			"Diamond bolts (e)", "Ruby bolts (e)", "Adamant bolts",
			"Chewed bones", "Dragon med helm", "Dragon spear",
			"Shield left half", "Rune 2h sword", "Rune kiteshield",
			"Dragonstone", "Uncut dragonstone", "Half of a key",
			"Half of a key", "Rune battleaxe", "Rune full helm",
			"Rune med helm", "Soul rune", "Blood rune", "Nature rune",
			"Diamond", "Uncut diamond", "Runite bar", "Rune mace",
			"Rune spear", "Rune knife", "Runite bolts" },

	BAD_LOOT_NAMES = { "Silver ore", "Nature talisman", "Rune arrow", "Rune dart",
			"Rune dart(p)" },

	CONDITIONAL_LOOT_NAMES = { "Shark", "Super defence mix(2)", "Super defence mix(1)",
			"Prayer mix(2)", "Prayer mix(1)", "Restore prayer mix(2)",
			"Restore prayer mix(1)" },

	CLUE_NAME = { "Clue scroll (hard)" },

	PRIORITY_LOOT_NAMES = { "Dragon full helm", "Draconic visage" };

	public static HashMap<Integer, String> LOOT_MAP = new HashMap<Integer, String>(
			LOOT_IDS.length),
			BAD_LOOT_MAP = new HashMap<Integer, String>(BAD_LOOT.length),
			CONDITIONAL_LOOT_MAP = new HashMap<Integer, String>(
					CONDITIONAL_LOOT.length),
			CLUE_MAP = new HashMap<Integer, String>(CLUE.length);
	
	public static void putMap() {
		for (int i = 0; i < LOOT_IDS.length; i++) {
			LOOT_MAP.put(LOOT_IDS[i], LOOT_NAMES[i]);
		}
		for (int j = 0; j < CONDITIONAL_LOOT.length; j++) {
			CONDITIONAL_LOOT_MAP.put(CONDITIONAL_LOOT[j], CONDITIONAL_LOOT_NAMES[j]);
		}
		for (int k = 0; k < CLUE.length; k++) {
			CLUE_MAP.put(CLUE[k], CLUE_NAME[0]);
		}
		for (int l = 0; l < BAD_LOOT.length; l++) {
			BAD_LOOT_MAP.put(BAD_LOOT[l], BAD_LOOT_NAMES[l]);
		}
	}
	
	public boolean checkRare() {
		RSGroundItem[] priority = GroundItems.findNearest(priorityLoot);
		if (priority.length > 0) {
			if (!priority[0].isOnScreen()) {
				Walking.clickTileMM(priority[0].getPosition(), 1);
				sleep(100, 150);
				Camera.turnToTile(priority[0].getPosition());
				sleep(200, 300);
				waitIsMovin();
			}
			String str = map.get(priority[0].getID());
			if(priority[0].click("Take " + str)){
			waitForInv(priority[0].getID());
			return true;
			}
		}
		return false;
	}
	
	public boolean checkLootInArea(RSGroundItem g){
		if(Tiles.mithDragSpawn1.contains(g.getPosition()))
			return true;
		return false;
	}
	
	public boolean isLoot(){
		RSGroundItem[] p1 = GroundItems.findNearest(loot);
		RSGroundItem[] p2 = GroundItems.findNearest(badLoot);
		RSGroundItem[] p3 = GroundItems.findNearest(clue);
		RSGroundItem[] dbone = GroundItems.findNearest(536);
		RSGroundItem[] mbar = GroundItems.findNearest(2359);
		return !(p1.length == 0 && p2.length == 0 && p3.length == 0
				&& dbone.length == 0 && mbar.length == 0);
	}
	
	public boolean makeRoomForDrop(){
		RSItem[] food = Inventory.find(foodIDs);
		RSItem[] dbone = Inventory.find("Dragon bones");		
		
		if((Combat.getMaxHP() - Combat.getHP()) > 15) {
			if(food.length > 0){
				if(food[0].click("Eat")){
					sleep(General.random(800, 1000));
					return true;
				}
			}
		}
		else if (Inventory.isFull() && dbone.length > 0){
			if(dbone[0].click("Bury")){
				sleep(General.random(800, 1000));
				return true;
			}
		}
		return false;
	}
	
	
	
	public boolean gotRareDrop(){
		RSItem[] RARE = Inventory.find(priorityLoot);
		if(RARE.length > 0){
			emergTele();
			sleep(3000,4000);
			println("You just got a visage or a dragon full helm YOU LUCKY BITCH!!!!!!!!");
			println("Please take a screenshot and post it on my thread! ty");
			println("Stopping the script so you can get a nice surprice when you login");
			
			scriptStatus = false;
			return true;
		}
		return false;
	}
	//TODO lootOrder
	public void lootOrder(){
		RSGroundItem[] Nests = GroundItems.findNearest(loot);
		RSGroundItem[] Nests2 = GroundItems.findNearest(clue);
		RSGroundItem[] Nests3 = GroundItems.findNearest(badLoot);
		RSGroundItem[] dbone = GroundItems.findNearest(536);
		RSGroundItem[] mbar = GroundItems.findNearest(2359);
		
		
		Mouse.setSpeed(General.random(200, 230));
		Inventory.drop(junk);
		sleep(100,150);
		Mouse.setSpeed(General.random(120,140));
		
		lootBolts();
		
		makeRoomForDrop();
				
		if(!Inventory.isFull()){
			if (Nests.length > 0) {
				String str = map.get(Nests[0].getID());
				if (Nests[0].click("Take " + str)){
					waitForInv(Nests[0].getID());
					if(!str.equals("Adamant bolts"))
						lastDrop = str;
				}
			} 
			else if (Nests2.length > 0) {
				String str = mapClue.get(Nests2[0].getID());
				if (Nests2[0].click("Take " + str)){
					waitForInv(Nests2[0].getID());
					lastDrop = str;
				}
			} 
			else if (Nests3.length > 0) {
				String str = mapBad.get(Nests3[0].getID());
				if (Nests3[0].click("Take " + str)){
					waitForInv(Nests3[0].getID());
					lastDrop = str;
				}
			}
			else if (dbone.length > 0){
				if (dbone[0].click("Take Dragon bones"))
					waitForInv(536);
				}
			else if (mbar.length > 0){
				if (mbar[0].click("Take Mithril bar"))
					waitForInv(2359);
			}
		}
		
	}
	
	//TODO lootBolts
	public void lootBolts(){
		RSItem[] ruby = Inventory.find(rubyEBolt);
		RSGroundItem[] rubyG = GroundItems.findNearest(rubyEBolt);
		RSGroundItem[] diaG = GroundItems.findNearest(diamondEBolt);
		
		RSItem[] addy = Inventory.find(addyBolt);
		RSGroundItem[] addyG = GroundItems.findNearest(addyBolt);
		
		if (useSpecialBolts) {
			if (rubyG.length > 0) {
				if (!rubyG[0].isOnScreen()) {
					Walking.blindWalkTo(rubyG[0].getPosition());
					waitIsMovin();
				}
				if (rubyG[0].click("Take Ruby bolts (e)")) {
					waitForInv(rubyEBolt);
					sleep(100, 120);
					ruby = Inventory.find(rubyEBolt);
				}
			}
			ruby = Inventory.find(rubyEBolt);
			if (rubyG.length == 0 && ruby.length > 0) {
				if (ruby[0].click("Wield"))
					sleep(100, 150);
			}
			if (diaG.length > 0) {
				if (diaG[0].click("Take Diamond bolts (e)")) {
					waitForInv(diamondEBolt);
					sleep(100, 120);
				}
			}
		}
		else{
			if(addyG.length > 0){
				if (!addyG[0].isOnScreen()) {
					Walking.blindWalkTo(addyG[0].getPosition());
					waitIsMovin();
				}
				if (addyG[0].click("Take Adamant bolts")) {
					waitForInv(addyBolt);
					sleep(100, 120);
					ruby = Inventory.find(addyBolt);
				}
			}
			ruby = Inventory.find(addyBolt);
			if(addy.length > 0) {
				if (addy[0].click("Wield"))
					sleep(100, 150);
			}
		}
}
}
