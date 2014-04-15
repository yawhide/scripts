package scripts.Avies.Utilities;

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

import scripts.Avies.Data.Constants;
import scripts.Avies.Main.Avies;

public class Bank {
	 public static boolean withdraw(final int num, final int[] ids){
	    	final int count = Inventory.getCount(ids);
	    	if(Banking.withdraw(num, ids)){
	    		Conditionals.waitForItem(ids, count);
	    		return true;
	    	}
	    	else if(Banking.find(ids).length == 0){
	    		General.println("Your bank does not have the item with ids: " + ids);
	    		Avies.mainLoopStatus = false;
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
	    		Avies.mainLoopStatus = false;
	    	}
	    	return false;
	    }
	    
	    public static void openBank(){
			if(Banking.openBankBooth()){
				if(Banking.isPinScreenOpen())
					Banking.inPin();
				else if (Banking.isBankScreenOpen()){
					bank();					
				}
			}
		}
	    
	    public static void bank(){
	    	Avies.addyCount += Avies.addyBars;
			Avies.coinsAmount += Avies.coins;
			Avies.ranarrCount += Avies.ranarrs;
			Avies.addyIni = 0;
			Avies.coinsIni = 0;
			Avies.ranarrIni = 0;
			
			Banking.depositAll();
			Conditionals.waitFor(Inventory.getAll().length == 0, 400, 500);
			
			if(!Avies.useHouseTab){
				withdrawPPots();
			}
			
			withdraw(1, Constants.RANGE_POT);
			withdraw(10, Constants.NAT);
			withdraw(52, Constants.FIRE);
			withdraw(2, Constants.LAW);
			withdraw(Avies.foodNumber+((Combat.getMaxHP() - Combat.getHP()) / 12), Avies.foodIDs);
			
			if(Avies.useHouseTab){
				if(Inventory.getCount(Constants.HTAB) < 5){
					withdraw(10, Constants.HTAB);
				}
			}
			else{
				if(Inventory.getCount(Constants.HTAB) < 5){
					withdraw(10, Constants.VTAB);
				}
			}
			if(YawsGeneral.getStackBolts(Avies.boltsID) < 500){
				withdraw(1000, Avies.boltsID);
			}
			
			closeBank();
			
			if(Inventory.find(Avies.boltsID).length > 0){
				Clicking.click("Equip", Inventory.find(Avies.boltsID)[0]);
			}
			
			while(Inventory.find(Avies.foodIDs).length > Avies.foodNumber){
			    final int i = Inventory.find(Avies.foodIDs).length;
				if(Clicking.click("Eat", Inventory.find(Avies.foodIDs)[0]))
					Conditionals.waitForEating(i);
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

				withdraw(1, Constants.PRAYER_POT);
				
				closeBank();

				pPot = Inventory.find(Constants.PRAYER_POT);
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