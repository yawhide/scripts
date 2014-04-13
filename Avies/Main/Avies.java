package scripts.Avies.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Pausing;

import scripts.Avies.Data.Constants;
import scripts.slayer.Timer;
import scripts.slayer.Zybez;

@ScriptManifest(authors = { "Yaw hide" }, category = "ranged", version=0.3, name = "Yaw hide's Ava Killer", description="Local version")
public class Avies extends Script implements Painting, Pausing, MessageListening07{

	public static int addybars, coins, ranarrs = 0;
	public static int addycount, coinscount, ranarrcount = 0;
	public static int addyIni, coinsIni, ranarrIni = 0;
	public static int MITHRIL_BOLTS = 9142;
	public static int FOOD_NUMBER = 11;
	public static int BOLTS_ID;
	public static int[] FOOD_IDS; // , 385, 7946, 1897 };
	public static int ADDY_PRICE = 2800;
	public static int RANARR_PRICE = 6400;
	
	public static long ANTIBAN = System.currentTimeMillis();
	
	public static boolean SCRIPT_STATUS = true;
	public static String FIGHT_STATUS;
	
	//GUI
	public static boolean MOVE_RANDOM = false;
	public static boolean USE_HOUSE = true;
	public static boolean WAIT_GUI = true;
	
	//paint
	public static int START_XP = Skills.getXP(SKILLS.HITPOINTS)
			+ Skills.getXP(SKILLS.RANGED);
	public static double VERSION;
	public static int CURRENT_XP;
	public static final long START_TIME = System.currentTimeMillis();
	public static double XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
	public static double XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
	public static int START_LV = Skills.getActualLevel(SKILLS.RANGED)
			+ Skills.getActualLevel(SKILLS.HITPOINTS);
	public static final String[] TYPE = { "Defence", "Ranged", "Hitpoints" };
	public static final SKILLS[] SKILL = { SKILLS.DEFENCE, SKILLS.RANGED,
			SKILLS.HITPOINTS, };
	public static final int[] XP = { Skills.getXP(SKILLS.DEFENCE),
			Skills.getXP(SKILLS.RANGED), Skills.getXP(SKILLS.HITPOINTS) };
	public static int[] START_LVS = { Skills.getActualLevel(SKILLS.DEFENCE),
			Skills.getActualLevel(SKILLS.RANGED),
			Skills.getActualLevel(SKILLS.HITPOINTS) };
	
	@Override
	public void run() {
		
		General.useAntiBanCompliance(true);
		//ABCUtil abc = new ABCUtil();
				
		BOLTS_ID = Equipment.getItem(SLOTS.ARROW).getID();
		if(Skills.getActualLevel(SKILLS.RANGED) < 70){
			println("You must be at least 70 range to use this script");
			SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.PRAYER) < 44){
			println("You must be at least 44 prayer to use this script");
			SCRIPT_STATUS = false;
		}
		else if (Skills.getActualLevel(SKILLS.AGILITY) < 60 && Skills.getActualLevel(SKILLS.STRENGTH) < 60){
			println("You must be at least 60 agility or strength to use this script");
			SCRIPT_STATUS = false;
		}
		if(Skills.getActualLevel(SKILLS.CONSTRUCTION) < 50){
			USE_HOUSE = false;
			println("You do not have 50 con for varrock portal focus, you must use varrock teletabs");
		}
		
		boolean devmode = true;
		
		if(devmode){
			FOOD_NUMBER = 9;
			FOOD_IDS = new int[] {379};
			USE_HOUSE = true;
		}
		else{
			AvieGUI g = new AvieGUI();
			g.setVisible(true);
			while (WAIT_GUI)
				sleep(500);
			g.setVisible(false);
		}
		
		if(!Combat.isAutoRetaliateOn()){
			Combat.setAutoRetaliate(true);
			sleep(500,600);
		}
		
		putMap();
		
		addyIni = Inventory.getCount(Constants.ADDY);
		coinsIni = Inventory.getCount(Constants.COIN);
		ranarrIni = Inventory.getCount(Constants.RANARR);
		ADDY_PRICE = Zybez.getPrice("Adamantite bar");
		RANARR_PRICE = Zybez.getPrice("Clean ranarr");
		
		sleep(250);
		
