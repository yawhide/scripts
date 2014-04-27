package scripts.MDK.Utilities;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.MDK.Data.Constants;
import scripts.MDK.Data.Tiles;
import scripts.MDK.Main.MDKGui;
import scripts.MDK.Main.MithDK;

public class Attack {
	// TODO fight
	public static void fight() {
		
		RSNPC mith = findNearestDragInArea();

		if(Utils.timeToTab(mith))
			Utils.emergTele();

		// check for potting
		Utils.checkToPotAntiFire();
		

		// turning prayers on
		if (mith == null || mith.getAnimation() == 91) {
			if(Prayer.getCurrentPrayers().length != 0)
				Praying.disableAllPrayers();
			Utils.checkToPot();
		} else {
			if (Skills.getCurrentLevel(SKILLS.PRAYER) < 10) {
				Praying.reactivatePrayer(PRAYERS.PROTECT_FROM_MISSILES);
			} else if (mith.isValid() && mith.isInteractingWithMe())
					Praying.activatePrayer(PRAYERS.PROTECT_FROM_MISSILES);
		}

		// looting and checking bolts
		if (mith == null) {
			if (Looting.lootExists())
				Looting.lootOrder();
			else {
				if (Inventory.find(MDKGui.foodID).length == 0 && Utils.getHp() < 75) {
					Utils.emergTele();
				} else
					Utils.gotoSafeSpot1();
			}
		}
		if (MDKGui.useRubyBolts) {
			if (mithAtHalf(mith)) {
				Utils.equipBolts(MDKGui.boltsUsing);
			} else {
				Utils.equipBolts(Constants.RUBY_E_BOLT);
			}
		}

		// actually attacking the dragon and prayer flick
		if (mith != null && !Utils.inArea(Tiles.safeSpotSpawn1)) {
			General.println("not in safe spot, lets go!");
			Utils.gotoSafeSpot1();
		} else if (mith != null && mith.isValid() && mith.isInteractingWithMe()
				&& mith.getAnimation() != 92) {
			if (Player.getRSPlayer().getInteractingCharacter() == null) {
				Praying.activatePrayer(PRAYERS.PROTECT_FROM_MISSILES);
				Utils.moveCamera(mith);
				if(clickNPC(mith, "Attack"))
					Conditionals.waitFor(Utils.isAttacking(), 3000, 4000);
			} else {
				if (mith != null && Utils.getHp() > 50 && MithDK.pottedAntiFire)
					Praying.prayFlick(mith);
				else
					General.sleep(1000, 1200);
			}

		} else if (mith != null
				&& mith.isValid()
				&& !mith.isInteractingWithMe()
				&& mith.getAnimablePosition().distanceTo(Tiles.safeSpotSpawn1P) > 2
				&& mith.getAnimation() != 92) {
			Praying.activatePrayer(PRAYERS.PROTECT_FROM_MISSILES);
			Utils.moveCamera(mith);
			if(clickNPC(mith, "Attack"))
				Conditionals.waitFor(Utils.isAttacking(), 3000, 4000);
		}

	}

	public static boolean mithAtHalf(RSNPC drag) {
		if (drag != null && drag.isInteractingWithMe())
			return drag.getHealth() <= 127 && drag.getHealth() > 0;
		return false;
	}
	
	public static RSNPC findNearestDragInArea(){
		for(RSNPC drag : NPCs.findNearest("Mithril dragon")){
			if(Tiles.mithDragSpawn1.contains(drag.getAnimablePosition()) && (drag.isInteractingWithMe() || !drag.isInCombat())
					&& drag.getAnimation() != 91){
				return drag;
			}
		}
		return null;
	}
	
	private static boolean clickNPC(RSNPC npc, String option) {
		RSTile loc = null;
		if (npc != null && npc.isOnScreen()) {
			loc = new RSTile(npc.getPosition().getX(), npc.getPosition().getY() -1);
			Mouse.move(Projection.tileToScreen(loc, 10));
			if (Game.isUptext(option)) {
				Mouse.click(1);
				return true;
			} else {
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
		}
		return false;
	}
}
