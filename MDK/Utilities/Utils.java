package scripts.MDK.Utilities;

import java.awt.Graphics;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.ext.Doors;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

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
					Conditionals.waitFor(door == null, 3000, 4000);
					return true;
				}
			}
			else{
				Walking.walkTo(doorTile);
				Camera.turnToTile(doorTile);
				waitIsMovin();
				return false;
			}
		}
		return true;
	}
	
	public static void useGameNeck(){
		openTab(TABS.INVENTORY);
		RSItem[] gamesNeck = Inventory.find(Constants.GAMES_NECKLACE);
		if (gamesNeck.length > 0){
			if (Clicking.click("Rub", gamesNeck)){
				General.sleep(1000,2000);
				NPCChat.selectOption("Barbarian Outpost.", true);
				Conditionals.waitFor(inArea(Tiles.toWhirlpoolA), 4000, 5000);
			}
		}
	}
	
	public static boolean equipBolts(int[] bolts){
		RSItem[] bolt = Inventory.find(bolts);
		if(bolt.length > 0){
			openTab(TABS.INVENTORY);
			if(Clicking.click("Wield", bolt)){
				Conditionals.waitFor(Inventory.getCount(bolts) == 0, 1000, 2000);
				return true;
			}
		}
		return false;
	}
	
	public static boolean equipBolts(int bolts){
		RSItem[] bolt = Inventory.find(bolts);
		if(bolt.length > 0){
			openTab(TABS.INVENTORY);
			if(Clicking.click("Wield", bolt)){
				Conditionals.waitFor(Inventory.getCount(bolts) == 0, 1000, 2000);
				return true;
			}
		}
		return false;
	}
	
	public static boolean drinkAntiFire(){
		RSItem[] pot = Inventory.find(Constants.ANTIFIRE_POT);
		openTab(TABS.INVENTORY);
		if(pot.length > 0){
			if(Clicking.click("Drink", pot)){
				return true;
			}
		}
		else
			emergTele();
		return false;
	}
	
	public static void checkToPotAntiFire(){
		if(!MithDK.pottedAntiFire && System.currentTimeMillis() - MithDK.antiFireT > 360000){
			General.println("potting antifire");
			if(drinkAntiFire()){
				MithDK.pottedAntiFire = true;
				MithDK.antiFireT = System.currentTimeMillis();
			}
		}
	}
	
	public static void potUp(String pot){
		RSItem[] pPot = Inventory.find(Constants.PRAYER_POT),
				dPot = Inventory.find(Constants.DEFENSE_POT),
				rPot = Inventory.find(Constants.RANGE_POT);
		switch(pot){
		case "prayer":
			if(pPot.length > 0){
				openTab(TABS.INVENTORY);
				final int currPrayerLv = Skills.getCurrentLevel(SKILLS.PRAYER);
				if(Clicking.click("Drink", pPot)){
					Conditionals.waitForPotting(SKILLS.PRAYER, currPrayerLv);
				}
			}
			else if(getHp() < 40){
					emergTele();
			}
			break;
		case "ranged":
			if(rPot.length > 0){
				openTab(TABS.INVENTORY);
				final int currRangeLv = Skills.getCurrentLevel(SKILLS.RANGED);
				if(Clicking.click("Drink", rPot)){
					Conditionals.waitForPotting(SKILLS.RANGED, currRangeLv);
				}
			}
			break;
		case "defence":
			if(dPot.length > 0){
				openTab(TABS.INVENTORY);
				final int currDefLv = Skills.getCurrentLevel(SKILLS.DEFENCE);
				if(Clicking.click("Drink", dPot)){
					Conditionals.waitForPotting(SKILLS.DEFENCE, currDefLv);
				}
			}
			break;
		}
	}
	
	public static void checkToPot(){		
		if(Skills.getCurrentLevel(SKILLS.PRAYER) < 10){
			potUp("prayer");
		}
		if(Skills.getCurrentLevel(SKILLS.RANGED) <= Skills.getActualLevel(SKILLS.RANGED) + 4){
			potUp("ranged");
		}
		if(Skills.getCurrentLevel(SKILLS.DEFENCE) <= Skills.getActualLevel(SKILLS.DEFENCE) + 4){
			potUp("defence");
		}
	}
	
	public static void drawTile(RSTile tile, Graphics g, boolean fill) {
		if (tile.getPosition().isOnScreen()) {
			if (fill) {
				g.fillPolygon(Projection.getTileBoundsPoly(tile, 0));
			} else {
				g.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
			}
		}
	}
	
	public static void eatFood(){
		RSItem[] food = Inventory.find(MDKGui.foodID);
		if(food.length > 0){
			final int foodCount = Inventory.getCount(MDKGui.foodID);
			if(Clicking.click("Eat", food)){
				Conditionals.waitForItem(MDKGui.foodID, foodCount);				
			}
		}
		else{
			emergTele();
		}
	}
	
	public static boolean haveGear(){
		  
		if(!Tiles.mithDragSpawn1.contains(pos())){
			
			if(MDKGui.useRubyBolts && Inventory.getCount(Constants.RUBY_E_BOLT)+Equipment.getCount(Constants.RUBY_E_BOLT) < 200){
				//General.println("We dont have ruby e bolts");
				return false;
			}
			else if (Inventory.getCount(Constants.HOUSE_TABLET) == 0){
				//General.println("We dont have house tabs");
				return false;
			}
			else if (Inventory.getCount(Constants.GAMES_NECKLACE) == 0 && !((inArea(Tiles.afterWhirlpool) && pos().getPlane() == 1)
				|| (inArea(Tiles.greenDragArea) && pos().getPlane() == 0) 
				|| (inArea(Tiles.toWhirlpoolA) && pos().getPlane() == 0))){
				//General.println("We dont have games necklaces ");
				return false;
			}
			else if (Inventory.getCount(Constants.RANGE_POT) == 0){
				//General.println("We dont have range pots");
				return false;
			}
			else if (Inventory.getCount(Constants.DEFENSE_POT) == 0){
				//General.println("We dont have defence pots");
				return false;
			}
			else if (Inventory.getCount(Constants.ANTIFIRE_POT) == 0){
				//General.println("We dont have antifire pots");
				return false;
			}
			else if (Inventory.getCount(Constants.PRAYER_POT) < 3){
				//General.println("We dont have we dont have prayer pots");
				return false;
			}
			else if (Inventory.getCount(MDKGui.foodID) < MDKGui.foodAmount){
				//General.println("We dont have enough food");
				return false;
			}
			else if (Equipment.getCount(MDKGui.boltsUsing)+Inventory.getCount(MDKGui.boltsUsing) < 200 ){
				//General.println("We dont have main bolts");
				return false;
			}
			else if (Equipment.getItem(SLOTS.RING) == null || Equipment.getItem(SLOTS.RING).getID() != 2570){
				//General.println("We dont have a ring of life");
				return false;
			}
		}
		else{ 
			if(MDKGui.useRubyBolts && Inventory.getCount(Constants.RUBY_E_BOLT)+Equipment.getCount(Constants.RUBY_E_BOLT) < 30){
				//General.println("We dont have ruby e bolts");
				return false;
			}
			else if (Equipment.getCount(MDKGui.boltsUsing)+Inventory.getCount(MDKGui.boltsUsing) < 30 ){
				//General.println("We dont have main bolts");
				return false;
			}
			else if (Inventory.getCount(Constants.HOUSE_TABLET) == 0){
				//General.println("We dont have house tabs");
				return false;
			}
			else if (Equipment.getItem(SLOTS.RING) == null || Equipment.getItem(SLOTS.RING).getID() != 2570){
				//General.println("We dont have a ring of life");
				return false;
			}
		}
		return true;
	}
	
	public static void moveCamera(RSNPC npc){
		if(!npc.isOnScreen()){
			Camera.turnToTile(npc.getAnimablePosition());
			Camera.setCameraAngle(General.random(45, 55));
		}
	}
	
	public static void gotoSafeSpot1(){
		if(!inArea(Tiles.safeSpotSpawn1)){
			if(Tiles.safeSpotSpawn1P.getPosition().isOnScreen()){
				Clicking.click(Tiles.safeSpotSpawn1P.getAnimablePosition());
			}
			else{
				Walking.walkTo(Tiles.safeSpotSpawn1P);
			}
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
				MithDK.needToBank = true;
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
		RSItem[] food = Inventory.find(MDKGui.foodID),
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
		if(GameTab.getOpen() != t){
			GameTab.open(t);
			Conditionals.waitForTab(t);
		}
	}

}
