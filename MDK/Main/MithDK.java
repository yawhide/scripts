package scripts.MDK.Main;

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
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Prayer.PRAYERS;
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

import scripts.MDK.Utilities.Looting;
import scripts.easyslayer.Timer;

@ScriptManifest(authors = { "Yaw hide" }, category = "Ranged", name = "Yaw hide's MithDK", version = 1.0)
public class MithDK extends Script implements MessageListening07, Painting, Ending{

	boolean useSpecialBolts = true;
	boolean pottedAntiFire = true;
	boolean bankStatus = true;
	boolean scriptStatus = true;
	long antiFireT;
	
	@Override
	public void run() {
		
		Looting.putMap();		
		Mouse.setSpeed(General.random(120,140));
		Walking.setWalkingTimeout(3000L);
		Walking.setControlClick(true);
		WebWalking.setUseRun(true);
		
		
		if((!lumbyArea.contains(pos()) || !chestA.contains(pos())) && haveGear()){
			bankStatus = false;
			println("We are in barbarian outpost (i hope) and off to mithril drags");
		}
		
		if(Equipment.find(SLOTS.WEAPON).length == 0)
			scriptStatus = false;
		
		while(scriptStatus){
			
			if(lumbyArea.contains(pos()) && !haveGear())
				bankStatus = true;
			
			if(bankStatus){
				if(Camera.getCameraAngle() < 90)
					Camera.setCameraAngle(General.random(90,  100));
				if(Skills.getCurrentLevel(SKILLS.PRAYER) < Skills.getActualLevel(SKILLS.PRAYER)){
					
					turnOffAllPrayers();
					
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

		openPrayers();
		
		while (drag != null && drag.isInteractingWithMe() && getHp() > 50 &&
				Skills.getCurrentLevel(SKILLS.PRAYER) > 10 && pottedAntiFire
				&& Player.getRSPlayer().getInteractingCharacter() != null) {
			
			if(mithAtHalf(drag)){
				equipDiamondBolts();
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
		
		turnOffPrayerEagle();
	}
	
	public void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)){
			//Options.setQuickPrayersOn(false);
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			sleep(800,1000);
		}
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
				}
			} 
			else if (Nests2.length > 0) {
				String str = mapClue.get(Nests2[0].getID());
				if (Nests2[0].click("Take " + str)){
					waitForInv(Nests2[0].getID());
					lastDrop = str;
				}
			} 
			else if (Nests3.length > 0) {
				String str = mapBad.get(Nests3[0].getID());
				if (Nests3[0].click("Take " + str)){
					waitForInv(Nests3[0].getID());
					lastDrop = str;
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
		while (Prayer.getCurrentPrayers().length > 0){
			Prayer.disable(Prayer.getCurrentPrayers()[0]);
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
		openPrayers();
		Prayer.enable(PRAYERS.PROTECT_FROM_MAGIC);
		sleep(200,300);
	}
	
	public void openPrayers(){
		if(!Prayer.isTabOpen()){
			GameTab.open(TABS.PRAYERS);
			sleep(200,250);
		}
	}
	
	public void activateProtectRanged(){
		openPrayers();
		Prayer.enable(PRAYERS.PROTECT_FROM_MISSILES);
		sleep(200,300);
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
