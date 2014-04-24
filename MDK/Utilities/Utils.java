package scripts.MDK.Utilities;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.tribot.api.Clicking;
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
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.ext.Doors;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.util.Util;

import scripts.MDK.Data.Constants;
import scripts.MDK.Data.Tiles;
import scripts.MDK.Main.MDKGui;
import scripts.MDK.Main.MithDK;

public class Utils {
	public static RSTile pos(){
		return Player.getPosition();
	}
	
	public static int getHp(){
		return Combat.getHPRatio();
	}
	
	public static void waitIsMovin(){
		Conditionals.waitFor(!Player.isMoving(), 3000, 4000);
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
	
	public void useGameNeck(){
		openTab(TABS.INVENTORY);
		RSItem[] gamesNeck = Inventory.find(Constants.GAMES_NECKLACE);
		if (gamesNeck.length > 0){
			if (Clicking.click("Rub", gamesNeck)){
				NPCChat.selectOption("Barbarian Outpost.", true);
				Conditionals.waitFor(inArea(Tiles.toWhirlpoolA), 4000, 5000);
			}
		}
	}
	
	public static boolean equipBolts(int[] bolts){
		RSItem[] bolt = Inventory.find(bolts);
		if(bolt.length > 0){
			Utils.openTab(TABS.INVENTORY);
			if(Clicking.click("Wield", bolt)){
				Conditionals.waitFor(Inventory.getCount(bolts) == 0, 1000, 2000);
				return true;
			}
		}
		return false;
	}
	
	public boolean drinkAntiFire(){
		RSItem[] pot = Inventory.find(Constants.ANTIFIRE_POT);
		Utils.openTab(TABS.INVENTORY);
		if(pot.length > 0){
			if(Clicking.click("Drink", pot)){
				General.sleep(1000,1200);
				return true;
			}
		}
		else
			emergTele();
		return false;
	}
	
	public void checkToPotAntiFire(){
		if(!MithDK.pottedAntiFire && System.currentTimeMillis() - MithDK.antiFireT > 360000){
			General.println("potting antifire");
			if(drinkAntiFire()){
				MithDK.pottedAntiFire = true;
				MithDK.antiFireT = System.currentTimeMillis();
			}
		}
	}
	
	public void checkToPot(){
		RSItem[] pPot = Inventory.find(Constants.PRAYER_POT),
				dPot = Inventory.find(Constants.DEFENSE_POT),
				rPot = Inventory.find(Constants.RANGE_POT);
		
		if(Skills.getCurrentLevel(SKILLS.PRAYER) < 10){
			if(pPot.length > 0){
				openTab(TABS.INVENTORY);
				final int pPotCount = Inventory.getCount(Constants.PRAYER_POT);
				if(Clicking.click("Drink", pPot)){
					Conditionals.waitForItem(Constants.PRAYER_POT, pPotCount);
				}
			}
			else if(getHp() < 40){
					emergTele();
			}
		}
		if(Skills.getCurrentLevel(SKILLS.RANGED) <= Skills.getActualLevel(SKILLS.RANGED) + 4){
			if(rPot.length > 0){
				openTab(TABS.INVENTORY);
				final int rPotCount = Inventory.getCount(Constants.RANGE_POT);
				if(Clicking.click("Drink", rPot)){
					Conditionals.waitForItem(Constants.RANGE_POT, rPotCount);
				}
			}
		}
		if(Skills.getCurrentLevel(SKILLS.DEFENCE) <= Skills.getActualLevel(SKILLS.DEFENCE) + 4){
			if(dPot.length > 0){
				openTab(TABS.INVENTORY);
				final int dPotCount = Inventory.getCount(Constants.DEFENSE_POT);
				if(Clicking.click("Drink", dPot)){
					Conditionals.waitForItem(Constants.DEFENSE_POT, dPotCount);
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
	
	public static void eatFood(){
		RSItem[] food = Inventory.find(MDKGui.foodIDs);
		if(food.length > 0){
			final int foodCount = Inventory.getCount(MDKGui.foodIDs);
			if(Clicking.click("Eat", food))
				Conditionals.waitForItem(MDKGui.foodIDs, foodCount);
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
	
	private static boolean clickNPC(RSNPC npc, String option) {
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
	
	public void moveCamera(RSNPC npc){
		if(!npc.isOnScreen()){
			Camera.turnToTile(npc.getAnimablePosition());
			Camera.setCameraAngle(General.random(45, 55));
		}
	}
	
	public void gotoSafeSpot1(){
		Point p = Projection.tileToScreen(Tiles.safeSpotSpawn1P.getPosition(), 0).getLocation();
		double x = p.getX();
		double y = p.getY();
		
		if(x > 40 && x < 485 && y > 40 && y < 315){
			Walking.walkScreenPath(Walking.generateStraightPath(Tiles.safeSpotSpawn1P.getPosition()));
			Conditionals.waitFor(inArea(Tiles.safeSpot), 4000, 5000);
		}
		else if (Walking.clickTileMM(Tiles.safeSpotSpawn1P, 1)) {
			Conditionals.waitFor(inArea(Tiles.safeSpot), 4000, 5000);
		}
	}
	
	public static boolean isAttacking() {
		return Combat.getAttackingEntities().length > 0;
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
	
	public static boolean inArea(RSTile[] t) {
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
	
	public static boolean inArea(RSArea a) {
		return a.contains(pos());
	}
	
	public static void emergTele(){
		RSItem[] houseTab = Inventory.find("Teleport to house");
		if(houseTab.length > 0){
			openTab(TABS.INVENTORY);
			if(Clicking.click("Break", houseTab)){
				Conditionals.waitFor(isInsideOurHouse(), 4000, 5000);
				MithDK.bankStatus = true;
			}
		}
	}
	
	public static boolean isInsideOurHouse(){
		return !inArea(Tiles.varrockArea)
				&& Objects.findNearest(50, "Portal").length > 0;
	}
		
	public static boolean timeToTab(RSNPC drag) {
		RSGroundItem[] mainLoot = GroundItems.findNearest(Looting.LOOT_IDS),
				clueLoot = GroundItems.findNearest(Looting.CLUE_NAME),
				badLoot = GroundItems.findNearest(Looting.BAD_LOOT);
		RSItem[] food = Inventory.find(MDKGui.foodIDs),
				dBone = Inventory.find(536),
				mithBar = Inventory.find(2359);

		if (Inventory.isFull()) {
			if (mainLoot.length == 0 && clueLoot.length == 0 && badLoot.length == 0
					&& (food.length == 0 || (getHp() < 70 && food.length == 1)))
				return true;
		} else {
			if (mainLoot.length == 0 && clueLoot.length == 0 && badLoot.length == 0
					&& (food.length == 0 || (getHp() < 70 && food.length == 1))
					&& dBone.length == 0 && mithBar.length == 0)
				return true;
		}
		return false;
		
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
	
	public static void openTab(TABS t) {
		GameTab.open(t);
		Conditionals.waitForTab(t);
	}
}
