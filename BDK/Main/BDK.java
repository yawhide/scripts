package scripts.BDK.Main;

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
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;

import scripts.BDK.Data.Constants;
import scripts.BDK.Utilities.Attack;
import scripts.BDK.Utilities.Conditionals;
import scripts.BDK.Utilities.Pray;
import scripts.BDK.Utilities.YawsGeneral;
import scripts.BDK.Utilities.Zybez;

@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", name = "Yaw hide's BDK", version = 1.0, description = "Local version")
public class BDK extends Script implements Painting, Pausing {

	// Variables
	public static int[] FOOD_IDS = {};
	public static int BOLTS_ID = 9142;
	public static long ANTIBAN = System.currentTimeMillis();
	public static int START_XP = Skills.getXP(SKILLS.HITPOINTS)
			+ Skills.getXP(SKILLS.RANGED);
	public static double VERSION;
	public static int CURRENT_XP;
	public static final long START_TIME = System.currentTimeMillis();
	public static double XP_TO_LVL_RANGE = Skills
			.getXPToNextLevel(SKILLS.RANGED);
	public static double XP_TO_LVL_HP = Skills
			.getXPToNextLevel(SKILLS.HITPOINTS);
	public static int START_LV = Skills.getActualLevel(SKILLS.RANGED)
			+ Skills.getActualLevel(SKILLS.HITPOINTS);
	public static final String[] SKILLS_TYPES = { "Defence", "Ranged",
			"Hitpoints" };
	public static final SKILLS[] SKILL = { SKILLS.DEFENCE, SKILLS.RANGED,
			SKILLS.HITPOINTS, };
	public static final int[] XP = { Skills.getXP(SKILLS.DEFENCE),
			Skills.getXP(SKILLS.RANGED), Skills.getXP(SKILLS.HITPOINTS) };
	public static int[] START_SKILL_LVS = {
			Skills.getActualLevel(SKILLS.DEFENCE),
			Skills.getActualLevel(SKILLS.RANGED),
			Skills.getActualLevel(SKILLS.HITPOINTS) };
	public static boolean SCRIPT_STATUS = true;
	public static int FOOD_NUM = 2;

	public static int DHIDES, DBONES = 0;
	public static int DHIDE_COUNT, DBONE_COUNT = 0;
	public static int DHIDES_INI, DBONES_INI = 0;
	public static int DHIDE_PRICE = 1775;
	public static int DBONES_PRICE = 1800;
	public static int MITH_BOLTS_PRICE;
	public static int RUNE_DAGGER_PRICE;
	public static int NAT_PRICE;
	static boolean WAIT_GUI = true;

	public static String FIGHT_STATUS;

