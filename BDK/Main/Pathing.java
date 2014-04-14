package scripts.BDK.Main;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;

import scripts.BDK.Data.Constants;
import scripts.BDK.Utilities.Bank;
import scripts.BDK.Utilities.Conditionals;
import scripts.BDK.Utilities.YawsGeneral;


public class Pathing {
	
	public static boolean gotoDrag(){
		if (YawsGeneral.inArea(Constants.BLUE_DRAG_AREA))
			return true;
		else if (YawsGeneral.inArea(Constants.LOWWALL_AREA)){
			BDK.FIGHT_STATUS = "doing low wall";
			RSObject[] LOWWALL = Objects.getAt(Constants.LOWWALL_TILE);
			if (LOWWALL.length > 0){
				if(Clicking.click("Climb-over", LOWWALL[0])){
					Conditionals.waitFor(YawsGeneral.inArea(Constants.TO_TAV_LADDER_AREA), 3000, 4000);
				}
			}
		}
		else if (YawsGeneral.inArea(Constants.FALLY_AREA)){
			Walking.setWalkingTimeout(1000L);
			if(Inventory.find(BDK.FOOD_IDS).length == BDK.FOOD_NUM && Inventory.find("Falador teleport").length > 0
					&& Inventory.find(Constants.LOOT).length == 0 && Inventory.find(Constants.RANGE_POTS).length > 0) {
				BDK.FIGHT_STATUS = "walking to low wall";
				WebWalking.walkTo(Constants.LOWWALL_TILE);
				YawsGeneral.waitIsMovin();
			}
			else if(YawsGeneral.inArea(Constants.FALLY_BANK_AREA)){
				BDK.FIGHT_STATUS = "banking";
				Bank.bank();
			}
			else{
				BDK.FIGHT_STATUS = "walking to bank";
				WebWalking.walkTo(Constants.FALLY_BANK_TILE);
				YawsGeneral.waitIsMovin();
			}
		}
		else if (YawsGeneral.inArea(Constants.TAV_LADDER_AREA)){
			BDK.FIGHT_STATUS = "clicking down ladder";
			RSObject[] TAVLAD = Objects.getAt(Constants.TAV_LADDER_TILE);
			if (TAVLAD.length > 0){
				if(TAVLAD[0].isOnScreen()){
					if(Clicking.click("Climb-down", TAVLAD[0])){
						Conditionals.waitFor(YawsGeneral.inArea(Constants.TAV_DUNG_LADDER_AREA), 4000,5000);
					}
				}
				else{
					BDK.FIGHT_STATUS = "walking to down ladder";
					Walking.clickTileMM(TAVLAD[0].getPosition(), 1);
					YawsGeneral.waitIsMovin();
				}
			}
		}
		else if (YawsGeneral.inArea(Constants.TO_TAV_LADDER_AREA)){
			BDK.FIGHT_STATUS = "walking to tavernly";
			Walking.walkPath(Constants.TO_TAVERNLY_DUNGEON_PATH);
			YawsGeneral.waitIsMovin();
		}
		else if (YawsGeneral.inArea(Constants.TAV_DUNG_LADDER_AREA)){
			BDK.FIGHT_STATUS = "doing pipe";
			RSObject[] pipe = Objects.findNearest(7, "Obstacle pipe");
			if(pipe.length > 0){
				if(pipe[0].isOnScreen()){
					if(Clicking.click("Squeeze-through", pipe[0])){
						Conditionals.waitFor(YawsGeneral.inArea(Constants.BLUE_DRAG_AREA), 5000, 6000);
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
			WebWalking.walkTo(Constants.FALLY_BANK_TILE);
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
