package scripts;

import org.tribot.api.Clicking;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;

public class ForBundit extends Script{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		RSTile t = new RSTile(3427, 2930, 0);
		ThreadSettings.get().setAlwaysRightClick(true);
		sleep(200);
		while(true){
			//RSObject[] altar = Objects.findNearest(20, "Elidinis Statuette");
			if(ChooseOption.isOpen()){
				ChooseOption.close();
				sleep(200,300);
			}
			RSObject[] altar2 = Objects.getAt(t);
			if(altar2.length > 0){
				
				if(altar2[0].isOnScreen()){
					Clicking.click("Pray-at", altar2[0]);
					sleep(3000,4000);
				}
				else{
					Walking.walkPath(Walking.generateStraightPath(t));
					sleep(1000,1200);
				}
			}
			sleep(200);
		}
	}

}
