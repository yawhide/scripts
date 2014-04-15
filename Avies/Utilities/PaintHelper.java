package scripts.Avies.Utilities;

import java.awt.Rectangle;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;

import scripts.Avies.Data.Constants;
import scripts.Avies.Main.Avies;

public class PaintHelper {

	public static int startXp, startLv, current_xp, xpGained, xpPerHour, dh, db, ran;
	public static long startTime, timeRan, timeRan2;
	public static double version, xpToLvRange, xpToLvHP, multiplier,
			secondsRan, hoursRan;

	public static Rectangle HIDE_ZONE = new Rectangle(342, 369, 542, 500);
	
	public static String[] skillName = { "Defence", "Ranged", "Hitpoints" };
	public static SKILLS[] skillSKILL = { SKILLS.DEFENCE, SKILLS.RANGED,
			SKILLS.HITPOINTS };
	
	public static int[] skillXP = { Skills.getXP(SKILLS.DEFENCE),
			Skills.getXP(SKILLS.RANGED), Skills.getXP(SKILLS.HITPOINTS) },
			
			startLvs = { Skills.getActualLevel(SKILLS.DEFENCE),
					Skills.getActualLevel(SKILLS.RANGED),
					Skills.getActualLevel(SKILLS.HITPOINTS) };
	
	public static void initXP() {
		startXp = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.RANGED);
		startLv = Skills.getActualLevel(SKILLS.RANGED)
				+ Skills.getActualLevel(SKILLS.HITPOINTS);
		startTime = System.currentTimeMillis();
		xpToLvRange = Skills.getXPToNextLevel(SKILLS.RANGED);
		xpToLvHP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	}
	
	public static void updatePaintVariables() {
		current_xp = Skills.getXP(SKILLS.RANGED)
				+ Skills.getXP(SKILLS.HITPOINTS);
		xpToLvRange = Skills.getXPToNextLevel(SKILLS.RANGED);
		xpToLvHP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
		Avies.addyBars = Inventory.getCount(Constants.ADDY) - Avies.addyIni;
		Avies.coins = Inventory.getCount(Constants.COIN) - Avies.coinsIni;
		Avies.ranarrs = Inventory.getCount(Constants.RANARR) - Avies.ranarrIni;
	}

	public static void updateXPVariables() {
		xpGained = current_xp - startXp;
		timeRan = System.currentTimeMillis() - startTime;
		multiplier = timeRan / 3600000D;
		xpPerHour = (int) (xpGained / multiplier);
		secondsRan = (int) (timeRan / 1000);
		hoursRan = secondsRan / 3600;
		dh = Avies.addyBars + Avies.addyCount; 
		db = Avies.coins + Avies.coinsAmount;
		ran = Avies.ranarrs + Avies.ranarrCount;
	}
}
