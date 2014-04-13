package scripts;

import java.awt.Graphics;
import java.awt.Polygon;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;


public class PVPtrainerTank extends Script implements Painting{

	boolean scriptStatus = true;
	RSTile t = new RSTile(3426, 2930, 0);
	ABCUtil abc_util = null;
	
	
	@Override
	public void run() {
		
		ThreadSettings.get().setAlwaysRightClick(true);
		sleep(200);
		abc_util = new ABCUtil();
				
		while(scriptStatus){
			if(getHp() > 99){
				checkAntiBan();
			}
			else if (getHp() <= 99){
				if(ChooseOption.isOpen()){
					ChooseOption.close();
					sleep(200,300);
				}
				clickAlter();
			}
			sleep(100);
		}
	}
	
	public void checkAntiBan(){
		if (abc_util.TIME_TRACKER.ROTATE_CAMERA.next() != 0){
			println("rotating camera");
			Camera.setCameraRotation(General.random(0, 355));
			abc_util.TIME_TRACKER.ROTATE_CAMERA.reset();
		}
		else if (abc_util.TIME_TRACKER.EXAMINE_OBJECT.next() != 0){
			println("Examining a random object");
			RSObject[] random = Objects.getAll(10);
			if(random.length > 0){
				Clicking.click("Examine", random);
			}
			abc_util.TIME_TRACKER.EXAMINE_OBJECT.reset();
		}
	}
	
	public void clickAlter(){
		RSObject[] altar2 = Objects.getAt(t);
		if(altar2.length > 0){
			if(altar2[0].isOnScreen()){
				Clicking.click("Pray-at", altar2[0]);
				sleep(200);
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return getHp() >= 100;
					}
				}, General.random(1000, 2000));
				Clicking.hover(altar2[0]);
			}
			else{
				Walking.walkPath(Walking.generateStraightPath(t));
				sleep(1000,1200);
			}
		}
	}
	
	public int getHp(){ return Combat.getHPRatio(); }

	@Override
	public void onPaint(Graphics g) {
		RSObject[] altar2 = Objects.getAt(t);
		if(altar2.length > 0){
			drawModel(altar2[0].getModel(), g, false);			
		}
	}
	
	public void drawModel(RSModel model, Graphics g, boolean fill) {
		if (model.getAllVisiblePoints().length != 0) {
			if (fill) {
				// fill triangles
				for (Polygon p : model.getTriangles()) {
					g.fillPolygon(p);
				}
			} else {
				// draw triangles
				for (Polygon p : model.getTriangles()) {
					g.drawPolygon(p);
				}
			}
		}
	}

}
