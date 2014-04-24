package scripts.MDK.Utilities;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;

import scripts.MDK.Data.Tiles;

public class Attack {
	// TODO fight
	public void fight() {
		
		RSNPC mith = findNearestDragInArea();

		if(Utils.timeToTab(mith))
			Utils.emergTele();

		// check for potting
		Utils.checkToPotAntiFire();
		Utils.checkToPot();

		// turning prayers on
		if (mith == null) {
			turnOffAllPrayers();
		} else {
			if (Skills.getCurrentLevel(SKILLS.PRAYER) < 10) {
				reactivatePrayer(1);
			} else {
				if (mith.isValid() && mith.isInteractingWithMe())
					activateProtectRanged();
				else if (mith.getAnimation() == 91)
					turnOffAllPrayers();
			}
		}

		// looting and checking bolts
		if (mith == null) {
			if (lootExists())
				lootOrder();
			else if (!lootExists()) {
				if (Inventory.find(foodIDs).length == 0 && getHp() < 75) {
					emergTele();
				} else
					gotoSafeSpot1();
			}
		}
		if (useSpecialBolts) {
			if (mithAtHalf(mith)) {
				equipDiamondBolts();
			} else {
				equipRubyBolts();
			}
		}

		// actually attacking the dragon and prayer flick
		if (mith != null && !inArea2(Tiles.safeSpot)) {// safeSpotSpawn1.contains(pos())){
			println("not in safe spot, lets go!");
			gotoSafeSpot1();
		} else if (mith != null && mith.isValid() && mith.isInteractingWithMe()
				&& mith.getAnimation() != 92) {
			if (Player.getRSPlayer().getInteractingCharacter() == null) {
				// println("I am not interacting with anyone");
				// println("Not interacting with dragon");
				activateProtectRanged();
				moveCamera(mith);
				// if(
				clickNPC(mith, "Attack");
				// ){
				// }
				sleep(1000, 1100);
			} else {
				if (mith != null && getHp() > 50 && pottedAntiFire)
					prayerflick(mith);
				else
					General.sleep(1000, 1200);
			}

		} else if (mith != null
				&& mith.isValid()
				&& !mith.isInteractingWithMe()
				&& mith.getAnimablePosition().distanceTo(Tiles.safeSpotSpawn1P) > 2
				&& mith.getAnimation() != 92) {

			activateProtectRanged();
			moveCamera(mith);
			// if(clickNPC(mith, "Attack"))
			clickNPC(mith, "Attack");
			sleep(1000, 1100);
		} else {/*
				 * if(getHp() > 50 && pottedAntiFire) prayerflick(mith); else
				 */
			General.sleep(1000, 1200);
		}

	}

	public static boolean mithAtHalf(RSNPC drag) {
		if (drag != null && drag.isInteractingWithMe())
			return drag.getHealth() <= 127 && drag.getHealth() >= 0;
		return false;
	}
	
	public static RSNPC findNearestDragInArea(){
		for(RSNPC drag : NPCs.findNearest("Mithril dragon")){
			if(Tiles.mithDragSpawn1.contains(drag.getAnimablePosition()) && (drag.isInteractingWithMe() || !drag.isInCombat())){
				return drag;
			}
		}
		return null;
	}
}