		Walking.setWalkingTimeout(5000L);
		Walking.setControlClick(true);
		
		
		while(SCRIPT_STATUS){
			CURRENT_XP = Skills.getXP(SKILLS.RANGED) + Skills.getXP(SKILLS.HITPOINTS);
			XP_TO_LVL_RANGE = Skills.getXPToNextLevel(SKILLS.RANGED);
			XP_TO_LVL_HP = Skills.getXPToNextLevel(SKILLS.HITPOINTS);
			Mouse.setSpeed(General.random(175,190));
			
			addybars = Inventory.getCount(Constants.ADDY) - addyIni;
			coins = Inventory.getCount(Constants.COIN) - coinsIni;
			ranarrs = Inventory.getCount(Constants.RANARR) - ranarrIni;
			
			if(Game.getRunEnergy() > 20) {
			    Options.setRunOn(true);
			}
			
			if(MOVE_RANDOM){
				if(pos().distanceTo(Constants.RANDOM_EAST_TILE) > pos().distanceTo(Constants.RANDOM_WEST_TILE)){
					Walking.walkPath(Walking.generateStraightPath(Constants.RANDOM_EAST_TILE));
					waitIsMovin();
				}
				else{
					Walking.walkPath(Walking.generateStraightPath(Constants.RANDOM_WEST_TILE));
					waitIsMovin();
				}
				MOVE_RANDOM = false;
			}
			else if(gotoAvies()){
				fight();
			}
		}
	}
	
	public void fight(){
		
		Walking.setControlClick(true);
		Walking.setWalkingTimeout(1500L);
		
		if(getHp() <= 30){
			HEAL();
		}
		else if(Inventory.isFull() || (Inventory.getAll().length > 26 && !isLoot())){
			println("Inv is full");
			ifFull();
		}
		else if(isLoot()){
			println("looting...");
			LOOT();
		}
		else if (Inventory.getCount(Constants.RANGE_POT) > 0
				&& Skills.getCurrentLevel(SKILLS.RANGED) < Skills
						.getActualLevel(SKILLS.RANGED) + 2) {

			println(Skills.getCurrentLevel(SKILLS.RANGED) + "  "
					+ Skills.getActualLevel(SKILLS.RANGED));
			FIGHT_STATUS = "Potting up";
			println("Potted up");
			drinkPotion(Constants.RANGE_POT);
		}
		else if (Prayer.getCurrentPrayers().length > 0){
			PRAYERS[] p = Prayer.getCurrentPrayers();
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return GameTab.getOpen() == TABS.PRAYERS;
					}
				}, General.random(400, 500));
			}
			for (int i = 0; i < p.length; i++) {
				final PRAYERS currentPrayer = p[i];
				Prayer.disable(currentPrayer);
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return !Prayer.isPrayerEnabled(currentPrayer);
					}
				}, General.random(400, 500));
			}
		}
		else if(isRanging()){
			if(Combat.getTargetEntity() != null && !Combat.getTargetEntity().getName().equals("Aviansie")){
				MOVE_RANDOM = true;
			}
			else
				waitForLoot(Combat.getTargetEntity());
		}
		else if (Inventory.find(Constants.ALC_LOOT).length > 0){
			alc(Constants.ALC_LOOT);
		}
		else{
			RSNPC[] avies = NPCs.findNearest("Aviansie");
			for(RSNPC a : avies){
				if(Constants.AVIES_Area.contains(a.getPosition()) && (a.getCombatLevel() == 69 || a.getCombatLevel() == 71 || a.getCombatLevel() == 83)){
					if (a.isOnScreen()) {
						if(clickObject(a, "Attack")){//Clicking.click(avies)) {
							FIGHT_STATUS = "killing an avie";
							
							Timing.waitCondition(new Condition() {
								public boolean active() {
									return Player.getAnimation() == 4230;
								}
							}, General.random(3000, 3200));
						}
					} 
					else {
						println("running to closest avie");
						Walking.clickTileMM(a.getAnimablePosition(), 1);
						Camera.turnToTile(a.getPosition());
						sleep(300,400); // can't really put a conditional wait here cuz i can't get the rotation
					}
					break;
				}
			}
		}
	}
	
	public boolean gotoAvies(){
		RSObject[] boulder = Objects.findNearest(7, "Boulder");
		RSObject[] hole = Objects.findNearest(7, "Hole");
	
		if (inArea(Constants.AVIES_Area) && pos().getPlane() == 2)
			return true;
		else if(!inArea(Constants.VARROCK_AREA) && Objects.findNearest(50, "Portal").length > 0){
			FIGHT_STATUS = "in house...";
			RSObject[] alter = Objects.findNearest(20, "Altar");
			RSObject[] portal = Objects.findNearest(20, "Varrock Portal");
			RSObject[] portal2 = Objects.findNearest(30, "Portal");
			
			if(alter.length == 0 || portal.length == 0){
				FIGHT_STATUS = "not inside house...";
				if(portal2.length > 0){
					if(portal2[0].isOnScreen()){
						if(portal2[0].click("Enter")){
							NPCChat.selectOption("Go to your house", true);
						}
					}
					else{
						Walking.walkPath(Walking.generateStraightPath(portal2[0].getPosition()));
						waitIsMovin();
					}
				}
				
				println("Could not determine where you are, are you outside of the house???");
				//scriptStatus = false;
			}
			
			else if (Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER)) {
				if(portal.length > 0){
					if (portal[0].isOnScreen()) {
						if (Clicking.click("Enter", portal[0])) {
							Timing.waitCondition(new Condition() {
								public boolean active() {
									return inArea(Constants.VARROCK_AREA);
								}
							}, General.random(1200, 2000));
						}
					} else {
						Positionable temp = new RSTile(portal[0].getPosition().getX(), portal[0].getPosition().getY()-2, 0);
						Walking.walkPath(Walking.generateStraightPath(temp));
						waitIsMovin();
					}
				}
			} else {
				if (alter.length > 0) {
					if (alter[0].isOnScreen()) {
						if (Clicking.click("Pray", alter[0])) {
							Timing.waitCondition(new Condition() {
								public boolean active() {
									return Skills.getCurrentLevel(SKILLS.PRAYER) == Skills.getActualLevel(SKILLS.PRAYER);
								}
							}, General.random(1500, 2300));
						}
					} else {
						Walking.walkPath(Walking.generateStraightPath(alter[0].getPosition()));
						waitIsMovin();
					}
				}
			}
		}
		else if(inGW()){
			FIGHT_STATUS = "walking to avies spot";
			Walking.walkPath(Walking.generateStraightPath(Constants.TO_AVIES[Constants.TO_AVIES.length-1]));
			//Walking.walkPath(toAvies);
			waitIsMovin();
		}
		else if (pos().distanceTo(Constants.TO_GWD[Constants.TO_GWD.length - 1]) <= 5) {
			FIGHT_STATUS = "walking to GW hole";
			if (Clicking.click("Climb-down", Constants.HOLE_TILE.getPosition())) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return inGW();
					}
				}, General.random(2000, 3000));
			}
		}
		else if (aroundPath(Constants.TO_GWD)){
			FIGHT_STATUS = "walking before the range trolls";
			Walking.walkPath(Constants.TO_GWD);
			waitIsMovin();
		}
		else if (pos().distanceTo(Constants.BEFORE_BOULDER[Constants.BEFORE_BOULDER.length - 1]) <= 5) {
			FIGHT_STATUS = "prayer off, walking to boulder";
			turnOffPrayerProtectionMissles();
			if (Clicking.click(Objects.findNearest(7, "Boulder"))) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return aroundPath(Constants.TO_GWD);
					}
				}, General.random(7000, 8000));
			}
		}
		else if (inArea(Constants.ROCK_SLIDE_AREA)){
			FIGHT_STATUS = "near first slide";
			if(Clicking.click(Objects.findNearest(7, "Rocks"))){
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return aroundPath(Constants.BEFORE_PRAY_RANGE_SPOT);
					}
				}, General.random(3500, 4500));
			}
		}
		else if (inArea(Constants.TROLL_TELEPORT_AREA)){
			FIGHT_STATUS = "walking to first slide";
			Walking.walkPath(Constants.TO_FIRST_SLIDE);
			waitIsMovin();
		}
		else if (aroundPath(Constants.BEFORE_BOULDER)){
			FIGHT_STATUS = "walking by trolls";
			if(!Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES)){
				if(!Prayer.isTabOpen()){
					GameTab.open(TABS.PRAYERS);
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return GameTab.getOpen() == TABS.PRAYERS;
						}
					}, General.random(400, 500));
				}
				Prayer.enable(PRAYERS.PROTECT_FROM_MISSILES);
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return !Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES);
					}
				}, General.random(400, 500));
			}
			Walking.walkPath(Constants.BEFORE_BOULDER);
			waitIsMovin();
		}
		else if (aroundPath(Constants.BEFORE_PRAY_RANGE_SPOT)){
			FIGHT_STATUS = "walking before the range trolls";
			Walking.walkPath(Constants.BEFORE_PRAY_RANGE_SPOT);
			waitIsMovin();
		}
		else if (inArea(Constants.VARROCK_AREA)){
			FIGHT_STATUS = "in varrock, banking...";
			if(!gotEquipment()){
				if (pos().distanceTo(Constants.BANK_TILE) <= 5){
					println("banking...");
					bank();
				}
				else{
					println("gonna web walk to bank");
					WebWalking.walkTo(Constants.BANK_TILE);
					waitIsMovin();
				}
			}
			else{
				teleToTroll();
			}
		}
		return false;
	}
	
	public boolean isLoot(){
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT2);
		return Nests.length > 0 && Constants.AVIES_Area.contains(Nests[0].getPosition());
	}
		
	public void LOOT() {
		FIGHT_STATUS = "looting";
		turnOffPrayerEagle();		
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT);
		
		for(int i = 0; i < Nests.length; i++){
			Walking.setWalkingTimeout(1000L);
			if(Nests[i].getID() == BOLTS_ID){
				if (Nests[i].getStack() > 9) {
					if (!Nests[i].isOnScreen()) {
						Walking.walkPath(Walking.generateStraightPath(Nests[i].getPosition()));
						Camera.turnToTile(Nests[i].getPosition());
						Camera.setCameraAngle(General.random(90, 100));
						waitIsMovin();
					}
					String str = Constants.LOOT_MAPPING.get(Nests[i].getID());
					final int tmpCount = itemCount(Nests[i].getID());
					if (Nests[i].click("Take " + str))
						waitForItem(tmpCount);
				}
			}
			else{
				if (!Nests[i].isOnScreen()) {
					Walking.walkPath(Walking.generateStraightPath(Nests[i].getPosition()));
					Camera.turnToTile(Nests[i].getPosition());
					Camera.setCameraAngle(General.random(90, 100));
					waitIsMovin();
				}
				String str = Constants.LOOT_MAPPING.get(Nests[i].getID());
				final int tmpCount = itemCount(Nests[i].getID());
				if(Nests[i].click("Take " + str))
					waitForItem(tmpCount);
			}
		}
	}
	
	public void bank(){
		if(Banking.openBankBooth()){
			if(Banking.isPinScreenOpen())
				Banking.inPin();
			else if (Banking.isBankScreenOpen()){
				
				addycount += addybars;
				coinscount += coins;
				ranarrcount += ranarrs;
				addyIni = 0;
				coinsIni = 0;
				ranarrIni = 0;
				
				Banking.depositAll();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return Inventory.getAll().length == 0;
					}
				}, General.random(400, 500));
				
				if(!USE_HOUSE){
					int pt = Skills.getActualLevel(SKILLS.PRAYER) / 3;
					int currP = Skills.getCurrentLevel(SKILLS.PRAYER);

					RSItem[] pPot;
					if (currP < pt * 2) {
						println("potting prayer, pt is: " + pt);

						Banking.withdraw(1, Constants.PRAYER_POT);
						waitForItem(Constants.PRAYER_POT);
						Banking.close();
						Timing.waitCondition(new Condition() {
							public boolean active() {
								return !Banking.isBankScreenOpen();
							}
						}, General.random(400, 500));

						pPot = Inventory.find(Constants.PRAYER_POT);
						if (pPot.length > 0) {
							do {
								if(Clicking.click("Drink", pPot[0])){
									sleep(500,1000);
								}
								sleep(50);
							} while (Skills.getCurrentLevel(SKILLS.PRAYER) <= (pt * 2) - 2);
						}
						
						if (!Banking.isBankScreenOpen()) {
							if (Banking.openBankBanker()) {
								Timing.waitCondition(new Condition() {
									public boolean active() {
										return Banking.isBankScreenOpen();
									}
								}, General.random(400, 500));
								
								Banking.depositAll();
								Timing.waitCondition(new Condition() {
									public boolean active() {
										return Inventory.getAll().length == 0;
									}
								}, General.random(400, 500));
							}
						}
					}
				}
				
				Banking.withdraw(1, Constants.RANGE_POT);
				waitForItem(Constants.RANGE_POT);
				Banking.withdraw(10, Constants.NAT);
				waitForItem(Constants.NAT);
				Banking.withdraw(52, Constants.FIRE);
				waitForItem(Constants.FIRE);
				Banking.withdraw(2, Constants.LAW);
				waitForItem(Constants.LAW);
				Banking.withdraw(FOOD_NUMBER+((Combat.getMaxHP() - Combat.getHP()) / 12), FOOD_IDS);
				waitForItem(FOOD_IDS);
				
				
				
				if(USE_HOUSE){
					if(lootCountStack(Constants.HTAB) < 5){
						Banking.withdraw(10, Constants.HTAB);
						waitForItem(Constants.HTAB);
					}
				}
				else{
					if(lootCountStack(Constants.HTAB) < 5){
						Banking.withdraw(10, Constants.VTAB);
						waitForItem(Constants.VTAB);
					}
				}
				if(getStackBolts() < 500){
					Banking.withdraw(1000, BOLTS_ID);
					waitForItem(BOLTS_ID);
				}
				
				Banking.close();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return !Banking.isBankScreenOpen();
					}
				}, General.random(400, 500));
				
				if(Inventory.find(BOLTS_ID).length > 0){
					Clicking.click("Equip", Inventory.find(BOLTS_ID)[0]);
				}
				
				while(Inventory.find(FOOD_IDS).length > FOOD_NUMBER){
				    final int i = Inventory.find(FOOD_IDS).length;
					if(Clicking.click("Eat", Inventory.find(FOOD_IDS)[0]))
						waitForEating(i);
				}
			}
		}
	}

	public void ifFull(){
		RSItem[] food = Inventory.find(FOOD_IDS);
		RSItem[] bolts = Inventory.find(BOLTS_ID);
		if(Inventory.find(Constants.JUNK).length > 0 || food.length > 0 || bolts.length > 0){
			FIGHT_STATUS = "dropping junk";
			if (food.length > 0){
				final int count = Inventory.getCount(FOOD_IDS);
				if(Clicking.click("Eat", food[0])){
					waitForEating(count);
				}
					
			}
			else if (bolts.length > 0){
				final int boltsCount = Equipment.getItem(SLOTS.ARROW).getStack();
				if(Clicking.click("Wield", bolts[0])){
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return Equipment.getItem(SLOTS.ARROW).getStack() > boltsCount;
						}
					}, General.random(400, 500));
				}
			}
			Inventory.drop(Constants.JUNK);
		}
		else{
			emergTele();
		}
	}
	
	public boolean gotEquipment(){
		println(lootCountStack(Constants.NAT));
		return (((!USE_HOUSE && lootCountStack(Constants.VTAB) > 0) || (USE_HOUSE && lootCountStack(Constants.HTAB) > 0))
				&& lootCountStack(Constants.NAT) >= 10 && lootCountStack(Constants.FIRE) >= 52
				&& lootCountStack(Constants.LAW) == 2 && Inventory.find(Constants.RANGE_POT).length == 1
				&& Inventory.find(FOOD_IDS).length == FOOD_NUMBER);
	}

	public void gotoAviesSpot(){
		FIGHT_STATUS = "going to avvieArea";
		Walking.setWalkingTimeout(1500L);
		Walking.walkPath(Constants.TO_AVIES);
		waitIsMovin();
	}

	@Override
	public void onPaint(Graphics g) {
		Rectangle hideZone = new Rectangle(342, 369, 542, 500);
		Point p = Mouse.getPos();
		
		int xpGained = CURRENT_XP - START_XP;
		long timeRan = System.currentTimeMillis() - START_TIME;
		
		double multiplier = timeRan / 3600000D;
		int xpPerHour = (int) (xpGained / multiplier);
					
		long timeRan2 = getRunningTime();
		double secondsRan = (int) (timeRan2/1000);
		double hoursRan = secondsRan/3600;
		int x = 0;
		for (int i = 0; i < XP.length; i++) {
			if (XP[i] != Skills.getXP(SKILL[i])) {

				double xp_per_hour = Math.round(((Skills.getXP(SKILL[i]) - XP[i])) / hoursRan);
				if (!hideZone.contains(p)) {
					g.setColor(new Color(0, 0, 0));
					g.fillRect(2, 326 - x, 515, 12);

					g.setColor(new Color(0, 255, 0, 255));
					g.drawString(SKILL[i] + ": " + (int) xp_per_hour + " Xp/h",
							5, (337 - x));

					int x1 = 125, y1 = 327 - x;
					int CUR_LVL = Skills.getActualLevel(SKILL[i]);
					int NXT_LVL = (CUR_LVL + 1);
					int Percentage = Skills
							.getPercentToLevel(SKILL[i], NXT_LVL);
					double nextLv = Skills.getXPToLevel(SKILL[i], NXT_LVL);

					double hours = (nextLv / xp_per_hour);

					g.drawString("Curr lv: " + CUR_LVL + " ("
							+ (CUR_LVL - START_LVS[i]) + ")  TTL " + (int) hours
							+ ":" + (int) ((hours - (int) hours) * 60)
							+ " hr:min  Exp to lv: " + (int) nextLv, 235,
							y1 + 10);

					g.setColor(new Color(255, 0, 0, 255));
					g.fillRect(x1, y1, 100, 10);
					g.setColor(new Color(0, 255, 0, 255));
					g.fillRect(x1, y1, Percentage, 10);

					g.setColor(new Color(0, 0, 0));
					g.drawString(Percentage + "% to " + NXT_LVL, x1 + 25,
							y1 + 9);

					x += 15;
				}
			}
		}
		
		int dh, db, ran;
		dh = addybars + addycount;
		db = coins + coinscount;
		ran = ranarrs + ranarrcount;
		g.setColor(Color.CYAN);
		RSGroundItem[] Nests = GroundItems.findNearest(Constants.LOOT2);
		
		Map<Integer, Integer> items = new HashMap<Integer, Integer>();
		RSItem[] item = Inventory.getAll();
		for(int i = 0; i < item.length; i++){
			int count = items.containsKey(item[i].getID()) ? items.get(item[i].getID()) : 0;
			items.put(item[i].getID(), count + 1);
		}
		Iterator it = items.entrySet().iterator();
		int k = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        g.drawString((Constants.LOOT_MAPPING.get(pairs.getKey()) != null ? Constants.LOOT_MAPPING.get(pairs.getKey()) : pairs.getKey()) 
	        		+ " = " + pairs.getValue(), 5, 100+k);
	        it.remove(); // avoids a ConcurrentModificationException
	        k+=15;
	    }
		
		for(int i = 0; i < Nests.length; i++){
			if(Constants.AVIES_Area.contains(Nests[i].getPosition())){
				RSTile t = Nests[i].getPosition();				
				drawTile(t, g, false);
			}
		}
		g.setColor(Color.RED);
		RSNPC[] avv = NPCs.findNearest("Aviansie");
		for(RSNPC a : avv){
			if(a.getCombatLevel() == 69 || a.getCombatLevel() == 71 || a.getCombatLevel() == 83)
				drawTile(a.getPosition(), g, false);			
		}
		g.drawString(Combat.getTargetEntity().getName(), 5, 50);
		g.drawString(avv[0].getAnimation() + "", 5, 60);
		g.setColor(Color.GREEN);
