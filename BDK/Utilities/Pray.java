package scripts.BDK.Utilities;

import org.tribot.api.General;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.util.ThreadSettings;

import scripts.BDK.Main.BDK;


public class Pray {
	
	public static void turnOffPrayerProtectionMissles(){
		if (Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES)){
			if(!Prayer.isTabOpen()){
				YawsGeneral.openTab(TABS.PRAYERS);
			}
			Prayer.disable(PRAYERS.PROTECT_FROM_MISSILES);
			Conditionals.waitFor(!Prayer.isQuickPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES), 400, 500);
		}
	}
	
	public static void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
			if(!Prayer.isTabOpen()){
				YawsGeneral.openTab(TABS.PRAYERS);
			}
			
			Prayer.disable(PRAYERS.EAGLE_EYE);
			Conditionals.waitFor(!Prayer.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE), 400, 500);
		}
	}
	
	public static void prayerFlick() {
		
		while (YawsGeneral.getHp() > 30 &&	Prayer.getPrayerPoints() > 5 && YawsGeneral.isRanging() && YawsGeneral.inSafeSpot() && !YawsGeneral.lootExists()) {
			BDK.FIGHT_STATUS = "flicking now!";
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
			}
			Timer t = new Timer(1100L);
			do {
				if (Player.getAnimation() != 4230) {
					General.sleep(250, 300);
					Prayer.enable(PRAYERS.EAGLE_EYE);
				} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
					Prayer.disable(PRAYERS.EAGLE_EYE);
					General.sleep(350, 400);
				} else {
					General.sleep(400, 450);
				}
			} while (t.getRemaining() > 0);
		}
		turnOffPrayerEagle();
		
	}
}
