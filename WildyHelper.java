package scripts;

import java.awt.Graphics;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

public class WildyHelper extends Script implements Painting, MessageListening07{

	boolean strick = true;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Mouse.setSpeed(300);
		
		while(true){
			if(strick) GameTab.open(TABS.LOGOUT); 
			if(Players.getAll().length > 1){
				GameTab.open(TABS.LOGOUT);
				Login.logout();
			}
				
			sleep(10);
		}
	}

	@Override
	public void onPaint(Graphics g) {
		// TODO Auto-generated method stub
		RSPlayer[] p = Players.getAll();
		for	(int i = 0; i < p.length; i++){
			RSPlayer tmp = p[i];
			
			if(!tmp.getName().equals(Player.getRSPlayer().getName()))
				g.drawString(p[i].getName() + " " + distanceTo(p[i].getPosition()) + " " + p[i].getCombatLevel() + " " + tmp.getLocalX(), 3, 100+i*15);
			
		}
	}

	public int distanceTo(RSTile t){ return Math.max(Math.abs(pos().getX()-t.getX()), Math.abs(pos().getY() - t.getY())); }
	public RSTile pos(){ return Player.getPosition(); }

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
		if(arg0.equals(Player.getRSPlayer().getName())){
			if(arg1.equals("Strickoff"))
				strick = false;
			else if (arg1.equals("Strick"))
				strick = true;
		}
	}

	@Override
	public void serverMessageReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
