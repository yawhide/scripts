package scripts.MDK.Utilities;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;

public class Fight {
	//TODO fight
		public void fight(){
			RSNPC[] drag = NPCs.findNearest("Mithril dragon");
			RSNPC mith = null;
			if(drag.length > 0){
				if(Tiles.mithDragSpawn1.contains(drag[0].getAnimablePosition()))
					mith = drag[0];
			}
			
			timeToTab(mith);
			
			//check for potting
			checkToPotAntiFire();
			checkToPot();
			
			// turning prayers on
			if(mith == null){
				turnOffAllPrayers();
			}
			else{
				if(Skills.getCurrentLevel(SKILLS.PRAYER) < 10){
					reactivatePrayer(1);
				}
				else{
					if(mith.isValid() && mith.isInteractingWithMe())
						activateProtectRanged();
					else if (mith.getAnimation() == 91)
						turnOffAllPrayers();
				}
			}
			
			//looting and checking bolts
			if(mith == null){
				if(isLoot())
					lootOrder();
				else if (!isLoot()){
					if(Inventory.find(foodIDs).length == 0 && getHp() < 75){
						emergTele();
					}
					else
						gotoSafeSpot1();
				}
			}
			if(useSpecialBolts){
				if(mithAtHalf(mith)){
					equipDiamondBolts();
			}
				else{
					equipRubyBolts();
				}
			}
			
			//actually attacking the dragon and prayer flick
			if(mith != null && !inArea2(Tiles.safeSpot)){//safeSpotSpawn1.contains(pos())){
				println("not in safe spot, lets go!");
				gotoSafeSpot1();
			}
			else if(mith != null && mith.isValid() && mith.isInteractingWithMe() && mith.getAnimation() != 92){
				if (Player.getRSPlayer().getInteractingCharacter() == null) {
					//println("I am not interacting with anyone");
					//println("Not interacting with dragon");
					activateProtectRanged();
					moveCamera(mith);
					//if(
							clickNPC(mith, "Attack");
					//		){
					//}
					sleep(1000,1100);
				}
				else{
					if(mith != null && getHp() > 50 && pottedAntiFire)
						prayerflick(mith);
					else
						General.sleep(1000, 1200);
				}
				
			}
			else if(mith != null && mith.isValid() && !mith.isInteractingWithMe() 
					&& mith.getAnimablePosition().distanceTo(Tiles.safeSpotSpawn1P) > 2 && mith.getAnimation() != 92){
				
				
					activateProtectRanged();
					moveCamera(mith);
					//if(clickNPC(mith, "Attack"))
					clickNPC(mith, "Attack");
						sleep(1000,1100);
			}
			else{/*
				if(getHp() > 50 && pottedAntiFire)
					prayerflick(mith);
				else*/
					General.sleep(1000, 1200);
			}
				
		}
}	
