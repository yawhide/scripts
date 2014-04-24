package scripts.MDK.Data;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Tiles {
	/**
	 * RSTiles
	 */
	public static RSTile afterStepsDown = new RSTile(1772, 5366, 0),

	trapDoorToChest = new RSTile(2309, 3215, 0);

	public static RSTile[] churchBoundaries = { new RSTile(3238, 3211, 0),
			new RSTile(3240, 3211, 0), new RSTile(3240, 3215, 0),
			new RSTile(3247, 3215, 0), new RSTile(3247, 3204, 0),
			new RSTile(3240, 3204), new RSTile(3240, 3209, 0),
			new RSTile(3238, 3209, 0) },

	toMith = { new RSTile(1775, 5361, 0), new RSTile(1777, 5355, 0),
			new RSTile(1778, 5346, 0) },

	safeSpot = { new RSTile(1776, 5346, 1), new RSTile(1777, 5344, 1) };

	/**
	 * Paths
	 */
	public static RSTile[] toChest = { new RSTile(3213, 9620, 0),
			new RSTile(3218, 9623, 0) },

	toAltar = { new RSTile(3241, 3210, 0), new RSTile(3242, 3207, 0) },

	toWhirlpool = { new RSTile(2518, 3563, 0), new RSTile(2518, 3555, 0),
			new RSTile(2517, 3546, 0), new RSTile(2516, 3537, 0),
			new RSTile(2514, 3530, 0), new RSTile(2514, 3524, 0),
			new RSTile(2511, 3518, 0), new RSTile(2511, 3512, 0) };

	/**
	 * areas because RSArea doesn't work with plane
	 */
	public static RSTile[] afterWhirlpool2 = { new RSTile(1763, 5367, 1),
			new RSTile(1768, 5365, 1) },

	greenDragArea2 = { new RSTile(1767, 5367, 0), new RSTile(1782, 5341, 0) },

	mithDragSpawn12 = { new RSTile(1776, 5348, 1), new RSTile(1784, 5338, 1) },

	lumbyArea2 = { new RSTile(3190, 3240, 0), new RSTile(3255, 3187, 0) },
	
	varrockAreaTiles = { new RSTile(3176, 3448, 0), new RSTile(3255, 3386, 0) },

	toWhirlpoolA2 = { new RSTile(2505, 3575, 0), new RSTile(2526, 3510, 0) };

	/**
	 * Positionables
	 */
	public static Positionable stepsDownToGD = new RSTile(1769, 5365, 1),

	stepsUpToMD = new RSTile(1778, 5344, 0),

	trapDoor = new RSTile(3209, 3216, 0),

	chest = new RSTile(3219, 9623, 0),

	altar = new RSTile(3243, 3207, 0),

	churchDoor = new RSTile(3238, 3210, 0),

	whirlpool = new RSTile(2512, 3511),

	whirlpoolT = new RSTile(2512, 3508),

	safeSpotSpawn1P = new RSTile(1777, 5345, 1),

	afterWhirlpoolT = new RSTile(1763, 5365, 1);

	/**
	 * RSArea
	 */
	public static RSArea afterWhirlpool = new RSArea(new RSTile(1762, 5368, 1),
			new RSTile(1769, 5364, 1)),

	greenDragArea = new RSArea(new RSTile(1767, 5367, 0), new RSTile(1782,
			5341, 0)),

	mithDragSpawn1 = new RSArea(new RSTile(1776, 5348, 1), new RSTile(1784,
			5338, 1)),

	safeSpotSpawn1 = new RSArea(safeSpot),

	lumbyArea = new RSArea(new RSTile(3190, 3240, 0), new RSTile(3255,
					3187, 0)),

	churchA = new RSArea(churchBoundaries),

	toWhirlpoolA = new RSArea(new RSTile(2505, 3575, 0), new RSTile(
					2526, 3510, 0)),

	varrockArea = new RSArea(varrockAreaTiles[0], varrockAreaTiles[1]),
					
	chestA = new RSArea(new RSTile(3219, 9624, 0), new RSTile(3208,
					9615, 0));
}
