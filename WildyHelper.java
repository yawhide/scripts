package scripts;

import java.awt.Graphics;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

public class WildyHelper extends Script implements Painting{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			sleep(1000);
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
}
