package scripts.Avies.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Options;
import org.tribot.api2007.Walking;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;

import scripts.Avies.Data.Constants;
import scripts.Avies.Data.Tiles;
import scripts.Avies.Utilities.Attack;
import scripts.Avies.Utilities.PaintHelper;
import scripts.Avies.Utilities.PaintUtil;
import scripts.Avies.Utilities.YawsGeneral;
import scripts.Avies.Utilities.Zybez;

@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", version = 1.01, name = "Yaw hide's Ava Killer", description = "Local version")
public class Avies extends Script implements Painting, Pausing,
		MessageListening07 {

	public static int addyBars, coins, ranarrs, addyCount, coinsAmount,
			ranarrCount, addyIni, coinsIni, ranarrIni = 0;

	public static int foodNumber = 11, addyZybezPrice = 2350, ranarrZybezPrice = 6400;

	public static int boltsID;
	public static int[] foodIDs; // , 385, 7946, 1897 };

	public static long antibanTime = System.currentTimeMillis();

	public static boolean mainLoopStatus = true;
	public static String fightStatus;

	// GUI
	public static boolean runAwayFromMonster = false, useHouseTab = true,
			waitForGuiToFinish = true;


	@Override
	public void run() {

		AvieGUI.onstart();

		while (mainLoopStatus) {
			
			PaintHelper.updatePaintVariables();
			Mouse.setSpeed(General.random(175, 190));

			if (Game.getRunEnergy() > 20) {
				Options.setRunOn(true);
			}

			if (runAwayFromMonster) {
				Pathing.runAwayFromMonster();
			} else if (Pathing.goToAvies()) {
				Attack.fight();
			}
		}
	}

	@Override
	public void onPaint(Graphics g) {
		PaintHelper.updateXPVariables();
		PaintUtil.drawExperiencePerHour(g);
		PaintUtil.drawMisc(g);
		PaintUtil.showPaint(g);
	}

	public Avies() {
		PaintHelper.version = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
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
		if (arg0.equals("You need a higher slayer level to know how to wound this monster.")) {
			runAwayFromMonster = true;
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

}
