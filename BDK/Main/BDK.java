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
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;

import scripts.BDK.Utilities.YawsGeneral;
import scripts.BDK.Utilities.Zybez;



@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", name = "Yaw hide's BDK", version = 1.0, description="Local version")
public class BDK extends Script implements Painting, Pausing {
	  
	// Variables
	static int[] FOOD_IDS  = { };
	int BOLTS_ID = 9142;
	long ANTIBAN = System.currentTimeMillis();
	int START_XP = Skills.getXP(SKILLS.HITPOINTS) + Skills.getXP(SKILLS.RANGED);
	double VERSION;
	int CURRENT_XP;
	final long START_TIME = System.currentTimeMillis();
	double XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
	double XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	int START_LV = Skills.getActualLevel(SKILLS.RANGED) + Skills.getActualLevel(SKILLS.HITPOINTS);
	final String[] SKILLS_TYPES = {"Defence", "Ranged", "Hitpoints" };
	final SKILLS[] SKILL = {SKILLS.DEFENCE, SKILLS.RANGED, SKILLS.HITPOINTS, };
	final int[] XP = { Skills.getXP(SKILLS.DEFENCE), Skills.getXP(SKILLS.RANGED), 
			Skills.getXP(SKILLS.HITPOINTS) };
	int[] START_SKILL_LVS = { 	Skills.getActualLevel(SKILLS.DEFENCE), Skills.getActualLevel(SKILLS.RANGED), 
			Skills.getActualLevel(SKILLS.HITPOINTS) };
	boolean SCRIPT_STATUS = true;
	static int FOOD_NUM = 2;
	
	int DHIDES,DBONES = 0;
	int DHIDE_COUNT, DBONE_COUNT = 0;
	int DHIDES_INI, DBONES_INI = 0;
	int DHIDE_PRICE = 1775;
	int DBONES_PRICE = 1800;
	int MITH_BOLTS_PRICE;
	int RUNE_DAGGER_PRICE;
	int NAT_PRICE;
	static boolean WAIT_GUI = true;
	
	String FIGHT_STATUS;
	
	@Override
	public void run() {
		
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			println("You must be at least 70 range to use this script");
			SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			println("You must be at least 44 prayer to use this script");
			SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 70){
			println("You must be at least 70 agility to use this script");
			SCRIPT_STATUS = false;
		}
		
		boolean devmode = false;
		
		if(devmode){
			FOOD_NUM = 2;
			FOOD_IDS = new int[] {379};
		}
		else{
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
		Mouse.setSpeed(General.random(175,190));
		Walking.setWalkingTimeout(5000L);
		General.useAntiBanCompliance(true);
		
		while(SCRIPT_STATUS){
			CURRENT_XP = Skills.getXP(SKILLS.RANGED) + Skills.getXP(SKILLS.HITPOINTS);
			XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
			XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
			
			
			DHIDES = Inventory.getCount(536) - DHIDES_INI;
			DBONES = Inventory.getCount(1751) - DBONES_INI;
			
			if(Inventory.find("Falador teleport").length > 0 && Inventory.find("Falador teleport")[0].getStack() == 1){
				println("Ran out of ftabs...");
				emergTele();
				SCRIPT_STATUS = false;
			}
			
			if(Equipment.getItem(SLOTS.ARROW).getStack() < 100){
				println("Ran out of bolts");
				emergTele();
				SCRIPT_STATUS = false;
			}
			
			if(Game.getRunEnergy() > 50) {
			    Options.setRunOn(true);
			}
			
			if(gotoDrag()){
				fight();
			}
		}
	}
	
	
	
	
	
	public boolean isLoot(){
		RSGroundItem[] Nests = GroundItems.findNearest(loot2);
		return Nests.length > 0 && blueDragArea.contains(Nests[0].getPosition());
	}
		
