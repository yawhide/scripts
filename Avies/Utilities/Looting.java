package scripts.Avies.Utilities;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSGroundItem;

import scripts.Avies.Data.Constants;
import scripts.Avies.Data.Tiles;
import scripts.Avies.Main.Avies;

public class Looting {
	public static boolean lootExists() {
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT_WITHOUT_ARROWS);
		return Nests.length > 0
				&& Tiles.AVIES_AREA.contains(Nests[0].getPosition());
	}

	public static void loot() {
		Avies.fightStatus = "looting";
		Pray.turnOffPrayerEagle();
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT);

		for (int i = 0; i < Nests.length; i++) {
			Walking.setWalkingTimeout(1000L);
			if (Nests[i].getID() == Avies.boltsID) {
				if (Nests[i].getStack() > 5) {
					lootItem(Nests[i]);
				}
			} else {
				lootItem(Nests[i]);
			}
		}
	}

	// TODO fix waitForLoot
	public static void waitForLoot(RSCharacter avv) {
		Avies.fightStatus = "prayerflicking";
		if (!lootExists() && YawsGeneral.isRanging() && YawsGeneral.inAviesArea() && YawsGeneral.getHp() > 30) {
			Pray.prayerFlick();
		}
		Pray.turnOffPrayerEagle();
	}
	
	public static void lootItem(RSGroundItem item){
		if (!item.isOnScreen()) {
			Walking.walkPath(Walking.generateStraightPath(item
					.getPosition()));
			Camera.turnToTile(item.getPosition());
			Camera.setCameraAngle(General.random(90, 100));
			YawsGeneral.waitIsMovin();
		}
		String str = Constants.LOOT_MAPPING.get(item.getID());
		final int tmpCount = Inventory.getCount(item.getID());
		General.println(str);
		if (Clicking.click("Take " + str, item))
			Conditionals.waitForItem(item.getID(), tmpCount);
	}
}
