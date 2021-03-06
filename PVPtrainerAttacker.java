package scripts;

import java.awt.Graphics;
import java.util.Date;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Prayer.PRAYERS;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.BDK.Data.Constants;
import scripts.BDK.Utilities.YawsGeneral;

@ScriptManifest(authors = { "Yaw hide" }, category = "Combat", name = "PVPtrainerAttack", version = 1.21)
public class PVPtrainerAttacker extends Script implements Painting {

	boolean scriptStatus = true;
	ABCUtil abc_util = null;
	int style = 2;
	long startTime;
	final int[] XP = { Skills.getXP(SKILLS.ATTACK),
			Skills.getXP(SKILLS.STRENGTH), Skills.getXP(SKILLS.DEFENCE),
			Skills.getXP(SKILLS.RANGED), Skills.getXP(SKILLS.PRAYER),
			Skills.getXP(SKILLS.MAGIC), Skills.getXP(SKILLS.HITPOINTS),
			Skills.getXP(SKILLS.SLAYER) };
	final SKILLS[] Names = { SKILLS.ATTACK, SKILLS.STRENGTH, SKILLS.DEFENCE,
			SKILLS.RANGED, SKILLS.PRAYER, SKILLS.MAGIC, SKILLS.HITPOINTS,
			SKILLS.SLAYER };
	int[] startLvs = { Skills.getActualLevel(SKILLS.ATTACK),
			Skills.getActualLevel(SKILLS.STRENGTH),
			Skills.getActualLevel(SKILLS.DEFENCE),
			Skills.getActualLevel(SKILLS.RANGED),
			Skills.getActualLevel(SKILLS.PRAYER),
			Skills.getActualLevel(SKILLS.MAGIC),
			Skills.getActualLevel(SKILLS.HITPOINTS),
			Skills.getActualLevel(SKILLS.SLAYER), };
	int startLvStr, startLvHP;
	RSTile t = new RSTile(3426, 2930, 0);

	@Override
	public void onPaint(Graphics g) {
		g.drawString("version: 1.2", 5, 100);
		g.drawString(
				(abc_util.TIME_TRACKER.CHECK_XP.next() - System
						.currentTimeMillis()) + "", 5, 115);
		g.drawString(
				RSUtil.getXpGainedAnHour(SKILLS.STRENGTH, XP[1], startTime),
				360, 20);
		g.drawString(
				RSUtil.getXpGainedAnHour(SKILLS.HITPOINTS, XP[6], startTime),
				360, 35);
		g.drawString(
				"Str: "
						+ startLvStr
						+ "("
						+ (Skills.getActualLevel(SKILLS.STRENGTH) - startLvStr)
						+ ")  "
						+ Math.round((Skills.getXPToLevel(SKILLS.STRENGTH,
								Skills.getActualLevel(SKILLS.STRENGTH) + 1)
								/ (double) RSUtil.getPerHour(
										Skills.getXP(SKILLS.STRENGTH) - XP[1],
										startTime) * 60)), 360, 50);
		g.drawString(
				"HP: "
						+ startLvHP
						+ "("
						+ (Skills.getActualLevel(SKILLS.HITPOINTS) - startLvHP)
						+ ")  "
						+ Math.round((Skills.getXPToLevel(SKILLS.HITPOINTS,
								Skills.getActualLevel(SKILLS.HITPOINTS) + 1)
								/ (double) RSUtil.getPerHour(
										Skills.getXP(SKILLS.HITPOINTS) - XP[6],
										startTime) * 60)), 360, 65);
		Date date = new Date(la);
		
	}

	long la, currHp;

	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		abc_util = new ABCUtil();
		General.useAntiBanCompliance(true);
		startLvStr = Skills.getActualLevel(SKILLS.STRENGTH);
		startLvHP = Skills.getActualLevel(SKILLS.HITPOINTS);
		updateLA();
		ThreadSettings.get().setAlwaysRightClick(true);
		currHp = General.random(General.random(50, 55), 99);
		
