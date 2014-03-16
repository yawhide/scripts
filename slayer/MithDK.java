package scripts.slayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Projection;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.ext.Doors;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.util.Util;

@ScriptManifest(authors = { "Yaw hide" }, category = "Combat", name = "Yaw hide's MithDK", version = 1.0)
public class MithDK extends Script implements MessageListening07, Painting, Ending{

	/**** RSTiles ****/
	RSTile afterStepsDown = new RSTile(1772, 5366, 0);
	RSTile trapDoorToChest = new RSTile(2309, 3215, 0);
	RSTile[] churchBoundaries = { new RSTile(3238, 3211, 0), new RSTile(3240, 3211, 0), new RSTile(3240, 3215, 0),
			new RSTile(3247, 3215, 0), new RSTile(3247, 3204, 0), new RSTile(3240, 3204), new RSTile(3240, 3209, 0),
			new RSTile(3238, 3209, 0) };
	RSTile[] toMith = { new RSTile(1775, 5361, 0), new RSTile(1777, 5355, 0), new RSTile(1778, 5346, 0) };
	
	/**** Paths ****/
	RSTile[] toChest = { new RSTile(3213, 9620, 0), new RSTile(3218, 9623, 0) };
	RSTile[] toAltar = { new RSTile(3241, 3210, 0), new RSTile(3242, 3207, 0) };
	RSTile[] toWhirlpool = { new RSTile(2518, 3563, 0), new RSTile(2518, 3555, 0), 
			new RSTile(2517, 3546, 0), new RSTile(2516, 3537, 0), new RSTile(2514, 3530, 0), 
			new RSTile(2514, 3524, 0), new RSTile(2511, 3518, 0), new RSTile(2511, 3512, 0) };
	
	/**** areas because RSArea doesn't work with plane ****/
	RSTile[] afterWhirlpool2 = {new RSTile(1763, 5367, 1), new RSTile(1768, 5365, 1)};
	RSTile[] greenDragArea2 = {new RSTile(1767, 5367, 0), new RSTile(1782, 5341, 0)};
	RSTile[] mithDragSpawn12 = {new RSTile(1776, 5348, 1), new RSTile(1784, 5338, 1)};
	RSTile[] lumbyArea2 = {new RSTile(3190, 3240, 0), new RSTile(3255, 3187, 0)};
	RSTile[] toWhirlpoolA2 = {new RSTile(2505, 3575, 0), new RSTile(2526, 3510, 0)};
	
	
	/**** Positionables ****/
	Positionable stepsDownToGD = new RSTile(1769, 5365, 1);
	Positionable stepsUpToMD = new RSTile(1778, 5344, 0);
	Positionable trapDoor = new RSTile(3209, 3216, 0);
	Positionable chest = new RSTile(3219, 9623, 0);
	Positionable altar = new RSTile(3243, 3207, 0);
	Positionable churchDoor = new RSTile(3238, 3210, 0);
	Positionable whirlpool = new RSTile(2512, 3511);
	Positionable whirlpoolT = new RSTile(2512, 3508);
	Positionable safeSpotSpawn1P = new RSTile(1777, 5345, 1);
	Positionable afterWhirlpoolT = new RSTile(1763, 5365, 1);
	
	RSTile[] safeSpot = {new RSTile(1776, 5346, 1), new RSTile(1777, 5344, 1)};
	/**** RSArea ****/
	RSArea afterWhirlpool = new RSArea(new RSTile(1762, 5368, 1), new RSTile(1769, 5364, 1));
	RSArea greenDragArea = new RSArea(new RSTile(1767, 5367, 0), new RSTile(1782, 5341, 0));
	RSArea mithDragSpawn1 = new RSArea(new RSTile(1776, 5348, 1), new RSTile(1784, 5338, 1));
	RSArea safeSpotSpawn1 = new RSArea(safeSpot);
	RSArea lumbyArea = new RSArea(new RSTile(3190, 3240, 0), new RSTile(3255, 3187, 0));
	RSArea churchA = new RSArea(churchBoundaries);
	RSArea toWhirlpoolA = new RSArea(new RSTile(2505, 3575, 0), new RSTile(2526, 3510, 0));
	
	
	
	RSArea chestA = new RSArea(new RSTile(3219, 9624, 0), new RSTile(3208, 9615, 0));
	
