package scripts.BDK.Utilities;

import java.awt.Graphics;
import java.awt.Polygon;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.Avies.Main.Avies;
import scripts.BDK.Data.Constants;
import scripts.BDK.Main.BDK;

public class YawsGeneral {


	public static RSTile pos() {
		return Player.getPosition();
	}

	public static boolean isRanging() {
		return Player.getRSPlayer().getInteractingCharacter() != null;
	}

	public static int distanceTo(RSTile t) {
		return Math.max(pos().getX() - t.getX(), pos().getY() - t.getY());
	}

	public static int getHp() {
		return Combat.getHPRatio();
	}

	public static int itemCount(int ID) {
		return Inventory.find(ID).length;
	}

	public static int lootCountStack(int ID) {
		return Inventory.getCount(ID);
	}

	public static boolean isMovin() {
		return Player.isMoving();
	}

	public static boolean inCombat() {
		return Player.getRSPlayer().isInCombat();
	}

	public static int getStackBolts(int i) {
		return Equipment.getItem(SLOTS.ARROW).getID() == i ? Equipment.getItem(
				SLOTS.ARROW).getStack() : 0;
	}


	public static boolean inArea(RSArea a) {
		return a.contains(pos());
	}

	public static void waitIsMovin() {
		Conditionals.waitFor(!Player.isMoving(), 4500, 6000);
	}

	public static void openTab(TABS t) {
		GameTab.open(t);
		Conditionals.waitForTab(t);
	}
	
	public static boolean inSafeSpot(){
		return inArea(Constants.NORTHEAST_SAFESPOT_AREA);
	}

	public static boolean checkActions(RSNPC object, String action) {
		if (object.getActions() != null) {
			for (String s : object.getActions())
				return s.contains(action);
		}
		return false;
	}

	public static boolean aroundPath(RSTile[] path) {
		for (int i = 0; i < path.length; i++) {
			if (pos().distanceTo(path[i]) <= 4) {
				return true;
			}
		}
		return false;
	}
	
	public static String underAttack() {
		RSCharacter[] mon = Combat.getAttackingEntities();
		if (mon.length > 0) {
			return mon[0].getName();
		}
		return null;
	}

	public static void putMap() {
		for (int i = 0; i < Constants.LOOT.length; i++) {
			if (i >= Constants.LOOT_NAMES.length) {
				Constants.LOOT_MAP
						.put(Constants.LOOT[i],
								Constants.LOOT_NAMES[Constants.LOOT_NAMES.length - 1]);
			} else
				Constants.LOOT_MAP.put(Constants.LOOT[i],
						Constants.LOOT_NAMES[i]);
		}
	}

	public static void drawTile(RSTile tile, Graphics g, boolean fill) {
		if (tile.getPosition().isOnScreen()) {
			if (fill) {
				g.fillPolygon(Projection.getTileBoundsPoly(tile, 0));
			} else {
				g.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
			}
		}
	}

