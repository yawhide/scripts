package scripts.BDK.Main;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;


public class Pathing {
	
	public boolean gotoDrag(){
		if (inArea(blueDragArea))
			return true;
		else if (inArea(lowWallArea)){
			FIGHT_STATUS = "doing low wall";
			RSObject[] LOWWALL = Objects.getAt(lowWallT);
			if (LOWWALL.length > 0){
				if(LOWWALL[0].click("Climb-over")){
					sleep(5000,6000);
				}
			}
		}
		else if (inArea(fallyArea)){
			Walking.setWalkingTimeout(1000L);
			if(Inventory.find(FOOD_IDS).length == FOOD_NUM && Inventory.find("Falador teleport").length > 0
					&& Inventory.find(loot).length == 0 && Inventory.find(rangepots).length > 0) {
				FIGHT_STATUS = "walking to low wall";
				WebWalking.walkTo(lowWall);
				waitIsMovin();
			}
			else if(inArea(bankArea)){
				FIGHT_STATUS = "banking";
				bank();
			}
			else{
				FIGHT_STATUS = "walking to bank";
				WebWalking.walkTo(bankT);
				waitIsMovin();
			}
		}
		else if (inArea(tavladderArea)){
			FIGHT_STATUS = "clicking down ladder";
			RSObject[] TAVLAD = Objects.getAt(tavernlyLadderT);
			if (TAVLAD.length > 0){
				if(TAVLAD[0].isOnScreen()){
					if(TAVLAD[0].click("Climb-down")){
						sleep(4000,5000);
					}
				}
				else{
					FIGHT_STATUS = "walking to down ladder";
					Walking.clickTileMM(TAVLAD[0].getPosition(), 1);
					waitIsMovin();
				}
			}
		}
		else if (inArea(toTavLadderDownArea)){
			FIGHT_STATUS = "walking to tavernly";
			Walking.walkPath(toTavernlyDungPath);
			waitIsMovin();
		}
		else if (inArea(afterTavernlyLadderArea)){
			FIGHT_STATUS = "doing pipe";
			RSObject[] pipe = Objects.findNearest(7, "Obstacle pipe");
			if(pipe.length > 0){
				if(pipe[0].isOnScreen()){
					if(pipe[0].click("Squeeze-through")){
						Timing.waitCondition(new Condition() {
							;
							@Override
	    					public boolean active() {
	    						return inArea(blueDragArea);
	    					}
	    				}, General.random(5000, 6000));
					}
				}
				else{
					Walking.clickTileMM(pipe[0].getPosition(), 1);
					waitIsMovin();
				}
			}
		}
		else{
			println("we are somewhere unsupported, gonna web walk to bank");
			WebWalking.walkTo(bankT);
			waitIsMovin();
		}
		return false;
	}
}
