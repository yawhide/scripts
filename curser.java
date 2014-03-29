package scripts;

import java.awt.Graphics;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "Yaw hide" }, category = "Magic", name = "Yawhide's Curser", description="local version")
public class curser extends Script implements Painting, MessageListening07 {

	boolean scriptStatus = true;
	boolean switchMon = true;
	int confusedAnim = 1163;
	String[] monsters = {"Thief", "Pirate", "Black knight", "Mugger"};
	int counter = 0;
	int magelv;
	boolean stuck = false;
	RSTile blackT = new RSTile(3016, 3183, 0);
	RSTile muggerT = new RSTile(3016, 3186, 0);
	RSTile pirateT = new RSTile(3012, 3193, 0);
	RSTile thiefT = new RSTile(3012, 3194, 0);
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		RSNPC[] cast = NPCs.find(monsters);
		sleep(500);
		magelv = Skills.getActualLevel(SKILLS.MAGIC);
		Mouse.setSpeed(General.random(120, 150));
		
		while(scriptStatus){
			
			if(Game.getRunEnergy() > 20) {
			    Options.setRunOn(true);
			}
			
			if(switchMon){
				counter++;
				switchMon = false;
			}
			else if (stuck){
				RSTile t = Player.getPosition();
				Walking.clickTileMS(new RSTile(t.getX() -1, t.getY()-1), 1);
				Walking.clickTileMS(new RSTile(t.getX() -1, t.getY()-1), 1);
				sleep(1000);
				stuck = false;
			}
			
			switch (counter % 4) {
			case 0:
				if (check(monsters[0]) != null) {
					castUpon(check(monsters[0]));
				}
				break;

			case 1:
				if (check(monsters[1]) != null) {
					castUpon(check(monsters[1]));
				}
				break;

			case 2:
				if (check(monsters[2]) != null) {
					castUpon(check(monsters[2]));
				}
				break;

			case 3:
				if (check(monsters[3]) != null) {
					castUpon(check(monsters[3]));
				}
				break;
			}
			
		}
	}
	
	RSNPC check(String i){
		RSNPC[] n = NPCs.findNearest(i);
		if(n.length > 0){
			return n[0];
		}
		return null;
	}
	
	void castUpon(RSNPC npc){
		if(npc.isOnScreen()){
			clickSpell();
			Clicking.click(npc);
			Timing.waitCondition(new Condition(){
				@Override
				public boolean active() {
					return Player.getAnimation() > -1 || switchMon;
				} 
			}, 2500);
			Timing.waitCondition(new Condition(){
				@Override
				public boolean active() {
					return Player.getAnimation() == -1 || switchMon;
				} 
			}, 2500);
		}
		else{
			if(counter % 4 == 2){
				Walking.clickTileMM(blackT, 1);
			}
			else if (counter % 4 == 0){
				Walking.clickTileMM(thiefT, 1);
			}
			else if (counter % 4 == 1){
				Walking.clickTileMM(pirateT, 1);
			}
			else if (counter % 4 == 3){
				Walking.clickTileMM(muggerT, 1);
			}
			Camera.turnToTile(npc.getAnimablePosition());
			sleep(2300, 3000);
		}
	}
	
	void clickSpell(){
		GameTab.open(TABS.MAGIC);
		sleep(200,300);
		int lv = Skills.getActualLevel(SKILLS.MAGIC);
		if(lv >=3 && lv < 11){
			Magic.selectSpell("Confuse");
		}
		else if (lv < 19){
			Magic.selectSpell("Weaken");
		}
		else if (lv >= 19){
			Magic.selectSpell("Curse");
		}
		else{
			println("You dont have 3+ magic, stopping the script");
			scriptStatus = false;
		}
		sleep(200,300);
	}

	@Override
	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawString("Magic level: " + Skills.getActualLevel(SKILLS.MAGIC), 5, 100);
		g.drawString("Levels gained: " + (Skills.getActualLevel(SKILLS.MAGIC) - magelv), 5, 115);
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverMessageReceived(String arg0) {
		if(arg0.contains("has already been lowered") || arg0.contains("has already been weakened")){
			switchMon = true;
			println(arg0);
		}
		else if (arg0.equals("I can't reach that!")){
			stuck = true;
		}
			
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}

}