		while (scriptStatus) {
			
			if (getHp() <= currHp && !Player.getRSPlayer().getName().equals("DJadamellis")){
				if(ChooseOption.isOpen()){
					ChooseOption.close();
					sleep(200,300);
				}
				clickAlter();
			}
			
			if (Combat.getTargetEntity() != null
					&& Combat.getTargetEntity().getHealth() < 10)
				Clicking.click("Walk here", Player.getRSPlayer().getPosition());
			if (System.currentTimeMillis() - la > General
					.random(100000, 240000)) {
				println("Time for some antiban to stay logged in");
				doSomething();
			}
			checkAntiBan();
			checkChat();

			if (Skills.getCurrentLevel(SKILLS.PRAYER) / (double) Skills.getActualLevel(SKILLS.PRAYER)
					* 100 < General.random(20, 50)) {
				clickAlter();
			}
			else if (Skills.getActualLevel(SKILLS.PRAYER) >= 25 && (!Prayer.isPrayerEnabled(PRAYERS.PROTECT_ITEMS) || 
					!Prayer.isPrayerEnabled(PRAYERS.SUPERHUMAN_STRENGTH) ||
					!Prayer.isPrayerEnabled(PRAYERS.IMPROVED_REFLEXES))){
				GameTab.open(TABS.PRAYERS);
				sleep(200);
				Prayer.enable(PRAYERS.PROTECT_ITEMS);
				sleep(200);
				Prayer.enable(PRAYERS.SUPERHUMAN_STRENGTH);
				sleep(200);
				Prayer.enable(PRAYERS.IMPROVED_REFLEXES);
			}
			if (Player.getRSPlayer().getInteractingCharacter() == null) {
				if (checkCorrectPlayer()) {
					if (clickNPC(findPlayer(), "Attack")) {
						Timing.waitCondition(new Condition() {
							public boolean active() {
								return Player.getRSPlayer()
										.getInteractingCharacter() != null;
							}
						}, General.random(2000, 3000));
					}
				}
			}

			sleep(100);
		}
	}
	
	public boolean checkCorrectPlayer(){
		if(Player.getRSPlayer().getName().equals("sortinghash")){
			return Players.find("arminvanbuu2").length > 0;
		}
		else if (Player.getRSPlayer().getName().equals("DJadamellis")){
			return Players.find("arminvanbuu2").length > 0;
		}
		else{
			return Players.find("sortinghash").length > 0;
		}
	}
	
	public RSPlayer findPlayer(){
		return Player.getRSPlayer().getName().equals("sortinghash") || 
				Player.getRSPlayer().getName().equals("DJadamellis")
				? Players.find("arminvanbuu2")[0] : Players.find("sortinghash")[0];
	}

	public void checkAntiBan() {
		if (abc_util.TIME_TRACKER.CHECK_XP.next() <= System.currentTimeMillis()
				&& abc_util.TIME_TRACKER.CHECK_XP.next() != 0) {
			println("checking xp");
			checkXp();
			abc_util.TIME_TRACKER.CHECK_XP.reset();
			updateLA();
		}
		if (abc_util.TIME_TRACKER.ROTATE_CAMERA.next() != 0
				&& abc_util.TIME_TRACKER.ROTATE_CAMERA.next() <= System
						.currentTimeMillis()) {
			println("rotating camera");
			Camera.setCameraRotation(General.random(0, 355));
			abc_util.TIME_TRACKER.ROTATE_CAMERA.reset();
			updateLA();
		}
		if (abc_util.TIME_TRACKER.EXAMINE_OBJECT.next() != 0
				&& abc_util.TIME_TRACKER.EXAMINE_OBJECT.next() <= System
						.currentTimeMillis()) {
			println("Examining a random object");
			RSObject[] random = Objects.getAll(4);
			if (random.length > 0) {
				Clicking.click("Examine", random);
			}
			abc_util.TIME_TRACKER.EXAMINE_OBJECT.reset();
			updateLA();
		}
	}

	public void checkChat() {
		if (NPCChat.getMessage() != null) {
			NPCChat.clickContinue(true);
			updateLA();
		}
	}

	public void checkXp() {
		GameTab.open(TABS.STATS);
		sleep(200, 300);
		if (style == 2)
			Mouse.moveBox(555, 243, 604, 264);
		else if (style == 1)
			Mouse.moveBox(554, 211, 603, 232);
		else if (style == 4)
			Mouse.moveBox(552, 306, 604, 325);
		updateLA();
		sleep(2000, 5000);
		GameTab.open(TABS.INVENTORY);
	}

	public void updateLA() {
		la = System.currentTimeMillis();
	}

	public void doSomething() {
		int i = General.random(0, 5);
		if (i == 0) {
			checkXp();
		} else if (i == 1) {
			randomRightClick();
		} else if (i == 2) {
			RSObject[] random = Objects.getAll(10);
			if (random.length > 0) {
				Clicking.click("Examine",
						random[General.random(0, random.length - 1)]);
			}
			updateLA();
		} else if (i == 3) {
			Camera.setCameraRotation(General.random(0, 355));
			updateLA();
		} else if (i == 4) {
			checkFriends();
		}
	}

	public void checkFriends() {
		GameTab.open(TABS.FRIENDS);
		sleep(1000, 5000);
		GameTab.open(TABS.INVENTORY);
		updateLA();
	}

	public void randomRightClick() {
		Mouse.move(General.random(40, 760), General.random(23, 458));
		sleep(200, 300);
		Mouse.click(3);
		sleep(200, 300);
		ChooseOption.close();
		updateLA();
	}

	public void clickAlter() {
		RSObject[] altar2 = Objects.getAt(t);
		if (altar2.length > 0) {
			if (altar2[0].isOnScreen()) {
				Clicking.click("Pray-at", altar2[0]);
				sleep(200);
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return getHp() >= 100;
					}
				}, General.random(1000, 2000));
			} else {
				Walking.walkPath(Walking.generateStraightPath(t));
				sleep(1000, 1200);
			}
		}
	}

	public int getHp() {
		return Combat.getHPRatio();
	}
	
	public static boolean clickNPC(RSPlayer rsPlayer, String option) {

		RSTile loc = null;
		if (rsPlayer != null && rsPlayer.isOnScreen()) {
			loc = rsPlayer.getAnimablePosition();
			Mouse.move(Projection.tileToScreen(loc, 10));
			if(Game.isUptext("Walk here / 4 more options")){
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
			else if (Game.isUptext(option)) {
				Mouse.click(1);
				return true;
			} else {
				Mouse.click(3);
				if (ChooseOption.isOpen()) {
					ChooseOption.select(option);
				}
			}
		}
		return false;
	}

}