	@Override
	public void run() {

		if (Skills.getActualLevel(SKILLS.RANGED) < 70) {
			println("You must be at least 70 range to use this script");
			SCRIPT_STATUS = false;
		} else if (Skills.getActualLevel(SKILLS.PRAYER) < 44) {
			println("You must be at least 44 prayer to use this script");
			SCRIPT_STATUS = false;
		} else if (Skills.getActualLevel(SKILLS.AGILITY) < 70) {
			println("You must be at least 70 agility to use this script");
			SCRIPT_STATUS = false;
		}

		boolean devmode = true;

		if (devmode) {
			FOOD_NUM = 2;
			FOOD_IDS = new int[] { 379 };
		} else {
			BDKGUI g = new BDKGUI();
			g.setVisible(true);
			while (WAIT_GUI)
				sleep(500);
			g.setVisible(false);
		}

		YawsGeneral.putMap();

		DHIDES_INI = Inventory.getCount(536);
		DBONES_INI = Inventory.getCount(1751);
		DHIDE_PRICE = Zybez.getPrice("Blue dragonhide");
		DBONES_PRICE = Zybez.getPrice("Dragon bones");
		sleep(250);
		Mouse.setSpeed(General.random(175, 190));
		Walking.setWalkingTimeout(5000L);
		General.useAntiBanCompliance(true);

		BOLTS_ID = Equipment.getItem(SLOTS.ARROW).getID();
		YawsGeneral.checkStats();

		while (SCRIPT_STATUS) {
			CURRENT_XP = Skills.getXP(SKILLS.RANGED)
					+ Skills.getXP(SKILLS.HITPOINTS);
			XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
			XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);

			DHIDES = Inventory.getCount(536) - DHIDES_INI;
			DBONES = Inventory.getCount(1751) - DBONES_INI;

			if (Inventory.find("Falador teleport").length > 0
					&& Inventory.find("Falador teleport")[0].getStack() == 1) {
				println("Ran out of ftabs...");
				YawsGeneral.emergTele();
				SCRIPT_STATUS = false;
			}

			if (Equipment.getItem(SLOTS.ARROW).getStack() < 100) {
				println("Ran out of bolts");
				YawsGeneral.emergTele();
				SCRIPT_STATUS = false;
			}

			if (Game.getRunEnergy() > 50) {
				Options.setRunOn(true);
			}

			if (Pathing.gotoDrag()) {
				Attack.fight();
			}
		}
	}

	@Override
	public void onPaint(Graphics g) {

		int xpGained = CURRENT_XP - START_XP;
		long timeRan = System.currentTimeMillis() - START_TIME;

		double multiplier = timeRan / 3600000D;
		int xpPerHour = (int) (xpGained / multiplier);

		long timeRan2 = getRunningTime();
		double secondsRan = (int) (timeRan2 / 1000);
		double hoursRan = secondsRan / 3600;
		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(SKILL[i])) {

				double xp_per_hour = Math
						.round(((Skills.getXP(SKILL[i]) - XP[i])) / hoursRan);

				g.setColor(new Color(0, 0, 0));
				g.fillRect(2, 326 - x, 515, 12);

				g.setColor(new Color(0, 255, 0, 255));
				g.drawString(SKILL[i] + ": " + (int) xp_per_hour + " Xp/h", 5,
						(337 - x));

				int x1 = 125, y1 = 327 - x;
				int CUR_LVL = Skills.getActualLevel(SKILL[i]);
				int NXT_LVL = (CUR_LVL + 1);
				int Percentage = Skills.getPercentToLevel(SKILL[i], NXT_LVL);
				double nextLv = Skills.getXPToLevel(SKILL[i], NXT_LVL);

				double hours = (nextLv / xp_per_hour);

				g.drawString("Curr lv: " + CUR_LVL + " ("
						+ (CUR_LVL - START_SKILL_LVS[i]) + ")  TTL "
						+ (int) hours + ":"
						+ (int) ((hours - (int) hours) * 60)
						+ " hr:min  Exp to lv: " + (int) nextLv, 235, y1 + 10);

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
		for (int i = 0; i < item.length; i++) {
			int count = items.containsKey(item[i].getID()) ? items.get(item[i]
					.getID()) : 0;
			items.put(item[i].getID(), count + 1);
		}
		Iterator it = items.entrySet().iterator();
		int k = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			g.drawString(
					(Constants.LOOT_MAP.get(pairs.getKey()) != null ? Constants.LOOT_MAP
							.get(pairs.getKey()) : pairs.getKey())
							+ " = " + pairs.getValue(), 5, 100 + k);
			it.remove(); // avoids a ConcurrentModificationException
			k += 15;
		}

		int dh, db;
		dh = DHIDES + DHIDE_COUNT;
		db = DBONES + DBONE_COUNT;
		g.setColor(new Color(60, 60, 60));
		g.fillRect(340, 370, 200, 150);

		g.setColor(Color.WHITE);
		g.drawString("Yawhide's BDK", 345, 385);
		g.drawString(
				"Version :" + VERSION + "   Curr world: "
						+ Game.getCurrentWorld(), 345, 405);
		g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
		g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour + "/h)",
				345, 435);
		g.drawString("State: " + FIGHT_STATUS, 345, 450);
		g.drawString("Dbones:" + dh + "       Dhide: " + db, 345, 465);
		g.drawString(
				"Gp/hr: "
						+ (int) ((dh * DHIDE_PRICE + db * DBONES_PRICE)
								/ hoursRan / 1000) + " K   Total: "
						+ (int) (dh * DHIDE_PRICE + db * DBONES_PRICE) / 1000,
				345, 480);
		g.drawString("In safespot? " + YawsGeneral.inSafeSpot(), 345, 495);
		g.setColor(Color.CYAN);
		for (int i = 0; i < Combat.getAttackingEntities().length; i++) {
			RSCharacter tmp = Combat.getAttackingEntities()[i];
			YawsGeneral.drawTile(new RSTile(tmp.getPosition().getX() - 1, tmp
					.getPosition().getY() - 1), g, false);
			g.setColor(Color.GREEN);
			YawsGeneral.drawTile(new RSTile(tmp.getPosition().getX(), tmp
					.getPosition().getY() - 1), g, false);
		}
		g.setColor(Color.RED);
		for (int i = 0; i < Constants.NORTHEAST_SAFESPOT_AREA_TILES.length; i++) {
			YawsGeneral.drawTile(Constants.NORTHEAST_SAFESPOT_AREA_TILES[i], g,
					false);
		}
	}

	public BDK() {
		VERSION = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
	}

	@Override
	public void onPause() {
		if (YawsGeneral.inArea(Constants.BLUE_DRAG_AREA)) {
			if (!YawsGeneral.inSafeSpot())
				Pathing.gotoSafeSpot();
		}
		Pray.turnOffPrayerEagle();
	}

	@Override
	public void onResume() {
		if (!YawsGeneral.inSafeSpot())
			Pathing.gotoSafeSpot();
	}

}
