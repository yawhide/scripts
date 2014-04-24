package scripts.MDK.Utilities;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.MDK.Data.Constants;
import scripts.MDK.Main.MDKGui;
import scripts.MDK.Main.MithDK;

public class Praying {
	
	public void prayflick(RSNPC drag) {

		while (drag != null && drag.isInteractingWithMe() && Utils.getHp() > 50 &&
				Skills.getCurrentLevel(SKILLS.PRAYER) > 10 && MithDK.pottedAntiFire
				&& Player.getRSPlayer().getInteractingCharacter() != null) {
			openPrayerBook();
			if(Attack.mithAtHalf(drag) && Equipment.isEquipped(MDKGui.boltsUsing)){
				Utils.equipBolts(MDKGui.boltsUsing);
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
		
		turnOffPrayer(PRAYERS.EAGLE_EYE);
	}
	
	public void activatePrayer(PRAYERS prayer){
		openPrayerBook();
		if(Prayer.enable(prayer))
			Conditionals.waitFor(Prayer.isPrayerEnabled(prayer), 1000, 2000);
	}
	
	public static void openPrayerBook(){
		Utils.openTab(TABS.PRAYERS);
	}
	
	public void reactivatePrayer(PRAYERS prayer){
		RSItem[] pPot = Inventory.find(Constants.PRAYER_POT);
		Utils.openTab(TABS.INVENTORY);
		if(pPot.length > 0){
			final int prayPoints = Skills.getCurrentLevel(SKILLS.PRAYER);
			if(Clicking.click("DrinK", pPot)){
				Conditionals.waitFor(Skills.getCurrentLevel(SKILLS.PRAYER) > prayPoints, 1000, 2000);
				activatePrayer(prayer);
			}
		}
		else{
			if(Utils.getHp() < 50)
				Utils.emergTele();
		}
	}
	
	public static void turnOffPrayer(PRAYERS prayer){
		if (Prayer.isPrayerEnabled(prayer)){
			openPrayerBook();			
			if(Prayer.disable(prayer))
				Conditionals.waitFor(!Prayer.isQuickPrayerEnabled(prayer), 1000, 2000);
		}
	}
		
	public static void disableAllPrayers(){
		PRAYERS[] p = Prayer.getCurrentPrayers();
		openPrayerBook();
		for (int i = 0; i < p.length; i++) {
			final PRAYERS currentPrayer = p[i];
			if(Prayer.disable(currentPrayer))
				Conditionals.waitFor(!Prayer.isPrayerEnabled(currentPrayer), 1000, 2000);
		}
	}
}
