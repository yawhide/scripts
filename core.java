package scripts;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;


@ScriptManifest (authors = {"Yaw"}, category = "Tutorial Script", name = "Tutscript1", description = "example script")
public class core extends Script implements Painting {
	
	public static int profit;
	public static ArrayList<Node> nodes = new ArrayList();
	public final long startTime = System.currentTimeMillis();
	public void run() {


			

		Mouse.setSpeed(100);
		Collections.addAll(nodes,new coreTask());    //add all nodes to the ArrayList
		loop(20, 40);
		}
	

	private void loop(int min, int max) {
		while (true) {
			
			for (final Node node : nodes) {    //loop through the nodes
				if (node.validate()) {     //validate each node
					node.execute();    //execute
					sleep(General.random(min, max));	//time inbetween executing nodes
				}
			}
		}
	}

	@Override
	public void onPaint(Graphics arg0) {
	
	

		
	}
}