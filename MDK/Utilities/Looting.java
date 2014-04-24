package scripts.MDK.Utilities;

import java.util.HashMap;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;

import scripts.MDK.Data.Constants;
import scripts.MDK.Data.Tiles;
import scripts.MDK.Main.MDKGui;
import scripts.MDK.Main.MithDK;

public class Looting {

	public static int[] LOOT_IDS = { 11335, 11286, Constants.DIAMOND_E_BOLT,
			Constants.RUBY_E_BOLT, Constants.ADDY_BOLT, Constants.BROAD_BOLT, 11338, 1615, 1631,
			1149, 1249, 2366, 1319, 1201, 985, 987, 1373, 1163, 1147, 566, 565,
			561, 1601, 1617, 2363, 1432, 1247, 868, 9144 },

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

	public static String[] LOOT_NAMES = { "Dragon full helm",
			"Draconic visage", "Diamond bolts (e)", "Ruby bolts (e)",
			"Adamant bolts", "Broad bolts", "Chewed bones", "Dragon med helm", "Dragon spear",
			"Shield left half", "Rune 2h sword", "Rune kiteshield",
			"Dragonstone", "Uncut dragonstone", "Tooth half of key",
			"Loop half of key", "Rune battleaxe", "Rune full helm",
			"Rune med helm", "Soul rune", "Blood rune", "Nature rune",
			"Diamond", "Uncut diamond", "Runite bar", "Rune mace",
			"Rune spear", "Rune knife", "Runite bolts" },

	BAD_LOOT_NAMES = { "Silver ore", "Nature talisman", "Rune arrow",
			"Rune dart", "Rune dart(p)" },

