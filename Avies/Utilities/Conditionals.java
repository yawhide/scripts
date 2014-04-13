package scripts.Avies.Utilities;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;

import scripts.Avies.Main.Avies;

public class Conditionals {
	public void waitForItem(final int[] id, final int count){
    	Timing.waitCondition(new Condition() {
			public boolean active() {
				return Inventory.getCount(id) > count;
			}
		}, General.random(500, 1000));
    }
    
    public void waitForItem(final int id, final int count){
    	Timing.waitCondition(new Condition() {
			public boolean active() {
				return Inventory.getCount(id) > count;
			}
		}, General.random(500, 1000));
    }
    
    public void waitForEating(final int count){
    	Timing.waitCondition(new Condition() {
			public boolean active() {
				return count > Inventory.find(Avies.FOOD_IDS).length;
			}
		}, General.random(500, 1000));
    }
}
