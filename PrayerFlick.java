package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;

public class PrayerFlick{
	
	public void prayerflick() {

		if (!Prayer.isTabOpen()) {
			GameTab.open(TABS.PRAYERS);
		}
		long t = Timing.currentTimeMillis();
		do {
			if (Player.getAnimation() != 4230) {
				General.sleep(250, 300);
				Prayer.enable(PRAYERS.EAGLE_EYE);
			} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)) {
				Prayer.disable(PRAYERS.EAGLE_EYE);
				General.sleep(350, 400);
			} else {
				General.sleep(400, 450);
			}
		} while (Timing.currentTimeMillis() - t < 1100);

	}
	
	public void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			General.sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			General.sleep(800,1000);
		}
	}
	
	public void turnOffPrayerProtectionMissles(){
		if (Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES)){
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
				General.sleep(500,650);
			}
			Prayer.disable(PRAYERS.PROTECT_FROM_MISSILES);
			General.sleep(800,1000);
		}
	}
}
