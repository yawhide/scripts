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
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.BDK.Data.Constants;
import scripts.BDK.Main.BDK;
import scripts.BDK.Main.Pathing;


public class Attack {

	public static void fight(){
		RSNPC[] drag = NPCs.findNearest(Constants.BLUE_DRAGON_IDS);
		RSNPC[] dragWithBaby = NPCs.findNearest(Constants.DRAGON_IDS);
		Walking.setWalkingTimeout(1500L);
		
		if(YawsGeneral.getHp() < 50)
			YawsGeneral.heal();
		else if(Inventory.isFull() || (Inventory.getAll().length == 27 && !YawsGeneral.lootExists())){
			General.println("Inv is full");
			YawsGeneral.isInvFull();
		}
		else if(YawsGeneral.lootExists()){
			General.println("looting...");
			YawsGeneral.loot();
		}
		else if (YawsGeneral.inCombat() && YawsGeneral.getHp() > 50){
			General.println("under attack somewhere");
			BDK.FIGHT_STATUS = "underattack";
			if (dragWithBaby.length > 0){
				for(RSNPC d : dragWithBaby){
					if(d.isInteractingWithMe()){
						if(!YawsGeneral.inSafeSpot()){
							General.println("under attack, not in safe spot");
							Pathing.goToSafeSpot();
						}
						else if(YawsGeneral.isRanging()){
							General.println("under attack by baby drag, is ranging");
							General.sleep(1000,1200);
						}
						else{
							General.println("attack baby dragon");
							if(d.getPosition().isOnScreen()){
								if (clickNPC(d, "Attack")){//Clicking.click("Attack", d)){//clickNPC(d, "Attack")) {
									final RSNPC tmp_blkdrag = d;
									Conditionals.waitFor(Player.getAnimation() == 4230
													|| YawsGeneral.inCombat()
													|| tmp_blkdrag.isInCombat()
													|| !YawsGeneral.inSafeSpot(), 2200, 2400);
									if (!YawsGeneral.inSafeSpot()) {
										General.println("attacked the dragon, running to safety!");
										Pathing.goToSafeSpot();
										Conditionals.waitFor(YawsGeneral.inSafeSpot(), 3000,4000);
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
					else if(!YawsGeneral.inSafeSpot()){
						General.println("under attack, not in safe spot");
						Pathing.goToSafeSpot();
					}
				}
			}
			else{
				Pathing.goToSafeSpot();
				YawsGeneral.waitIsMovin();
			}
		}
		else if (YawsGeneral.inSafeSpot()){
			if (NPCChat.clickContinue(true)){
				General.println("clicking to continue");
			}
			else if (Inventory.getCount(Constants.RANGE_POTS) > 0
					&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
							.getActualLevel(SKILLS.RANGED) + 2) {

				General.println(Skills.getCurrentLevel(SKILLS.RANGED) + "  "
						+ Skills.getActualLevel(SKILLS.RANGED));
				BDK.FIGHT_STATUS = "Potting up";
				YawsGeneral.drinkPotion(Constants.RANGE_POTS);
				Conditionals.waitFor(Skills.getCurrentLevel(SKILLS.RANGED) > Skills.getActualLevel(SKILLS.RANGED), 1000, 1200);
				General.println("Potted up");
			}
			else if(YawsGeneral.isRanging()){
				YawsGeneral.waitForLoot();
				
				//sleep(2000,4000);
			}
			else{
				if(drag.length > 0){
					if(Camera.getCameraRotation() < 100 || Camera.getCameraRotation() > 270){
						Camera.setCameraRotation(General.random(140, 180));
					}
					for(RSNPC d : drag){
						if (!d.isInCombat()
								|| (d.isInCombat() && d.isInteractingWithMe())
								|| d.isInteractingWithMe()) {
							General.println("d not in combat, or is interacting with me or is in combat and interacting with me");
							BDK.FIGHT_STATUS = "attacking dragon " + d.getID();
							if (d.getPosition().isOnScreen()) {
								if (clickNPC(d, "Attack")){//Clicking.click("Attack", d)){//clickNPC(d, "Attack")) {
									final RSNPC tmp_blkdrag = d;
									Conditionals.waitFor(
											Player.getAnimation() == 4230
													|| YawsGeneral.inCombat()
													|| tmp_blkdrag.isInCombat()
													|| !YawsGeneral.inSafeSpot(), 2200,	2400);
									if (!YawsGeneral.inSafeSpot()) {
										General.println("attacked the dragon, running to safety!");
										Pathing.goToSafeSpot();
										Conditionals.waitFor(
												YawsGeneral.inSafeSpot(), 3000,
												4000);
									}
								}
							} else {
								General.println("turning to face dragon");
								Camera.turnToTile(d.getPosition());
								Camera.setCameraAngle(General.random(50, 70));
							}
						}
					}
				}
			}
		}
		else if (!YawsGeneral.inSafeSpot()){
			General.println("end case, not in safespot");
			Pathing.goToSafeSpot();
		}
		else{
			General.println("doing nothing...");
			General.sleep(1000);
			//println("else case...");
			//gotoSafeSpot();
		}
	}
	
	public static boolean clickNPC(RSNPC npc, String option) {

		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			loc = new RSTile((npc.getID() != Constants.EAST_DRAGON_ID ? npc.getPosition().getX()-1 : npc.getPosition().getX()), npc.getPosition().getY()-1);
			
			Mouse.move(Projection.tileToScreen(loc, 10));
			if(Game.isUptext("Walk here / 2 more options")){
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
			else if (Game.isUptext(option)) {
				Mouse.click(1);
				YawsGeneral.waitForDrag(npc);
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

}
