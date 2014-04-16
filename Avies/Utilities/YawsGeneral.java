package scripts.Avies.Utilities;

import java.awt.Graphics;
import java.awt.Polygon;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
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
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.Avies.Data.Constants;
import scripts.Avies.Data.Tiles;
import scripts.Avies.Main.Avies;

public class YawsGeneral {

	public static boolean inHouse() {
		return !inArea(Tiles.VARROCK_AREA)
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
		return pos().distanceTo(Tiles.GWD_CENTER) <= 50
				&& pos().getPlane() == 2;
	}

	public static boolean inAviesArea() {
		return inArea(Tiles.AVIES_AREA) && pos().getPlane() == 2;
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
		Conditionals.waitFor(inArea(Tiles.TROLL_TELEPORT_AREA), 3500, 4500);
	}

	public static String underAttack() {
		return Combat.getAttackingEntities().length > 0 ? Combat.getAttackingEntities()[0].getName() : null;
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
		Avies.fightStatus = "tele tabbing";
		RSItem[] tab = Inventory.find(Constants.VTAB);
		if (Avies.useHouseTab)
			tab = Inventory.find(Constants.HTAB);
		openTab(TABS.INVENTORY);
		if (tab.length > 0) {
			if (Clicking.click("Break", tab[0])) {
				Conditionals.waitFor(Avies.useHouseTab ? inHouse()
						: inArea(Tiles.VARROCK_AREA), 2500, 3000);
			}
		} else {
			General.println("Out of tabs");
			Avies.mainLoopStatus = false;
		}
	}

	public static void heal() {
		RSItem[] food = Inventory.find(Avies.foodIDs);
		Avies.fightStatus = "eating food";
		if (GameTab.getOpen() != TABS.INVENTORY) {
			openTab(TABS.INVENTORY);
		}

		if (food.length > 0) {
			final int count = Inventory.getCount(Avies.foodIDs);
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
						Conditionals.waitFor(inArea(Tiles.CW_AREA), 4500,
								5500);
					}
				}
			} else {
				emergTele();
			}
		}
	}

	public static boolean gotEquipment() {
		return (((!Avies.useHouseTab && lootCountStack(Constants.VTAB) > 0) || (Avies.useHouseTab && lootCountStack(Constants.HTAB) > 0))
				&& lootCountStack(Constants.NAT) >= 10
				&& lootCountStack(Constants.FIRE) >= 52
				&& lootCountStack(Constants.LAW) == 2
				&& Inventory.find(Constants.RANGE_POT).length == 1 && Inventory
					.find(Avies.foodIDs).length == Avies.foodNumber);
	}

	public static void isInvFull() {
		RSItem[] food = Inventory.find(Avies.foodIDs);
		RSItem[] bolts = Inventory.find(Avies.boltsID);
		if (Inventory.find(Constants.JUNK).length > 0 || food.length > 0
				|| bolts.length > 0) {
			Avies.fightStatus = "dropping junk";
			if (food.length > 0) {
				final int count = Inventory.getCount(Avies.foodIDs);
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
	
	public static void checkStats(){
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			General.println("You must be at least 70 range to use this script");
			Avies.mainLoopStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			General.println("You must be at least 44 prayer to use this script");
			Avies.mainLoopStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 60 && Skills.getActualLevel(SKILLS.STRENGTH) < 60){
			General.println("You must be at least 60 agility or strength to use this script");
			Avies.mainLoopStatus = false;
		}
		if(Skills.getActualLevel(SKILLS.CONSTRUCTION) < 50){
			Avies.useHouseTab = false;
			General.println("You do not have 50 con for varrock portal focus, you must use varrock teletabs");
		}
	}
	
	public static void prayAtHouseAltar(){
		Avies.fightStatus = "in house...";
		RSObject[] alter = Objects.findNearest(20, "Altar");
		RSObject[] portal = Objects.findNearest(20, "Varrock Portal");
		RSObject[] portal2 = Objects.findNearest(30, "Portal");
		
		if(alter.length == 0 || portal.length == 0){
			Avies.fightStatus = "not inside house...";
			if(portal2.length > 0){
				if(portal2[0].isOnScreen()){
					if(Clicking.click("Enter", portal2[0])){
						NPCChat.selectOption("Go to your house", true);
					}
				}
				else{
					Walking.walkPath(Walking.generateStraightPath(portal2[0].getPosition()));
					YawsGeneral.waitIsMovin();
				}
			}
			
			General.println("Could not determine where you are, are you outside of the house???");
			//scriptStatus = false;
		}
		
		else if (Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER)) {
			if(portal.length > 0){
				if (portal[0].isOnScreen()) {
					if (Clicking.click("Enter", portal[0])) {
						Conditionals.waitFor(YawsGeneral.inArea(Tiles.VARROCK_AREA), 1200, 2000);
					}
				} else {
					Positionable temp = new RSTile(portal[0].getPosition().getX(), portal[0].getPosition().getY()-2, 0);
					Walking.walkPath(Walking.generateStraightPath(temp));
					YawsGeneral.waitIsMovin();
				}
			}
		} else {
			if (alter.length > 0) {
				if (alter[0].isOnScreen()) {
					if (Clicking.click("Pray", alter[0])) {
						Conditionals.waitFor(Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER), 1500, 2300);
					}
				} else {
					Walking.walkPath(Walking.generateStraightPath(alter[0].getPosition()));
					YawsGeneral.waitIsMovin();
				}
			}
		}
	}
}