	public void LOOT() {
		FIGHT_STATUS = "looting";
		
		turnOffPrayer();
		
		
		RSGroundItem[] Nests = GroundItems.findNearest(loot);
		if (getHp() <= 50) {
			if (Inventory.getCount(FOOD_IDS) == 0) {
				emergTele();
			} 
			else
				HEAL();
		}
		
		for(int i = 0; i < Nests.length; i++){
			if(Nests[i].getID() == 9142){
				if (Nests[i].getStack() > 9) {
					if (!Nests[i].isOnScreen()) {
						Walking.clickTileMM(Nests[i].getPosition(), 1);
						Camera.turnToTile(Nests[i].getPosition());
						Camera.setCameraAngle(General.random(90, 100));
						waitIsMovin();
					}
					String str = map.get(Nests[i].getID());
					int tmpCount = lootCount(Nests[i].getID());
					if (Nests[i].click("Take " + str))
						waitForInv(Nests[i].getID(), tmpCount);
				}
			}
			else{
				if (!Nests[i].isOnScreen()) {
					Walking.clickTileMM(Nests[i].getPosition(), 1);
					Camera.turnToTile(Nests[i].getPosition());
					Camera.setCameraAngle(General.random(90, 100));
					waitIsMovin();
				}
				String str = map.get(Nests[i].getID());
				int tmpCount = lootCount(Nests[i].getID());
				if(Nests[i].click("Take " + str))
					waitForInv(Nests[i].getID(), tmpCount);
			}
		}
	}

	
	public void bank(){
		println("banking...");
		
		if(Banking.openBankBooth()){
			if(Banking.isPinScreenOpen())
				Banking.inPin();
			else if (Banking.isBankScreenOpen()){
				
				DHIDE_COUNT += DHIDES;
				DBONE_COUNT += DBONES;
				DHIDES_INI = 0;
				DBONES_INI = 0;
				
				Banking.depositAllExcept("Falador teleport");
				sleep(100,150);
				
				
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
							Banking.deposit(1, ppot);
							sleep(200,250);
						}
					}
				}
				sleep(200,300);
				
				if(Banking.find(FOOD_IDS).length == 0 || 
						(Inventory.find("Falador teleport").length == 0 && 
						Banking.find("Falador teleport").length == 0)){
					println("Ran out of food");
					SCRIPT_STATUS = false;
				}
				
				Banking.withdraw(1, rangepots);
				sleep(600, 650);
				
