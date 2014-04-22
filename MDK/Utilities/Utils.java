package scripts.MDK.Utilities;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.ext.Doors;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.util.Util;

import scripts.MDK.Data.Tiles;
import scripts.MDK.Main.MithDK;

public class Utils {
	public static RSTile pos(){
		return Player.getPosition();
	}
	
	public static int getHp(){
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
		  
		if(!Tiles.mithDragSpawn1.contains(pos()) && !Tiles.afterWhirlpool.contains(pos())
				&& !Tiles.greenDragArea.contains(pos()) && !Tiles.toWhirlpoolA.contains(pos())){
			
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
	
	public void gotoSafeSpot1(){
		Point p = Projection.tileToScreen(Tiles.safeSpotSpawn1P.getPosition(), 0).getLocation();
		double x = p.getX();
		double y = p.getY();
		
		Mouse.setSpeed(General.random(100, 110));
		if(x > 40 && x < 485 && y > 40 && y < 315){
			Walking.walkScreenPath(new RSTile[] {Tiles.safeSpotSpawn1P.getPosition()});
			waitIsMovin();
		}
		else if (Walking.clickTileMM(Tiles.safeSpotSpawn1P, 1)) {

			Timing.waitCondition(new Condition() {
				;
				@Override
				public boolean active() {
					return inArea(Tiles.safeSpot);
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
	
	public boolean inArea(RSTile[] t) {
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
	
	public static void checkStats(){
		if(Skills.getActualLevel(SKILLS.RANGED) < 80){
			General.println("You must be at least 80 range to use this script");
			MithDK.mainLoopStatus = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			General.println("You must be at least 44 prayer to use this script");
			MithDK.mainLoopStatus = false;
		}
		if(Skills.getActualLevel(SKILLS.CONSTRUCTION) < 50){
			General.println("You do not have 50 con for varrock portal focus, you must use varrock teletabs");
			MithDK.mainLoopStatus = false;
		}
	}
}
