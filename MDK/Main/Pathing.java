package scripts.MDK.Main;


import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Camera;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.MDK.Data.Tiles;
import scripts.MDK.Utilities.Attack;
import scripts.MDK.Utilities.Bank;
import scripts.MDK.Utilities.Conditionals;
import scripts.MDK.Utilities.Praying;
import scripts.MDK.Utilities.Utils;

public class Pathing {
	
	public static void checkStatus() {
		if (Utils.isInsideOurHouse()) {
			if (Camera.getCameraAngle() < 90)
				Camera.setCameraAngle(General.random(90, 100));
			Praying.disableAllPrayers();
			prayAtHouseAltar();
		} else if (Utils.inArea(Tiles.varrockArea)) {
			checkEquipment();
		} else if (Utils.getHp() < 50) {
			Utils.eatFood();
		} else if (Utils.pos().distanceTo(Tiles.whirlpool) <= 4
				&& Utils.pos().getPlane() == 0) {
			useWhirpool();
		} else if (Utils.inArea(Tiles.toWhirlpoolA2) && Utils.pos().getPlane() == 0) {
			goToWhirlpool();
		}
		else if (Utils.inArea(Tiles.afterWhirlpool2)) {
			Utils.checkToPot();
			Utils.checkToPotAntiFire();
			stepDownToGD();
		} else if (Utils.pos().distanceTo(Tiles.stepsUpToMD) <= 4
				&& Utils.pos().getPlane() == 0) {
			stepUpToMD();
		} else if (Utils.inArea(Tiles.greenDragArea2)) {
			Utils.checkToPotAntiFire();
			Praying.activatePrayer(PRAYERS.PROTECT_FROM_MAGIC);
			goToMith();
		} else if (Utils.inArea(Tiles.mithDragSpawn12)) {
			Attack.fight();
		} else {
			General.println("we are lost");
			General.sleep(100,120);
			// emergTele();
		}
	}
	
	public static void stepDownToGD(){
		if(Camera.getCameraAngle() < 90){
			Camera.setCameraAngle(General.random(90, 100));
		}
		RSObject[] STAIRS = Objects.getAt(Tiles.stepsDownToGD);
		if (STAIRS.length > 0){
			if(STAIRS[0].isOnScreen()){
				if(Clicking.click("Climb-down", STAIRS)){
					Conditionals.waitFor(!Tiles.afterWhirlpool.contains(Utils.pos()), 2000, 2500);
				}	
			}
			else{
				Camera.turnToTile(Tiles.stepsDownToGD);
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90, 100));
				}
			}
		}
	}
	
	public static void stepUpToMD(){
		RSObject[] STAIRS = Objects.getAt(Tiles.stepsUpToMD);
		if (STAIRS.length > 0){
			if(STAIRS[0].isOnScreen()){
				if(Clicking.click("Climb-up", STAIRS)){
					Conditionals.waitFor(!Tiles.greenDragArea.contains(Utils.pos()), 2000, 2500);
				}
			}
			else{
				Camera.turnToTile(Tiles.stepsDownToGD);
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90, 100));
				}
			}
		}
	}
	
	public void goDownTrapDoorLumby(){
		RSObject[] TRAP_DOOR = Objects.getAt(Tiles.trapDoor);
		if(TRAP_DOOR.length > 0){
			if(TRAP_DOOR[0].isOnScreen()){
				if(Clicking.click("Climb-down", TRAP_DOOR)){
					Conditionals.waitFor(Player.getAnimation() == 827, 4000, 4500);
				}
			}
		}
	}
	
	public void prayAtAltar(){
		RSObject[] ALTAR = Objects.getAt(Tiles.altar);
		if(ALTAR.length > 0){
			if(ALTAR[0].isOnScreen()){
				if(Clicking.click("Pray-at", ALTAR)){
					Conditionals.waitFor(Player.getAnimation() == 645, 3000, 3500);
				}
			}
		}
	}
	
	public void goToChest(){
		Walking.walkPath(Tiles.toChest);
		Utils.waitIsMovin();
	}
	
	public void goToAltar(){
		Walking.walkPath(Tiles.toAltar);
		Utils.waitIsMovin();
	}
	
	public static void goToWhirlpool(){
		Walking.walkPath(Walking.randomizePath(Tiles.toWhirlpool, 2, 2));
		Utils.waitIsMovin();
	}
	
	public static void goToMith(){
		Walking.walkPath(Tiles.toMith);
		Utils.waitIsMovin();
	}
	
	public static void useWhirpool(){
		if(Tiles.whirlpoolT.getAnimablePosition().isOnScreen()){
			if(Clicking.click(Tiles.whirlpoolT.getAnimablePosition())){
				Conditionals.waitFor(!Tiles.afterWhirlpool.contains(Utils.pos()), 8000, 9000);
				General.sleep(4000, 5000);
			}
		}
		else{
			Camera.turnToTile(Tiles.whirlpoolT);
			Camera.setCameraAngle(Camera.getTileAngle(Tiles.whirlpoolT));
		}
	}
	
	public static void prayAtHouseAltar(){
		RSObject[] alter = Objects.findNearest(20, "Altar");
		RSObject[] portal = Objects.findNearest(20, "Varrock Portal");
		RSObject[] portal2 = Objects.findNearest(30, "Portal");
		
		if(alter.length == 0 || portal.length == 0){
			if(portal2.length > 0){
				if(portal2[0].isOnScreen()){
					if(Clicking.click("Enter", portal2[0])){
						NPCChat.selectOption("Go to your house", true);
					}
				}
				else{
					Walking.walkPath(Walking.generateStraightPath(portal2[0].getPosition()));
					Utils.waitIsMovin();
				}
			}
			General.println("Could not determine where you are, are you outside of the house???");
		}
		
		else if (Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER)) {
			if(portal.length > 0){
				if (portal[0].isOnScreen()) {
					if (Clicking.click("Enter", portal[0])) {
						Conditionals.waitFor(Utils.inArea(Tiles.varrockArea), 2200, 3000);
					}
				} else {
					Positionable temp = new RSTile(portal[0].getPosition().getX(), portal[0].getPosition().getY()-2, 0);
					Walking.walkPath(Walking.generateStraightPath(temp));
					Utils.waitIsMovin();
				}
			}
		} else {
			if (alter.length > 0) {
				if (alter[0].isOnScreen()) {
					if (Clicking.click("Pray", alter[0])) {
						Conditionals.waitFor(Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER), 1500, 2300);
					}
				} else {
					Walking.walkPath(Walking.generateStraightPath(alter[0].getPosition()));
					Utils.waitIsMovin();
				}
			}
		}
	}
	
	public static void checkEquipment() {
		if (!Utils.haveGear()) {
			walkToBank();
		} else {
			MithDK.needToBank = false;
			Utils.useGameNeck();
		}
	}
	
	public static void walkToBank() {
		if (Utils.pos().distanceTo(Tiles.bankTile) <= 5) {
			Bank.openBank();
		} else {
			WebWalking.walkTo(Tiles.bankTile);
		}
	}
}
