package scripts.MDK.Utilities;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
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

	public static boolean withdraw(final int num, final int[] ids) {
		final int count = Inventory.getCount(ids);
		if (Banking.withdraw(num, ids)) {
			Conditionals.waitForItem(ids, count);
			return true;
		} else if (Banking.find(ids).length == 0) {
			General.println("Your bank does not have the item with ids: " + ids);
			MithDK.mainLoopStatus = false;
		}
		return false;
	}

	public static boolean withdraw(final int num, final int id) {
		final int count = Inventory.getCount(id);
		if (Banking.withdraw(num, id)) {
			Conditionals.waitForItem(id, count);
			return true;
		} else if (Banking.find(id).length == 0) {
			General.println("Your bank does not have the item with id: " + id);
			MithDK.mainLoopStatus = false;
		}
		return false;
	}

	public static boolean withdraw(final int num, final String id) {
		final int count = Inventory.getCount(id);
		if (Banking.withdraw(num, id)) {
			Conditionals.waitForItem(id, count);
			return true;
		} else if (Banking.find(id).length == 0) {
			General.println("Your bank does not have the item with id: " + id);
			MithDK.mainLoopStatus = false;
		}
		return false;
	}

	public static boolean withdraw(final int num, final String[] id) {
		final int count = Inventory.getCount(id);
		if (Banking.withdraw(num, id)) {
			Conditionals.waitForItem(id, count);
			return true;
		} else if (Banking.find(id).length == 0) {
			General.println("Your bank does not have the item with id: " + id);
			MithDK.mainLoopStatus = false;
		}
		return false;
	}

	public static void openBank() {
		if (openChest()) {
			if (Banking.isPinScreenOpen())
				Banking.inPin();
			else if (Banking.isBankScreenOpen()) {
				bank();
			}
		}
	}

	public static void bank() {

		Banking.depositAll();
		Conditionals.waitFor(Inventory.getAll().length == 0, 400, 500);

		//checkItemsInBank();

		withdraw(1, Constants.RANGE_POT);
		withdraw(1, Constants.DEFENSE_POT);
		withdraw(1, Constants.ANTIFIRE_POT);
		withdraw(4, Constants.PRAYER_POT);
		withdraw(5, Constants.HOUSE_TABLET);
		withdraw(1, Constants.GAMES_NECKLACE);

		getMoreBoltsIfNeeded();

		if (!Equipment.isEquipped(Constants.RING_OF_LIFE)) {
			withdraw(1, Constants.RING_OF_LIFE);
		}

		if (Utils.getHp() < 90) {
			withdraw(
					(((Skills.getActualLevel(SKILLS.HITPOINTS) - Skills.getCurrentLevel(SKILLS.HITPOINTS)) 
							/ (MDKGui.foodIDs[0] == 385 ? 20 : 16)) + 1), MDKGui.foodIDs);
		}

		closeBank();
		
		if (MDKGui.useRubyBolts) {
			if (Inventory.find(Constants.RUBY_E_BOLT).length > 0) {
				Clicking.click("Equip", Inventory.find(Constants.RUBY_E_BOLT));
			}
		}
		else{
			if (Inventory.find(MDKGui.boltsUsing).length > 0) {
				Clicking.click("Equip", Inventory.find(MDKGui.boltsUsing));
			}
		}
		
		if (Inventory.find(Constants.RING_OF_LIFE).length > 0) {
			Clicking.click("Equip", Inventory.find(Constants.RING_OF_LIFE));
		}

		while (Inventory.find(MDKGui.foodIDs).length > 0) {
			final int i = Inventory.find(MDKGui.foodIDs).length;
			if (Clicking.click("Eat", Inventory.find(MDKGui.foodIDs)))
				Conditionals.waitForEating(i);
		}

		openBank();

		withdraw(MDKGui.foodAmount, MDKGui.foodIDs);
		closeBank();

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

	public static void getMoreBoltsIfNeeded() {
		int rubyBoltCount = Equipment.getCount(Constants.RUBY_E_BOLT)
				+ Inventory.getCount(Constants.RUBY_E_BOLT);
		int boltCount = Equipment.getCount(MDKGui.boltsUsing)
				+ Inventory.getCount(MDKGui.boltsUsing);

		if (MDKGui.useRubyBolts) {
			if (rubyBoltCount < 200) {
				withdraw(100 - rubyBoltCount, Constants.RUBY_E_BOLT);
			}
		}
		if (boltCount < 200) {
			if (boltCount < 200) {
				withdraw(200 - boltCount, MDKGui.boltsUsing);
			}
		}
	}
	
	public static void closeBank() {
		Banking.close();
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return !Banking.isBankScreenOpen();
			}
		}, General.random(400, 500));
	}
	
//	public static void checkitemsInBank(){
//		 if (Banking.find(rangePot).length == 0
//		 || Banking.find(defencePot).length == 0
//		 || Banking.find(antifirePot).length == 0
//		 || Banking.find(prayerPot).length == 0
//		 || Banking.find(gamesNecklace).length == 0
//		 || Banking.find(addyBolt).length == 0
//		 || Banking.find(lumTab).length == 0
//		 || Banking.find(foodIDs).length == 0) {
//		 println("ran out of supplies");
//		 scriptStatus = false;
//		 }
//	}
}
