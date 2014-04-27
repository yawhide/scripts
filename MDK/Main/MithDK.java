package scripts.MDK.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.NumberFormat;
import java.util.Locale;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.MDK.Data.Constants;
import scripts.MDK.Data.Tiles;
import scripts.MDK.Utilities.Looting;
import scripts.MDK.Utilities.Utils;

@ScriptManifest(authors = { "Yaw hide" }, category = "Ranged", name = "Yaw hide's MithDK", version = 1.02)
public class MithDK extends Script implements MessageListening07, Painting, Ending{

	static boolean useSpecialBolts = true;
	public static boolean pottedAntiFire = true;
	public static boolean needToBank = true;
	public static boolean mainLoopStatus = true;
	public static long antiFireT;
	String preMsg = "", prePreMsg = "";
	double version = 1.04;
	
	@Override
	public void run() {
		
		MDKGui.onstart();
		
		while(mainLoopStatus){
			Mouse.setSpeed(General.random(120, 140));
			Pathing.checkStatus();
			sleep(50,70);
		}
		
	}

	final String[] type = {"Defence", "Ranged", "Prayer", "Hitpoints" };
	final SKILLS[] Names = {SKILLS.DEFENCE, SKILLS.RANGED, 
			SKILLS.PRAYER, SKILLS.HITPOINTS, };
	final int[] XP = { Skills.getXP(SKILLS.DEFENCE), Skills.getXP(SKILLS.RANGED), 
    		Skills.getXP(SKILLS.PRAYER), Skills.getXP(SKILLS.HITPOINTS) };
	int[] startLvs = { 	Skills.getActualLevel(SKILLS.DEFENCE), Skills.getActualLevel(SKILLS.RANGED), 
			Skills.getActualLevel(SKILLS.PRAYER), Skills.getActualLevel(SKILLS.HITPOINTS) };
	String lastDrop;
	
	public void lastDrops(Graphics graph){
		RSGroundItem[] g = GroundItems.findNearest(2359);
		RSTile drop = null;
		
		if(g.length > 0){
			drop = g[0].getPosition();
		}
		int y = 45;
		for(RSGroundItem gr : GroundItems.getAt(drop)){
			int i = gr.getID();
			if(i != 536 && i != 2359 && 
					i != Constants.RUBY_E_BOLT && i != MDKGui.boltsUsing){
				graph.drawString(gr.getDefinition().getName(), 5, y);
				y+= 15;
			}				
		}
	}
	
	@Override
	public void onPaint(Graphics g) {
		// TODO onPaint
		RSNPC[] drag = NPCs.findNearest("Mithril dragon");
		if (drag.length > 0) {
			Utils.drawTile(drag[0].getAnimablePosition(), g, false);
			g.drawString("Loot status: " + Looting.lootStatus, 5, 170);
			g.drawString("Mith Anim: " + drag[0].getAnimation(), 5, 185);
			g.drawString("Mith HP: " + drag[0].getHealth(), 5, 200);
		}
		if(useSpecialBolts){
			g.drawString("Bolts: "+ (Equipment.getItem(SLOTS.ARROW).getID() == Constants.RUBY_E_BOLT ? 
				"Ruby Bolts e" : "Main bolts"), 5, 215);
		}
		
		g.drawString("Version " + version, 420, 470);
		long time = (360000 - System.currentTimeMillis() + antiFireT);
		g.drawString("AntiFire Status: " + (pottedAntiFire ? "Potted" : "Unpotted") + ", time to pot: "
				+ (time > 0 ? Timing.msToString(time) : null), 5, 275);
		g.drawString(Mouse.getSpeed() +"", 5, 260);
		long timeRan = getRunningTime();
		double secondsRan = (int) (timeRan/1000);
		double hoursRan = secondsRan/3600;
		
		for(int i = 0; i < Tiles.safeSpot.length; i++){
			Utils.drawTile(Tiles.safeSpot[i], g, false);
		}
		
		g.drawString("Gear status: " + (Utils.haveGear() ? true : false), 5, 230);
		g.drawString("Time running: " + Timing.msToString(timeRan) , 5, 245);
		
		int x = 5, y = 337;
		for(int i = 0; i < 4; i++){
			g.drawString(type[i] + ": " + Skills.getCurrentLevel(Names[i]) 
					+ "/" + Skills.getActualLevel(Names[i]),  x,  y-(i*15));
		}		
		int x1 = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(Names[i])) {
				g.setColor(new Color(0, 0, 0));
				g.fillRect(2, 341 + x1, 515, 12);
				
				g.setColor(new Color(0, 255, 0, 255));
				g.drawString(Names[i]
						+ ": "
						+ NumberFormat.getNumberInstance(Locale.US).format(Math.round(((Skills
											.getXP(Names[i]) - XP[i]))/ hoursRan)) 
						+ " Xp/h", 5,(352 + x1));
			int x2 = 150, y1 = 342 + x1;
			int CUR_LVL = Skills.getActualLevel(Names[i]);
			int NXT_LVL = (CUR_LVL + 1);
			int Percentage = Skills.getPercentToLevel(Names[i], NXT_LVL);
			
			g.drawString("Cur lv: " + CUR_LVL + "   Lvs gained: " + (CUR_LVL - startLvs[i]), 257, y1+10);
			
			g.setColor(new Color(255, 0, 0, 255));
			g.fillRect(x2, y1, 100, 10);
			g.setColor(new Color(0, 255, 0, 255));
			g.fillRect(x2, y1, Percentage, 10);
			
			g.setColor(new Color(0, 0, 0));
			g.drawString(Percentage + "% to " + NXT_LVL, x2+25, y1 + 9);
			x1 += 15;
			}
		}
	}
	
	@Override
	public void onEnd() {
		// TODO onEnd()
	}
	
	@Override
	public void serverMessageReceived(String arg0) {
		// TODO serverMessageReceived
		
		if(arg0.equals("Your antifire potion has expired.") || 
				((!arg0.equals("Your potion protects you from the heat of the dragon's breath!") 
				|| arg0.equals("Your dragonfire shield is already fully charged.")) 
				&& preMsg.equals("Your shield absorbs most of the dragon fire!"))){
			pottedAntiFire = false;
		}
		preMsg = arg0;
	}
	
	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
