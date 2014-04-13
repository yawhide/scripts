package scripts.BDK.Utilities;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.GameTab.TABS;

import scripts.Avies.Main.Avies;

public class Conditionals {
	
	public static void waitFor(final boolean b, int begin, int end){
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return b;
			}
		}, General.random(begin, end));
	}
	
	public static void waitForItem(final int[] id, final int count){
		waitFor(Inventory.getCount(id) > count, 500, 1000);
    }
    
    public static void waitForItem(final int id, final int count){
    	waitFor(Inventory.getCount(id) > count, 500, 1000);
    }
    
    public static void waitForEating(final int count){
    	waitFor(count > Inventory.find(Avies.FOOD_IDS).length, 500, 1000);
    }
    
    public static void waitForTab(TABS t){
		final TABS tab = t;
		waitFor(GameTab.getOpen() == tab, 500, 1000);
	}
	
	
}
