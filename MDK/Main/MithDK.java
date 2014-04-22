package scripts.MDK.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Projection;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.ext.Doors;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.util.Util;

import scripts.MDK.Utilities.Looting;
import scripts.easyslayer.Timer;

@ScriptManifest(authors = { "Yaw hide" }, category = "Ranged", name = "Yaw hide's MithDK", version = 1.0)
public class MithDK extends Script implements MessageListening07, Painting, Ending{

	static boolean useSpecialBolts = true;
	static boolean pottedAntiFire = true;
	static boolean bankStatus = true;
	public static boolean mainLoopStatus = true;
	static long antiFireT;
	
	String preMsg = "", prePreMsg = "";
	
	@Override
	public void run() {
		
		MDKGui.onstart();
		
		while(mainLoopStatus){
			
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
	
	public void lastDrops(Graphics2D graph){
		RSGroundItem[] g = GroundItems.findNearest(2359);
		RSTile drop = null;
		
		if(g.length > 0){
			drop = g[0].getPosition();
		}
		int y = 45;
		for(RSGroundItem gr : GroundItems.getAt(drop)){
			int i = gr.getID();
			if(i != 536 && i != 2359 && 
					i != rubyEBolt && i != diamondEBolt
					&& i != addyBolt){
				graph.drawString(gr.getDefinition().getName(), 5, y);
				y+= 15;
			}				
		}
	}
	
	@Override
	public void onPaint(Graphics arg0) {
		// TODO onPaint
		Graphics2D g = (Graphics2D)arg0;
		RSNPC[] drag = NPCs.findNearest("Mithril dragon");
		if (drag.length > 0) {
			drawTile(drag[0].getAnimablePosition(), g, false);
			g.drawString("Anim: " + drag[0].getAnimation(), 5, 185);
			g.drawString("HP: " + drag[0].getHealth(), 5, 200);
		}
		if(useSpecialBolts){
			g.drawString("Bolts: "+ (Equipment.getItem(SLOTS.ARROW).getID() == rubyEBolt ? 
				"Ruby Bolts e" : "Diamond Bolts e"), 5, 215);
		}
		
		g.drawString("Last drop: " + lastDrop, 5,  260);
		long time = (360000 - System.currentTimeMillis() - antiFireT) / 60000;
		g.drawString("AntiFire Status: " + (pottedAntiFire ? "Potted" : "Unpotted") + ", time to pot: "
				+ (time > 0 ? time : null), 5, 275);
				
		long timeRan = getRunningTime();
		double secondsRan = (int) (timeRan/1000);
		double hoursRan = secondsRan/3600;
		
		
		
		g.drawString("Gear status: " + (haveGear() ? true : false), 5, 230);
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
		
		if((!arg0.equals("Your potion protects you from the heat of the dragon's breath!") 
				|| arg0.equals("Your dragonfire shield is already fully charged.")) 
				&& preMsg.equals("Your shield absorbs most of the dragon fire!")){
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
