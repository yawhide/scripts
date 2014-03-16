package scripts.slayer;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@ScriptManifest(authors={"Platinum Force Scripts and Yaw hide"}, name="Has Tabs/Runes Helper", category="Tools")
public class YawhideHelper extends Script {

	public static String Locations[] = { "Varrock", "Falador", "Camelot", "Ardougne", "Lumbridge" };
	public static Map<String, String[]> TASK = new LinkedHashMap<String, String[]>();
	public static Map<String, Integer> TABS = new LinkedHashMap<String, Integer>();
	public static Map<String, Integer[]> RUNES = new LinkedHashMap<String, Integer[]>();
	public static Map<String, Integer[]> RUNES2 = new LinkedHashMap<String, Integer[]>();
	
	static int VTAB = 8007;
	static int FTAB = 8009;
	static int CTAB = 8010;
	static int LTAB = 8008;
	static int ATAB = 8011;
	
	static int LAW = 563;
	static int EARTH = 557;
	static int AIR = 556;
	static int WATER = 555;
	static int FIRE = 554;
	
	public static void setupYawhideHelper(){
		TASK.put("Varrock", new String[] { "dwarves", "minotaurs", "rats", "skeletons", "zombies", "VARROCK" });
		TASK.put("Falador", new String[] { "bats", "cows", "ghosts", "icefiends", "monkeys", "FALADOR" });
		TASK.put("Camelot", new String[] { "cave_crawlers", "wolves", "CAMELOT" });
		TASK.put("Ardougne", new String[] { "bears", "birds", "dogs", "ARDOUGNE" });
		TASK.put("Lumbridge", new String[] { "cave_slimes", "cave_bugs", "goblins", "spiders", "LUMBRIDGE" });

		// Add tabs by location
		TABS.put("Varrock", VTAB);
		TABS.put("Falador", FTAB);
		TABS.put("Camelot", CTAB);
		TABS.put("Lumbridge", LTAB);
		TABS.put("Ardougne", ATAB);

		// Add runes by location
		RUNES.put("Varrock", new Integer[] { LAW, AIR, FIRE });
		RUNES.put("Falador", new Integer[] { WATER, LAW, AIR });
		RUNES.put("Camelot", new Integer[] { AIR, LAW });
		RUNES.put("Lumbridge", new Integer[] { EARTH, LAW, AIR });
		RUNES.put("Ardougne", new Integer[] { WATER, LAW });

		RUNES2.put("Varrock", new Integer[] { 1, 3, 1 });
		RUNES2.put("Falador", new Integer[] { 1, 1, 3 });
		RUNES2.put("Camelot", new Integer[] { 5, 1 });
		RUNES2.put("Lumbridge", new Integer[] { 1, 1, 3 });
		RUNES2.put("Ardougne", new Integer[] { 2, 2 });
	}
	@Override
	public void run(){
		//Add tasks by location
		
		
	}

	/* Checks if the player has the required tab for a specific task */
	
	public static boolean hasReqTab(boolean usingTabs, String currTask){
		for(String loc : Locations){
			//for(String monster : TASK.get(loc)){
			//if(monster.equals(currTask)){
			if(Arrays.asList(TASK.get(loc)).contains(currTask)){
				if (usingTabs){
					RSItem[] tabs = Inventory.find(TABS.get(loc));
					if(tabs.length > 0)
						return true;
				}
				else{
					int c = 0;
					for(Integer i : RUNES.get(loc)){
						int count = Inventory.getCount(i);
						//for(int j = 0; j < RUNES2.get(loc).length; j++){
							//if ( j == c){
						if(RUNES2.get(loc)[c] > count)
							return false;
							//}
						//}
						c++;
					}
					return true;
				}
				break;
			}
		}
		//}
		return false;
	}
	
	
}