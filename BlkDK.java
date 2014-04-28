package scripts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;


@ScriptManifest(authors = { "Yaw hide" }, version = 2.03, category = "Ranged", name = "Yawhide's BlkDK", description = "Welcome to my first black dragon script!")
public class BlkDK extends Script implements Painting, Pausing {
	
	private int[] foodIDs  = { 333, 329, 379, 385, 7946 };
	int foodID;
	int[] ppot = { 2434, 139, 141, 143 }; 
	
	private int chicken = 2138;
	private int[] dragz = { 5978, 5980 };
	private int boltsID = 9142;

	RSTile[] toShrinePath = { new RSTile(2386, 4457, 0), new RSTile(2394, 4454, 0), new RSTile(2402, 4453, 0), new RSTile(2409, 4453, 0), new RSTile(2414, 4455, 0), new RSTile(2420, 4455, 0), new RSTile(2426, 4457, 0), new RSTile(2433, 4456, 0), new RSTile(2441, 4454, 0), new RSTile(2447, 4456, 0), new RSTile(2452, 4459, 0), new RSTile(2453, 4468, 0), new RSTile(2453, 4476, 0) };
	
	private RSTile[] toMiningArea = { new RSTile(2471, 4363, 0), new RSTile(2476, 4369, 0), new RSTile(2483, 4372, 0),
			new RSTile(2485, 4374, 0) };

	private RSTile[] toSafeSpotFromMiningArea = { new RSTile(2485, 4375, 0), new RSTile(2476, 4369, 0), new RSTile(2472, 4363, 0) };
	
	RSTile[] toBank = { new RSTile(2453, 4476, 0), new RSTile(2451, 4468, 0), new RSTile(2452, 4463, 0), new RSTile(2451, 4457, 0), new RSTile(2446, 4453, 0), new RSTile(2440, 4453, 0), new RSTile(2435, 4456, 0), new RSTile(2429, 4457, 0), new RSTile(2423, 4459, 0), new RSTile(2418, 4455, 0), new RSTile(2412, 4454, 0), new RSTile(2406, 4452, 0), new RSTile(2401, 4452, 0), new RSTile(2396, 4453, 0), new RSTile(2390, 4454, 0), new RSTile(2386, 4459, 0) };
	
	private RSTile[] toPortalPath = { new RSTile(2463, 4371, 0), new RSTile(2462, 4360, 0), new RSTile(2461, 4356, 0)};
	   
	private int[] rangepots = { 169, 2444, 171, 173 };
	
