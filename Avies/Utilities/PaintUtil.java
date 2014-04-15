package scripts.Avies.Utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.Avies.Data.Constants;
import scripts.Avies.Data.Tiles;
import scripts.Avies.Main.Avies;

public class PaintUtil {

	public static void drawExperiencePerHour(Graphics g) {
		int x = 0;
		for (int i = 0; i < PaintHelper.skillSKILL.length; i++) {
			if (PaintHelper.skillXP[i] != Skills
					.getXP(PaintHelper.skillSKILL[i])) {

				int x1 = 125, y1 = 327 - x, CUR_LVL = Skills
						.getActualLevel(PaintHelper.skillSKILL[i]), NXT_LVL = (CUR_LVL + 1), Percentage = Skills
						.getPercentToLevel(PaintHelper.skillSKILL[i], NXT_LVL);
				double xp_per_hour = Math
						.round(((Skills.getXP(PaintHelper.skillSKILL[i]) - PaintHelper.skillXP[i]))
								/ PaintHelper.hoursRan), nextLv = Skills
						.getXPToLevel(PaintHelper.skillSKILL[i], NXT_LVL), hours = (nextLv / xp_per_hour);	

				if (!PaintHelper.HIDE_ZONE.contains(Mouse.getPos())) {
					g.setColor(new Color(0, 0, 0));
					g.fillRect(2, 326 - x, 515, 12);

					g.setColor(new Color(0, 255, 0, 255));
					g.drawString(PaintHelper.skillSKILL[i] + ": "
							+ (int) xp_per_hour + " Xp/h", 5, (337 - x));
					g.drawString("Curr lv: " + CUR_LVL + " ("
							+ (CUR_LVL - PaintHelper.startLvs[i]) + ")  TTL "
							+ (int) hours + ":"
							+ (int) ((hours - (int) hours) * 60)
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
	}
	
	public static void showPaint(Graphics g){
		g.setColor(Color.GREEN);
		if (!PaintHelper.HIDE_ZONE.contains(Mouse.getPos())) {
			g.setColor(new Color(60, 60, 60));
			g.fillRect(340, 370, 200, 150);

			g.setColor(Color.WHITE);
			g.drawString("Yawhide's BDK", 345, 385);
			g.drawString(
					"Version :" + PaintHelper.version + "   Curr world: "
							+ Game.getCurrentWorld(), 345, 405);
			g.drawString("Running for: " + Timing.msToString(PaintHelper.timeRan), 345, 420);
			g.drawString("Total XP ganed: " + PaintHelper.xpGained + " (" + PaintHelper.xpPerHour
					+ "/h)", 345, 435);
			g.drawString("State: " + Avies.fightStatus, 345, 450);
			g.drawString("Addy bars: " + PaintHelper.dh + "       GP: " + PaintHelper.db / 1000 + " K",
					345, 465);
			g.drawString("Gp/hr: " + calcProfit() + " K   Total: "	+ calcRevenue()	+ " K", 345, 480);

		}
	}
	
	public static int calcProfit(){
		return (int) ((PaintHelper.dh * Avies.addyZybezPrice + PaintHelper.db + PaintHelper.ran * Avies.ranarrZybezPrice)
				/ PaintHelper.hoursRan / 1000);
	}
	
	public static int calcRevenue(){
		return (int) ((PaintHelper.dh * Avies.addyZybezPrice + PaintHelper.db + PaintHelper.ran * Avies.ranarrZybezPrice) / 1000);
	}
	
	public static void drawMisc(Graphics g){
		g.setColor(Color.CYAN);

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
					(Constants.LOOT_MAPPING.get(pairs.getKey()) != null ? Constants.LOOT_MAPPING
							.get(pairs.getKey()) : pairs.getKey())
							+ " = " + pairs.getValue(), 5, 100 + k);
			it.remove(); // avoids a ConcurrentModificationException
			k += 15;
		}
		RSGroundItem[] Nests = GroundItems
				.findNearest(Constants.LOOT_WITHOUT_ARROWS);
		for (int i = 0; i < Nests.length; i++) {
			if (Tiles.AVIES_AREA.contains(Nests[i].getPosition())) {
				RSTile t = Nests[i].getPosition();
				YawsGeneral.drawTile(t, g, false);
			}
		}

		g.setColor(Color.RED);
		RSNPC[] avv = NPCs.findNearest("Aviansie");
		for (RSNPC a : avv) {
			if (a.getCombatLevel() == 69 || a.getCombatLevel() == 71
					|| a.getCombatLevel() == 83)
				YawsGeneral.drawTile(a.getPosition(), g, false);
		}
		g.drawString(Combat.getTargetEntity().getName(), 5, 50);
	}
	
}
