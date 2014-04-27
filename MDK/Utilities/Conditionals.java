package scripts.MDK.Utilities;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;

import scripts.MDK.Main.MDKGui;

public class Conditionals {
	
	public static void waitFor(final boolean b, int begin, int end){
		Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				return b;
			}
		}, General.random(begin, end));
	}
	
	public static void waitForItem(int[] id, final int count){
		waitFor(Inventory.getCount(id) != count, 2500, 3000);
    }
    
    public static void waitForItem(int id, final int count){
    	waitFor(Inventory.getCount(id) != count, 2500, 3000);
    }
    
    public static void waitForItem(String id, final int count) {
    	waitFor(Inventory.getCount(id) != count, 2500, 3000);
	}
    
    public static void waitForItem(String[] id, final int count) {
    	waitFor(Inventory.getCount(id) != count, 2500, 3000);
	}
    
    public static void waitForEating(final int count){
    	waitFor(count != Inventory.getCount(MDKGui.foodID), 2000, 3000);
    }
    
    public static void waitForTab(TABS t){
		final TABS tab = t;
		waitFor(GameTab.getOpen() == tab, 2000, 3000);
	}

	public static void waitForPotting(SKILLS skill, final int currLv) {
		waitFor(Skills.getCurrentLevel(skill) > currLv, 1000, 1200);
	}

}