	private int[] junk = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767, 117,
			6963, 554, 556, 829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180,
			6965, 1969, 6183, 6181, 6962 };

	private int[] loot = { 1369, 816, 1747, 536, 9142, 868, 563,
			1615, 1315, 1319, 1373, 1247, 1303, 1249, 1123, 1149, 1197, 1201,
			1186, 1113, 1079, 892, 565, 560, 561, 563, 2361, 2366, 443, 1462,
			985, 987, 2363, 1617, 1619, 574, 11286, /* clue scroll id starts */
			2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 };

	private int[] loot2 = { 1369, 816, 1747, 536, 868, 563, 1615, 1315,
			1319, 1373, 1247, 1303, 1249, 1123, 1149, 1197, 1201, 1186, 1113,
			1079, 892, 565, 560, 561, 563, 2361, 2366, 443, 1462, 985, 987,
			2363, 1617, 1619, 574, 11286, /* clue scroll id starts */
			2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 };

	HashMap<Integer, String> map1 = new HashMap<Integer, String>(151);
	HashMap<Integer, String> map = new HashMap<Integer, String>(loot.length);
	
	private String[] names = { "Mithril battleaxe", "Adamant dart(p)",
			"Black dragonhide", "Dragon bones", "Mithril bolts",
			"Rune knife", "Law rune", "Dragonstone",
			"Mithril 2h sword", "Rune 2h sword", "Rune battleaxe",
			"Rune spear", "Rune longsword", "Dragon spear",
			"Adamant platebody", "Dragon med helm", "Mithril kiteshield",
			"Rune kiteshield", "Rune sq shield", "Rune chainbody",
			"Rune platelegs", "Rune arrow", "Blood rune", "Death rune",
			"Nature rune", "Law rune", "Adamantite bar", "Shield left half",
			"Silver ore", "Nature talisman", "Tooth half of key", "Loop half of key",
			"Runite bar", "Uncut diamond", "Uncut ruby", "Air orb",
			"Draconic visage", "Clue scroll (hard)" };

	public void putMap() {
		for (int i = 0; i < loot.length; i++) {
			if (i >= names.length) {
				map.put(loot[i], names[names.length - 1]);
			} else
				map.put(loot[i], names[i]);
		}
	}

	// antiban!
	private long antiban = System.currentTimeMillis();
	
	public State currentState;

	// zanaris stuff
	
	private RSTile zbanktile = new RSTile(2386, 4458, 0);
	private RSTile southshrine = new RSTile(2452, 4453, 0);
	private RSTile middlearea = new RSTile(2412, 4458, 0);
	private boolean forcebank = false;
	private RSTile shrinetile = new RSTile(2453, 4476, 0);
	private RSTile shrinetileT = new RSTile(2453, 4477, 0);
	private int shrine = 12093;
	private RSTile miningtile = new RSTile(2484, 4377, 0);
	private RSTile eloottile = new RSTile(2465, 4363, 0);
	private RSTile loottile;

	private RSTile nwbankz = new RSTile(2383, 4462, 0);
	private RSTile sebankz = new RSTile(2389, 4455, 0);

	// zanaris
	private RSTile nwdragz = new RSTile(2445, 4393, 0);
	private RSTile sedragz = new RSTile(2480, 4356, 0);

	// weird spot you shouldn't be in
	private RSTile nwWeirdSpotZ = new RSTile(2450, 4376, 0);
	private RSTile seWeirdSpotZ = new RSTile(2461, 4357, 0);

	// zanaris safe spot tiles
	private RSTile nwESafeSpotZ = new RSTile(2470, 4364, 0);
	private RSTile seESafeSpotZ = new RSTile(2473, 4363, 0);
	
	private RSTile evilChickenTile = new RSTile(2460, 4397, 0);
	private RSTile[] toSafeSpotPath = { new RSTile(2460, 4387, 0), new RSTile(2460, 4380, 0), new RSTile(2462, 4371, 0), 
			new RSTile(2466, 4363, 0), new RSTile(2472, 4363, 0) };
	
	RSTile weird = new RSTile(2473, 4363, 0);

	private RSTile safez;
	private RSTile safeEastZanarisMM = new RSTile(2471, 4363, 0);
	
	// general safespot variables
	private RSTile nwSafeSpotZ;
	private RSTile seSafeSpotZ;

	// ALL PAINT STUFF
	private int startXp = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.RANGED);
	private double version;
	private int current_xp;
	private final long start_time = System.currentTimeMillis();
	private double XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
	private double XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	RSTile lootTile = new RSTile(2465, 4363, 0);
	private RSTile portaltile = new RSTile(2459, 4354, 0);
	
	int startLv = Skills.getActualLevel(SKILLS.RANGED) + Skills.getActualLevel(SKILLS.HITPOINTS);
	
	public int antibancounter = 0;
	boolean usedchicken = false;
	String fightstate;

	// for counting # of items for paint
	private int tmp_lootCount = 0;
	private int dbones = 0;
	private int dhides = 0;
	private int visage = 0;
	private int cluescroll = 0;

	public BlkDK() {
		version = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
	}
	
	final String[] type = {"Defence", "Ranged", "Prayer", "Hitpoints" };
	final SKILLS[] Names = {SKILLS.DEFENCE, SKILLS.RANGED, 
			SKILLS.PRAYER, SKILLS.HITPOINTS, };
	final int[] XP = { Skills.getXP(SKILLS.DEFENCE), Skills.getXP(SKILLS.RANGED), 
    		Skills.getXP(SKILLS.PRAYER), Skills.getXP(SKILLS.HITPOINTS) };
	int[] startLvs = { 	Skills.getActualLevel(SKILLS.DEFENCE), Skills.getActualLevel(SKILLS.RANGED), 
			Skills.getActualLevel(SKILLS.PRAYER), Skills.getActualLevel(SKILLS.HITPOINTS) };
	
	@Override
	public void onPaint(Graphics g) {

		int xpGained = current_xp - startXp;
		long timeRan = System.currentTimeMillis() - start_time;
		
		double multiplier = timeRan / 3600000D;
		int xpPerHour = (int) (xpGained / multiplier);
		
		RSObject[] SHRINE = Objects.findNearest(10, shrine);
		if (SHRINE.length > 0) {
			drawModel(SHRINE[0].getModel(), g, false);
		}

		RSNPC[] drag = NPCs.findNearest(dragz);
		if (drag.length > 0) {
			if (drag[0].isInteractingWithMe()){
				drawTile(drag[0].getPosition(), g, false);
			}
			drawTile(drag[0].getPosition(), g, false);
		}
		
	
		long timeRan2 = getRunningTime();
		double secondsRan = (int) (timeRan2/1000);
		double hoursRan = secondsRan/3600;
		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(Names[i])) {

				double xp_per_hour = Math.round(((Skills.getXP(Names[i]) - XP[i])) / hoursRan);

				g.setColor(new Color(0, 0, 0));
				g.fillRect(2, 326 - x, 515, 12);

				g.setColor(new Color(0, 255, 0, 255));
				g.drawString(Names[i] + ": " + (int) xp_per_hour + " Xp/h", 5, (337 - x));

				int x1 = 150, y1 = 327 - x;
				int CUR_LVL = Skills.getActualLevel(Names[i]);
				int NXT_LVL = (CUR_LVL + 1);
				int Percentage = Skills.getPercentToLevel(Names[i], NXT_LVL);
				double nextLv = Skills.getXPToLevel(Names[i], NXT_LVL);
				
				double hours = (nextLv / xp_per_hour);

				g.drawString("Current level: " + CUR_LVL + "   Levels gained: " 
						+ (CUR_LVL - startLvs[i]) + " TTL " + 
						(int) hours + ":" + (int) ((hours - (int) hours) * 60)
						+ " hr:min"
				, 257, y1 + 10);

				g.setColor(new Color(255, 0, 0, 255));
				g.fillRect(x1, y1, 100, 10);
				g.setColor(new Color(0, 255, 0, 255));
				g.fillRect(x1, y1, Percentage, 10);

				g.setColor(new Color(0, 0, 0));
				g.drawString(Percentage + "% to " + NXT_LVL, x1 + 25, y1 + 9);

				x += 15;
			}
		}
		
			g.setColor(new Color(60, 60, 60));
			g.fillRect(340, 370, 225, 180);

			g.setColor(Color.WHITE);
			g.drawString("Yawhide's BlkDK", 345, 385);
			g.drawString("Version :" + version, 345, 405);
			g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
			g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour
					+ "/h)", 345, 435);
			
			g.drawString("Current State: " + currentState, 345, 450);
			g.drawString("Sub Fight State: " + fightstate, 345, 465);
			g.drawString("Dbones:" + dbones + " Dhide: " + dhides
					+ " visages: " + visage + " clues: " + cluescroll,
					345, 480);
		
	}

	
	// gui variables here
	public boolean GUI_COMPLETE = false;
	public int location;
	public String safespot;
	public String foodusing;
	public boolean rangepotting = false;
	boolean prayFlick = false;
	boolean scriptStatus = true;

	@Override
	public void run() {

		myForm GUI = new myForm();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenW = screenSize.width;
		int screenH = screenSize.height;

		Dimension dim = GUI.getSize();

		GUI.setVisible(true);

		GUI.setLocation((screenW / 2) - (dim.width / 2), (screenH / 2)
				- (dim.height / 2));

		while (!GUI_COMPLETE) {
			sleep(200);
		}
		GUI.setVisible(false);

		println("   ");
		println("GUI CLOSED and COMPLETED!");
		println("Number of food is: " + location);
		println("Food going to be used: " + foodusing);
		println("Using range pots? " + rangepotting);
		println("   ");

		// after gui variable initialization
		
		figureoutwhattodo(); // check if we are going to kill stuff in
								
		sleep(80, 100);

		println("Welcome to my BlkDK!!!!");
		Mouse.setSpeed(General.random(120, 140));

		putMap();
		sleep(200, 250);
		General.useAntiBanCompliance(true);
		boltsID = Equipment.getItem(SLOTS.ARROW).getID();
		if (boltsID < 1){
			scriptStatus = false;
			println("YOu need to have the bolts you want to use equiped when you use this script");
		}
		sleep(200,300);
		
		while (scriptStatus) {

			current_xp = Skills.getXP(SKILLS.RANGED) + Skills.getXP(SKILLS.HITPOINTS);
			currentState = getState();
			XpToLVrange = Skills.getXPToNextLevel(SKILLS.RANGED);
			XpToLVhp = Skills.getXPToNextLevel(SKILLS.HITPOINTS);

			if(pos().distanceTo(new RSTile(3201, 3169, 0)) <=5 && pos().getPlane() == 0){
				println("somehow i got out of zanaris");
				scriptStatus = false;
			}
			
			if (getHp() < 30) {

				println("My current position is: " + pos().getX() + " , "
						+ pos().getY());
				println("My current state: " + currentState);
				println("My current substate: " + fightstate);
				sleep(200, 250);
			}

			switch (getState()) {

			case HEAL:
				HEAL();
				break;
			case DROPJUNK:
				DROPJUNK();
				break;
			case LOOT:
				LOOT();
				break;
			case ANTIBAN:
				ANTIBAN();
				break;
			case GOTO_SHRINE:
				GOTO_SHRINE();
				break;
			case USE_SHRINE:
				USE_SHRINE();
				break;
			case GOTO_SOUTHSHRINE:
				GOTO_SOUTHSHRINE();
				break;
			case GOTO_MIDDLEAREA:
				GOTO_MIDDLEAREA();
				break;
			case GOTO_BANKZ:
				GOTO_BANKZ();
				break;
			case BANKZ:
				BANKZ();
				break;
			case FIGHTZ:
				FIGHTZ();
				break;
			case GOTO_SAFEZ:
				GOTO_SAFEZ();
				break;
			case WALK_PATH_SHRINE:
				WALKING_PATH_SHRINE();
				break;
			case WALKING_PATH_BANK:
				WALKING_PATH_BANK();
				break;
			case GOTO_SAFEZ_FROM_MINING:
				GOTO_SAFEZ_FROM_MINING();
				break;
			case UNKNOWN_POSITION:
				break;
			case BANK:
				break;
			case GOTO_BANK:
				break;
			
			}
			sleep(50, 100);
		}
	}

	private static enum State {
		ANTIBAN, HEAL, DROPJUNK, LOOT, BANK, USE_SHRINE, UNKNOWN_POSITION, 
		
		GOTO_BANK, JUMP_WALL, GOTO_WALL, GOTO_LADDER, CLIMB_LADDER, GOTO_SPIKE, JUMP_SPIKE, GOTO_DRAG,

		GOTO_SHRINE, GOTO_SOUTHSHRINE, GOTO_MIDDLEAREA, GOTO_BANKZ, BANKZ, FIGHTZ, GOTO_SAFEZ, 
		
		WALK_PATH_SHRINE, WALKING_PATH_BANK, GOTO_SAFEZ_FROM_MINING
	}

	public State getState() {

		
		if (pos().distanceTo(evilChickenTile) <= 6){
			Walking.walkPath(toSafeSpotPath);
		}
		else if (((pos().distanceTo(portaltile) <= 2 && inCombat()) || inArea(
				nwWeirdSpotZ, seWeirdSpotZ))
				&& Inventory.getCount(foodID) <= 5
				&& Inventory.getCount(loot) < 3 && forcebank == false)
			return State.GOTO_SAFEZ;

		else if (getHp() <= 50) {
			if (Inventory.getCount(foodID) == 0) {
				
				println("Out of food and low hp, getting the hell out of here");
				forcebank = true;
				if (inDragArea())
					return State.GOTO_BANKZ;
				else if (pos().distanceTo(zbanktile) <= 7)
					return State.BANKZ;
				else
					return State.GOTO_BANKZ;
			} 
			else if (inDragArea()){
				return State.HEAL;
			}
			else if (pos().distanceTo(zbanktile) <= 7)
				return State.BANKZ;
			else
				return State.GOTO_BANKZ;
		}
		else if (isFull()) {
			if (pos().distanceTo(zbanktile) <= 7)
				return State.BANKZ;
			else if (!inDragArea()){
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90,  100));
				}
				return State.GOTO_BANKZ;
			}
			else if (Inventory.getCount(foodID) > 0 || Inventory.getCount(junk) > 0) {
				return State.DROPJUNK;
			}
			else {
				return State.GOTO_BANKZ;
			}

		}

		else if (inDragArea()) {

			return State.FIGHTZ;
		}
		
		else if (doneBanking() && !inDragArea()) {
			
			if (pos().distanceTo(shrinetile) <= 4)
				return State.USE_SHRINE;
			
			return State.WALK_PATH_SHRINE;
		}

		else if (pos().distanceTo(miningtile) <= 4)
			return State.GOTO_SAFEZ_FROM_MINING;

		else if (!doneBanking()) {

			if (pos().distanceTo(zbanktile) <= 7)
				return State.BANKZ;
			return State.GOTO_BANKZ;
		}
		
		// antiban logic
		else if (System.currentTimeMillis() - antiban > (General.random(300000,
				600000)))
			return State.ANTIBAN;

		return State.UNKNOWN_POSITION;

	}
	
	//TODO 
	// Walking path method
	public void WALKING_PATH_SHRINE(){

		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		Walking.setWalkingTimeout(500L);
		Walking.walkPath(toShrinePath);
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		sleep(50, 100);
	}
	
	public void WALKING_PATH_BANK(){

		Walking.setWalkingTimeout(500L);
		Walking.walkPath(toBank);
		sleep(50, 100);
	}
	public void GOTO_SAFEZ_FROM_MINING(){

		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		Walking.setWalkingTimeout(500L);
		Walking.walkPath(toSafeSpotFromMiningArea);
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		sleep(50, 100);
	}
	
	public void GOTO_SHRINE() {

		Walking.setWalkingTimeout(500L);
		Walking.walkPath(Walking.generateStraightPath(shrinetile));
		sleep(50, 100);
	}

	public void GOTO_SOUTHSHRINE() {

		Walking.setWalkingTimeout(500L);
		Walking.walkPath(Walking.generateStraightPath(southshrine));
		sleep(80, 100);
	}

	public void GOTO_MIDDLEAREA() {

		Walking.setWalkingTimeout(500L);
		Walking.walkPath(Walking.generateStraightPath(middlearea));
		sleep(80, 100);
	}

	public void USE_SHRINE() {

		Camera.setCameraRotation(General.random(0, 3));
		RSItem[] chicken = Inventory.find(2138);
		RSObject[] shrine = Objects.getAt(shrinetileT);//.findNearest(5, 12093);

		if (shrine.length > 0) {

			if (shrine[0].isOnScreen()) {
				if (chicken.length > 0) {
					if (chicken[0].click("Use")) {
						sleep(500, 1000);
						// Mouse.setSpeed(General.random(330, 360));
						// println("setting mouse speed to 330-360");
						if (clickOBJ(shrine[0], "Use")) {// DynamicClicking.clickRSObject(shrine[0],
															// "Use")) {
							
							Timing.waitCondition(new Condition() {
								;
								@Override
								public boolean active() {
									return inDragArea();
								}
							}, General.random(2200, 2300));
						}

					}
				}
			}

		}
		sleep(1000, 1200);

	}
	
	public void USE_PORTAL() {
		
		RSObject[] PORTAL = Objects.findNearest(10, "Portal");
		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		
		Camera.turnToTile(portaltile);
		sleep(200,300);
		
		if (PORTAL.length > 0){
			
			if(PORTAL[0].click("Use")){//clickOBJ(PORTAL[0], "Use")){
				Timing.waitCondition(new Condition() {
					;
					@Override
					public boolean active() {
						return !inDragArea();
					}
				}, General.random(2200, 2300));
			}
		}
		
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
	}

	public void GOTO_BANKZ() {

		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		Walking.setWalkingTimeout(500L);
		if(pos().distanceTo(portaltile) <=2){
			USE_PORTAL();
			sleep(200,300);
		}
		else if(inDragArea()){
			Walking.walkPath(toPortalPath);
			sleep(200,300);
			USE_PORTAL();
		}
		else
			Walking.walkPath(toBank);
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		sleep(80, 100);
	}

	public void GOTO_SAFEZ() { // safez

		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		Mouse.setSpeed(General.random(100, 105));
		Walking.setWalkingTimeout(1500L);
		fightstate = "Going to safe spot";
		
		if (pos().distanceTo(safez) > 3)
			Walking.clickTileMM(safez, 1);
		else if (safez.isOnScreen() && pos().distanceTo(safez) > 2) {
			if (Camera.getCameraAngle() < 60) {
				Camera.setCameraAngle(General.random(60, 70));
			}
			Walking.blindWalkTo(safez);
		} 
		else if (!inSafeSpotZ()){
			Walking.clickTileMM(safez, 1);
		}
		waitIsMovin();
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		sleep(80, 100);
		if(pos().distanceTo(weird) == 0){
			Walking.walkScreenPath(Walking.generateStraightPath(safez));
			waitIsMovin();
		}
		Mouse.setSpeed(General.random(140, 160));
	}
	
	//TODO banking method
	
	public void BANKZ() {

		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);

		if (inArea(nwbankz, sebankz)) {

			Camera.turnToTile(zbanktile);
			Camera.setCameraAngle(General.random(95, 100));
			sleep(100, 150);
			if (Banking.openBankBanker()){
				sleep(800, 1100);
			
				if (Banking.isPinScreenOpen()) {

					Banking.inPin();
					sleep(200, 250);
				}
				else if (Banking.isBankScreenOpen()) {
					if (Inventory.getAll().length != 0){
						Banking.depositAll();
						sleep(200, 250);
					}
						/*	this is where prayer flicking was invented*/
					if(prayFlick){
					int pt = Skills.getActualLevel(SKILLS.PRAYER) / 3;
					int currP = Skills.getCurrentLevel(SKILLS.PRAYER);
					RSItem[] pPot;
					if( currP < pt * 2){
						println("potting prayer, pt is: "+pt);
						
						Banking.withdraw(1, ppot);
						sleep(600,640);
						Banking.close();
						sleep(200,230);
						
						pPot = Inventory.find(ppot);
						if(pPot.length > 0){
							do{
								pPot[0].click("Drink");
								sleep(1000,1200);
							}while(Skills.getCurrentLevel(SKILLS.PRAYER) <= (pt*2)-2);
						}
						sleep(200,250);
						if (!Banking.isBankScreenOpen()) {
							if (Banking.openBankBanker()) {
								sleep(200, 250);
							}
						}
					}
					sleep(200,300);
					
					Banking.depositAll();
					sleep(200,300);
					}
					
					
					if (getHp() <= 85) {

						println("eating to full HP");
						Banking.withdraw(((Skills
								.getActualLevel(SKILLS.HITPOINTS) - Skills
								.getCurrentLevel(SKILLS.HITPOINTS)) / 9), foodID);
					
						sleep(200, 250);
						Banking.close();
						
						sleep(200, 250);
						do {
							HEAL();
							sleep(200, 250);
						} while (Inventory.getCount(foodID) > 0 && getHp() < 85);
						sleep(1200, 1250);
						
						if (!Banking.isBankScreenOpen()) {
							if (Banking.openBankBanker()) {
								sleep(1200, 1250);
							}
						}
						
					}
					if (Banking.isBankScreenOpen()) {

						Banking.depositAll();
						sleep(600,650);
						Banking.withdraw(location, foodID);
						sleep(600, 650);
						
						if(Banking.find(chicken).length == 0)
							scriptStatus = false;
						Banking.withdraw(1, chicken);
						sleep(600, 650);
						
						if (Banking.find(rangepots).length > 0
								&& rangepotting) {
							
							Banking.withdraw(1, rangepots);
							sleep(600, 650);
						}
						
						if (getStackBolts() < 300) {

							println("withdrawing some bolts cuz we have less than 500");
							
							Banking.withdraw(500, boltsID);
							sleep(600, 650);
						}

						sleep(600, 650);
						Banking.close();
						sleep(200, 250);
						forcebank = false;
						
						if(Inventory.find(boltsID).length > 0){
							Inventory.find(boltsID)[0].click("Wield");
							sleep(600,650);
						}
						
						println("Done banking for stuff");
						
					}
				}
			}
		} else
			Walking.walkScreenPath(Walking
					.generateStraightScreenPath(zbanktile));
		
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		sleep(80, 100);
	}

	//TODO
	//loot method
	public void LOOT() {

		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
		fightstate = "looting";
		
		RSGroundItem[] Nests = GroundItems.findNearest(loot);

		if (Camera.getCameraRotation() > 42 && Camera.getCameraRotation() < 140
				&& pos().getX() > (loottile.getX() + 2))
			Camera.turnToTile(loottile);
		if (Camera.getCameraAngle() > 60
				&& pos().getX() > (loottile.getX() + 2))
			Camera.setCameraAngle(General.random(38, 42));

		if (getHp() <= 50) {
			if (Inventory.getCount(foodID) == 0) {
				forcebank = true;
			} 
			else
				HEAL();
		}

		if (Nests.length > 0) {

			if (!Nests[0].isOnScreen()) {

				Camera.turnToTile(loottile);
			}
			String str = map.get(Nests[0].getID());

			tmp_lootCount = lootCount(Nests[0].getID());

			Nests[0].click("Take " + str);

			waitForInv(Nests[0].getID());

			if (str == "Clue scroll")
				cluescroll++;

			tmp_lootCount = 0;
		}

		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
	}

	// TODO
	// fight and loot methods

	public void FIGHTZ() {

		RSNPC[] blkdragsz = NPCs.findNearest(dragz);
		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);

		// if there is loot PICK IT UP!
		if (lootIsOnGround() && pos().distanceTo(portaltile) > 1) {

			if (Inventory.getCount(junk) > 0 && Inventory.getAll().length > 24
					&& inSafeSpotZ())
				DROPJUNK();

			if (Inventory.getCount(junk) > 0)
				dropJunk_noEating();

			sleep(80, 100);
			println("lootin time");
			LOOT();
			sleep(80, 100);

		}

		// if I am west of the loot tile, get back to safe spot
		else if (inCombat()	&& gotFood() && getHp() > 60 && !inSafeSpotZ()) {
			println("I am a little too far out from the safespot, lets go back");

			GOTO_SAFEZ();
		}
		
		// basically, pot up if you are rangelv + 2 and below
		else if (Inventory.getCount(rangepots) > 0
				&& inSafeSpotZ()
				&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
						.getActualLevel(SKILLS.RANGED) + 2) {

			println(Skills.getCurrentLevel(SKILLS.RANGED) + "  "
					+ Skills.getActualLevel(SKILLS.RANGED));
			fightstate = "Potting up";
			drinkPotion(rangepots);
			sleep(1000,1200);
			println("Potted up");

		}

		// in safe spot, dragon isn't in cb
		else if (blkdragsz.length > 0 && 
				!blkdragsz[0].isInteractingWithMe() && !isRanging() && inSafeSpotZ() 
				&& pos().distanceTo(blkdragsz[0].getPosition()) < 15) {

			killDragon(blkdragsz);
		}

		// dragon is in combat and I am not in combat.
		else if (blkdragsz.length > 0 && blkdragsz[0].isInteractingWithMe() && inSafeSpotZ()) {
			if (!isRanging()) {
				killDragon(blkdragsz);
			} 
			else if (prayFlick)
				prayerflick();
			else {
				fightstate = "Doing some antiban";

				//if(Skills.getCurrentLevel(SKILLS.PRAYER) > 10)
				//	prayerflick(blkdragsz[0]);
				
				if (antibancounter == General.random(120, 140)) {
					ANTIBAN();
					antibancounter = 0;
				} 
				else {
					sleep(1000, 1200);
					antibancounter += 1;
				}
			}
		}

		// basically, i'll just be standing in the danger zone doing
		// nothing....so
		// go back to safespot.
		else if (!inSafeSpotZ()) {

			// Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
			println("going back to safespot, nothing else to do");

			GOTO_SAFEZ();

			// Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		}

		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
		sleep(80, 100);

	}

	public void HEAL() {
		fightstate = "Healing up";
		GameTab.open(TABS.INVENTORY);
		if (Inventory.getCount(foodID) > 0) {
			if(Clicking.click("Eat", Inventory.find(foodID)))
				sleep(300, 550);
		} else {
			forcebank = true;
			println("forcebank = true");
		}
	}

	public boolean DROPJUNK() {

		// hate dropping junk where i loot, bad idea
		fightstate = "Dropping junk/food";
		if (pos().distanceTo(eloottile) < 1) {
			if (Camera.getCameraAngle() >= 38 || Camera.getCameraAngle() <= 42) {
				Camera.setCameraAngle(General.random(80, 90));
			}
			sleep(80, 100);
		}
		GameTab.open(GameTab.TABS.INVENTORY);
		sleep(150, 200);
		RSItem[] junks = Inventory.find(junk);
		RSItem[] food = Inventory.find(foodID);
		RSItem[] bolts = Inventory.find(9142);
		RSItem[] coinstack = Inventory.find(995);
		if (bolts.length > 0) {
			bolts[0].click("Wield");
			sleep(200, 250);
		}
		if (food.length > 0) {
			if (getHp() == 100)
				Inventory.drop(food[0].getID());
			else
				food[0].click("Eat");
			sleep(650, 700);
		}
		sleep(150, 200);
		Inventory.drop(junk);
		sleep(80, 100);
		if (Inventory.getAll().length > 25 && coinstack.length > 0
				&& coinstack[0].getStack() < 1000) {
			Inventory.drop(995);
			sleep(100, 150);
		}
		return junks.length == 0;
	}

	public boolean dropJunk_noEating() {

		fightstate = "Droping junk items ONLY";
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		sleep(150, 200);
		RSItem[] junks = Inventory.find(junk);
		RSItem[] bolts = Inventory.find(9142);
		RSItem[] coinstack = Inventory.find(995);
		if (bolts.length > 0) {
			bolts[0].click("Wield");
			sleep(200, 250);
		}
		Inventory.drop(junk);
		sleep(80, 100);
		if (Inventory.getAll().length > 25 && coinstack.length > 0
				&& coinstack[0].getStack() < 1000) {
			Inventory.drop(995);
			sleep(100, 150);
		}
		return junks.length == 0;
	}

	public boolean isFull() { return Inventory.isFull() || Inventory.getAll().length == 27;	}

	public void cameraDrag() {	Camera.setCameraRotation(General.random(88, 92));}

	public void cameraPortal() {	Camera.setCameraRotation(General.random(178, 182));	}

	public void cameraBank() {	Camera.setCameraRotation(General.random(0, 2));	}

	public boolean inCombat() {	return Player.getRSPlayer().isInCombat();	}

	public int getHp() {return Combat.getHPRatio();	}

	public RSTile pos() {	return Player.getPosition();}

	public boolean inArea(RSTile nw, RSTile se) {
		RSTile pos = pos();
		int posX = pos.getX();
		int posY = pos.getY();
		int nwX = nw.getX();
		int nwY = nw.getY();
		int seX = se.getX();
		int seY = se.getY();
		int p = pos().getPlane();
		int p2 = nw.getPlane();
		int p3 = se.getPlane();

		if (posX >= nwX && posX <= seX && posY >= seY && posY <= nwY
				&& p == p2 && p == p3) {
			return true;
		}
		return false;
	}

	public void checkXP() {

		GameTab.open(TABS.STATS);
		sleep(80, 100);
		Mouse.moveBox(557, 307, 600, 321);
		sleep(1700, 2200);
		GameTab.open(TABS.INVENTORY);
		sleep(80, 100);

	}

	public void checkFriends() {

		GameTab.open(TABS.FRIENDS);
		sleep(80, 100);
		Mouse.moveBox(616, 240, 664, 261);
		sleep(1700, 2200);
		GameTab.open(TABS.INVENTORY);
		sleep(80, 100);
	}

	private void ANTIBAN() {

		int x = General.random(1, 2);
		if (x == 1)
			checkXP();
		else
			checkFriends();
		antiban = System.currentTimeMillis();
	}

	private boolean clickNPC(RSNPC npc, String option) {
		
		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			loc = npc.getPosition();
			Mouse.move(Projection.tileToScreen(loc, 10));
			if (Game.isUptext(option)) {
				Mouse.click(1);
				waitForDrag(npc);
				return true;
			} else {
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
		}
		return false;
	}