	/**** Item Ids ****/
	int[] foodIDs = { 385, 7946 };
	int[] rangePot = { 2444, 169, 171, 173 };
	int[] defencePot = { 2442, 163, 165, 167 };
	int[] prayerPot = { 2434, 139, 141, 143 };
	int[] antifirePot = {2452, 2454, 2456, 2458 };
	int diamondEBolt = 9243;
	int rubyEBolt = 9242;
	int addyBolt = 9143;
	boolean useSpecialBolts = true;
	int[] gamesNecklace = { 3867, 3855, 3857, 3859, 3861, 3863, 3865, 3853 };
	int ringOfLife = 2570;
	int antiDragonFireShield = 1540;
	int[] junk = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767, 117,
			6963, 554, 556, 829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180,
			6965, 1969, 6183, 6181, 6962, 865, 41 };
	int varTab = 8007;
	int lumTab = 8008;
	//int[] depositAllExcept = { 3867, 3855, 3857, 3859, 3861, 3863, 3865, 3853, varTab, lumTab };
	int mbar = 2359;
	int dbone = 536;
	
	/**** Monster Ids ****/
	String bgd = "Brutal green dragon"; // 2399
	String md = "Mithril dragon"; //2400
	String wf = "Waterfiend";
	int[] mithDrag = {};
	
	/**** looting ****/
	int[] loot = {      
			11335, 11286, diamondEBolt, rubyEBolt, addyBolt, 11338, 1615, 1631, 1149, 
			1249, 2366,  
			1319, 1201, 985, 987, 1373, 1163,  
			1147, 566, 565, 561,  1601, 1617, 2363,			
			1432, 1247, 868,  9144
			};
	int[] badLoot = {443, 1462,
			892, 811, 817, };
	
	int[] conditionalLoot = { 385, 11497, 11499, 11465, 
			11467, 11465, 11467 };
	
	int[] clue = {2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049};
	
	String[] names = {     
"Dragon full helm", "Draconic visage", "Diamond bolts (e)", "Ruby bolts (e)", "Adamant bolts", "Chewed bones", "Dragon med helm", "Dragon spear", "Shield left half",  
			"Rune 2h sword", "Rune kiteshield", 
			"Dragonstone", "Uncut dragonstone", "Half of a key", "Half of a key", "Rune battleaxe", "Rune full helm", 
			"Rune med helm", "Soul rune", "Blood rune", "Nature rune",  "Diamond", "Uncut diamond", "Runite bar", 
			"Rune mace", "Rune spear", "Rune knife",  "Runite bolts"};
	String[] badNames = {		  
			 "Silver ore", "Nature talisman", 
			 "Rune arrow", "Rune dart", "Rune dart(p)" };
	
	String[] conditionNames = { "Shark", "Super defence mix(2)", "Super defence mix(1)", "Prayer mix(2)",
			"Prayer mix(1)", "Restore prayer mix(2)", "Restore prayer mix(1)" };
	
	
	String[] clueStr = {"Clue scroll"};
	String[] priorityLoot = { "Dragon full helm", "Draconic visage" };
	
	HashMap<Integer, String> map = new HashMap<Integer, String>(loot.length);
	HashMap<Integer, String> mapBad = new HashMap<Integer, String>(badLoot.length);
	HashMap<Integer, String> mapConditional = new HashMap<Integer, String>(conditionalLoot.length);
	HashMap<Integer, String> mapClue = new HashMap<Integer, String>(clue.length);
	
	public void putMap() {
		for (int i = 0; i < loot.length; i++) {
			map.put(loot[i], names[i]);
		}
		for (int j = 0; j < conditionalLoot.length; j++) {
			mapConditional.put(conditionalLoot[j], conditionNames[j]);
		}
		for (int k = 0; k < clue.length; k++) {
			mapClue.put(clue[k], clueStr[0]);
		}
		for (int l = 0; l < badLoot.length; l++) {
			mapBad.put(badLoot[l], badNames[l]);
		}
	}
	
	
	boolean pottedAntiFire = true;
	boolean bankStatus = true;
	boolean scriptStatus = true;
	long antiFireT;
	
	@Override
	public void run() {
		// TODO run
		putMap();
		sleep(100,150);
		
		Mouse.setSpeed(General.random(120,140));
		Walking.setWalkingTimeout(3000L);
		Walking.setControlClick(true);
		WebWalking.setUseRun(true);
		
		useSpecialBolts = false;
		
		if((!lumbyArea.contains(pos()) || !chestA.contains(pos())) && haveGear()){
			bankStatus = false;
			println("We are in barbarian outpost (i hope) and off to mithril drags");
		}
		
		if(Equipment.find(SLOTS.WEAPON).length == 0)
			scriptStatus = false;
			//stopScript();
		
		while(scriptStatus){
			
			if(lumbyArea.contains(pos()) && !haveGear())
				bankStatus = true;
			
			if(bankStatus){
				if(Camera.getCameraAngle() < 90)
					Camera.setCameraAngle(General.random(90,  100));
				if(Skills.getCurrentLevel(SKILLS.PRAYER) < Skills.getActualLevel(SKILLS.PRAYER)){
					if(!PrayerBook.getActive().isEmpty()){
						turnOffAllPrayers();
					}
					if (pos().distanceTo(altar) <= 3){
						println("near altar");
						prayAtAltar();
					}
					else if (churchA.contains(pos())){
						println("in church");
						gotoAltar();
					}
					else if (pos().distanceTo(churchDoor) <=4){
						println("opening the door to the church");
						if(openDoor(churchDoor))
							gotoAltar();
					}
					else{
						WebWalking.walkTo(churchDoor);
						waitIsMovin();
					}
					
				}
				else {
					
					if(haveGear()){
						bankStatus = false;
						println("Off to mithril drags");
						useGameNeck();
					}
					else if(Banking.isPinScreenOpen() || Banking.isBankScreenOpen()){
						
						bank();
					}
					else if(pos().distanceTo(chest) <= 4)
						openChest();
					else if (chestA.contains(pos()))
						gotoChest();
					else if (pos().distanceTo(trapDoor) <=4)
						goDownTrapDoorLumby();
					else{
						WebWalking.walkTo(trapDoor);
						waitIsMovin();
					}
				}
			}
			//TODO AcientTavern
			else{
				if(getHp() < 50)
					eatFood();
				
				checkRare();
				if (gotRareDrop())
					emergTele();
				if(!haveGear())
					emergTele();
				
				
				if(pos().distanceTo(whirlpool) <= 4 && pos().getPlane() == 0){
					useWhirpool();
				}
				else if (inArea2(toWhirlpoolA2) && pos().getPlane() == 0){//toWhirlpoolA.contains(pos())){
					gotoWhirlpool();
				}
				
				else if (inArea2(afterWhirlpool2)){//afterWhirlpool.contains(pos()) 
						//|| (pos().distanceTo(stepsDownToGD) <=2 && pos().getPlane() == 1)){
					checkToPot();
					checkToPotAntiFire();
					stepDownToGD();
				}
				else if (pos().distanceTo(stepsUpToMD) <= 4 && pos().getPlane() == 0){
					//println("close to the steps up to miths");
					stepUpToMD();
				}
				else if (inArea2(greenDragArea2)){ //greenDragArea.contains(pos()) && pos().getPlane() == 0){
					//println("in green dragon area");
					checkToPotAntiFire();
					activateProtectMage();
					gotoMith();
				}
				else if (inArea2(mithDragSpawn12)){//mithDragSpawn1.contains(pos()) && pos().getPlane() == 1){
					fight();
				}
				else if (haveGear()){
					bankStatus = false;
					//println("Off to mithril drags");
					useGameNeck();
				}
				else{
					//println("we are lost");
					//emergTele();
				}
			}
			
			sleep(50,70);
		}
		
	}

	String preMsg = "";
	String prePreMsg = "";
	@Override
	public void serverMessageReceived(String arg0) {
		// TODO serverMessageReceived
		
		if((!arg0.equals("Your potion protects you from the heat of the dragon's breath!") 
				|| arg0.equals("Your dragonfire shield is already fully charged.")) 
				&& //Your shield absorbs most of the dragon fire!") && 
				preMsg.equals("Your shield absorbs most of the dragon fire!")){
			pottedAntiFire = false;
		}
		//println("PREMSG " + preMsg);
		//println("CURR MSG " + arg0);
		preMsg = arg0;
		
	}

	final String[] type = {"Defence", "Ranged", "Prayer", "Hitpoints" };
	final SKILLS[] Names = {SKILLS.DEFENCE, SKILLS.RANGED, 
			SKILLS.PRAYER, SKILLS.HITPOINTS, };
	final int[] XP = { Skills.getXP(SKILLS.DEFENCE), Skills.getXP(SKILLS.RANGED), 
    		Skills.getXP(SKILLS.PRAYER), Skills.getXP(SKILLS.HITPOINTS) };
	int[] startLvs = { 	Skills.getActualLevel(SKILLS.DEFENCE), Skills.getActualLevel(SKILLS.RANGED), 
			Skills.getActualLevel(SKILLS.PRAYER), Skills.getActualLevel(SKILLS.HITPOINTS) };
	String lastDrop;
	
	public void lastDrops(Graphics2D graph){
		RSGroundItem[] g = GroundItems.findNearest(2359);
		RSTile drop = null;
		//ArrayList<String> drops = new ArrayList<String>();
		
		if(g.length > 0){
			drop = g[0].getPosition();
		}
		int y = 45;
		for(RSGroundItem gr : GroundItems.getAt(drop)){
			int i = gr.getID();
			if(i != 536 && i != 2359 && 
					i != rubyEBolt && i != diamondEBolt
					&& i != addyBolt){
				graph.drawString(gr.getDefinition().getName(), 5, y);
				y+= 15;
			}
				//drops.add(gr.getDefinition().getName());
				
		}
		
	}
	
	@Override
	public void onPaint(Graphics arg0) {
		// TODO onPaint
		Graphics2D g = (Graphics2D)arg0;
		RSNPC[] drag = NPCs.findNearest("Mithril dragon");
		if (drag.length > 0) {
			drawTile(drag[0].getAnimablePosition(), g, false);
			g.drawString("Anim: " + drag[0].getAnimation(), 5, 185);
			g.drawString("HP: " + drag[0].getHealth(), 5, 200);
		}
		if(useSpecialBolts){
			g.drawString("Bolts: "+ (Equipment.getItem(SLOTS.ARROW).getID() == rubyEBolt ? 
				"Ruby Bolts e" : "Diamond Bolts e"), 5, 215);
		}
		
		g.drawString("Last drop: " + lastDrop, 5,  260);
		long time = (360000 - System.currentTimeMillis() - antiFireT) / 60000;
		g.drawString("AntiFire Status: " + (pottedAntiFire ? "Potted" : "Unpotted") + ", time to pot: "
				+ (time > 0 ? time : null), 5, 275);
		
		//g.drawString("Drops from last mith dragon", 5, 30);
		//lastDrops(g);
		
		long timeRan = getRunningTime();
		double secondsRan = (int) (timeRan/1000);
		double hoursRan = secondsRan/3600;
		
		
		
		g.drawString("Gear status: " + (haveGear() ? true : false), 5, 230);
		g.drawString("Time running: " + Timing.msToString(timeRan) , 5, 245);
		
		int x = 5, y = 337;
		for(int i = 0; i < 4; i++){
			g.drawString(type[i] + ": " + Skills.getCurrentLevel(Names[i]) 
					+ "/" + Skills.getActualLevel(Names[i]),  x,  y-(i*15));
		}
		
		
        
		
		int x1 = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(Names[i])) {
				g.setColor(new Color(0, 0, 0));
				g.fillRect(2, 341 + x1, 515, 12);
				
				g.setColor(new Color(0, 255, 0, 255));
				g.drawString(Names[i]
						+ ": "
						+ NumberFormat.getNumberInstance(Locale.US).format(Math.round(((Skills
											.getXP(Names[i]) - XP[i]))/ hoursRan)) 
						+ " Xp/h", 5,(352 + x1));
			int x2 = 150, y1 = 342 + x1;
			int CUR_LVL = Skills.getActualLevel(Names[i]);
			int NXT_LVL = (CUR_LVL + 1);
			int Percentage = Skills.getPercentToLevel(Names[i], NXT_LVL);
			
			g.drawString("Cur lv: " + CUR_LVL + "   Lvs gained: " + (CUR_LVL - startLvs[i]), 257, y1+10);
			
			g.setColor(new Color(255, 0, 0, 255));
			g.fillRect(x2, y1, 100, 10);
			g.setColor(new Color(0, 255, 0, 255));
			g.fillRect(x2, y1, Percentage, 10);
			
			g.setColor(new Color(0, 0, 0));
			g.drawString(Percentage + "% to " + NXT_LVL, x2+25, y1 + 9);
			

			
			x1 += 15;
			}
		}
		
	}

	  
	
	@Override
	public void onEnd() {
		// TODO onEnd()
		
	}

	public void stepDownToGD(){
		if(Camera.getCameraAngle() < 90){
			Camera.setCameraAngle(General.random(90, 100));
			sleep(200,300);
		}
		RSObject[] STAIRS = Objects.getAt(stepsDownToGD);
		if (STAIRS.length > 0){
			if(STAIRS[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(STAIRS[0],  "Climb-down")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return !afterWhirlpool.contains(pos());
						}
					}, General.random(2000, 2500));
				}
					
			}
			else{
				Camera.turnToTile(stepsDownToGD);
				sleep(200,300);
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90, 100));
					sleep(200,300);
				}
			}
		}
	}
	
	public void stepUpToMD(){
		RSObject[] STAIRS = Objects.getAt(stepsUpToMD);
		if (STAIRS.length > 0){
			if(STAIRS[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(STAIRS[0],  "Climb-up")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return !greenDragArea.contains(pos());
						}
					}, General.random(2000, 2500));
				}
					
			}
			else{
				Camera.turnToTile(stepsDownToGD);
				sleep(200,300);
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90, 100));
					sleep(200,300);
				}
			}
		}
	}
	
	public void goDownTrapDoorLumby(){
		RSObject[] TRAP_DOOR = Objects.getAt(trapDoor);
		if(TRAP_DOOR.length > 0){
			if(TRAP_DOOR[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(TRAP_DOOR[0],  "Climb-down")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return Player.getAnimation() == 827;
						}
					}, General.random(4000, 4500));
					sleep(1000,1200);
				}
			}
		}
	}
	
	public void prayAtAltar(){
		RSObject[] ALTAR = Objects.getAt(altar);
		if(ALTAR.length > 0){
			if(ALTAR[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(ALTAR[0],  "Pray-at")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return Player.getAnimation() == 645;
						}
					}, General.random(3000, 3500));
					sleep(1000,1200);
				}
			}
		}
	}
	
	public void gotoChest(){
		Walking.walkPath(toChest);
			waitIsMovin();
	}
	
	public void gotoAltar(){
		Walking.walkPath(toAltar);
			waitIsMovin();
	}
	
	public void gotoWhirlpool(){
		Walking.walkPath(toWhirlpool);
			waitIsMovin();
	}
	
	public void gotoMith(){
		
		Walking.walkPath(toMith);
			waitIsMovin();
	}
	
	public void useWhirpool(){
		//RSObject[] WHIRLPOOL = Objects.getAt(whirlpoolT);//.findNearest(10, "Whirlpool");
		Point p = Projection.tileToScreen(whirlpoolT, 0);
		
		Camera.turnToTile(whirlpoolT);
		sleep(200,300);
		Camera.setCameraAngle(Camera.getTileAngle(whirlpoolT));
		
		Mouse.move(p);
		sleep(100,150);
		if(Game.isUptext("Dive in Whirlpool")){
			Mouse.click(1);
			sleep(100,150);
			Timing.waitCondition(new Condition() {
				;
				@Override
				public boolean active() {
					return !afterWhirlpool.contains(pos());
				}
			}, General.random(5000, 6000));
			sleep(5000,6200);
		}
		/*for(int i = 0; i < 50; i++){
			if(Player.getAnimation() == 6723){
				
			}
			sleep(200);
		}*/
		
		
		/*
		if(WHIRLPOOL.length > 0){
			if(WHIRLPOOL[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(WHIRLPOOL[0],  "Dive in")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return !afterWhirlpool.contains(pos());
						}
					}, General.random(16000, 17000));
					sleep(5000,6200);
				}
			}
			else{
				Camera.turnToTile(whirlpoolT);
				sleep(200,300);
				Camera.setCameraAngle(Camera.getTileAngle(whirlpoolT));
			}
		}*/
	}
	
	public void openChest(){
		
		RSObject[] CHEST = Objects.getAt(chest);
		if(CHEST.length > 0){
			if(CHEST[0].isOnScreen()){
				if(CHEST[0].click("Bank")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return Banking.isPinScreenOpen() || Banking.isBankScreenOpen();
						}
					}, General.random(4000, 4500));
				}	
			}
		}
	}
	
	//TODO bank
	public void bank(){
		
		if(Banking.isPinScreenOpen()){
			sleep(500,700);
			if(Banking.inPin())
			//Banking.inPin();
				sleep(200,300);
			else{
				println("you dont have a pin setup");
				scriptStatus = false;
			}
			
		}
		else if(Banking.isBankScreenOpen()){
			
			//Banking.depositAllExcept(depositAllExcept);
			Banking.depositAll();
			sleep(200,300);
			//Mouse.clickBox(471, 78, 478, 84, 1);
			//sleep(200,300);
			if(Banking.find(rangePot).length == 0 ||
					Banking.find(defencePot).length == 0 ||
					Banking.find(antifirePot).length == 0 ||
					Banking.find(prayerPot).length == 0 ||
					Banking.find(gamesNecklace).length == 0 ||
					Banking.find(addyBolt).length == 0 ||
							Banking.find(lumTab).length == 0 ||
					Banking.find(foodIDs).length == 0){
				println("ran out of supplies");
				scriptStatus = false;
			}
			
			Banking.withdraw(1, rangePot);
				sleep(600,700);
			
			Banking.withdraw(1,  defencePot);
			sleep(200,300);
			if (Banking.find(antifirePot).length == 0)
				scriptStatus = false;
			Banking.withdraw(1, antifirePot);
			sleep(200,300);
			Banking.withdraw(4,  prayerPot);
			sleep(200,300);
			if(Inventory.find(lumTab).length == 0){
				Banking.withdraw(5, lumTab);
				sleep(200,300);
			}
			if(Inventory.find(gamesNecklace).length == 0){
				Banking.withdraw(1,  gamesNecklace);
				sleep(200,300);
			}
			
			if (useSpecialBolts) {
				int count = Equipment.getCount(rubyEBolt);
				int count2 = Inventory.getCount(rubyEBolt);
				if (count + count2 < 100) {
					Banking.withdraw(100 - count - count2, rubyEBolt);
					sleep(200, 300);
				}
				int count3 = Equipment.getCount(diamondEBolt)
						+ Inventory.getCount(diamondEBolt);
				if (count3 < 100) {
					Banking.withdraw(100 - count3, diamondEBolt);
					sleep(200, 300);
				}
			}
			else{
				int count = Equipment.getCount(addyBolt);
				if(count < 200){
					Banking.withdraw(200-count, addyBolt);
					sleep(200,300);
				}
			}
			
			if(!Equipment.isEquipped(ringOfLife)){
				Banking.withdraw(1,  ringOfLife);
				sleep(200,300);
			}
			
			if (getHp() < 90) {

				println("eating to full HP");
				Banking.withdraw((((Skills
						.getActualLevel(SKILLS.HITPOINTS) - Skills
						.getCurrentLevel(SKILLS.HITPOINTS)) / 20) + 1), foodIDs);
				sleep(200, 250);
			}
			
			while(Banking.isBankScreenOpen()){
				Banking.close();
				sleep(1200,1300);
			}
			
			if(Inventory.find(ringOfLife).length > 0){
				Inventory.find(ringOfLife)[0].click("Wear");
				sleep(200,300);
			}
			
			if (useSpecialBolts) {
				if (Inventory.find(rubyEBolt).length > 0) {
					equipRubyBolts();
					sleep(200, 300);
				}
			}
			else{
				if (Inventory.find(addyBolt).length > 0) {
					equipAddyBolts();
					sleep(200, 300);
				}
			}
			
			do {
				RSItem[] food = Inventory.find(foodIDs);
				if (food.length > 0) {
					if (food[0].click("Eat"))
						sleep(200, 250);
				}
			} while (getHp() < 90);
			
			openChest();
			
			sleep(200,300);
			if(Banking.isBankScreenOpen()){
				Banking.withdraw(20,  foodIDs);
				sleep(200,300);
				
				Banking.close();
				
			}
			println("Done banking");
		}
	}
	
	//TODO fight
	public void fight(){
		RSNPC[] drag = NPCs.findNearest("Mithril dragon");
		RSNPC mith = null;
		if(drag.length > 0){
			if(mithDragSpawn1.contains(drag[0].getAnimablePosition()))
				mith = drag[0];
		}
		
		timeToTab(mith);
		
		//check for potting
		checkToPotAntiFire();
		checkToPot();
		
		// turning prayers on
		if(mith == null){
			turnOffAllPrayers();
		}
		else{
			if(Skills.getCurrentLevel(SKILLS.PRAYER) < 10){
				reactivatePrayer(1);
			}
			else{
				if(mith.isValid() && mith.isInteractingWithMe())
					activateProtectRanged();
				else if (mith.getAnimation() == 91)
					turnOffAllPrayers();
			}
		}
		
		//looting and checking bolts
		if(mith == null){
			if(isLoot())
				lootOrder();
			else if (!isLoot()){
				if(Inventory.find(foodIDs).length == 0 && getHp() < 75){
					emergTele();
				}
				else
					gotoSafeSpot1();
			}
		}
		if(useSpecialBolts){
			if(mithAtHalf(mith)){
				equipDiamondBolts();
		}
			else{
				equipRubyBolts();
			}
		}
		
		//actually attacking the dragon and prayer flick
		if(mith != null && !inArea2(safeSpot)){//safeSpotSpawn1.contains(pos())){
			println("not in safe spot, lets go!");
			gotoSafeSpot1();
		}
		else if(mith != null && mith.isValid() && mith.isInteractingWithMe() && mith.getAnimation() != 92){
			if (Player.getRSPlayer().getInteractingCharacter() == null) {
				//println("I am not interacting with anyone");
				//println("Not interacting with dragon");
				activateProtectRanged();
				moveCamera(mith);
				//if(
						clickNPC(mith, "Attack");
				//		){
				//}
				sleep(1000,1100);
			}
			else{
				if(mith != null && getHp() > 50 && pottedAntiFire)
					prayerflick(mith);
				else
					General.sleep(1000, 1200);
			}
			
		}
		else if(mith != null && mith.isValid() && !mith.isInteractingWithMe() 
				&& mith.getAnimablePosition().distanceTo(safeSpotSpawn1P) > 2 && mith.getAnimation() != 92){
			
			
				activateProtectRanged();
				moveCamera(mith);
				//if(clickNPC(mith, "Attack"))
				clickNPC(mith, "Attack");
					sleep(1000,1100);
		}
		else{/*
			if(getHp() > 50 && pottedAntiFire)
				prayerflick(mith);
			else*/
				General.sleep(1000, 1200);
		}
			
	}
	
	public void prayerflick(RSNPC drag) {

		if (GameTab.getOpen() != GameTab.TABS.PRAYERS) {
			GameTab.open(org.tribot.api2007.GameTab.TABS.PRAYERS);
			sleep(80, 100);
		}
		while (drag != null && drag.isInteractingWithMe() && getHp() > 50 &&
				Skills.getCurrentLevel(SKILLS.PRAYER) > 10 && pottedAntiFire
				&& Player.getRSPlayer().getInteractingCharacter() != null) {
			
			if(mithAtHalf(drag)){
				equipDiamondBolts();
			}
			
			Timer t = new Timer(1100L);
			do {
				if (!PrayerBook.isOpen()) {
					PrayerBook.open();
					sleep(50, 100);
				}
				if (Player.getAnimation() != 4230){
					sleep(700,750);
					PrayerBook.activate(Prayer.EAGLE_EYE);
				}
				else{
					if (Prayer.EAGLE_EYE.isActive()){
						sleep(50,150);
						Prayer.EAGLE_EYE.click();
						sleep(450,510);
					}
				}
				
			} while(t.getRemaining() > 0);
		}
		
		if (!PrayerBook.isOpen()) {
			PrayerBook.open();
			sleep(80, 100);
		}
		if (PrayerBook.activate(Prayer.EAGLE_EYE, PrayerState.DEACTIVATED));
			sleep(80, 100);
		
		
	}
	
	public boolean checkRare() {
		RSGroundItem[] priority = GroundItems.findNearest(priorityLoot);
		if (priority.length > 0) {
			if (!priority[0].isOnScreen()) {
				Walking.clickTileMM(priority[0].getPosition(), 1);
				sleep(100, 150);
				Camera.turnToTile(priority[0].getPosition());
				sleep(200, 300);
				waitIsMovin();
			}
			String str = map.get(priority[0].getID());
			if(priority[0].click("Take " + str)){
			waitForInv(priority[0].getID());
			return true;
			}
		}
		return false;
	}
	
	public boolean checkLootInArea(RSGroundItem g){
		if(mithDragSpawn1.contains(g.getPosition()))
			return true;
		return false;
	}
	
	public boolean isLoot(){
		RSGroundItem[] p1 = GroundItems.findNearest(loot);
		RSGroundItem[] p2 = GroundItems.findNearest(badLoot);
		RSGroundItem[] p3 = GroundItems.findNearest(clue);
		RSGroundItem[] dbone = GroundItems.findNearest(536);
		RSGroundItem[] mbar = GroundItems.findNearest(2359);
		/*if(p1.length == 0 && p2.length == 0 && p3.length == 0
				&& dbone.length == 0 && mbar.length == 0)
			return false;
		else{ 
			if (p1.length > 0 || p2.length > 0 || p3.length > 0)
				return true;
			else if (!Inventory.isFull()){
				return dbone.length > 0 && mbar.length > 0;
			}
			else{
				return false;
			}
		}
		*/
		return !(p1.length == 0 && p2.length == 0 && p3.length == 0
				&& dbone.length == 0 && mbar.length == 0);
	}
	
	public boolean makeRoomForDrop(){
		RSItem[] food = Inventory.find(foodIDs);
		RSItem[] dbone = Inventory.find("Dragon bones");		
		
		if((Combat.getMaxHP() - Combat.getHP()) > 15) {
			if(food.length > 0){
				if(food[0].click("Eat")){
					sleep(General.random(800, 1000));
					return true;
				}
			}
		}
		else if (Inventory.isFull() && dbone.length > 0){
			if(dbone[0].click("Bury")){
				sleep(General.random(800, 1000));
				return true;
			}
		}
		return false;
	}
	
	
	
	public boolean gotRareDrop(){
		RSItem[] RARE = Inventory.find(priorityLoot);
		if(RARE.length > 0){
			emergTele();
			sleep(3000,4000);
			println("You just got a visage or a dragon full helm YOU LUCKY BITCH!!!!!!!!");
			println("Please take a screenshot and post it on my thread! ty");
			println("Stopping the script so you can get a nice surprice when you login");
			
			scriptStatus = false;
			return true;
		}
		return false;
	}
	//TODO lootOrder
	public void lootOrder(){
		RSGroundItem[] Nests = GroundItems.findNearest(loot);
		RSGroundItem[] Nests2 = GroundItems.findNearest(clue);
		RSGroundItem[] Nests3 = GroundItems.findNearest(badLoot);
		RSGroundItem[] dbone = GroundItems.findNearest(536);
		RSGroundItem[] mbar = GroundItems.findNearest(2359);
		
		
		Mouse.setSpeed(General.random(200, 230));
		Inventory.drop(junk);
		sleep(100,150);
		Mouse.setSpeed(General.random(120,140));
		
		lootBolts();
		
		makeRoomForDrop();
				
		if(!Inventory.isFull()){
			if (Nests.length > 0) {
				String str = map.get(Nests[0].getID());
				if (Nests[0].click("Take " + str)){
					waitForInv(Nests[0].getID());
					if(!str.equals("Adamant bolts"))
						lastDrop = str;
					/*try {
						writeToFile(Nests[0].getID());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			} 
			else if (Nests2.length > 0) {
				String str = mapClue.get(Nests2[0].getID());
				if (Nests2[0].click("Take " + str)){
					waitForInv(Nests2[0].getID());
					lastDrop = str;
					/*try {
						writeToFile(Nests2[0].getID());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			} 
			else if (Nests3.length > 0) {
				String str = mapBad.get(Nests3[0].getID());
				if (Nests3[0].click("Take " + str)){
					waitForInv(Nests3[0].getID());
					lastDrop = str;
					/*try {
						writeToFile(Nests3[0].getID());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			}
			else if (dbone.length > 0){
				if (dbone[0].click("Take Dragon bones"))
					waitForInv(536);
				}
			else if (mbar.length > 0){
				if (mbar[0].click("Take Mithril bar"))
					waitForInv(2359);
			}
		}
		
	}
	
	//TODO lootBolts
	public void lootBolts(){
		RSItem[] ruby = Inventory.find(rubyEBolt);
		RSGroundItem[] rubyG = GroundItems.findNearest(rubyEBolt);
		RSGroundItem[] diaG = GroundItems.findNearest(diamondEBolt);
		
		RSItem[] addy = Inventory.find(addyBolt);
		RSGroundItem[] addyG = GroundItems.findNearest(addyBolt);
		
		if (useSpecialBolts) {
			if (rubyG.length > 0) {
				if (!rubyG[0].isOnScreen()) {
					Walking.blindWalkTo(rubyG[0].getPosition());
					waitIsMovin();
				}
				if (rubyG[0].click("Take Ruby bolts (e)")) {
					waitForInv(rubyEBolt);
					sleep(100, 120);
					ruby = Inventory.find(rubyEBolt);
				}
			}
			ruby = Inventory.find(rubyEBolt);
			if (rubyG.length == 0 && ruby.length > 0) {
				if (ruby[0].click("Wield"))
					sleep(100, 150);
			}
			if (diaG.length > 0) {
				if (diaG[0].click("Take Diamond bolts (e)")) {
					waitForInv(diamondEBolt);
					sleep(100, 120);
				}
			}
		}
		else{
			if(addyG.length > 0){
				if (!addyG[0].isOnScreen()) {
					Walking.blindWalkTo(addyG[0].getPosition());
					waitIsMovin();
				}
				if (addyG[0].click("Take Adamant bolts")) {
					waitForInv(addyBolt);
					sleep(100, 120);
					ruby = Inventory.find(addyBolt);
				}
			}
			ruby = Inventory.find(addyBolt);
			if(addy.length > 0) {
				if (addy[0].click("Wield"))
					sleep(100, 150);
			}
		}
}
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *  Utility Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public RSTile pos(){
		return Player.getPosition();
	}
	
	public int getHp(){
		
		return Combat.getHPRatio();
	}
	
	public void waitIsMovin(){
		
		for(int i = 0; i < 57; i++, sleep(30, 40)){
			if (!Player.isMoving())
				break;
		}
	}
	
	public boolean openDoor(Positionable doorTile){
		RSObject door = Doors.getDoorAt(doorTile);
		if(door != null){
			if(door.isOnScreen()){
				if(Doors.handleDoor(door, true)){
					waitIsMovin();
					sleep(1000,1200);
					return true;
				}
			}
			else{
				Walking.walkTo(doorTile);
				Camera.turnToTile(doorTile);
				waitIsMovin();
				sleep(200,300);
				return false;
			}
		}
		return true;
	}
	
	public void turnOffAllPrayers(){
		while (!PrayerBook.getActive().isEmpty()){
			if(GameTab.getOpen() != GameTab.TABS.PRAYERS){
				GameTab.open(TABS.PRAYERS);
				sleep(200,300);
			}
			PrayerBook.getActive().get(0).click();
			sleep(200,300);
		}
	}
	
	public void useGameNeck(){
		gotoTab(GameTab.TABS.INVENTORY);
		sleep(200,300);
		
		RSItem[] gamesNeck = Inventory.find(gamesNecklace);
		if (gamesNeck.length > 0){
			
			if (gamesNeck[0].click("Rub")){
				sleep(3500,4750);
				RSInterface gamebox = Interfaces.get(230, 2);
				if (gamebox != null){
					gamebox.click("Continue");
					sleep(5200,5250);
				}
			}
		}
	}
	
	public boolean mithAtHalf(RSNPC drag){
		if(drag != null && drag.isInteractingWithMe())
			return drag.getHealth() <= 127 && drag.getHealth() > 0;
		return false;
	}
	
	public boolean equipDiamondBolts(){
		RSItem[] bolt = Inventory.find(diamondEBolt);
		
		if(bolt.length > 0){
			gotoTab(GameTab.TABS.INVENTORY);
			if(bolt[0].click("Wield")){
				sleep(100,150);
				return true;
			}
		}
		return false;
	}
	
	public boolean equipRubyBolts(){
		RSItem[] bolt = Inventory.find(rubyEBolt);
		
		if(bolt.length > 0){
			gotoTab(GameTab.TABS.INVENTORY);
			if(bolt[0].click("Wield")){
				sleep(100,150);
				return true;
			}
		}
		return false;
	}
	
	public boolean equipAddyBolts(){
		RSItem[] bolt = Inventory.find(addyBolt);
		
		if(bolt.length > 0){
			gotoTab(GameTab.TABS.INVENTORY);
			if(bolt[0].click("Wield")){
				sleep(100,150);
				return true;
			}
		}
		return false;
	}
	
	public boolean drinkAntiFire(){
		RSItem[] pot = Inventory.find(antifirePot);
		gotoTab(TABS.INVENTORY);
		if(pot.length > 0){
			if(pot[0].click("Drink")){
				sleep(1200,1250);
				pottedAntiFire = true;
				antiFireT = System.currentTimeMillis();
				return true;
			}
		}
		else
			emergTele();
		return false;
	}
	
	public void checkToPotAntiFire(){
		if(!pottedAntiFire && System.currentTimeMillis() - antiFireT > 360000){
			println("potting antifire");
			drinkAntiFire();
		}
	}
	
	public void checkToPot(){
		RSItem[] pPot = Inventory.find(prayerPot);
		RSItem[] dPot = Inventory.find(defencePot);
		RSItem[] rPot = Inventory.find(rangePot);
		
		if(Skills.getCurrentLevel(SKILLS.PRAYER) < 10){
			println("Potting prayer");
			
			if(pPot.length > 0){
				if(pPot[0].click("Drink")){
					gotoTab(GameTab.TABS.INVENTORY);
					sleep(1200,1250);
				}
			}
			else{
				if(getHp() < 30)
					emergTele();
			}
		}
		if(Skills.getCurrentLevel(SKILLS.RANGED) <= Skills.getActualLevel(SKILLS.RANGED) + 4){
			println("Potting range");
			//gotoTab(GameTab.TABS.INVENTORY);
			if(rPot.length > 0){
				gotoTab(GameTab.TABS.INVENTORY);
				if(rPot[0].click("Drink")){
					sleep(1200,1250);
				}
			}
		}
		if(Skills.getCurrentLevel(SKILLS.DEFENCE) <= Skills.getActualLevel(SKILLS.DEFENCE) + 4){
			println("Potting defence");
			//gotoTab(GameTab.TABS.INVENTORY);
			if(dPot.length > 0){
				gotoTab(GameTab.TABS.INVENTORY);
				if(dPot[0].click("Drink")){
					sleep(1200,1250);
				}
			}
		}
	}
	
	public void activateProtectMage(){
		
		ArrayList<Prayer> list = PrayerBook.getActive();
		if(list.isEmpty()){
			gotoTab(GameTab.TABS.PRAYERS);
			PrayerBook.activate(Prayer.PROTECT_FROM_MAGIC);
		}
		else if (list.size() == 1 && list.get(0) != Prayer.PROTECT_FROM_MAGIC){
			turnOffAllPrayers();
			gotoTab(GameTab.TABS.PRAYERS);
			PrayerBook.activate(Prayer.PROTECT_FROM_MAGIC);
		}
	}
	
	public void activateProtectRanged(){
		
		ArrayList<Prayer> list = PrayerBook.getActive();
		if(list.isEmpty()){
			gotoTab(GameTab.TABS.PRAYERS);
			PrayerBook.activate(Prayer.PROTECT_FROM_MISSILES);
		}
		else if (list.size() == 1 && list.get(0) != Prayer.PROTECT_FROM_MISSILES){
			turnOffAllPrayers();
			gotoTab(GameTab.TABS.PRAYERS);
			PrayerBook.activate(Prayer.PROTECT_FROM_MISSILES);
		}
		
		//PrayerBook.activate(Prayer.PROTECT_FROM_MISSILES);
		//sleep(200,300);
	}
	
	public void reactivatePrayer(int option){
		
		RSItem[] pPot = Inventory.find(prayerPot);
		gotoTab(GameTab.TABS.INVENTORY);
		if(pPot.length > 0){
			println("Potting prayer");
			if(pPot[0].click("Drink")){
				sleep(1200,1250);
				if(option == 0)
					activateProtectMage();
				else
					activateProtectRanged();
			}
		}
		else{
			if(getHp() < 50)
				emergTele();
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
	
	private void waitForInv(int lootID) {
		int k = 0;
		while (k < 100	&& Player.isMoving()) {
			sleep(50);
			k++;
		}
	}
	//TODO writeToFile
	public void writeToFile(int item)
			throws IOException {
		String str = null;
		String tmp = null;
		tmp = map.get(item);
		if(tmp.equals(null)){
			tmp = mapBad.get(item);
			if(tmp.equals(null)){
				tmp = mapClue.get(item);
				
			}
		}
		str = tmp;
		
		FileWriter f = new FileWriter(Util.getWorkingDirectoryURL() + File.separator
				+ "MithDK" + File.separator + "drops.txt");
		println("Writing to file:" + Inventory.getCount(item) + " " + str);
		f.write(Inventory.getCount(item) + " " + str);
		f.close();
	}
	
	
	public void writeConfigSettings()
			throws IOException {
		println("step 1");
		FileWriter f = new FileWriter(Util.getWorkingDirectory()
				+ File.separator + "MithDK" 
				+ File.separator + "hello" + ".txt"); // writing to an exact
														// path
		
		
		//for (String setting : settings) { // running though the strings
			f.write("hello" + System.getProperty("line.separator"));
		//}
		f.close(); // closing the stream
	}
		
	
	public void eatFood(){
		RSItem[] food = Inventory.find(foodIDs);
		if(food.length > 0){
			if(food[0].click("Eat"))
				sleep(200,250);
		}
		else{
			emergTele();
		}
	}
	
	public boolean haveGear(){
		  
		if(!mithDragSpawn1.contains(pos()) && !afterWhirlpool.contains(pos())
				&& !greenDragArea.contains(pos()) && !toWhirlpoolA.contains(pos())){
			
			if(useSpecialBolts && Inventory.getCount(diamondEBolt) < 95 && Equipment.getCount(diamondEBolt) < 95){
				println("We dont have diamond e bolts");
				return false;
			}
			else if (Inventory.getCount(lumTab) == 0){
				println("We dont have lumby tabs");
				return false;
			}
			else if (Inventory.getCount(gamesNecklace) == 0){
				println("We dont have games necklaces ");
				return false;
			}
			else if (Inventory.getCount(rangePot) == 0){
				println("We dont have range pots");
				return false;
			}
			else if (Inventory.getCount(defencePot) == 0){
				println("We dont have defence pots");
				return false;
			}
			else if (Inventory.getCount(antifirePot) == 0){
				println("We dont have antifire pots");
				return false;
			}
			else if (Inventory.getCount(prayerPot) < 3){
				println("We dont have we dont have prayer pots");
				return false;
			}
			else if (Inventory.getCount(foodIDs) < 17){
				println("We dont have food");
				return false;
			}
			else if (useSpecialBolts && Equipment.getCount(rubyEBolt) < 95 && Inventory.getCount(rubyEBolt) < 95){
				println("We dont have ruby e bolts");
				return false;
			}
			else if (!useSpecialBolts && Equipment.getCount(addyBolt) < 190 ){
				println("We dont have addy bolts");
				return false;
			}
			else if (Equipment.getCount(ringOfLife) == 0){
				println("We dont have a ring of life");
				return false;
			}
		}
		else if((afterWhirlpool.contains(pos()) && pos().getPlane() == 1)
				|| (greenDragArea.contains(pos()) && pos().getPlane() == 0) 
				|| (toWhirlpoolA.contains(pos()) && pos().getPlane() == 0)){
			if(useSpecialBolts && Inventory.getCount(diamondEBolt) < 95 && Equipment.getCount(diamondEBolt) < 95){
				println("We dont have diamond e bolts");
				return false;
			}
			else if (!useSpecialBolts && Equipment.getCount(addyBolt) < 190 && Inventory.getCount(addyBolt) < 190){
				println("We dont have addy bolts");
				return false;
			}
			else if (Inventory.getCount(lumTab) == 0){
				println("We dont have lumby tabs");
				return false;
			}
			else if (Inventory.getCount(rangePot) == 0){
				println("We dont have range pots");
				return false;
			}
			else if (Inventory.getCount(defencePot) == 0){
				println("We dont have defence pots");
				return false;
			}
			else if (Inventory.getCount(antifirePot) == 0){
				println("We dont have antifire pots");
				return false;
			}
			else if (Inventory.getCount(prayerPot) < 3){
				println("We dont have we dont have prayer pots");
				return false;
			}
			else if (Inventory.getCount(foodIDs) < 17){
				println("We dont have food");
				return false;
			}
			else if (useSpecialBolts && Equipment.getCount(rubyEBolt) < 95 && Inventory.getCount(rubyEBolt) < 95){
				println("We dont have ruby e bolts");
				return false;
			}
			else if (Equipment.getCount(ringOfLife) == 0){
				println("We dont have a ring of life");
				return false;
			}
		}
		else{ 
			
			if(useSpecialBolts && Inventory.getCount(diamondEBolt) < 10 && Equipment.getCount(diamondEBolt) < 10){
				println("We dont have diamond e bolts");
				return false;
			}
			else if (!useSpecialBolts && (Inventory.getCount(addyBolt) + Equipment.getCount(addyBolt) < 30)){
				println("We dont have addy bolts");
				return false;
			}
			else if (Inventory.getCount(lumTab) == 0){
				println("We dont have lumby tabs");
				return false;
			}
			else if (useSpecialBolts && Equipment.getCount(rubyEBolt) < 10 && Inventory.getCount(rubyEBolt) < 10){
				println("We dont have ruby e bolts");
				return false;
			}
			else if (Equipment.getCount(ringOfLife) == 0){
				println("We dont have a ring of life");
				return false;
			}
		}
		
		
		return true;
	}
	
	private boolean clickNPC(RSNPC npc, String option) {
		//Mouse.setSpeed(General.random(100,120));
		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			loc = new RSTile(npc.getPosition().getX(), npc.getPosition().getY() -1);
			Mouse.move(Projection.tileToScreen(loc, 10));
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
		return false;
	}
	
	public void moveCamera(RSNPC n){
		if(!n.isOnScreen()){
			Camera.turnToTile(n.getAnimablePosition());
			sleep(100,120);
			Camera.setCameraAngle(General.random(45, 55));
			sleep(100,120);
		}
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void gotoSafeSpot1(){
		Point p = Projection.tileToScreen(safeSpotSpawn1P.getPosition(), 0).getLocation();
		double x = p.getX();
		double y = p.getY();
		
		Mouse.setSpeed(General.random(100, 110));
		if(x > 40 && x < 485 && y > 40 && y < 315){
			Walking.walkScreenPath(new RSTile[] {safeSpotSpawn1P.getPosition()});
			waitIsMovin();
		}
		else if (Walking.clickTileMM(safeSpotSpawn1P, 1)) {

			Timing.waitCondition(new Condition() {
				;
				@Override
				public boolean active() {
					return inArea2(safeSpot);
				}
			}, General.random(3000, 3500));
			sleep(1000, 1200);
		}
		Mouse.setSpeed(General.random(120, 140));
	}
	
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
	
	public static boolean isAttacking() {
		return Combat.getAttackingEntities().length > 0;
	}
	
	public boolean inArea2(RSTile[] t) {
		RSTile pos = pos();
		int posX = pos.getX();
		int posY = pos.getY();
		int nwX = t[0].getX();
		int nwY = t[0].getY();
		int seX = t[1].getX();
		int seY = t[1].getY();
		int p = pos().getPlane();
		int p2 = t[0].getPlane();
		int p3 = t[1].getPlane();
				

		if (posX >= nwX && posX <= seX && posY >= seY && posY <= nwY
				&& p == p2 && p == p3) {
			return true;
		}
		return false;
	}
	
	public void emergTele(){
		RSItem[] LUM_TAB = Inventory.find(lumTab);
		if(LUM_TAB.length > 0){
			gotoTab(GameTab.TABS.INVENTORY);
			if(LUM_TAB[0].click("Break")){
				sleep(3000,3500);
				bankStatus = true;
			}
		}
		else{
			gotoTab(GameTab.TABS.MAGIC);
			Magic.selectSpell("Lumbridge Home Teleport");
			sleep(10000,12000);
			bankStatus = true;
		}
	}
	
	public void gotoTab(GameTab.TABS tab){
		if(GameTab.getOpen() != tab){
			tab.open();
			sleep(200,300);
		}
	}
	
	public void timeToTab(RSNPC drag){
		RSGroundItem[] Nests = GroundItems.findNearest(loot);
		RSGroundItem[] Nests2 = GroundItems.findNearest(clue);
		RSGroundItem[] Nests3 = GroundItems.findNearest(badLoot);
		RSItem[] food = Inventory.find(foodIDs);
		RSItem[] dbone2 = Inventory.find(536);
		RSItem[] mbar2 = Inventory.find(2359);
		boolean tele = false;
		
		//if(drag == null){
			if(Inventory.isFull()){
				if (Nests.length == 0 && Nests2.length == 0 && Nests3.length == 0
					&& (food.length == 0 || (getHp() < 60 && food.length == 1)))
					tele = true;
			}
			else{
				if (Nests.length == 0 && Nests2.length == 0 && Nests3.length == 0
						&& (food.length == 0 || (getHp() < 60 && food.length == 1))
						&& dbone2.length == 0 && mbar2.length == 0)
						tele = true;
			}
		//}
		
		if (tele)
			emergTele();
	}
	
}
