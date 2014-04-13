package scripts.BDK.Utilities;

import java.awt.Point;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;


public class Attack {

	public void fight(){
		RSNPC[] drag = NPCs.findNearest(dragIDs);
		RSNPC[] dragWithBaby = NPCs.findNearest(dragWithBabyArr);
		Walking.setWalkingTimeout(1500L);
		
		if(Inventory.isFull() || (Inventory.getAll().length == 27 && !isLoot())){
			println("Inv is full");
			isFull();
		}
		else if(isLoot()){
			println("looting...");
			LOOT();
		}
		else if (inCombat() && getHp() > 50){
			println("under attack somewhere");
			FIGHT_STATUS = "underattack";
			if (dragWithBaby.length > 0){
				for(RSNPC d : dragWithBaby){
					if(d.isInteractingWithMe()){
						if(!inSafeSpot()){
							println("under attack, not in safe spot");
							gotoSafeSpot();
						}
						else if(isRanging()){
							println("under attack, is ranging");
							sleep(1000,1200);
						}
						else{
							println("attack baby dragon");
							if(d.getPosition().isOnScreen()){
								if (clickNPC(d, "Attack")) {
									final RSNPC tmp_blkdrag = d;
									Timing.waitCondition(new Condition() {
										public boolean active() {
											return Player.getAnimation() == 4230
													|| inCombat()
													|| tmp_blkdrag.isInCombat()
													|| !inSafeSpot();
										}
									}, General.random(2200, 2400));
									for (int i = 0; i < 57; i++, sleep(30, 40)) {
										if (!inSafeSpot()) {
											println("attacked the dragon, running to safety!");
											gotoSafeSpot();
											break;
										}
									}
								}
							}
							else{
								Camera.turnToTile(d.getPosition());
								Camera.setCameraAngle(General.random(50, 70));
							}
						}
						break;
					}
					else if(!inSafeSpot()){
						println("under attack, not in safe spot");
						gotoSafeSpot();
					}
				}
			}
			else{
				gotoSafeSpot();
				waitIsMovin();
			}
		}
		else if (inSafeSpot()){
			if (NPCChat.clickContinue(true)){
				println("clicking to continue");
			}
			else if (Inventory.getCount(rangepots) > 0
					&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
							.getActualLevel(SKILLS.RANGED) + 2) {

				println(Skills.getCurrentLevel(SKILLS.RANGED) + "  "
						+ Skills.getActualLevel(SKILLS.RANGED));
				FIGHT_STATUS = "Potting up";
				drinkPotion(rangepots);
				sleep(1000,1200);
				println("Potted up");
			}
			else if(isRanging()){
				waitForLoot();
				
				//sleep(2000,4000);
			}
			else{
				if(drag.length > 0){
					if(Camera.getCameraRotation() < 100 || Camera.getCameraRotation() > 270){
						Camera.setCameraRotation(General.random(140, 180));
					}
					RSNPC d = drag[0];
					if (!d.isInCombat() || (d.isInCombat() && d.isInteractingWithMe()) || d.isInteractingWithMe()) {
						println("d not in combat, or is interacting with me or is in combat and interacting with me");
						FIGHT_STATUS = "attacking dragon " + d.getID();
						if(d.getPosition().isOnScreen()){
							if (clickNPC(d, "Attack")) {
								final RSNPC tmp_blkdrag = d;
								Timing.waitCondition(new Condition() {
									public boolean active() {
										return Player.getAnimation() == 4230
												|| inCombat()
												|| tmp_blkdrag.isInCombat()
												|| !inSafeSpot();
									}
								}, General.random(2200, 2400));
								for (int i = 0; i < 57; i++, sleep(30, 40)) {
									if (!inSafeSpot()) {
										println("attacked the dragon, running to safety!");
										gotoSafeSpot();
										break;
									}
								}
							}
							else{
								sleep(500,700);
							}
						}
						else {
							println("turning to face dragon");
							Camera.turnToTile(d.getPosition());
							Camera.setCameraAngle(General.random(50, 70));
						}
					}
					else if (drag.length > 1){
						d = drag[1];
						if (!d.isInCombat() || (d.isInCombat() && d.isInteractingWithMe()) || d.isInteractingWithMe()) {
							println("d not in combat, or is interacting with me or is in combat and interacting with me");
							FIGHT_STATUS = "attacking dragon " + d.getID();
							if(d.getPosition().isOnScreen()){
								if (clickNPC(d, "Attack")) {
									final RSNPC tmp_blkdrag = d;
									Timing.waitCondition(new Condition() {
										public boolean active() {
											return Player.getAnimation() == 4230
													|| inCombat()
													|| tmp_blkdrag.isInCombat()
													|| !inSafeSpot();
										}
									}, General.random(2200, 2400));
									for (int i = 0; i < 57; i++, sleep(30, 40)) {
										if (!inSafeSpot()) {
											println("attacked the dragon, running to safety!");
											gotoSafeSpot();
											break;
										}
									}
								}
								else{
									sleep(500,700);
								}
							}
							else {
								println("turning to face dragon");
								Camera.turnToTile(d.getPosition());
								Camera.setCameraAngle(General.random(50, 70));
							}
						}
					}
				}
			}
		}
		else if (!inSafeSpot()){
			println("end case, not in safespot");
			gotoSafeSpot();
		}
		else{
			println("doing nothing...");
			sleep(1000);
			//println("else case...");
			//gotoSafeSpot();
		}
	}

}
