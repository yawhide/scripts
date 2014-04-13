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
import scripts.Avies.Utilities.Attack;
import scripts.Avies.Utilities.YawsGeneral;
import scripts.slayer.Zybez;

@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", version=0.3, name = "Yaw hide's Ava Killer", description="Local version")
public class Avies extends Script implements Painting, Pausing, MessageListening07{

	public static int ADDY_BARS, COINS, RANARRS = 0;
	public static int ADDY_COUNT, COINS_AMOUNT, RANARR_COUNT = 0;
	public static int ADDY_INI, COINS_INI, RANARR_INI = 0;
	public static int MITHRIL_BOLTS = 9142;
	public static int FOOD_NUMBER = 11;
	public static int BOLTS_ID;
	public static int[] FOOD_IDS; // , 385, 7946, 1897 };
	public static int ADDY_PRICE = 2800;
	public static int RANARR_PRICE = 6400;
	
	public static long ANTIBAN = System.currentTimeMillis();
	
	public static boolean SCRIPT_STATUS = true;
	public static String FIGHT_STATUS;
	
	//GUI
	public static boolean MOVE_RANDOM = false;
	public static boolean USE_HOUSE = true;
	public static boolean WAIT_GUI = true;
	
	//paint
	public static int START_XP = Skills.getXP(SKILLS.HITPOINTS)
			+ Skills.getXP(SKILLS.RANGED);
	public static double VERSION;
	public static int CURRENT_XP;
	public static final long START_TIME = System.currentTimeMillis();
	public static double XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
	public static double XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	public static int START_LV = Skills.getActualLevel(SKILLS.RANGED)
			+ Skills.getActualLevel(SKILLS.HITPOINTS);
	public static final String[] TYPE = { "Defence", "Ranged", "Hitpoints" };
	public static final SKILLS[] SKILL = { SKILLS.DEFENCE, SKILLS.RANGED,
			SKILLS.HITPOINTS, };
	public static final int[] XP = { Skills.getXP(SKILLS.DEFENCE),
			Skills.getXP(SKILLS.RANGED), Skills.getXP(SKILLS.HITPOINTS) };
	public static int[] START_LVS = { Skills.getActualLevel(SKILLS.DEFENCE),
			Skills.getActualLevel(SKILLS.RANGED),
			Skills.getActualLevel(SKILLS.HITPOINTS) };
	
	@Override
	public void run() {
		
		General.useAntiBanCompliance(true);
		//ABCUtil abc = new ABCUtil();
				
		BOLTS_ID = Equipment.getItem(SLOTS.ARROW).getID();
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			println("You must be at least 70 range to use this script");
			SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			println("You must be at least 44 prayer to use this script");
			SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 60 && Skills.getActualLevel(SKILLS.STRENGTH) < 60){
			println("You must be at least 60 agility or strength to use this script");
			SCRIPT_STATUS = false;
		}
		if(Skills.getActualLevel(SKILLS.CONSTRUCTION) < 50){
			USE_HOUSE = false;
			println("You do not have 50 con for varrock portal focus, you must use varrock teletabs");
		}
		
		boolean devmode = true;
		
		if(devmode){
			FOOD_NUMBER = 9;
			FOOD_IDS = new int[] {379};
			USE_HOUSE = true;
		}
		else{
			AvieGUI g = new AvieGUI();
			g.setVisible(true);
			while (WAIT_GUI)
				sleep(500);
			g.setVisible(false);
		}
		
		if(!Combat.isAutoRetaliateOn()){
			Combat.setAutoRetaliate(true);
			sleep(500,600);
		}
		
		YawsGeneral.putMap();
		
		ADDY_INI = Inventory.getCount(Constants.ADDY);
		COINS_INI = Inventory.getCount(Constants.COIN);
		RANARR_INI = Inventory.getCount(Constants.RANARR);
		ADDY_PRICE = Zybez.getPrice("Adamantite bar");
		RANARR_PRICE = Zybez.getPrice("Clean ranarr");
		
		sleep(250);
		
