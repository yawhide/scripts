package scripts.MDK.Utilities;

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

import scripts.easyslayer.Timer;

public class Praying {
	public void prayerflick(RSNPC drag) {

		openPrayers();
		
		while (drag != null && drag.isInteractingWithMe() && getHp() > 50 &&
				Skills.getCurrentLevel(SKILLS.PRAYER) > 10 && pottedAntiFire
				&& Player.getRSPlayer().getInteractingCharacter() != null) {
			
			if(mithAtHalf(drag)){
				equipDiamondBolts();
			}
			
			Timer t = new Timer(1100L);
			do {
				if (Player.getAnimation() != 4230) {
					sleep(250, 300);
					Prayer.enable(PRAYERS.EAGLE_EYE);
					//Options.setQuickPrayersOn(true);
				} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)) {
					//Options.setQuickPrayersOn(false);
					Prayer.disable(PRAYERS.EAGLE_EYE);
					sleep(350, 400);
				} else {
					sleep(400, 450);
				}
			} while (t.getRemaining() > 0);
		}
		
		turnOffPrayerEagle();
	}
	
	public void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){//.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE)){
			//Options.setQuickPrayersOn(false);
			if(!Prayer.isTabOpen()) GameTab.open(TABS.PRAYERS);
			sleep(200,250);
			Prayer.disable(PRAYERS.EAGLE_EYE);
			sleep(800,1000);
		}
	}
	
	public void activateProtectMage(){
		openPrayers();
		Prayer.enable(PRAYERS.PROTECT_FROM_MAGIC);
		sleep(200,300);
	}
	
	public void openPrayers(){
		if(!Prayer.isTabOpen()){
			GameTab.open(TABS.PRAYERS);
			sleep(200,250);
		}
	}
	
	public void activateProtectRanged(){
		openPrayers();
		Prayer.enable(PRAYERS.PROTECT_FROM_MISSILES);
		sleep(200,300);
	}
	
	public void reactivatePrayer(int option){
		
		RSItem[] pPot = Inventory.find(prayerPot);
		gotoTab(GameTab.TABS.INVENTORY);
		if(pPot.length > 0){
			println("Potting prayer");
			if(pPot[0].click("Drink")){
				sleep(1200,1250);
				if(option == 0)
					activateProtectMage();
				else
					activateProtectRanged();
			}
		}
		else{
			if(getHp() < 50)
				emergTele();
		}
	}
}
