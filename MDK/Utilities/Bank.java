package scripts.MDK.Utilities;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSObject;

import scripts.MDK.Data.Constants;
import scripts.MDK.Data.Tiles;
import scripts.MDK.Main.MDKGui;
import scripts.MDK.Main.MithDK;

public class Bank {

	public static boolean withdraw(int num, int[] ids) {
		final int count = Inventory.getCount(ids);
		if (Banking.withdraw(num, ids)) {
			Conditionals.waitForItem(ids, count);
			return true;
		}
		// } else if (Banking.find(ids).length == 0) {
		// General.println("Your bank does not have the item with ids: " + ids);
		// MithDK.mainLoopStatus = false;
		// }
		return false;
	}

	public static boolean withdraw(int num, int id) {
		final int count = Inventory.getCount(id);
		if (Banking.withdraw(num, id)) {
			Conditionals.waitForItem(id, count);
			return true;
		}
		// } else if (Banking.find(id).length == 0) {
		// General.println("Your bank does not have the item with id: " + id);
		// MithDK.mainLoopStatus = false;
		// }
		return false;
	}

	public static boolean withdraw(int num, String id) {
		final int count = Inventory.getCount(id);
		if (Banking.withdraw(num, id)) {
			Conditionals.waitForItem(id, count);
			return true;
		}
		// } else if (Banking.find(id).length == 0) {
		// General.println("Your bank does not have the item with id: " + id);
		// MithDK.mainLoopStatus = false;
		// }
		return false;
	}

	public static boolean withdraw(int num, String[] id) {
		final int count = Inventory.getCount(id);
		if (Banking.withdraw(num, id)) {
			Conditionals.waitForItem(id, count);
			return true;
		}
		// } else if (Banking.find(id).length == 0) {
		// General.println("Your bank does not have the item with id: " + id);
		// MithDK.mainLoopStatus = false;
		// }
		return false;
	}

	public static void openBank() {
		if (Banking.openBank()) {
			if (Banking.isPinScreenOpen())
				Banking.inPin();
			Conditionals.waitFor(Banking.isBankScreenOpen(), 3000, 4000);
			if (Banking.isBankScreenOpen())
				bank();
		}
	}

	public static void bank() {
		boolean needToOpenBankTwice = false;

		Banking.depositAll();
		Conditionals.waitFor(Inventory.getAll().length == 0, 400, 500);

		withdraw(1, Constants.RANGE_POT);
		withdraw(1, Constants.DEFENSE_POT);
		withdraw(1, Constants.ANTIFIRE_POT);
		withdraw(4, Constants.PRAYER_POT);
		withdraw(5, Constants.HOUSE_TABLET);
		withdraw(1, Constants.GAMES_NECKLACE);

		if (getMoreBoltsIfNeeded())
			needToOpenBankTwice = true;

		if (!Equipment.isEquipped(Constants.RING_OF_LIFE)) {
			withdraw(1, Constants.RING_OF_LIFE);
			needToOpenBankTwice = true;
		}

		if (Combat.getMaxHP() - (MDKGui.foodID == 385 ? 20 : 16) >= Combat
				.getHP()) {
			withdraw(
					(((Skills.getActualLevel(SKILLS.HITPOINTS) - Skills.getCurrentLevel(SKILLS.HITPOINTS)) / (MDKGui.foodID == 385 ? 20
							: 16))), MDKGui.foodID);
			needToOpenBankTwice = true;
		}

		if (needToOpenBankTwice && closeBank()) {
			Conditionals.waitFor(!Banking.isBankScreenOpen(), 2000, 3000);

			if (MDKGui.useRubyBolts)
				if (Inventory.getCount(Constants.RUBY_E_BOLT) > 0) {
					Clicking.click("Wield",
							Inventory.find(Constants.RUBY_E_BOLT));
				} else if (Inventory.getCount(MDKGui.boltsUsing) > 0) {
					Clicking.click("Wield", Inventory.find(MDKGui.boltsUsing));
				}

			if (Inventory.getCount(Constants.RING_OF_LIFE) > 0) {
				Clicking.click("Wear", Inventory.find(Constants.RING_OF_LIFE));
			}

			while (Inventory.getCount(MDKGui.foodID) > 0 || Utils.getHp() > 90) {
				final int i = Inventory.getCount(MDKGui.foodID);
				if (Clicking.click("Eat", Inventory.find(MDKGui.foodID)))
					Conditionals.waitForEating(i);
			}

			if (Banking.openBank()) {
				Conditionals.waitFor(Banking.isBankScreenOpen(), 2000, 3000);
				General.println(MDKGui.foodAmount + " " + MDKGui.foodID);
				withdraw(20, MDKGui.foodUsing);
				Banking.close();
			}
		}
	}

	public static boolean openChest() {
		RSObject[] CHEST = Objects.getAt(Tiles.chest);
		if (CHEST.length > 0) {
			if (CHEST[0].isOnScreen()) {
				if (Clicking.click("Bank", CHEST[0])) {
					Conditionals.waitFor(
							Banking.isPinScreenOpen()
									|| Banking.isBankScreenOpen(), 4000, 4500);
					if (Banking.isBankScreenOpen() || Banking.isPinScreenOpen())
						return true;
				}
			}
		}
		return false;
	}

	public static boolean getMoreBoltsIfNeeded() {
		int rubyBoltCount = Equipment.getCount(Constants.RUBY_E_BOLT)
				+ Inventory.getCount(Constants.RUBY_E_BOLT);
		int boltCount = Equipment.getCount(MDKGui.boltsUsing)
				+ Inventory.getCount(MDKGui.boltsUsing);
		boolean didWithdrawBolts = false;
		if (MDKGui.useRubyBolts) {
			if (rubyBoltCount < 200) {
				withdraw(300 - rubyBoltCount, Constants.RUBY_E_BOLT);
				didWithdrawBolts = true;
			}
		}
		if (boltCount < 200) {
			if (boltCount < 200) {
				withdraw(300 - boltCount, MDKGui.boltsUsing);
				if (!MDKGui.useRubyBolts)
					didWithdrawBolts = true;
			}
		}
		return didWithdrawBolts;
	}

	public static boolean closeBank() {
		return (Banking.close());
	}

	// public static void checkitemsInBank(){
	// if (Banking.find(rangePot).length == 0
	// || Banking.find(defencePot).length == 0
	// || Banking.find(antifirePot).length == 0
	// || Banking.find(prayerPot).length == 0
	// || Banking.find(gamesNecklace).length == 0
	// || Banking.find(addyBolt).length == 0
	// || Banking.find(lumTab).length == 0
	// || Banking.find(foodIDs).length == 0) {
	// println("ran out of supplies");
	// scriptStatus = false;
	// }
	// }
}
