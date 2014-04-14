package scripts.Avies.Main;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.Avies.Data.Constants;
import scripts.Avies.Utilities.Bank;
import scripts.Avies.Utilities.Conditionals;
import scripts.Avies.Utilities.Pray;
import scripts.Avies.Utilities.YawsGeneral;

public class Pathing {
	
	public static boolean gotoAvies(){
	
		if (YawsGeneral.inArea(Constants.AVIES_AREA) && YawsGeneral.pos().getPlane() == 2)
			return true;
		else if(!YawsGeneral.inArea(Constants.VARROCK_AREA) && Objects.findNearest(50, "Portal").length > 0){
			YawsGeneral.doHouseStuff();
		}
		else if(YawsGeneral.inGW()){
			Avies.FIGHT_STATUS = "walking to avies spot";
			Walking.walkPath(Walking.generateStraightPath(Constants.TO_AVIES[Constants.TO_AVIES.length-1]));
			YawsGeneral.waitIsMovin();
		}
		else if (YawsGeneral.pos().distanceTo(Constants.TO_GWD[Constants.TO_GWD.length - 1]) <= 5) {
			Avies.FIGHT_STATUS = "walking to GW hole";
			if (Clicking.click("Climb-down", Constants.HOLE_TILE.getPosition())) {
				Conditionals.waitFor(YawsGeneral.inGW(), 2000, 3000);
			}
		}
		else if (YawsGeneral.aroundPath(Constants.TO_GWD)){
			Avies.FIGHT_STATUS = "walking before the range trolls";
			Walking.walkPath(Constants.TO_GWD);
			YawsGeneral.waitIsMovin();
		}
		else if (YawsGeneral.pos().distanceTo(Constants.BEFORE_BOULDER[Constants.BEFORE_BOULDER.length - 1]) <= 5) {
			Avies.FIGHT_STATUS = "prayer off, walking to boulder";
			Pray.turnOffPrayerProtectionMissles();
			if (Clicking.click(Objects.findNearest(7, "Boulder"))) {
				Conditionals.waitFor(YawsGeneral.aroundPath(Constants.TO_GWD), 7000, 8000);
			}
		}
		else if (YawsGeneral.inArea(Constants.ROCK_SLIDE_AREA)){
			Avies.FIGHT_STATUS = "near first slide";
			if(Clicking.click(Objects.findNearest(7, "Rocks"))){
				Conditionals.waitFor(YawsGeneral.aroundPath(Constants.BEFORE_PRAY_RANGE_SPOT), 3500, 4500);
			}
		}
		else if (YawsGeneral.inArea(Constants.TROLL_TELEPORT_AREA)){
			Avies.FIGHT_STATUS = "walking to first slide";
			Walking.walkPath(Constants.TO_FIRST_SLIDE);
			YawsGeneral.waitIsMovin();
		}
		else if (YawsGeneral.aroundPath(Constants.BEFORE_BOULDER)){
			Avies.FIGHT_STATUS = "walking by trolls";
			Pray.turnOffPrayerProtectionMissles();
			Walking.walkPath(Constants.BEFORE_BOULDER);
			YawsGeneral.waitIsMovin();
		}
		else if (YawsGeneral.aroundPath(Constants.BEFORE_PRAY_RANGE_SPOT)){
			Avies.FIGHT_STATUS = "walking before the range trolls";
			Walking.walkPath(Constants.BEFORE_PRAY_RANGE_SPOT);
			YawsGeneral.waitIsMovin();
		}
		else if (YawsGeneral.inArea(Constants.VARROCK_AREA)){
			Avies.FIGHT_STATUS = "in varrock, banking...";
			if(!YawsGeneral.gotEquipment()){
				if (YawsGeneral.pos().distanceTo(Constants.BANK_TILE) <= 5){
					General.println("banking...");
					Bank.openBank();
				}
				else{
					General.println("gonna web walk to bank");
					WebWalking.walkTo(Constants.BANK_TILE);
					YawsGeneral.waitIsMovin();
				}
			}
			else{
				YawsGeneral.teleToTroll();
			}
		}
		return false;
	}
	
	public static void gotoAviesSpot(){
		Avies.FIGHT_STATUS = "going to avvieArea";
		Walking.setWalkingTimeout(1500L);
		Walking.walkPath(Constants.TO_AVIES);
		YawsGeneral.waitIsMovin();
	}
}
