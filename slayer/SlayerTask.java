package scripts.slayer;

import java.util.ArrayList;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api.interfaces.Positionable;
import org.tribot.script.Script;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Screen;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSNode;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.EventBlockingOverride;

public class SlayerTask{

	private static ArrayList<String> monsterStr = new ArrayList<String>();
	private static String name = null;
	private static ArrayList<String> reqItems = new ArrayList<String>();
	private static int leftToKill;
	private static Positionable monsterMiddle;
	private static String[] reqItemsArr = null;
	private static RSNPC[] monsterStrArr = null;
	private static ArrayList<RSNode> node = new ArrayList<RSNode>();
	
	
	// classes (like hitpoints before)

	public SlayerTask(String name, int leftToKill) {
		this.name = name;
		setMonsterString();
		setReqItems();
		this.leftToKill = leftToKill;
		reqItemsArr = reqItems.toArray(new String[reqItems.size()]);
		monsterStrArr = NPCs.findNearest(monsterStr.toArray(new String[monsterStr.size()]));
		pathCreation();
	}

	// now the getters and setters

	public static RSNPC[] getMonsterStr() {
		return monsterStrArr;
	}

	public static String getMonsterName() {
		return name;
	}

	public String[] getSpecialItems() {
		return reqItemsArr;
	}

	public static int getAmountLeft() {
		return leftToKill;
	}

	public static String getMonsterToKill() {
		return name;
	}

	public static void setMonsterString(){
		switch(name){
		case "banshees":
			monsterStr.add("Banshee");
			break;
		case "bats":
			monsterStr.add("Giant bat");
			break;
		case "bears":
			monsterStr.add("Grizzly bear");
			monsterStr.add("Bear");
			monsterStr.add("Bear cub");
			monsterStr.add("Black bear");
			monsterStr.add("Grizzly bear cub");
			break;
		case "birds":
			monsterStr.add("Terrorbird");
			break;
		case "cave_bugs":
			monsterStr.add("Cave bug");
			break;
		case "cave_crawlers":
			monsterStr.add("Cave crawler");
			break;
		case "cave_slimes":
			monsterStr.add("Cave slime");
			break;
		case "cows":
			monsterStr.add("Cow");
			monsterStr.add("Cow calf");
			monsterStr.add("Unicow");
			break;
		case "crawling_hands":
			monsterStr.add("Crawling Hand");
			break;
		case "dogs":
			monsterStr.add("Guard dog");
			monsterStr.add("Wild dog");
			break;
		case "dwarves":
			monsterStr.add("Dwarf");
			monsterStr.add("Black guard");
			monsterStr.add("Chaos dwarf");
			break;
		case "ghosts":
			monsterStr.add("Ghost");
			break;
		case "goblins":
			monsterStr.add("Goblin");
			monsterStr.add("Cave goblin guard");
			monsterStr.add("Hobgoblin");
			break;
		case "icefiends":
			monsterStr.add("Icefiends");
			break;
		case "kalphite":
			monsterStr.add("Kalphite Worker");
			monsterStr.add("Kalphite Guardian");
			monsterStr.add("Kalphite Queen");
			monsterStr.add("Kalphite Soldier");
			break;
		case "minotaurs":
			monsterStr.add("Minotaur");
			break;
		case "monkeys":
			monsterStr.add("Monkey");
			monsterStr.add("Monkey archer");
			monsterStr.add("Monkey guard");
			monsterStr.add("Ninja monkey");
			monsterStr.add("Zombie monkey");
			break;
		case "rats":
			monsterStr.add("Giant rat");
			monsterStr.add("Rat");
			monsterStr.add("Brine rat");
			monsterStr.add("Crypt rat");
			monsterStr.add("Giant crypt rat");
			break;
		case "scorpions":
			monsterStr.add("King scorpion");
			monsterStr.add("Poison scorpion");
			monsterStr.add("Scorpion");
			break;
		case "skeletons":
			monsterStr.add("Giant skeleton");
			monsterStr.add("Skeleton");
			break;
		case "spiders":
			monsterStr.add("Spider");
			monsterStr.add("Giant spider");
			monsterStr.add("Crypt spider");
			monsterStr.add("Deadly red spider");
			monsterStr.add("Giant crypt spider");
			monsterStr.add("Jungle spider");
			break;
		case "werewolves":
			monsterStr.add("Yuri");
			monsterStr.add("Zoja");
			monsterStr.add("Lev");
			monsterStr.add("Svetlana");
			monsterStr.add("Boris");
			monsterStr.add("Irina");
			monsterStr.add("Sofiya");
			monsterStr.add("Yadviga");
			monsterStr.add("Nikita");
			monsterStr.add("Fidelio");
			monsterStr.add("Joseph");
			monsterStr.add("Georgy");
			break;
		case "wolves":
			monsterStr.add("Big wolf");
			monsterStr.add("Desert wolf");
			monsterStr.add("Dire wolf");
			monsterStr.add("Ice wolf");
			monsterStr.add("Jungle wolf");
			monsterStr.add("White wolf");
			monsterStr.add("Wolf");
			break;
		case "desert_lizards":
			monsterStr.add("Desert lizard");
			monsterStr.add("Lizard");
			monsterStr.add("Small lizard");
			break;
		case "zombies":
			monsterStr.add("Undead one");
			monsterStr.add("Zombie");
			break;
		}
		return;
		
	}

	public static void setReqItems(){
		switch(name){
		case "banshees":
			reqItems.add("Earmuffs");
			break;
		case "bats":
			
			break;
		case "bears":
			
			break;
		case "birds":
			
			break;
		case "cave_bugs":
			reqItems.add("Spiny helmet");
			break;
		case "cave_crawlers":

			break;
		case "cave_slimes":
			reqItems.add("Spiny helmet");
			break;
		case "cows":

			break;
		case "crawling_hands":

			break;
		case "dogs":

			break;
		case "dwarves":

			break;
		case "ghosts":

			break;
		case "goblins":

			break;
		case "icefiends":

			break;
		case "kalphite":

			break;
		case "minotaurs":

			break;
		case "monkeys":

			break;
		case "rats":

			break;
		case "scorpions":

			break;
		case "skeletons":

			break;
		case "spiders":

			break;
		case "werewolves":

			break;
		case "wolves":

			break;
		case "desert_lizards":
			reqItems.add("Ice cooler");
			break;
		case "zombies":

			break;
		}
		return;
	}
	
	public static void pathCreation(){
		switch(name){
		case "banshees":
			
			break;
		case "bats":
			
			break;
		case "bears":
			
			break;
		case "birds":
			
			break;
		case "cave_bugs":
			
			break;
		case "cave_crawlers":

			break;
		case "cave_slimes":
			
			break;
		case "cows":

			break;
		case "crawling_hands":

			break;
		case "dogs":

			break;
		case "dwarves":

			break;
		case "ghosts":

			break;
		case "goblins":

			break;
		case "icefiends":

			break;
		case "kalphite":

			break;
		case "minotaurs":

			break;
		case "monkeys":

			break;
		case "rats":

			break;
		case "scorpions":

			break;
		case "skeletons":

			break;
		case "spiders":

			break;
		case "werewolves":

			break;
		case "wolves":

			break;
		case "desert_lizards":
			
			break;
		case "zombies":

			break;
		}
		return;
		
	}
	
}