		Walking.setWalkingTimeout(5000L);
		Walking.setControlClick(true);
		
		
		while(SCRIPT_STATUS){
			CURRENT_XP = Skills.getXP(SKILLS.RANGED) + Skills.getXP(SKILLS.HITPOINTS);
			XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
			XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
			Mouse.setSpeed(General.random(175,190));
			
			ADDY_BARS = Inventory.getCount(Constants.ADDY) - ADDY_INI;
			COINS = Inventory.getCount(Constants.COIN) - COINS_INI;
			RANARRS = Inventory.getCount(Constants.RANARR) - RANARR_INI;
			
			if(Game.getRunEnergy() > 20) {
			    Options.setRunOn(true);
			}
			
			if(MOVE_RANDOM){
				if(YawsGeneral.pos().distanceTo(Constants.RANDOM_EAST_TILE) > YawsGeneral.pos().distanceTo(Constants.RANDOM_WEST_TILE)){
					Walking.walkPath(Walking.generateStraightPath(Constants.RANDOM_EAST_TILE));
					YawsGeneral.waitIsMovin();
				}
				else{
					Walking.walkPath(Walking.generateStraightPath(Constants.RANDOM_WEST_TILE));
					YawsGeneral.waitIsMovin();
				}
				MOVE_RANDOM = false;
			}
			else if(Pathing.gotoAvies()){
				Attack.fight();
			}
		}
	}
	
	@Override
	public void onPaint(Graphics g) {
		Rectangle hideZone = new Rectangle(342, 369, 542, 500);
		Point p = Mouse.getPos();
		
		int xpGained = CURRENT_XP - START_XP;
		long timeRan = System.currentTimeMillis() - START_TIME;
		
		double multiplier = timeRan / 3600000D;
		int xpPerHour = (int) (xpGained / multiplier);
					
		long timeRan2 = getRunningTime();
		double secondsRan = (int) (timeRan2/1000);
		double hoursRan = secondsRan/3600;
		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(SKILL[i])) {

				double xp_per_hour = Math.round(((Skills.getXP(SKILL[i]) - XP[i])) / hoursRan);
				if (!hideZone.contains(p)) {
					g.setColor(new Color(0, 0, 0));
					g.fillRect(2, 326 - x, 515, 12);

					g.setColor(new Color(0, 255, 0, 255));
					g.drawString(SKILL[i] + ": " + (int) xp_per_hour + " Xp/h",
							5, (337 - x));

					int x1 = 125, y1 = 327 - x;
					int CUR_LVL = Skills.getActualLevel(SKILL[i]);
					int NXT_LVL = (CUR_LVL + 1);
					int Percentage = Skills
							.getPercentToLevel(SKILL[i], NXT_LVL);
					double nextLv = Skills.getXPToLevel(SKILL[i], NXT_LVL);

					double hours = (nextLv / xp_per_hour);

					g.drawString("Curr lv: " + CUR_LVL + " ("
							+ (CUR_LVL - START_LVS[i]) + ")  TTL " + (int) hours
							+ ":" + (int) ((hours - (int) hours) * 60)
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
		
		int dh, db, ran;
		dh = ADDY_BARS + ADDY_COUNT;
		db = COINS + COINS_AMOUNT;
		ran = RANARRS + RANARR_COUNT;
		g.setColor(Color.CYAN);
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT2);
		
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
	        g.drawString((Constants.LOOT_MAPPING.get(pairs.getKey()) != null ? Constants.LOOT_MAPPING.get(pairs.getKey()) : pairs.getKey()) 
	        		+ " = " + pairs.getValue(), 5, 100+k);
	        it.remove(); // avoids a ConcurrentModificationException
	        k+=15;
	    }
		
		for(int i = 0; i < Nests.length; i++){
			if(Constants.AVIES_Area.contains(Nests[i].getPosition())){
				RSTile t = Nests[i].getPosition();				
				YawsGeneral.drawTile(t, g, false);
			}
		}
		g.setColor(Color.RED);
		RSNPC[] avv = NPCs.findNearest("Aviansie");
		for(RSNPC a : avv){
			if(a.getCombatLevel() == 69 || a.getCombatLevel() == 71 || a.getCombatLevel() == 83)
				YawsGeneral.drawTile(a.getPosition(), g, false);			
		}
		g.drawString(Combat.getTargetEntity().getName(), 5, 50);
		g.drawString(avv[0].getAnimation() + "", 5, 60);
		g.setColor(Color.GREEN);
		
		if (!hideZone.contains(p)) {
			g.setColor(new Color(60, 60, 60)); 
			g.fillRect(340, 370, 200, 150);

			g.setColor(Color.WHITE);
			g.drawString("Yawhide's BDK", 345, 385);
			g.drawString("Version :" + VERSION + "   Curr world: " + Game.getCurrentWorld(), 345, 405);
			g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
			g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour
					+ "/h)", 345, 435);
			g.drawString("State: " + FIGHT_STATUS, 345, 450);
			g.drawString("Addy bars: " + dh + "       GP: " + db/1000 + " K", 345, 465);
			g.drawString("Gp/hr: " + (int) ((dh*ADDY_PRICE + db + ran*RANARR_PRICE)/hoursRan/1000) + " K   Total: " + (int)(dh*ADDY_PRICE + db + ran*RANARR_PRICE)/1000 + " K", 345, 480);
			
		}
	}
	
	
	public Avies() {
		VERSION = ((ScriptManifest) getClass().getAnnotation(
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
		if(arg0.equals("You need a higher slayer level to know how to wound this monster.")){
			MOVE_RANDOM = true;
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