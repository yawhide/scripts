package scripts;

import java.awt.Graphics;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "Yaw hide" }, category = "Thieving", name = "Yaw hide's Silk Stealer")
public class SilkGetter extends Script implements Painting{

	boolean scriptStatus = true;
	RSTile t = new RSTile(2658, 3324);
	RSTile stall = new RSTile(2662, 3316, 0);
	RSTile stallT = new RSTile(2662, 3314, 0);
	int[] dropping = {};
	private ABCUtil abc_util = null;
	
	
	@Override
	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawString("Current level: " + Skills.getActualLevel(SKILLS.THIEVING), 3, 100);
		g.drawString((abc_util.TIME_TRACKER.CHECK_XP.next()- System.currentTimeMillis()) +", " + abc_util.TIME_TRACKER.ROTATE_CAMERA.next() + ", " 
						+ abc_util.TIME_TRACKER.EXAMINE_OBJECT.next(), 3, 115);
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		abc_util = new ABCUtil();
		
		while(scriptStatus){
			if(Player.getRSPlayer().isInCombat()){
				Walking.walkPath(Walking.randomizePath(Walking.generateStraightPath(t), 2, 2));
				sleep(2000,3000);
				break;
			} 
			
			//println(abc_util.TIME_TRACKER.CHECK_XP.next() + " " + abc_util.TIME_TRACKER.ROTATE_CAMERA.next());
			if(abc_util.TIME_TRACKER.CHECK_XP.next() <= System.currentTimeMillis() && abc_util.TIME_TRACKER.CHECK_XP.next() != 0){
				println("checking xp");
				checkXp();
				abc_util.TIME_TRACKER.CHECK_XP.reset();
			}
			else if (abc_util.TIME_TRACKER.ROTATE_CAMERA.next() != 0 && abc_util.TIME_TRACKER.ROTATE_CAMERA.next() <= System.currentTimeMillis()){
				println("rotating camera");
				Camera.setCameraRotation(General.random(0, 355));
				abc_util.TIME_TRACKER.ROTATE_CAMERA.reset();
			}
			else if (abc_util.TIME_TRACKER.EXAMINE_OBJECT.next() != 0 && abc_util.TIME_TRACKER.EXAMINE_OBJECT.next() <= System.currentTimeMillis()){
				println("Examining a random object");
				RSObject[] random = Objects.getAll(10);
				if(random.length > 0){
					//random[General.random(0, random.length -1)].click("Examine");
					Clicking.click("Examine", random);
				}
				abc_util.TIME_TRACKER.EXAMINE_OBJECT.reset();
			}
			
			if(Inventory.isFull()){
				Mouse.setSpeed(General.random(160, 180));
				GameTab.open(TABS.INVENTORY);
				sleep(200,300);
				Inventory.dropAllExcept(dropping);
				sleep(200, 300);
				Mouse.setSpeed(General.random(130, 150));
			}
			else if(Objects.findNearest(10, "Silk stall").length > 0){
				final int i = Inventory.getCount("Silk");
				Clicking.click("Steal-from", stallT);//Objects.findNearest(10, "Silk stall"));
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return Inventory.getCount("Silk") > i;
					}
				}, General.random(2000, 3000));
				
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return Objects.findNearest(10, "Silk stall").length > 0 || Inventory.isFull();
					}
				}, General.random(5000, 7000));
			}
			sleep(100);
		}
	}
	
	public void checkXp(){
		GameTab.open(TABS.STATS);
		sleep(200,300);
		Mouse.moveBox(617,303, 663, 325);
		sleep(3000, 5000);
	}
	
}