	public static void drawModel(RSModel model, Graphics g, boolean fill) {
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

	public static void alc(int[] loot) {
		RSItem[] l = Inventory.find(loot);
		openTab(TABS.MAGIC);
		Magic.selectSpell("High Level Alchemy");
		Conditionals.waitFor(!Magic.getSelectedSpellName().equals(""), 500,
				1000);
		final int exp = Skills.getXP(SKILLS.MAGIC);
		if (l.length > 0 && Clicking.click("Cast", l[0])) {
			Conditionals.waitFor(Skills.getXP(SKILLS.MAGIC) > exp, 1100, 1400);
		}
	}

	public static void emergTele() {
		BDK.FIGHT_STATUS = "tele tabbing";
		RSItem[] tab = Inventory.find("Falador teleport");
		openTab(TABS.INVENTORY);
		if (tab.length > 0) {
			if (Clicking.click("Break", tab[0])) {
				Conditionals.waitFor(inArea(Constants.FALLY_AREA), 2500, 3000);
			}
		} else {
			General.println("Out of tabs");
			BDK.SCRIPT_STATUS = false;
		}
	}

	public static void heal() {
		RSItem[] food = Inventory.find(BDK.FOOD_IDS);
		BDK.FIGHT_STATUS = "eating food";
		if (GameTab.getOpen() != TABS.INVENTORY) {
			openTab(TABS.INVENTORY);
		}

		if (food.length > 0) {
			final int count = Inventory.getCount(BDK.FOOD_IDS);
			if (Clicking.click("Eat", food[0]))
				Conditionals.waitForEating(count);
		} else
			emergTele();
	}

	public static void isInvFull() {
		RSItem[] food = Inventory.find(BDK.FOOD_IDS);
		RSItem[] bolts = Inventory.find(BDK.BOLTS_ID);
		if (Inventory.find(Constants.JUNK).length > 0 || food.length > 0
				|| bolts.length > 0) {
			BDK.FIGHT_STATUS = "dropping junk";
			if (food.length > 0) {
				final int count = Inventory.getCount(BDK.FOOD_IDS);
				if (Clicking.click("Eat", food[0])) {
					Conditionals.waitForEating(count);
				}

			} else if (bolts.length > 0) {
				final int boltsCount = Equipment.getItem(SLOTS.ARROW)
						.getStack();
				if (Clicking.click("Wield", bolts[0])) {
					Conditionals.waitFor(Equipment.getItem(SLOTS.ARROW).getStack() > boltsCount, 400, 500);
				}
			}
			Inventory.drop(Constants.JUNK);
		} else {
			emergTele();
		}
	}

	public static boolean lootExists() {
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT2);
		return Nests.length > 0
				&& Constants.BLUE_DRAG_AREA.contains(Nests[0].getPosition());
	}

	public static void loot() {
		BDK.FIGHT_STATUS = "looting";
		
		Pray.turnOffPrayerEagle();
		
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT);
		if (YawsGeneral.getHp() <= 50) {
			if (Inventory.getCount(BDK.FOOD_IDS) == 0) {
				YawsGeneral.emergTele();
			} 
			else
				YawsGeneral.heal();
		}
		
		for(int i = 0; i < Nests.length; i++){
			if(Nests[i].getID() == 9142){
				if (Nests[i].getStack() > 9) {
					if (!Nests[i].isOnScreen()) {
						Walking.clickTileMM(Nests[i].getPosition(), 1);
						Camera.turnToTile(Nests[i].getPosition());
						Camera.setCameraAngle(General.random(90, 100));
						YawsGeneral.waitIsMovin();
					}
					String str = Constants.LOOT_MAP.get(Nests[i].getID());
					int tmpCount = Inventory.getCount(Nests[i].getID());
					if (Nests[i].click("Take " + str))
						Conditionals.waitForItem(Nests[i].getID(), tmpCount);
				}
			}
			else{
				if (!Nests[i].isOnScreen()) {
					Walking.clickTileMM(Nests[i].getPosition(), 1);
					Camera.turnToTile(Nests[i].getPosition());
					Camera.setCameraAngle(General.random(90, 100));
					YawsGeneral.waitIsMovin();
				}
				String str = Constants.LOOT_MAP.get(Nests[i].getID());
				int tmpCount = Inventory.getCount(Nests[i].getID());
				if(Nests[i].click("Take " + str))
					Conditionals.waitForItem(Nests[i].getID(), tmpCount);
			}
		}
	}

	public static void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills
				.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if (Clicking.click("Drink", potion[0])) {
					Conditionals.waitFor(Skills.getCurrentLevel(SKILLS.RANGED) > Skills.getActualLevel(SKILLS.RANGED), 1000, 1200);
				}
			}
		}
	}
		
	public static void waitForLoot(){
		BDK.FIGHT_STATUS = "prayerflicking";
		while(!lootExists() && isRanging() && inSafeSpot() && !isMovin()){
			
			Pray.prayerFlick();
		}
		Pray.turnOffPrayerEagle();
	}

	
	public void waitForDrag(RSNPC drag) {

		while (drag.isInCombat() && inSafeSpot() && !inCombat())
			General.sleep(1000, 1300);
	}
	
	public static void checkStats(){
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			General.println("You must be at least 70 range to use this script");
			BDK.SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			General.println("You must be at least 44 prayer to use this script");
			BDK.SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 70){
			General.println("You must be at least 70 agility ");
			BDK.SCRIPT_STATUS = false;
		}
	}
	
}
