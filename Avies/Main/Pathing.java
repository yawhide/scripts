package scripts.Avies.Main;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;

import scripts.Avies.Data.Tiles;
import scripts.Avies.Utilities.Bank;
import scripts.Avies.Utilities.Conditionals;
import scripts.Avies.Utilities.Pray;
import scripts.Avies.Utilities.YawsGeneral;

public class Pathing {

	public static boolean goToAvies() {

		if (YawsGeneral.inArea(Tiles.AVIES_AREA)
				&& YawsGeneral.pos().getPlane() == 2)
			return true;
		else if (!YawsGeneral.inArea(Tiles.VARROCK_AREA)
				&& Objects.findNearest(50, "Portal").length > 0) {
			YawsGeneral.prayAtHouseAltar();
			
		} else if (YawsGeneral.inGW()) {
			Avies.fightStatus = "walking to avies spot";
			Walking.walkPath(Walking
					.generateStraightPath(Tiles.TO_AVIES[Tiles.TO_AVIES.length - 1]));
			
		} else if (YawsGeneral.pos().distanceTo(
				Tiles.TO_GWD[Tiles.TO_GWD.length - 1]) <= 5) {
			Avies.fightStatus = "walking to GW hole";
			if (Clicking.click("Climb-down", Tiles.HOLE_TILE.getPosition())) {
				Conditionals.waitFor(YawsGeneral.inGW(), 2000, 3000);
			}
			
		} else if (YawsGeneral.aroundPath(Tiles.TO_GWD)) {
			Avies.fightStatus = "walking before the range trolls";
			Walking.walkPath(Tiles.TO_GWD);

		} else if (YawsGeneral.pos().distanceTo(
				Tiles.BEFORE_BOULDER[Tiles.BEFORE_BOULDER.length - 1]) <= 5) {
			Avies.fightStatus = "prayer off, walking to boulder";
			Pray.turnOffPrayerProtectionMissles();
			if (Clicking.click(Objects.findNearest(7, "Boulder"))) {
				Conditionals.waitFor(YawsGeneral.aroundPath(Tiles.TO_GWD),
						7000, 8000);
			}
			
		} else if (YawsGeneral.inArea(Tiles.ROCK_SLIDE_AREA)) {
			Avies.fightStatus = "near first slide";
			if (Clicking.click(Objects.findNearest(7, "Rocks"))) {
				Conditionals.waitFor(
						YawsGeneral.aroundPath(Tiles.BEFORE_PRAY_RANGE_SPOT),
						3500, 4500);
			}
			
		} else if (YawsGeneral.inArea(Tiles.TROLL_TELEPORT_AREA)) {
			Avies.fightStatus = "walking to first slide";
			Walking.walkPath(Tiles.TO_FIRST_SLIDE);
			
		} else if (YawsGeneral.aroundPath(Tiles.BEFORE_BOULDER)) {
			Avies.fightStatus = "walking by trolls";
			Pray.turnOffPrayerProtectionMissles();
			Walking.walkPath(Tiles.BEFORE_BOULDER);
			
		} else if (YawsGeneral.aroundPath(Tiles.BEFORE_PRAY_RANGE_SPOT)) {
			Avies.fightStatus = "walking before the range trolls";
			Walking.walkPath(Tiles.BEFORE_PRAY_RANGE_SPOT);
			
		} else if (YawsGeneral.inArea(Tiles.VARROCK_AREA)) {
			checkEquipment();
			
		}
		YawsGeneral.waitIsMovin();
		return false;
	}

	public static void checkEquipment() {
		Avies.fightStatus = "in varrock, banking...";
		if (!YawsGeneral.gotEquipment()) {
			walkToBank();
		} else {
			YawsGeneral.teleToTroll();
		}
	}

	public static void goToAviesSpot() {
		Avies.fightStatus = "going to avvieArea";
		Walking.setWalkingTimeout(1500L);
		Walking.walkPath(Tiles.TO_AVIES);
		YawsGeneral.waitIsMovin();
	}

	public static void runAwayFromMonster() {
		if (YawsGeneral.pos().distanceTo(Tiles.RANDOM_EAST_TILE) > YawsGeneral
				.pos().distanceTo(Tiles.RANDOM_WEST_TILE)) {
			Walking.walkPath(Walking
					.generateStraightPath(Tiles.RANDOM_EAST_TILE));
		} else {
			Walking.walkPath(Walking
					.generateStraightPath(Tiles.RANDOM_WEST_TILE));

		}
		YawsGeneral.waitIsMovin();
		Avies.runAwayFromMonster = false;
	}

	public static void walkToBank() {
		if (YawsGeneral.pos().distanceTo(Tiles.BANK_TILE) <= 5) {
			Bank.openBank();
		} else {
			WebWalking.walkTo(Tiles.BANK_TILE);
		}
	}

}
