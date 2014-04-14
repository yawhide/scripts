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

import scripts.BDK.Data.Constants;
import scripts.BDK.Utilities.Conditionals;
import scripts.BDK.Utilities.YawsGeneral;


public class Pathing {
	
	public static boolean gotoDrag(){
		if (inArea(blueDragArea))
			return true;
		else if (inArea(lowWallArea)){
			BDK.FIGHT_STATUS = "doing low wall";
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
				BDK.FIGHT_STATUS = "walking to low wall";
				WebWalking.walkTo(lowWall);
				waitIsMovin();
			}
			else if(inArea(bankArea)){
				BDK.FIGHT_STATUS = "banking";
				bank();
			}
			else{
				BDK.FIGHT_STATUS = "walking to bank";
				WebWalking.walkTo(bankT);
				waitIsMovin();
			}
		}
		else if (inArea(tavladderArea)){
			BDK.FIGHT_STATUS = "clicking down ladder";
			RSObject[] TAVLAD = Objects.getAt(tavernlyLadderT);
			if (TAVLAD.length > 0){
				if(TAVLAD[0].isOnScreen()){
					if(TAVLAD[0].click("Climb-down")){
						sleep(4000,5000);
					}
				}
				else{
					BDK.FIGHT_STATUS = "walking to down ladder";
					Walking.clickTileMM(TAVLAD[0].getPosition(), 1);
					waitIsMovin();
				}
			}
		}
		else if (inArea(toTavLadderDownArea)){
			BDK.FIGHT_STATUS = "walking to tavernly";
			Walking.walkPath(toTavernlyDungPath);
			waitIsMovin();
		}
		else if (inArea(afterTavernlyLadderArea)){
			BDK.FIGHT_STATUS = "doing pipe";
			RSObject[] pipe = Objects.findNearest(7, "Obstacle pipe");
			if(pipe.length > 0){
				if(pipe[0].isOnScreen()){
					if(Clicking.click("Squeeze-through", pipe[0])){
						Conditionals.waitFor(inArea(blueDragArea), 5000, 6000);
					}
				}
				else{
					Walking.clickTileMM(pipe[0].getPosition(), 1);
					YawsGeneral.waitIsMovin();
				}
			}
		}
		else{
			General.println("we are somewhere unsupported, gonna web walk to bank");
			WebWalking.walkTo(bankT);
			YawsGeneral.waitIsMovin();
		}
		return false;
	}
	
	public static void gotoSafeSpot(){
		BDK.FIGHT_STATUS = "going to safe spot";
		Walking.setWalkingTimeout(1500L);
		if(Constants.SAFESPOT_TILE.getPosition().isOnScreen()){
			General.println("clicking on screen");
			Walking.clickTileMS(Constants.SAFESPOT_TILE, 1);
		}
		else if (YawsGeneral.pos().distanceTo(Constants.SAFESPOT_TILE) >= 7){
			General.println("generating a path");
			Walking.walkPath(Walking.generateStraightPath(Constants.SAFESPOT_TILE)); //safeSpotPath);//
		}
		else{
			General.println("clicking minimap");
			Walking.clickTileMM(Constants.SAFESPOT_TILE, 1);
		}	
		General.println("clicked, now gonna wait till I am not moving anymore");
		YawsGeneral.waitIsMovin();
	}
}
