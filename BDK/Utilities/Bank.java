package scripts.BDK.Utilities;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;

import scripts.BDK.Data.Constants;
import scripts.BDK.Main.BDK;


public class Bank {
	 public static boolean withdraw(final int num, final int[] ids){
	    	final int count = Inventory.getCount(ids);
	    	if(Banking.withdraw(num, ids)){
	    		Conditionals.waitForItem(ids, count);
	    		return true;
	    	}
	    	else if(Banking.find(ids).length == 0){
	    		General.println("Your bank does not have the item with ids: " + ids);
	    		BDK.SCRIPT_STATUS = false;
	    	}
	    	return false;
	    }
	    
	    public static boolean withdraw(final int num, final int id){
	    	final int count = Inventory.getCount(id);
	    	if(Banking.withdraw(num, id)){
	    		Conditionals.waitForItem(id, count);
	    		return true;
	    	}
	    	else if(Banking.find(id).length == 0){
	    		General.println("Your bank does not have the item with id: " + id);
	    		BDK.SCRIPT_STATUS = false;
	    	}
	    	return false;
	    }
	    
	    public static void bank(){
			if(Banking.openBankBooth()){
				if(Banking.isPinScreenOpen())
					Banking.inPin();
				else if (Banking.isBankScreenOpen()){
					
					BDK.DHIDE_COUNT += BDK.DHIDES;
					BDK.DBONE_COUNT += BDK.DBONES;
					BDK.DHIDES_INI = 0;
					BDK.DBONES_INI = 0;
					
					Banking.depositAllExcept("Falador teleport");
					Conditionals.waitFor(Inventory.getAll().length == 1, 400, 500);
					
					withdrawPPots();
										
					withdraw(1, Constants.RANGE_POTS);
					withdraw(BDK.FOOD_NUM+((Combat.getMaxHP() - Combat.getHP()) / 12), BDK.FOOD_IDS);
					
					if(YawsGeneral.getStackBolts(BDK.BOLTS_ID) < 500){
						withdraw(500, BDK.BOLTS_ID);
					}
					
					closeBank();
					
					if(Inventory.find(BDK.BOLTS_ID).length > 0){
						Clicking.click("Equip", Inventory.find(BDK.BOLTS_ID)[0]);
					}
					
					while(Inventory.find(BDK.FOOD_IDS).length > BDK.FOOD_NUM){
					    final int i = Inventory.find(BDK.FOOD_IDS).length;
						if(Clicking.click("Eat", Inventory.find(BDK.FOOD_IDS)[0]))
							Conditionals.waitForEating(i);
					}
				}
			}
		}
	    
	    public static void closeBank(){
	    	Banking.close();
			Timing.waitCondition(new Condition() {
				public boolean active() {
					return !Banking.isBankScreenOpen();
				}
			}, General.random(400, 500));
	    }
	    
	    public static void withdrawPPots(){
	    	int pt = Skills.getActualLevel(SKILLS.PRAYER) / 3;
			int currP = Skills.getCurrentLevel(SKILLS.PRAYER);

			RSItem[] pPot;
			if (currP < pt * 2) {
				General.println("potting prayer, pt is: " + pt);

				withdraw(1, Constants.PRAYER_POTS);
				
				closeBank();

				pPot = Inventory.find(Constants.PRAYER_POTS);
				if (pPot.length > 0) {
					do {
						if(Clicking.click("Drink", pPot[0])){
							General.sleep(500,1000);
						}
						General.sleep(50);
					} while (Skills.getCurrentLevel(SKILLS.PRAYER) <= (pt * 2) - 2);
				}
				
				if (!Banking.isBankScreenOpen()) {
					if (Banking.openBankBanker()) {
						Conditionals.waitFor(Banking.isBankScreenOpen(), 400, 500);
						
						Banking.depositAll();
						Conditionals.waitFor(Inventory.getAll().length == 0, 400, 500);
					}
				}
			}
	    }
}