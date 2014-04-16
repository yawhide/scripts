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
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.Avies.Data.Constants;
import scripts.Avies.Data.Tiles;
import scripts.Avies.Main.Avies;

public class Attack {

	public static void fight() {

		Walking.setControlClick(true);
		Walking.setWalkingTimeout(1500L);

		if (YawsGeneral.getHp() <= 30) {
			YawsGeneral.heal();
			
		} else if (Inventory.isFull()
				|| (Inventory.getAll().length > 26 && !Looting.lootExists())) {
			YawsGeneral.isInvFull();
			
		} else if (Looting.lootExists()) {
			Looting.loot();
			
		} else if (Inventory.getCount(Constants.RANGE_POT) > 0
				&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
						.getActualLevel(SKILLS.RANGED) + 2) {
			drinkPotion(Constants.RANGE_POT);
			
		} else if (Prayer.getCurrentPrayers().length > 0) {
			Pray.disableAllPrayers();
			
		} else if (YawsGeneral.isRanging()) {
			checkTargetIsValid();
			
		} else if (Inventory.find(Constants.ALC_LOOT).length > 0) {
			YawsGeneral.alc(Constants.ALC_LOOT);
			
		} else {
			attackAvie();
			
		}
	}

	public static void checkTargetIsValid() {
		if (Combat.getTargetEntity() != null
				&& !Combat.getTargetEntity().getName().equals("Aviansie")) {
			Avies.runAwayFromMonster = true;
		}
	}

	public static void attackAvie() {
		RSNPC[] avies = NPCs.findNearest("Aviansie");
		for (RSNPC a : avies) {
			if (Tiles.AVIES_AREA.contains(a.getPosition())
					&& (a.getCombatLevel() == 69 || a.getCombatLevel() == 71 || a
							.getCombatLevel() == 83)) {
				if (a.isOnScreen()) {
					if (Clicking.click("Attack", a)) {// clickObject(a,
														// "Attack")) {//
						Avies.fightStatus = "killing an avie";
						Conditionals.waitFor(Player.getAnimation() == 4230,
								3000, 3200);
					}
				} else {
					General.println("running to closest avie");
					Walking.clickTileMM(a.getAnimablePosition(), 1);
					Camera.turnToTile(a.getPosition());
					General.sleep(300, 400); // can't really put a conditional
												// wait
					// here cuz i can't get the rotation
				}
				break;
			}
		}
	}

	public static void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills
				.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if (Clicking.click("Drink", potion[0])) {
					Conditionals.waitFor(
							Skills.getCurrentLevel(SKILLS.RANGED) > Skills
									.getActualLevel(SKILLS.RANGED), 1000, 1200);
				}
			}
		}
	}

}
