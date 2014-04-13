package scripts.Avies.Utilities;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;

import scripts.Avies.Main.Avies;

public class Banking {
	 public static boolean withdraw(final int num, final int[] ids){
	    	final int count = Inventory.getCount(ids);
	    	if(Banking.withdraw(num, ids)){
	    		waitForItem(ids, count);
	    		return true;
	    	}
	    	else if(Banking.find(ids).length == 0){
	    		println("Your bank does not have the item with ids: " + ids);
	    		Avies.SCRIPT_STATUS = false;
	    	}
	    	return false;
	    }
	    
	    public static boolean withdraw(final int num, final int id){
	    	final int count = Inventory.getCount(id);
	    	if(Banking.withdraw(num, id)){
	    		waitForItem(id, count);
	    		return true;
	    	}
	    	else if(Banking.find(id).length == 0){
	    		println("Your bank does not have the item with id: " + id);
	    		scriptStatus = false;
	    	}
	    	return false;
	    }
}