				Banking.withdraw(FOOD_NUM+((Combat.getMaxHP() - Combat.getHP()) / 7), FOOD_IDS);
				sleep(100,150);
				if(Inventory.find("Falador teleport").length == 0){
					Banking.withdraw(10, "Falador teleport");
					sleep(100,150);
				}
				Banking.close();
				DBONES_INI = 0; 
				DHIDES_INI = 0;
				while(Inventory.find(FOOD_IDS).length > FOOD_NUM){
					Inventory.find(FOOD_IDS)[0].click("Eat");
					sleep(300);
				}
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
		double secondsRan = (int) (timeRan2/1000);
		double hoursRan = secondsRan/3600;
		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(SKILL[i])) {

				double xp_per_hour = Math.round(((Skills.getXP(SKILL[i]) - XP[i])) / hoursRan);

				g.setColor(new Color(0, 0, 0));
				g.fillRect(2, 326 - x, 515, 12);

				g.setColor(new Color(0, 255, 0, 255));
				g.drawString(SKILL[i] + ": " + (int) xp_per_hour + " Xp/h", 5, (337 - x));

				int x1 = 125, y1 = 327 - x;
				int CUR_LVL = Skills.getActualLevel(SKILL[i]);
				int NXT_LVL = (CUR_LVL + 1);
				int Percentage = Skills.getPercentToLevel(SKILL[i], NXT_LVL);
				double nextLv = Skills.getXPToLevel(SKILL[i], NXT_LVL);
				
				double hours = (nextLv / xp_per_hour);

				g.drawString("Curr lv: " + CUR_LVL + " (" 
						+ (CUR_LVL - START_SKILL_LVS[i]) + ")  TTL " + 
						(int) hours + ":" + (int) ((hours - (int) hours) * 60)
						+ " hr:min  Exp to lv: " + (int) nextLv
				, 235, y1 + 10);
				
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
		for(int i = 0; i < item.length; i++){
			int count = items.containsKey(item[i].getID()) ? items.get(item[i].getID()) : 0;
			items.put(item[i].getID(), count + 1);
		}
		Iterator it = items.entrySet().iterator();
		int k = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        g.drawString((map.get(pairs.getKey()) != null ? map.get(pairs.getKey()) : pairs.getKey()) 
	        		+ " = " + pairs.getValue(), 5, 100+k);
	        it.remove(); // avoids a ConcurrentModificationException
	        k+=15;
	    }
		
		int dh, db;
		dh = DHIDES + DHIDE_COUNT;
		db = DBONES + DBONE_COUNT;
		g.setColor(new Color(60, 60, 60));
		g.fillRect(340, 370, 200, 150);

		g.setColor(Color.WHITE);
		g.drawString("Yawhide's BDK", 345, 385);
		g.drawString("Version :" + VERSION + "   Curr world: " + Game.getCurrentWorld(), 345, 405);
		g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
		g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour
				+ "/h)", 345, 435);
		g.drawString("State: " + FIGHT_STATUS, 345, 450);
		g.drawString("Dbones:" + dh + "       Dhide: " + db, 345, 465);
		g.drawString("Gp/hr: " + (int) ((dh*DHIDE_PRICE + db*DBONES_PRICE)/hoursRan/1000) + " K   Total: " + (int)(dh*DHIDE_PRICE + db*DBONES_PRICE)/1000, 345, 480);
		//g.drawString("Ini: " + dhidesIni + " " + dbonesIni + "  Reg: " + dhides + " " + dbones, 345, 495);
		g.drawString("In safespot? " + inSafeSpot(), 345, 495);
		g.setColor(Color.CYAN);
		for(int i = 0; i < Combat.getAttackingEntities().length;i++){
			RSCharacter tmp = Combat.getAttackingEntities()[i];
			drawTile(new RSTile(tmp.getPosition().getX()-1, tmp.getPosition().getY()-1), g, false);
			g.setColor(Color.GREEN);
			drawTile(new RSTile(tmp.getPosition().getX(), tmp.getPosition().getY()-1), g, false);
		}
		g.setColor(Color.RED);
		for(int i = 0; i < neSSA.length; i++){
			drawTile(neSSA[i], g, false);
		}
			
	}
	

	
	
	public void isFull(){
		RSItem[] coin = Inventory.find("Coins");
		RSItem[] food = Inventory.find(FOOD_IDS);
		RSItem[] bolts = Inventory.find("Mithril bolts");
		if(Inventory.find(junk).length > 0 || food.length > 0 || coin.length > 0 || bolts.length > 0){
			FIGHT_STATUS = "dropping junk";
			
			if(coin.length > 0 && coin[0].getStack() < 1500){
				Inventory.drop(coin[0].getID());
				sleep(150,200);
			}
			else if (food.length > 0){
				food[0].click("Eat");
				sleep(300,350);
			}
			else if (bolts.length > 0){
				bolts[0].click("Wield");
				sleep(300, 350);
			}
			Inventory.drop(junk);
		}
		else{
			emergTele();
		}
	}

	public void gotoSafeSpot(){
		FIGHT_STATUS = "going to safe spot";
		Walking.setWalkingTimeout(1500L);
		if(neSS.isOnScreen()){
			println("clicking on screen");
			Walking.clickTileMS(clickNESS, 1);
		}
		else if (pos().distanceTo(clickNESS) >= 7){
			println("generating a path");
			Walking.walkPath(Walking.generateStraightPath(clickNESS)); //safeSpotPath);//
		}
		else{
			println("clicking minimap");
			Walking.clickTileMM(clickNESS, 1);
		}	
		println("clicked, now gonna wait till I am not moving anymore");
		waitIsMovin();
	}
	
	public void waitForLoot(){
		FIGHT_STATUS = "prayerflicking";
		while(!isLoot() && isRanging() && inSafeSpot() && !isMovin()){
			
			prayerflick();
		}
		turnOffPrayer();
	}
	
	public String underAttack(){
		RSCharacter[] mon = Combat.getAttackingEntities();
		if(mon.length > 0){
			return mon[0].getName();
		}
		return null;
	}
	
	public boolean inSafeSpot(){
		return inArea(neSSArea);// || inArea(neSSArea);//inArea(sSSArea) || 
	}
	
	public boolean inArea(RSArea a) {
		return a.contains(pos());
	}
	
	public void waitIsMovin(){ 
		for(int i = 0; i < 57; i++, sleep(30, 40)){
			if (!Player.isMoving())
				break;
		}
	}
	
	public void prayerflick(){//RSNPC drag) {
		
		while (getHp() > 30 &&	Prayer.getPrayerPoints() > 5 && isRanging() && inSafeSpot() && !isLoot()) {
			FIGHT_STATUS = "flicking now!";
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
			}
			Timer t = new Timer(1100L);
			do {
				if (Player.getAnimation() != 4230) {
					sleep(250, 300);
					Prayer.enable(PRAYERS.EAGLE_EYE);
					//Options.setQuickPrayersOn(true);
				} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)) {
					//Options.setQuickPrayersOn(false);
					Prayer.disable(PRAYERS.EAGLE_EYE);
					sleep(350, 400);
				} else {
					sleep(400, 450);
				}
			} while (t.getRemaining() > 0);
		}
		turnOffPrayer();
	}
	
	public void turnOffPrayer(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)){
			//Options.setQuickPrayersOn(false);
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			sleep(800,1000);
		}
	}
	
	public int getStackBolts() {
		return Equipment.getItem(SLOTS.ARROW).getID() == BOLTS_ID ? Equipment.getItem(SLOTS.ARROW).getStack() : 0;
	}
	
	public void emergTele(){
		FIGHT_STATUS = "tele tabbing";
		RSItem[] tab = Inventory.find("Falador teleport");
		GameTab.open(TABS.INVENTORY);
		sleep(100, 150);
		if(tab.length > 0){
			if(tab[0].click("Break")){
				sleep(General.random(4000,  5000));
			}
		}
		else{
			println("Out of f tabs");
			SCRIPT_STATUS = false;
		}
	}
	
	public void HEAL() {
		FIGHT_STATUS = "eating food";
		GameTab.open(TABS.INVENTORY); sleep(100, 150);
		RSItem[] food = Inventory.find(FOOD_IDS);
		if (food.length > 0) {
			if(food[0].click("Eat"))
				sleep(300, 550);
		}
		else
			emergTele();
	}
	
	private void waitForInv(int lootID, int lootNum) {
		FIGHT_STATUS = "waiting for loot";
		int k = 0;
		while (lootCount(lootID) == lootNum && k < 200
				&& Player.isMoving()) {
			sleep(80);
			k++;
		}
		if (lootCount(lootID) > lootNum) {
			if (lootID == 1751)
				DHIDES++;
			else if (lootID == 536)
				DBONES++;
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
	
	public BDK() {
		VERSION = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
	}

	@Override
	public void onPause() {
		if(inArea(blueDragArea)){
			if(!inSafeSpot())
				gotoSafeSpot();
		}
		turnOffPrayer();
	}

	@Override
	public void onResume() {
		if(!inSafeSpot())
			gotoSafeSpot();
	}
	
	private boolean clickNPC(RSNPC npc, String option) {

		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			if(npc.getID() == drag247){
				loc = new RSTile(npc.getPosition().getX()-1, npc.getPosition().getY()-1);
			}
			else{
				loc = new RSTile(npc.getPosition().getX(), npc.getPosition().getY()-1);
			}
			Mouse.move(Projection.tileToScreen(loc, 10));
			if(Game.isUptext("Walk here / 2 more options")){
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
			else if (Game.isUptext(option)) {
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
	
	public void waitForDrag(RSNPC drag) {

		while (drag.isInCombat() && inSafeSpot() && !inCombat())
			sleep(1000, 1300);
	}
	
	public void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if(Clicking.click("Drink", potion[0])){
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return Skills.getCurrentLevel(SKILLS.RANGED) > Skills.getActualLevel(SKILLS.RANGED);
						}
					}, General.random(1000, 1200));
				}
			}
		}
	}

	public void waitForItem(int[] i) {
		final int[] item = i;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return Inventory.getCount(item) > 0;
			}
		}, General.random(500, 1000));
	}

	public void waitForItem(int i) {
		final int item = i;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return Inventory.getCount(item) > 0;
			}
		}, General.random(500, 1000));
	}

	public void waitForTab(TABS t) {
		final TABS tab = t;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return GameTab.getOpen() == tab;
			}
		}, General.random(500, 1000));
	}
	
	
	
}
