package scripts.Avies.Utilities;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;

import scripts.Avies.Data.Constants;
import scripts.Avies.Main.Avies;

public class Attack {

	public static void fight() {

		Walking.setControlClick(true);
		Walking.setWalkingTimeout(1500L);

		if (YawsGeneral.getHp() <= 30) {
			YawsGeneral.heal();
		} else if (Inventory.isFull()
				|| (Inventory.getAll().length > 26 && !YawsGeneral.lootExists())) {
			General.println("Inv is full");
			YawsGeneral.isInvFull();
		} else if (YawsGeneral.lootExists()) {
			General.println("looting...");
			YawsGeneral.loot();
		} else if (Inventory.getCount(Constants.RANGE_POT) > 0
				&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
						.getActualLevel(SKILLS.RANGED) + 2) {

			General.println(Skills.getCurrentLevel(SKILLS.RANGED) + "  "
					+ Skills.getActualLevel(SKILLS.RANGED));
			Avies.FIGHT_STATUS = "Potting up";
			General.println("Potted up");
			YawsGeneral.drinkPotion(Constants.RANGE_POT);
		} else if (Prayer.getCurrentPrayers().length > 0) {
			PRAYERS[] p = Prayer.getCurrentPrayers();
			if (!Prayer.isTabOpen()) {
				YawsGeneral.openTab(TABS.PRAYERS);
			}
			for (int i = 0; i < p.length; i++) {
				final PRAYERS currentPrayer = p[i];
				Prayer.disable(currentPrayer);
				Conditionals.waitFor(!Prayer.isPrayerEnabled(currentPrayer), 400, 500);
			}
		} else if (YawsGeneral.isRanging()) {
			if (Combat.getTargetEntity() != null
					&& !Combat.getTargetEntity().getName().equals("Aviansie")) {
				Avies.MOVE_RANDOM = true;
			}
		} else if (Inventory.find(Constants.ALC_LOOT).length > 0) {
			YawsGeneral.alc(Constants.ALC_LOOT);
		} else {
			RSNPC[] avies = NPCs.findNearest("Aviansie");
			for (RSNPC a : avies) {
				if (Constants.AVIES_AREA.contains(a.getPosition())
						&& (a.getCombatLevel() == 69
								|| a.getCombatLevel() == 71 
								|| a.getCombatLevel() == 83)) {
					if (a.isOnScreen()) {
						if (Clicking.click("Attack", a)){//clickObject(a, "Attack")) {// 
							Avies.FIGHT_STATUS = "killing an avie";
							Conditionals.waitFor(Player.getAnimation() == 4230, 3000, 3200);
						}
					} else {
						General.println("running to closest avie");
						Walking.clickTileMM(a.getAnimablePosition(), 1);
						Camera.turnToTile(a.getPosition());
						General.sleep(300, 400); // can't really put a conditional wait
											// here cuz i can't get the rotation
					}
					break;
				}
			}
		}
	}

}
