package scripts.BDK.Data;

import java.util.HashMap;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {
	// loot
	public static int[] JUNK = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767,
			117, 6963, 554, 556, 829, 1971, 687, 464, 1973, 1917, 808, 1454,
			6180, 6965, 1969, 6183, 6181, 6962, 449, 1197, 243, 995 };

	public static int[] LOOT = { 1369, 816, 9142, 1751, 536, 868, 563, 1615,
			1247, 1303, 1249, 1123, 1149, 561, 563, 2361, 2366, 1462, 985, 987,
			2363, 1617, 1619, 1161, 1213, 2363, 1642, 207, 209, 211, 213, 215,
			217, 219, 2485, 3049, 3051,
			/* clue scroll id starts */
			2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 };

	public static int[] LOOT2 = { 1369, 816, 1751, 536, 868, 563, 1615, 1247,
			1303, 1249, 1123, 1149, 561, 563, 2361, 2366, 1462, 985, 987, 2363,
			1617, 1619, 1161, 1213, 2363, 1642, 207, 209, 211, 213, 215, 217,
			219, 2485, 3049, 3051,
			/* clue scroll id starts */
			2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 };

	public static HashMap<Integer, String> LOOT_MAP = new HashMap<Integer, String>(
			LOOT.length);

	public static String[] LOOT_NAMES = { "Mithril battleaxe",
			"Adamant dart(p)", "Mithril bolts", "Blue dragonhide",
			"Dragon bones", "Rune knife", "Law rune", "Dragonstone",
			"Rune spear", "Rune longsword", "Dragon spear",
			"Adamant platebody", "Dragon med helm", "Nature rune", "Law rune",
			"Adamantite bar", "Shield left half", "Nature talisman",
			"Tooth half of key", "Loop half of key", "Runite bar",
			"Uncut diamond", "Uncut ruby", "Adamant full helm", "Rune dagger",
			"Rune bar", "Nature talisman", "Herb", "Herb", "Herb", "Herb",
			"Herb", "Herb", "Herb", "Herb", "Herb", "Herb",
			"Clue scroll (hard)" };

	public static final int[] BLUE_DRAGON_IDS = { 5030, 5033 }, // ids have to
																// be used
																// because I
																// need specific
																// dragons, not
																// all
			DRAGON_IDS = { 5030, 5033, 5003, 5002, 5004 },

			PRAYER_POTS = { 2434, 139, 141, 143 }, RANGE_POTS = { 169, 2444,
					171, 173 };

	public static final int EAST_DRAGON_ID = 5033; // east drag

}