//		RSNPC[] o = NPCs.findNearest(avaIDs);
//		if(o.length > 0){
//			int x2 = (int) o[0].getModel().getEnclosedArea().getBounds()
//					.getCenterX();
//			int y2 = (int) o[0].getModel().getEnclosedArea().getBounds()
//					.getCenterY();
//			g.drawRect(x2, y2, 1, 1);
//			g.drawRect(x2-4, y2-4, 1, 1);
//			g.drawRect(x2+4, y2+4, 1, 1);
//		}
		//drawModel(Combat.getTargetEntity().getModel(), g, true);
		
		
		
		if (!hideZone.contains(p)) {
			g.setColor(new Color(60, 60, 60)); 
			g.fillRect(340, 370, 200, 150);

			g.setColor(Color.WHITE);
			g.drawString("Yawhide's BDK", 345, 385);
			g.drawString("Version :" + VERSION + "   Curr world: " + Game.getCurrentWorld(), 345, 405);
			g.drawString("Running for: " + Timing.msToString(timeRan), 345, 420);
			g.drawString("Total XP ganed: " + xpGained + " (" + xpPerHour
					+ "/h)", 345, 435);
			g.drawString("State: " + FIGHT_STATUS, 345, 450);
			g.drawString("Addy bars: " + dh + "       GP: " + db/1000 + " K", 345, 465);
			g.drawString("Gp/hr: " + (int) ((dh*ADDY_PRICE + db + ran*RANARR_PRICE)/hoursRan/1000) + " K   Total: " + (int)(dh*ADDY_PRICE + db + ran*RANARR_PRICE)/1000 + " K", 345, 480);
			
		}
	}
	
	public void teleToTroll(){
		GameTab.open(TABS.MAGIC);
		waitForTab(TABS.MAGIC);
		Magic.selectSpell("Trollheim Teleport");
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return inArea(Constants.TROLL_TELEPORT_AREA);
			}
		}, General.random(3500, 4500));
	}
	
	public void CWTele(){
		RSItem[] ROD = Equipment.find(SLOTS.RING);
		
		GameTab.open(TABS.EQUIPMENT);
		waitForTab(TABS.EQUIPMENT);
		if(ROD.length > 0){
			if(!inCombat()){
				if (Clicking.click("Operate", ROD[0])) {
					if (NPCChat.selectOption("Castle Wars Arena.", true)) {
						Timing.waitCondition(new Condition() {
							public boolean active() {
								return inArea(Constants.CW_AREA);
							}
						}, General.random(4500, 5500));
					}
				}
			}
			else{
				emergTele();
			}
		}
	}
	
	public RSTile pos(){ return Player.getPosition(); }
	public boolean isRanging() { return Player.getRSPlayer().getInteractingCharacter() != null; }
	public int distanceTo(RSTile t){ return Math.max(pos().getX()-t.getX(), pos().getY() - t.getY()); }
	public int getHp() { return Combat.getHPRatio(); }
	public boolean isFull() { return Inventory.isFull(); }
	public int itemCount(int ID) { return Inventory.find(ID).length; }
	public int lootCountStack(int ID) { return Inventory.getCount(ID); }
	public boolean isMovin() { return Player.isMoving();}
	public boolean inCombat() { return Player.getRSPlayer().isInCombat(); }
	public int getStackBolts() { return Equipment.getItem(SLOTS.ARROW).getID() == BOLTS_ID ? Equipment.getItem(SLOTS.ARROW).getStack() : 0;}
	
	public void putMap() {
		for (int i = 0; i < Constants.LOOT.length; i++) {
			if (i >= Constants.LOOT_STRING_NAMES.length) {
				Constants.LOOT_MAPPING.put(Constants.LOOT[i], Constants.LOOT_STRING_NAMES[Constants.LOOT_STRING_NAMES.length - 1]);
			} else
				Constants.LOOT_MAPPING.put(Constants.LOOT[i], Constants.LOOT_STRING_NAMES[i]);
		}
	}
	
	public void drawTile(RSTile tile, Graphics g, boolean fill) {
		if (tile.getPosition().isOnScreen()) {
			if (fill) {
				g.fillPolygon(Projection.getTileBoundsPoly(tile, 0));
			} else {
				g.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
			}
		}
	}
	
	public Avies() {
		VERSION = ((ScriptManifest) getClass().getAnnotation(
				ScriptManifest.class)).version();
	}
	
	public void emergTele(){
		FIGHT_STATUS = "tele tabbing";
		RSItem[] tab = Inventory.find(Constants.VTAB);
		if(USE_HOUSE)
			tab = Inventory.find(Constants.HTAB);
		GameTab.open(TABS.INVENTORY);
		waitForTab(TABS.INVENTORY);
		if(tab.length > 0){
			if(Clicking.click("Break", tab[0])){
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return USE_HOUSE ? inHouse() : inArea(Constants.VARROCK_AREA);
					}
				}, General.random(2500, 3000));
			}
		}
		else{
			println("Out of tabs");
			SCRIPT_STATUS = false;
		}
	}
	
	public void HEAL() {
		RSItem[] food = Inventory.find(FOOD_IDS);
		FIGHT_STATUS = "eating food";
		if(GameTab.getOpen() != TABS.INVENTORY){
			GameTab.open(TABS.INVENTORY); 
			waitForTab(TABS.INVENTORY);
		}
		
		if (food.length > 0) {
			final int count = Inventory.getCount(FOOD_IDS);
			if(Clicking.click("Eat", food[0]))
				waitForEating(count);
		}
		else
			emergTele();
	}
		
	public void prayerFlick() {

		while (!MOVE_RANDOM && getHp() > 30 && Skills.getCurrentLevel(SKILLS.PRAYER) > 5 && isRanging() && inAviesSpot() && !isLoot()) {
			FIGHT_STATUS = "flicking now!";
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
			}
			Timer t = new Timer(1100L);
			do {
				if (Player.getAnimation() != 4230) {
					sleep(250, 300);
					Prayer.enable(PRAYERS.EAGLE_EYE);
				} else if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
					Prayer.disable(PRAYERS.EAGLE_EYE);
					sleep(350, 400);
				} else {
					sleep(400, 450);
				}
			} while (t.getRemaining() > 0);
		}

		turnOffPrayerEagle();
	}
	
	//TODO fix waitForLoot
	public void waitForLoot(RSCharacter avv){
		FIGHT_STATUS = "prayerflicking";
		if(!isLoot() && isRanging() && inAviesSpot() && getHp() > 30){
			prayerFlick();
		}
		turnOffPrayerEagle();
	}
	
	public void turnOffPrayerEagle(){
		if (Prayer.isPrayerEnabled(PRAYERS.EAGLE_EYE)){
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
				waitForTab(TABS.PRAYERS);
			}
			
			Prayer.disable(PRAYERS.EAGLE_EYE);
			Timing.waitCondition(new Condition() {
				public boolean active() {
					return !Prayer.isQuickPrayerEnabled(PRAYERS.EAGLE_EYE);
				}
			}, General.random(400, 500));
		}
	}
	
	public String underAttack(){
		RSCharacter[] mon = Combat.getAttackingEntities();
		if(mon.length > 0){
			return mon[0].getName();
		}
		return null;
	}
	
	public boolean inAviesSpot(){	return inArea(Constants.AVIES_Area);	}
	
	public boolean inArea(RSArea a) {	return a.contains(pos());	}
	
	public void waitIsMovin(){ 
		for(int i = 0; i < 57; i++, sleep(30, 40)){
			if (!Player.isMoving())
				break;
		}
	}
	
	public void drinkPotion(int[] pot) {
		if (Skills.getCurrentLevel(SKILLS.RANGED) < Skills.getActualLevel(SKILLS.RANGED) + 2) {
			Inventory.open();
			RSItem[] potion = Inventory.find(pot);
			if (potion.length > 0) {
				if(Clicking.click("Drink", potion[0])){
					Timing.waitCondition(new Condition() {
						public boolean active() {
							return Skills.getCurrentLevel(SKILLS.RANGED) > Skills.getActualLevel(SKILLS.RANGED);
						}
					}, General.random(1000, 1200));
				}
			}
		}
	}
	
	public void turnOffPrayerProtectionMissles(){
		if (Prayer.isPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES)){
			if(!Prayer.isTabOpen()){
				GameTab.open(TABS.PRAYERS);
				waitForTab(TABS.PRAYERS);
			}
			Prayer.disable(PRAYERS.PROTECT_FROM_MISSILES);
			Timing.waitCondition(new Condition() {
				public boolean active() {
					return !Prayer.isQuickPrayerEnabled(PRAYERS.PROTECT_FROM_MISSILES);
				}
			}, General.random(400, 500));
		}
	}
	
	private void waitForInv(int lootID, int lootNum) {
		FIGHT_STATUS = "waiting for loot";
		final int lootIDFinal = lootID;
		final int lootNumFinal = lootNum;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return itemCount(lootIDFinal) == lootNumFinal && Player.isMoving();
			}
		}, General.random(3500, 4000));
	}
	
	public boolean aroundPath(RSTile[] path){
		for(int i = 0; i < path.length; i++){
			if(pos().distanceTo(path[i]) <= 4){
				return true;
			}	
		}
		return false;
	}
	
	public boolean inGW(){	return pos().distanceTo(Constants.GWD_CENTER) <= 50 && pos().getPlane() == 2;	}
	
	public void alc(int[] loot){
		RSItem[] l = Inventory.find(loot);
		GameTab.open(TABS.MAGIC);
		waitForTab(TABS.MAGIC);
		Magic.selectSpell("High Level Alchemy");
		waitFor(!Magic.getSelectedSpellName().equals(""), 500, 1000);
		final int exp = Skills.getXP(SKILLS.MAGIC);
		if(Clicking.click("Cast", l[0])){
			waitFor(Skills.getXP(SKILLS.MAGIC) > exp, 1100, 1400);
		}
	}
	
	public void waitForTab(TABS t){
		final TABS tab = t;
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return GameTab.getOpen() == tab;
			}
		}, General.random(500, 1000));
	}
	
	public void waitFor(final boolean b, int begin, int end){
		Timing.waitCondition(new Condition() {
			public boolean active() {
				return b;
			}
		}, General.random(begin, end));
	}
	
	public boolean inHouse(){
		return !inArea(Constants.VARROCK_AREA) && Objects.findNearest(50, "Portal").length > 0;
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		
	}

	@Override
	public void serverMessageReceived(String arg0) {
		if(arg0.equals("You need a higher slayer level to know how to wound this monster.")){
			MOVE_RANDOM = true;
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		
	}
	
	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}
	
	private boolean checkActions(RSNPC object, String action) {
		if (object.getActions() != null) {
			for (String s : object.getActions())
				return s.contains(action);
		}
		return false;
	}

	public boolean clickObject(RSNPC a, String option) {
		RSNPC object = a;
		if (object != null) {
			int x = (int) object.getModel().getEnclosedArea().getBounds()
					.getCenterX();
			int y = (int) object.getModel().getEnclosedArea().getBounds()
					.getCenterY();
			Point p = new Point(x + General.random(-4, 4), y
					+ General.random(-4, 0));
			if (object.getPosition().isOnScreen()) {
				if(ChooseOption.isOpen()){
					ChooseOption.close();
				}
				Mouse.move(p);
				if (Game.getUptext().contains(option)
						&& (checkActions(object, option)) && Game.getUptext().contains("Aviansie")) {
					Mouse.click(1);
					return true;
				}
				else if (!Game.getUptext().contains(option)) {
					Mouse.click(3);
					if (ChooseOption.isOpen()
							&& ChooseOption.isOptionValid(option))
						ChooseOption.select(option);
					if (ChooseOption.isOpen()
							&& !ChooseOption.isOptionValid(option)){
						ChooseOption.close();
						Camera.turnToTile(object.getPosition());
					}
				}
			} else {
				if (Player.getPosition().distanceTo(object.getPosition()) > 4)
					WebWalking.walkTo(object.getPosition());
				if (!object.getPosition().isOnScreen())
					Camera.turnToTile(object.getPosition());
				while (Player.isMoving()) {
					sleep(50, 80);
				}
			}
		}
		return false;
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
    
	public class AvieGUI extends javax.swing.JFrame {

	    /**
	     * Creates new form AvieGUI
	     */
	    public AvieGUI() {
	        initComponents();
	    }

	    /**
	     * This method is called from within the constructor to initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is always
	     * regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initComponents() {

	        jPanel1 = new javax.swing.JPanel();
	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        foodamount = new javax.swing.JTextField();
	        jLabel3 = new javax.swing.JLabel();
	        foodusing = new javax.swing.JComboBox();
	        jButton1 = new javax.swing.JButton();
	        jLabel4 = new javax.swing.JLabel();
	        telemethod = new javax.swing.JComboBox();

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
	        jLabel1.setText("Yaw hide's Avies Killer");

	        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel2.setText("Food Amount");

	        foodamount.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                foodamountActionPerformed(evt);
	            }
	        });

	        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel3.setText("Food Using");

	        foodusing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "lobster", "salmon", "trout", "shark" }));
	        foodusing.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                foodusingActionPerformed(evt);
	            }
	        });

	        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jButton1.setText("Start");
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel4.setText("Tele method");

	        telemethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "house", "varrock" }));
	        telemethod.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                telemethodActionPerformed(evt);
	            }
	        });

	        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addGap(105, 105, 105)
	                .addComponent(jButton1)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap(40, Short.MAX_VALUE)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jLabel1)
	                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                        .addGroup(jPanel1Layout.createSequentialGroup()
	                            .addComponent(jLabel4)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(telemethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGroup(jPanel1Layout.createSequentialGroup()
	                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addComponent(jLabel2)
	                                .addComponent(jLabel3))
	                            .addGap(63, 63, 63)
	                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addComponent(foodamount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addComponent(foodusing, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
	                .addContainerGap(52, Short.MAX_VALUE))
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jLabel1)
	                .addGap(33, 33, 33)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(foodamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel3)
	                    .addComponent(foodusing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel4)
	                    .addComponent(telemethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
	                .addComponent(jButton1))
	        );

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addContainerGap())
	        );

	        pack();
	    }// </editor-fold>                        

	    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {      
	    	String food = foodusing.getSelectedItem().toString();
	    	switch(food){
	    	case "lobster":
	    		FOOD_IDS = new int[] {379}; break;
	    	case "trout":
	    		FOOD_IDS = new int[] {333}; break;
	    	case "salmon":
	    		FOOD_IDS = new int[] {329}; break;
	    	case "shark":
	    		FOOD_IDS = new int[] {385}; break;
	    	}
	    	FOOD_NUMBER = Integer.parseInt(foodamount.getText());
	    	USE_HOUSE = telemethod.getSelectedItem().toString().equals("house") ? true : false;
	        WAIT_GUI = false;
	    }                                       

	    private void foodamountActionPerformed(java.awt.event.ActionEvent evt) { 
	    }                                          

	    private void foodusingActionPerformed(java.awt.event.ActionEvent evt) {   
	    }                                         

	    private void telemethodActionPerformed(java.awt.event.ActionEvent evt) { 
	    }

	    // Variables declaration - do not modify                     
	    private javax.swing.JTextField foodamount;
	    private javax.swing.JComboBox foodusing;
	    private javax.swing.JButton jButton1;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JComboBox telemethod;
	    // End of variables declaration                   
	}
	

}
