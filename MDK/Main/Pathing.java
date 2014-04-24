package scripts.MDK.Main;

import java.awt.Point;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSObject;

import scripts.MDK.Data.Tiles;
import scripts.MDK.Utilities.Utils;

public class Pathing {
	public void checkStatus(){
		if(Tiles.lumbyArea.contains(pos()) && !haveGear())
			MithDK.bankStatus = true;
		
	}
	
	
	
	if(bankStatus){
		if(Camera.getCameraAngle() < 90)
			Camera.setCameraAngle(General.random(90,  100));
		if(Skills.getCurrentLevel(SKILLS.PRAYER) < Skills.getActualLevel(SKILLS.PRAYER)){
			
			turnOffAllPrayers();
			
			if (pos().distanceTo(Tiles.altar) <= 3){
				println("near altar");
				prayAtAltar();
			}
			else if (Tiles.churchA.contains(pos())){
				println("in church");
				gotoAltar();
			}
			else if (pos().distanceTo(Tiles.churchDoor) <=4){
				println("opening the door to the church");
				if(openDoor(Tiles.churchDoor))
					gotoAltar();
			}
			else{
				WebWalking.walkTo(Tiles.churchDoor);
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
			else if(pos().distanceTo(Tiles.chest) <= 4)
				openChest();
			else if (Tiles.chestA.contains(pos()))
				gotoChest();
			else if (pos().distanceTo(Tiles.trapDoor) <=4)
				goDownTrapDoorLumby();
			else{
				WebWalking.walkTo(Tiles.trapDoor);
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
		
		
		if(pos().distanceTo(Tiles.whirlpool) <= 4 && pos().getPlane() == 0){
			useWhirpool();
		}
		else if (inArea2(Tiles.toWhirlpoolA2) && pos().getPlane() == 0){//toWhirlpoolA.contains(pos())){
			gotoWhirlpool();
		}
		
		else if (inArea2(Tiles.afterWhirlpool2)){//afterWhirlpool.contains(pos()) 
				//|| (pos().distanceTo(stepsDownToGD) <=2 && pos().getPlane() == 1)){
			checkToPot();
			checkToPotAntiFire();
			stepDownToGD();
		}
		else if (pos().distanceTo(Tiles.stepsUpToMD) <= 4 && pos().getPlane() == 0){
			//println("close to the steps up to miths");
			stepUpToMD();
		}
		else if (inArea2(Tiles.greenDragArea2)){ //greenDragArea.contains(pos()) && pos().getPlane() == 0){
			//println("in green dragon area");
			checkToPotAntiFire();
			activateProtectMage();
			gotoMith();
		}
		else if (inArea2(Tiles.mithDragSpawn12)){//mithDragSpawn1.contains(pos()) && pos().getPlane() == 1){
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
	
	public void stepDownToGD(){
		if(Camera.getCameraAngle() < 90){
			Camera.setCameraAngle(General.random(90, 100));
			sleep(200,300);
		}
		RSObject[] STAIRS = Objects.getAt(Tiles.stepsDownToGD);
		if (STAIRS.length > 0){
			if(STAIRS[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(STAIRS[0],  "Climb-down")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return !Tiles.afterWhirlpool.contains(pos());
						}
					}, General.random(2000, 2500));
				}
					
			}
			else{
				Camera.turnToTile(Tiles.stepsDownToGD);
				sleep(200,300);
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90, 100));
					sleep(200,300);
				}
			}
		}
	}
	
	public void stepUpToMD(){
		RSObject[] STAIRS = Objects.getAt(Tiles.stepsUpToMD);
		if (STAIRS.length > 0){
			if(STAIRS[0].isOnScreen()){
				if(DynamicClicking.clickRSObject(STAIRS[0],  "Climb-up")){
					Timing.waitCondition(new Condition() {
						;
						@Override
						public boolean active() {
							return !Tiles.greenDragArea.contains(pos());
						}
					}, General.random(2000, 2500));
				}
					
			}
			else{
				Camera.turnToTile(Tiles.stepsDownToGD);
				sleep(200,300);
				if(Camera.getCameraAngle() < 90){
					Camera.setCameraAngle(General.random(90, 100));
					sleep(200,300);
				}
			}
		}
	}
	
	public void goDownTrapDoorLumby(){
		RSObject[] TRAP_DOOR = Objects.getAt(Tiles.trapDoor);
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
		RSObject[] ALTAR = Objects.getAt(Tiles.altar);
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
		Walking.walkPath(Tiles.toChest);
		Utils.waitIsMovin();
	}
	
	public void gotoAltar(){
		Walking.walkPath(Tiles.toAltar);
		Utils.waitIsMovin();
	}
	
	public void gotoWhirlpool(){
		Walking.walkPath(Tiles.toWhirlpool);
		Utils.waitIsMovin();
	}
	
	public void gotoMith(){
		Walking.walkPath(Tiles.toMith);
		Utils.waitIsMovin();
	}
	
	public void useWhirpool(){
		Point p = Projection.tileToScreen(Tiles.whirlpoolT, 0);
		
		Camera.turnToTile(Tiles.whirlpoolT);
		sleep(200,300);
		Camera.setCameraAngle(Camera.getTileAngle(Tiles.whirlpoolT));
		
		Mouse.move(p);
		sleep(100,150);
		if(Game.isUptext("Dive in Whirlpool")){
			Mouse.click(1);
			sleep(100,150);
			Timing.waitCondition(new Condition() {
				;
				@Override
				public boolean active() {
					return !Tiles.afterWhirlpool.contains(pos());
				}
			}, General.random(5000, 6000));
			sleep(5000,6200);
		}
	}
	
}