//TODO doneBanking
	private boolean doneBanking() {
		return (getHp() >= 85
				&& (Inventory.getCount(foodID) == location)
				&& (Inventory.getCount(rangepots) == 1 || rangepotting == false)
				&& Inventory.getCount(chicken) == 1);
	}

	// TODO
	public boolean lootIsOnGround() {
				
		RSGroundItem[] nest = GroundItems.findNearest(loot2);
		int junky = 0;
		
		for (int i = 0; i < nest.length; i++) {
			if (nest[i].getPosition().distanceTo(loottile) > 2)
				junky += 1;
		}
		if (junky == nest.length)
			return false;
		return true;
	}

	public void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				potion[0].click("Drink");
				General.sleep(1100, 1300);
			}
		}
	}
	//TODO
	// dragon killing method
	public void killDragon(RSNPC[] dragon) {
		if (dragon[0].isOnScreen()) {

			// so drag is more than 8 tiles, that means I'll be running
			// after i attack
			// so just click back to to the tile right after i attack
			if (dragon[0].getPosition().distanceTo(pos()) > 6) {
				clickNPC(dragon[0], "Attack"); 
				println("attacked a dragon that is further than 8 tiles away, run to safespot!");
				

				final RSNPC[] tmp_blkdrag = dragon;
				Timing.waitCondition(new Condition() {
					;
					@Override
					public boolean active() {

						return Player.getAnimation() == 4230
								|| inCombat()
								|| (tmp_blkdrag.length > 0 && tmp_blkdrag[0]
										.isInCombat());
					}
				}, General.random(2200, 2400));
				
				
				for (int i = 0; i < 57; i++, sleep(30, 40)) {
					if (!inSafeSpotZ()) {
						GOTO_SAFEZ();
						break;
					}
				}
			}
			// drag is close enough that i wont run when I attack it.
			else {
				clickNPC(dragon[0], "Attack"); // clickModel(blkdragsz[0].getModel(),
												// "Attack", true);
				println("attacking black dragon at safespot!");
		

				final RSNPC[] tmp_blkdrag = dragon;
				Timing.waitCondition(new Condition() {
					;
					@Override
					public boolean active() {

						return Player.getAnimation() == 4230
								|| inCombat()
								|| (tmp_blkdrag.length > 0 && tmp_blkdrag[0]
										.isInCombat());
					}
				}, General.random(1200, 1400));
				for (int i = 0; i < 57; i++, sleep(30, 40)) {
					if (!inSafeSpotZ()) {
						GOTO_SAFEZ();
						break;
					}
				}
			}
		}
		// drag isn't on screen
		else {
			println("Turning to face dragon");
			Camera.turnToTile(dragon[0].getPosition());
			if (Camera.getCameraAngle() < 35 || Camera.getCameraAngle() > 38) {
				Camera.setCameraAngle(General.random(35, 38));
			}
		}
		sleep(1500, 1700);
	}
	
	private boolean clickOBJ(RSObject npc, String option) {
		Camera.turnToTile(npc.getPosition());
		
		if (npc.isOnScreen()) {
			Point[] points = npc.getModel().getAllVisiblePoints();

			if (points.length > 0) {
				Mouse.move(points[points.length / 2]);
				
				if (Game.isUptext(option)) {
					Mouse.click(1);
					return true;
				} else {
					Mouse.click(3);
					if (ChooseOption.isOpen()) {
						ChooseOption.select(option);
					}
				}
			}
		}
		return false;
	}

	

	public boolean inSafeSpotZ() {	return inArea(nwSafeSpotZ, seSafeSpotZ);}

	public boolean isMovin() {return Player.isMoving();}

	public boolean gotFood() {return Inventory.getCount(foodID) > 0;}

	public int distanceTo(RSTile myPos, RSTile t){
		return Math.max(myPos.getX()-t.getX(), myPos.getY() - t.getY());
		
	}
	
	public void runFromCombat() {
		
		Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
	
		Walking.setWalkingTimeout(500L);
		Walking.walkPath(toMiningArea);
		println("waiting about 5 seconds just to make sure the chicken leaves!");
		sleep(4900, 5500);
		Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
	}

	public int getStackBolts() {return Equipment.getItem(SLOTS.ARROW).getStack();}

	public int lootCount(int ID) {	return Inventory.getCount(ID); }

	// improved waiting for loot
	private void waitForInv(int lootID) {
		int k = 0;

		while (lootCount(lootID) == tmp_lootCount && k < 200
				&& Player.isMoving()) {
			sleep(80);
			k++;
		}

		if (lootCount(lootID) > tmp_lootCount) {

			if (lootID == 1747)
				dhides++;
			else if (lootID == 536)
				dbones++;
			else if (lootID == 11286)
				visage++;
		}
	}

	public boolean clickModel(RSModel model, String option, boolean rightClick){

		Point[] points = model.getAllVisiblePoints();
		int length = points.length;
		if (length != 0) {
			Point p = points[General.random(0, length - 1)];
			Mouse.move(p);
			{
				String top = org.tribot.api2007.Game.getUptext();
				if (top.contains(option)) {
					Mouse.click(p, 0);
					return true;
				} 
				else if (rightClick) {
					Mouse.click(3);
					Timer t = new Timer(500);
					while (t.isRunning() && !ChooseOption.isOpen())
						sleep(50, 100);
					if (!ChooseOption.isOpen())
						return false;
					if (ChooseOption.select(option))
						return true;
				}
			}
		}
		return false;
	}

	public void figureoutwhattodo() {
		safez = safeEastZanarisMM;
		nwSafeSpotZ = nwESafeSpotZ;
		seSafeSpotZ = seESafeSpotZ;
		loottile = eloottile;
		
		
		if (foodusing.equals("Trout")) {

			foodID = foodIDs[0];
			println("We are going to use trout as our food");
		} else if (foodusing.equals("Salmon")) {

			foodID = foodIDs[1];
			println("We are going to use Salmon as our food");
		} else if (foodusing.equals("Lobster")){
			foodID = foodIDs[2];
			println("We are going to use Lobster as our food");
			println("Food ID is: " + foodID);
		}
		else if (foodusing.equals("Shark")) {

			foodID = foodIDs[3];
			println("We are going to use Tuna as our food");
			println("Food ID is: " + foodID);
		} 
		 else if (foodusing.equals("Monkfish")){
			foodID = foodIDs[4];
			println("We are going to use Monkfish as our food");
			println("Food ID is: " + foodID);
		}
		 else{
			 println("Something went wrong with the GUI");
			 scriptStatus = false;
			 
		 }
	}
	
	public void waitForDrag(RSNPC drag){
		while(drag.isInCombat() && isRanging() && inSafeSpotZ())
			sleep(1000,1300);
	}

	public boolean isRanging() {return Player.getRSPlayer().getInteractingCharacter() != null; }
	
	public boolean inDragArea() {
		
		if(inArea(nwdragz, sedragz))
			return true;
		return false;
	}
	
	@Override
	public void onPause() {
		if (inDragArea() && !inSafeSpotZ()) {
			Walking.walkPath(Walking.generateStraightPath(safez));
			sleep(50, 100);
		}

		
	}
	@Override
	public void onResume() {
		
	}

	public void waitIsMovin(){
		
		for(int i = 0; i < 57; i++, sleep(30, 40)){
			if (!Player.isMoving())
				break;
		}
	}
	
	//GOTO prayerFlick
	public void prayerflick() {// RSNPC drag) {

		while (getHp() > 30 && Skills.getCurrentLevel(SKILLS.PRAYER) > 5 && isRanging() && inSafeSpotZ() && !lootIsOnGround()) {
			
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
			}
			Timer t = new Timer(1100L);
			do {
				if (Player.getAnimation() != 4230) {
					sleep(250, 300);
					Prayer.enable(PRAYERS.EAGLE_EYE);
				} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
					Prayer.disable(PRAYERS.EAGLE_EYE);
					sleep(350, 400);
				} else {
					sleep(400, 450);
				}
			} while (t.getRemaining() > 0);
		}

		turnOffPrayerEagle();
	}
	
	public void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			sleep(800,1000);
		}
	}
	public void drawModel(RSModel model, Graphics g, boolean fill) {
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

	
	public void drawPath(RSTile[] path, Graphics g, boolean minimap) {
		
		if (minimap) {
			// Drawing on the other whatever
			for (int i = 0; i < path.length - 1; i++) {
				Point tilePoint = Projection.tileToMinimap(path[i]);
				if (Projection.isInMinimap(tilePoint)) {
					if (i < path.length - 1) {
						Point nextPoint = Projection.tileToMinimap(path[i + 1]);
						if (Projection.isInMinimap(nextPoint)) {
							g.drawLine(tilePoint.x, tilePoint.y, nextPoint.x,
									nextPoint.y);
						}
					}
					g.fillOval(tilePoint.x, tilePoint.y, 3, 3);
				}
			}
		} else {
			for (int i = 0; i < path.length - 1; i++) {
				if (path[i].getPosition().isOnScreen()) {
					if (i > 0) { // could just start index
						Point currentTile = Projection.tileToScreen(path[i], 0);
						Point lastTile = Projection
								.tileToScreen(path[i - 1], 0);
						g.drawLine(currentTile.x, currentTile.y, lastTile.x,
								lastTile.y);
					}
					//g.setColor("#FF0000");
					drawTile(path[i], g, false);
				}
			}
		}
	}

	public void drawTile(RSTile tile, Graphics g, boolean fill) {
		if (tile.getPosition().isOnScreen()) {
			if (fill) {
				g.fillPolygon(Projection.getTileBoundsPoly(tile, 0));
			} else {
				g.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
			}
		}
	}
	
	@SuppressWarnings("serial")
	class myForm extends javax.swing.JFrame {

		/**
		 * Creates new form myForm
		 */
		public myForm() {
			initComponents();
		}

		/**
		 * This method is called from within the constructor to initialize the
		 * form. WARNING: Do NOT modify this code. The content of this method is
		 * always regenerated by the Form Editor.
		 */
		@SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initComponents() {

	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        jLabel4 = new javax.swing.JLabel();
	        jLabel5 = new javax.swing.JLabel();
	        DATA_RANGEPOT = new javax.swing.JRadioButton();
	        DATA_LOC = new javax.swing.JComboBox();
	        DATA_FOOD = new javax.swing.JComboBox();
	        jLabel6 = new javax.swing.JLabel();
	        jLabel7 = new javax.swing.JLabel();
	        DATA_START_BUTTON = new javax.swing.JButton();
	        jLabel8 = new javax.swing.JLabel();
	        DATA_RANGEPOT1 = new javax.swing.JRadioButton();

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
	        jLabel1.setText("Yaw hide's Dragon Ranger - Black dragon edition");

	        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
	        jLabel2.setText("How much food to withdraw");

	        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
	        jLabel4.setText("Choose your food!");

	        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
	        jLabel5.setText("Will you drink ranging potions?");

	        DATA_RANGEPOT.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
	        DATA_RANGEPOT.setText("Yes (uncheck for no)");
	        DATA_RANGEPOT.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                DATA_RANGEPOTActionPerformed(evt);
	            }
	        });

	        DATA_LOC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
	        DATA_LOC.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                DATA_LOCActionPerformed(evt);
	            }
	        });

	        DATA_FOOD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Trout", "Salmon", "Lobster", "Shark", "Monkfish" }));
	        DATA_FOOD.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                DATA_FOODActionPerformed(evt);
	            }
	        });

	        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
	        jLabel6.setText("Please remember to have all the required items in the top left ");

	        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
	        jLabel7.setText("corner of your bank!");

	        DATA_START_BUTTON.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
	        DATA_START_BUTTON.setText("START");
	        DATA_START_BUTTON.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                DATA_START_BUTTONActionPerformed(evt);
	            }
	        });

	        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
	        jLabel8.setText("Will you be prayer flicking?");

	        DATA_RANGEPOT1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
	        DATA_RANGEPOT1.setText("Yes (uncheck for no)");
	        DATA_RANGEPOT1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                DATA_RANGEPOT1ActionPerformed(evt);
	            }
	        });

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGap(183, 183, 183)
	                .addComponent(DATA_START_BUTTON)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(3, 3, 3)
	                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
	                    .addComponent(jLabel7))
	                .addGap(0, 0, Short.MAX_VALUE))
	            .addGroup(layout.createSequentialGroup()
	                .addGap(51, 51, 51)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jLabel5)
	                            .addComponent(jLabel2)
	                            .addComponent(jLabel4))
	                        .addGap(63, 63, 63)
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(DATA_LOC, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
	                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                                    .addComponent(DATA_RANGEPOT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
	                                    .addComponent(DATA_FOOD, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                                .addGap(0, 0, Short.MAX_VALUE))))
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(layout.createSequentialGroup()
	                                .addComponent(jLabel8)
	                                .addGap(84, 84, 84)
	                                .addComponent(DATA_RANGEPOT1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                            .addComponent(jLabel1))
	                        .addContainerGap())))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jLabel1)
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(DATA_LOC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel4)
	                    .addComponent(DATA_FOOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel5)
	                    .addComponent(DATA_RANGEPOT))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel8)
	                    .addComponent(DATA_RANGEPOT1))
	                .addGap(16, 16, 16)
	                .addComponent(jLabel6)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(jLabel7)
	                .addGap(18, 18, 18)
	                .addComponent(DATA_START_BUTTON)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );

	        pack();
	    }// </editor-fold>                        

	    private void DATA_RANGEPOTActionPerformed(java.awt.event.ActionEvent evt) {                                              
	        // TODO add your handling code here:
	    }                                             

	    private void DATA_START_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {                                                  
	        // TODO add your handling code here:
	    	location = Integer.parseInt(DATA_LOC.getSelectedItem().toString());
			foodusing = DATA_FOOD.getSelectedItem().toString();
			rangepotting = DATA_RANGEPOT.isSelected();
			prayFlick = DATA_RANGEPOT1.isSelected();
			GUI_COMPLETE = true;
	    }                                                 

	    private void DATA_LOCActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    }                                        

	    private void DATA_FOODActionPerformed(java.awt.event.ActionEvent evt) {                                          
	        // TODO add your handling code here:
	    }                                         

	    private void DATA_RANGEPOT1ActionPerformed(java.awt.event.ActionEvent evt) {                                               
	        // TODO add your handling code here:
	    }                                       
	

		// Variables declaration - do not modify
		@SuppressWarnings("rawtypes")
		private javax.swing.JComboBox DATA_FOOD;
		@SuppressWarnings("rawtypes")
		private javax.swing.JComboBox DATA_LOC;
		private javax.swing.JRadioButton DATA_RANGEPOT;
		private javax.swing.JRadioButton DATA_RANGEPOT1;
		@SuppressWarnings("rawtypes")
		private javax.swing.JButton DATA_START_BUTTON;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JLabel jLabel5;
		private javax.swing.JLabel jLabel6;
		private javax.swing.JLabel jLabel7;
		private javax.swing.JLabel jLabel8;
		// End of variables declaration
	}

			
}