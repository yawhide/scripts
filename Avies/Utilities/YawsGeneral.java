package scripts.Avies.Utilities;

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

import scripts.Avies.Data.Constants;
import scripts.Avies.Main.Avies;

public class YawsGeneral {

	public static boolean inHouse() {
		return !inArea(Constants.VARROCK_AREA)
				&& Objects.findNearest(50, "Portal").length > 0;
	}

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

	public static boolean isFull() {
		return Inventory.isFull();
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

	public static boolean inGW() {
		return pos().distanceTo(Constants.GWD_CENTER) <= 50
				&& pos().getPlane() == 2;
	}

	public static boolean inAviesSpot() {
		return inArea(Constants.AVIES_Area);
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

	public static void teleToTroll() {
		openTab(TABS.MAGIC);

		Magic.selectSpell("Trollheim Teleport");
		Conditionals.waitFor(inArea(Constants.TROLL_TELEPORT_AREA), 3500, 4500);
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
			if (i >= Constants.LOOT_STRING_NAMES.length) {
				Constants.LOOT_MAPPING
						.put(Constants.LOOT[i],
								Constants.LOOT_STRING_NAMES[Constants.LOOT_STRING_NAMES.length - 1]);
			} else
				Constants.LOOT_MAPPING.put(Constants.LOOT[i],
						Constants.LOOT_STRING_NAMES[i]);
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
		Avies.FIGHT_STATUS = "tele tabbing";
		RSItem[] tab = Inventory.find(Constants.VTAB);
		if (Avies.USE_HOUSE)
			tab = Inventory.find(Constants.HTAB);
		openTab(TABS.INVENTORY);
		if (tab.length > 0) {
			if (Clicking.click("Break", tab[0])) {
				Conditionals.waitFor(Avies.USE_HOUSE ? inHouse()
						: inArea(Constants.VARROCK_AREA), 2500, 3000);
			}
		} else {
			General.println("Out of tabs");
			Avies.SCRIPT_STATUS = false;
		}
	}

	public static void heal() {
		RSItem[] food = Inventory.find(Avies.FOOD_IDS);
		Avies.FIGHT_STATUS = "eating food";
		if (GameTab.getOpen() != TABS.INVENTORY) {
			openTab(TABS.INVENTORY);
		}

		if (food.length > 0) {
			final int count = Inventory.getCount(Avies.FOOD_IDS);
			if (Clicking.click("Eat", food[0]))
				Conditionals.waitForEating(count);
		} else
			emergTele();
	}

	public static void CWTele() {
		RSItem[] ROD = Equipment.find(SLOTS.RING);

		openTab(TABS.EQUIPMENT);
		if (ROD.length > 0) {
			if (!inCombat()) {
				if (Clicking.click("Operate", ROD[0])) {
					if (NPCChat.selectOption("Castle Wars Arena.", true)) {
						Conditionals.waitFor(inArea(Constants.CW_AREA), 4500,
								5500);
					}
				}
			} else {
				emergTele();
			}
		}
	}

	public static boolean gotEquipment() {
		return (((!Avies.USE_HOUSE && lootCountStack(Constants.VTAB) > 0) || (Avies.USE_HOUSE && lootCountStack(Constants.HTAB) > 0))
				&& lootCountStack(Constants.NAT) >= 10
				&& lootCountStack(Constants.FIRE) >= 52
				&& lootCountStack(Constants.LAW) == 2
				&& Inventory.find(Constants.RANGE_POT).length == 1 && Inventory
					.find(Avies.FOOD_IDS).length == Avies.FOOD_NUMBER);
	}

	public static void isInvFull() {
		RSItem[] food = Inventory.find(Avies.FOOD_IDS);
		RSItem[] bolts = Inventory.find(Avies.BOLTS_ID);
		if (Inventory.find(Constants.JUNK).length > 0 || food.length > 0
				|| bolts.length > 0) {
			Avies.FIGHT_STATUS = "dropping junk";
			if (food.length > 0) {
				final int count = Inventory.getCount(Avies.FOOD_IDS);
				if (Clicking.click("Eat", food[0])) {
					Conditionals.waitForEating(count);
				}

			} else if (bolts.length > 0) {
				final int boltsCount = Equipment.getItem(SLOTS.ARROW)
						.getStack();
				if (Clicking.click("Wield", bolts[0])) {
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return Equipment.getItem(SLOTS.ARROW).getStack() > boltsCount;
						}
					}, General.random(400, 500));
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
				&& Constants.AVIES_Area.contains(Nests[0].getPosition());
	}

	public static void loot() {
		Avies.FIGHT_STATUS = "looting";
		Pray.turnOffPrayerEagle();
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT);

		for (int i = 0; i < Nests.length; i++) {
			Walking.setWalkingTimeout(1000L);
			if (Nests[i].getID() == Avies.BOLTS_ID) {
				if (Nests[i].getStack() > 9) {
					if (!Nests[i].isOnScreen()) {
						Walking.walkPath(Walking.generateStraightPath(Nests[i]
								.getPosition()));
						Camera.turnToTile(Nests[i].getPosition());
						Camera.setCameraAngle(General.random(90, 100));
						waitIsMovin();
					}
					String str = Constants.LOOT_MAPPING.get(Nests[i].getID());
					final int tmpCount = Inventory.getCount(Nests[i].getID());
					if (Nests[i].click("Take " + str))
						Conditionals.waitForItem(Nests[i].getID(), tmpCount);
				}
			} else {
				if (!Nests[i].isOnScreen()) {
					Walking.walkPath(Walking.generateStraightPath(Nests[i]
							.getPosition()));
					Camera.turnToTile(Nests[i].getPosition());
					Camera.setCameraAngle(General.random(90, 100));
					waitIsMovin();
				}
				String str = Constants.LOOT_MAPPING.get(Nests[i].getID());
				final int tmpCount = Inventory.getCount(Nests[i].getID());
				if (Nests[i].click("Take " + str))
					Conditionals.waitForItem(Nests[i].getID(), tmpCount);
			}
		}
	}

	// TODO fix waitForLoot
	public static void waitForLoot(RSCharacter avv) {
		Avies.FIGHT_STATUS = "prayerflicking";
		if (!lootExists() && isRanging() && inAviesSpot() && getHp() > 30) {
			Pray.prayerFlick();
		}
		Pray.turnOffPrayerEagle();
	}

	public static void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills
				.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if (Clicking.click("Drink", potion[0])) {
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return Skills.getCurrentLevel(SKILLS.RANGED) > Skills
									.getActualLevel(SKILLS.RANGED);
						}
					}, General.random(1000, 1200));
				}
			}
		}
	}
}