	CONDITIONAL_LOOT_NAMES = { "Shark", "Super defence mix(2)",
			"Super defence mix(1)", "Prayer mix(2)", "Prayer mix(1)",
			"Restore prayer mix(2)", "Restore prayer mix(1)" },

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
			CONDITIONAL_LOOT_MAP.put(CONDITIONAL_LOOT[j],
					CONDITIONAL_LOOT_NAMES[j]);
		}
		for (int k = 0; k < CLUE.length; k++) {
			CLUE_MAP.put(CLUE[k], CLUE_NAME[0]);
		}
		for (int l = 0; l < BAD_LOOT.length; l++) {
			BAD_LOOT_MAP.put(BAD_LOOT[l], BAD_LOOT_NAMES[l]);
		}
	}

	public static boolean checkRare() {
		RSGroundItem[] priority = GroundItems.findNearest(PRIORITY_LOOT_NAMES);
		if (priority.length > 0) {
			if (!priority[0].isOnScreen()) {
				Walking.clickTileMM(priority[0].getPosition(), 1);
				Conditionals.waitFor(Utils.pos() == priority[0].getPosition(),
						3000, 4000);
				Camera.turnToTile(priority[0].getPosition());
				Utils.waitIsMovin();
			}
			String str = LOOT_MAP.get(priority[0].getID());
			if (Clicking.click("Take " + str, priority[0])) {
				final int itemCount = Inventory.getCount(priority[0].getID());
				Conditionals.waitForItem(priority[0].getID(), itemCount);
				return true;
			}
		}
		return false;
	}

	public boolean checkLootInArea(RSGroundItem g) {
		if (Tiles.mithDragSpawn1.contains(g.getPosition()))
			return true;
		return false;
	}

	public static boolean lootExists(){
		int regLoot =  GroundItems.findNearest(LOOT_NAMES).length,
				badLoot = GroundItems.findNearest(BAD_LOOT).length,
				clueLoot = GroundItems.findNearest(CLUE_NAME).length,
				condLoot = GroundItems.findNearest(CONDITIONAL_LOOT).length,
				dboneLoot = GroundItems.findNearest(536).length,
				mithBarLoot = GroundItems.findNearest(2359).length;
		
//		if (Inventory.isFull()) {
//			if (regLoot > 0 || badLoot > 0 || clueLoot > 0 || condLoot > 0
//					|| dboneLoot > 0)
//				return true;
//		}
		return (regLoot > 0 || badLoot > 0 || clueLoot > 0 || condLoot > 0
				|| dboneLoot > 0 || (Inventory.isFull() ? false : mithBarLoot > 0));
	}

	public static boolean makeRoomForDrop() {
		RSItem[] food = Inventory.find(MDKGui.foodIDs);
		RSItem[] dbone = Inventory.find("Dragon bones");
		RSItem[] mbar = Inventory.find("Mithril bar");
		int regLoot =  GroundItems.findNearest(LOOT_NAMES).length,
				badLoot = GroundItems.findNearest(BAD_LOOT).length,
				clueLoot = GroundItems.findNearest(CLUE_NAME).length;
		Utils.openTab(TABS.INVENTORY);
		if ((Combat.getMaxHP() - Combat.getHP()) > 15) {
			if (food.length > 0) {
				final int foodCount = food.length;
				if (Clicking.click("Eat", food)) {
					Conditionals.waitForEating(foodCount);
					return true;
				}
			}
		} else if (Inventory.isFull()) {
			if (dbone.length > 0) {
				final int dboneCount = Inventory.getCount("Dragon bones");
				if (Clicking.click("Bury", dbone)) {
					Conditionals.waitForItem("Dragon bones", dboneCount);
					return true;
				}
			}
			else if (regLoot > 0 || badLoot > 0 || clueLoot > 0){
				food = Inventory.find(MDKGui.foodIDs);
				final int mithBarsCount = Inventory.getCount("Mithril bar"),
						foodCount = food.length;
				if(mithBarsCount > 0){
					Inventory.drop(mbar[General.random(0, mithBarsCount-1)]);
					Conditionals.waitForItem("Mithril bar", mithBarsCount);
					return true;
				}
				else if (Clicking.click("Eat", food)) {
					Conditionals.waitForEating(foodCount);
					return true;
				}
			}
		}
		return false;
	}

	public boolean gotRareDrop() {
		if (Inventory.getCount(PRIORITY_LOOT_NAMES) > 0) {
			Utils.emergTele();
			General.sleep(3000, 4000);
			General.println("You just got a visage or a dragon full helm YOU LUCKY BITCH!!!!!!!!");
			General.println("Please take a screenshot and post it on my thread! ty");
			General.println("Stopping the script so you can get a nice surprice when you login");
			MithDK.mainLoopStatus = false;
			return true;
		}
		return false;
	}

	// TODO lootOrder
	public static void lootOrder() {
		if(dropJunk() > 0 || !Inventory.isFull())
			lootBolts();
		if(makeRoomForDrop() || !Inventory.isFull())
			lootValuables();
	}

	// TODO lootBolts
	public static void lootBolts() {
		RSItem[] invRubyBolts = Inventory.find(Constants.RUBY_E_BOLT);
		RSGroundItem[] groundRubyBolts = GroundItems.findNearest(Constants.RUBY_E_BOLT);
		//RSGroundItem[] groundDiaBolts = GroundItems.findNearest(diamondEBolt);

		RSItem[] invMainBolts = Inventory.find(MDKGui.boltsUsing);
		RSGroundItem[] groundMainBolts = GroundItems.findNearest(MDKGui.boltsUsing);

		if (MDKGui.useRubyBolts) {
			if (groundRubyBolts.length > 0) {
				if (!groundRubyBolts[0].isOnScreen()) {
					Walking.blindWalkTo(groundRubyBolts[0].getPosition());
					Conditionals.waitFor(Utils.pos() == groundRubyBolts[0].getPosition(), 3000, 4000);
				}
				final int rubyCount = Inventory.getCount(Constants.RUBY_E_BOLT);
				if (Clicking.click("Take Ruby bolts (e)", groundRubyBolts[0])) {
					Conditionals.waitForItem(groundRubyBolts[0].getID(), rubyCount);
					Utils.openTab(TABS.INVENTORY);
					if(Clicking.click("Wield", Inventory.find(Constants.RUBY_E_BOLT))){
						Conditionals.waitFor(Inventory.getCount(Constants.RUBY_E_BOLT) == 0, 1000, 1200);
					}
				}
			}
		} 
		
		else if (groundMainBolts.length > 0) {
			if (!groundMainBolts[0].isOnScreen()) {
				Walking.blindWalkTo(groundMainBolts[0].getPosition());
				Conditionals.waitFor(Utils.pos() == groundMainBolts[0].getPosition(), 3000, 4000);
			}
			final int mainBoltsCount = Inventory.getCount(MDKGui.boltsUsing);
			if (Clicking.click("Take "+
			(MDKGui.boltsUsing[0] == Constants.ADDY_BOLT ? "Adamant bolts" : "Broad bolts")
					+" bolts", groundMainBolts[0])) {
				Conditionals.waitForItem(groundMainBolts[0].getID(), mainBoltsCount);
				Utils.openTab(TABS.INVENTORY);
				if(Clicking.click("Wield", Inventory.find(MDKGui.boltsUsing))){
					Conditionals.waitFor(Inventory.getCount(MDKGui.boltsUsing) == 0, 1000, 1200);
				}
			}
		}
	}

	public static int dropJunk() {
		Utils.openTab(TABS.INVENTORY);
		//final int junkCount = Inventory.getCount(JUNK);
		return Inventory.drop(JUNK);
			//Conditionals.waitForItem(JUNK, junkCount);
	}
	
	public static void lootValuables(){
		RSGroundItem[] regLoot = GroundItems.findNearest(LOOT_NAMES), 
				badLoot = GroundItems.findNearest(BAD_LOOT), 
				clueLoot = GroundItems.findNearest(CLUE_NAME), 
				condLoot = GroundItems.findNearest(CONDITIONAL_LOOT), 
				dboneLoot = GroundItems.findNearest(536), 
				mithBarLoot = GroundItems.findNearest(2359);
		
		if (regLoot.length > 0) {
			String str = LOOT_MAP.get(regLoot[0].getID());
			final int regLootCount = Inventory.getCount(LOOT_IDS);
			if (Clicking.click("Take " + str, regLoot[0])) {
				Conditionals.waitForItem(LOOT_IDS, regLootCount);
			}
		} else if (clueLoot.length > 0) {
			String str = CLUE_MAP.get(clueLoot[0].getID());
			final int clueLootCount = Inventory.getCount(CLUE_NAME);
			if (Clicking.click("Take " + str, clueLoot[0])) {
				Conditionals.waitForItem(CLUE_NAME, clueLootCount);
			}
		} else if (badLoot.length > 0) {
			String str = BAD_LOOT_MAP.get(badLoot[0].getID());
			final int badLootCount = Inventory.getCount(BAD_LOOT);
			if (Clicking.click("Take " + str, badLoot[0])) {
				Conditionals.waitForItem(BAD_LOOT, badLootCount);
			}
		} else if (condLoot.length > 0) {
			String str = CONDITIONAL_LOOT_MAP.get(condLoot[0].getID());
			final int condLootCount = Inventory.getCount(CONDITIONAL_LOOT);
			if (Clicking.click("Take " + str, condLoot[0])) {
				Conditionals.waitForItem(CONDITIONAL_LOOT, condLootCount);
			}
		} else if (dboneLoot.length > 0) {
			final int dboneLootCount = Inventory.getCount("Dragon bones");
			if (Clicking.click("Take Dragon bones", dboneLoot[0])) {
				Conditionals.waitForItem(CONDITIONAL_LOOT, dboneLootCount);
			}
		} else if (mithBarLoot.length > 0) {
			final int mithBarLootCount = Inventory.getCount("Mithril bar");
			if (Clicking.click("Take Mithril bar", mithBarLoot[0])) {
				Conditionals.waitForItem(CONDITIONAL_LOOT, mithBarLootCount);
			}
		} 		
	}
}
