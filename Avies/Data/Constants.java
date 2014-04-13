package scripts.Avies.Data;

import java.util.HashMap;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {
	// loot
	public static int[] JUNK = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767,
			117, 6963, 556, 829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180,
			6965, 1969, 6183, 6181, 6962, 449, 1197, 243, 314, 526 };

	public static int[] LOOT = { 9142, 1615, 1247, 1303, 1249, 1149, 2361,
			2366, 1462, 985, 987, 2363, 1617, 1213, 2363, 207, 1229, 5678,
			9431, 561,
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

	public static int[] LOOT2 = { 1615, 1247, 1303, 1249, 1149, 2361, 2366,
			1462, 985, 987, 2363, 1617, 1213, 2363, 207, 1229, 5678, 9431, 561,
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

	public static int[] ALC_LOOT = { 1213, 1229, 5678, 9431 };

	public static HashMap<Integer, String> LOOT_MAPPING = new HashMap<Integer, String>(
			LOOT.length);

	public static String[] LOOT_STRING_NAMES = { "Mithril bolts", "Dragonstone",
			"Rune spear", "Rune longsword", "Dragon spear", "Dragon med helm",
			"Adamantite bar", "Shield left half", "Nature talisman",
			"Tooth half of key", "Loop half of key", "Runite bar",
			"Uncut diamond", "Rune dagger", "Rune bar", "Herb",
			"Rune dagger(p)", "Rune dagger(p+)", "Runite limbs", "Nature rune",
			"Clue scroll (hard)" };

	// RSPaths
	public static RSTile[] TO_FIRST_SLIDE = { new RSTile(2889, 3683, 0),
			new RSTile(2884, 3679, 0), new RSTile(2881, 3673, 0),
			new RSTile(2887, 3668, 0), new RSTile(2885, 3665, 0),
			new RSTile(2880, 3668, 0) };
	public static RSTile[] BEFORE_PRAY_RANGE_SPOT = { new RSTile(2878, 3664, 0),
			new RSTile(2876, 3667, 0), new RSTile(2873, 3669, 0),
			new RSTile(2872, 3672, 0), new RSTile(2872, 3676, 0),
			new RSTile(2871, 3679, 0), new RSTile(2871, 3682, 0),
			new RSTile(2870, 3686, 0), new RSTile(2872, 3689, 0),
			new RSTile(2873, 3691, 0) };
	public static RSTile[] BEFORE_BOULDER = { new RSTile(2876, 3693, 0),
			new RSTile(2879, 3695, 0), new RSTile(2881, 3698, 0),
			new RSTile(2883, 3701, 0), new RSTile(2887, 3702, 0),
			new RSTile(2891, 3703, 0), new RSTile(2895, 3703, 0),
			new RSTile(2897, 3701, 0), new RSTile(2899, 3702, 0),
			new RSTile(2899, 3705, 0), new RSTile(2899, 3708, 0),
			new RSTile(2899, 3711, 0), new RSTile(2899, 3713, 0) };
	public static RSTile[] TO_GWD = { new RSTile(2899, 3721, 0),
			new RSTile(2902, 3724, 0), new RSTile(2904, 3726, 0),
			new RSTile(2906, 3729, 0), new RSTile(2907, 3731, 0),
			new RSTile(2908, 3732, 0), new RSTile(2908, 3733, 0),
			new RSTile(2909, 3734, 0), new RSTile(2909, 3735, 0),
			new RSTile(2910, 3738, 0), new RSTile(2912, 3742, 0),
			new RSTile(2915, 3742, 0), new RSTile(2913, 3744, 0),
			new RSTile(2916, 3746, 0) };
	public static RSTile[] TO_AVIES = { new RSTile(2883, 5311, 2),
			new RSTile(2879, 5310, 2), new RSTile(2873, 5309, 2),
			new RSTile(2867, 5307, 2), new RSTile(2861, 5307, 2),
			new RSTile(2856, 5304, 2), new RSTile(2857, 5299, 2),
			new RSTile(2861, 5295, 2), new RSTile(2865, 5294, 2),
			new RSTile(2871, 5293, 2), new RSTile(2885, 5309, 2),
			new RSTile(2890, 5309, 2), new RSTile(2893, 5308, 2),
			new RSTile(2896, 5304, 2), new RSTile(2896, 5298, 2),
			new RSTile(2896, 5296, 2), new RSTile(2896, 5294, 2),
			new RSTile(2892, 5291, 2), new RSTile(2888, 5291, 2),
			new RSTile(2881, 5307, 2), new RSTile(2881, 5304, 2),
			new RSTile(2882, 5300, 2), new RSTile(2880, 5298, 2),
			new RSTile(2880, 5295, 2), new RSTile(2878, 5291, 2),
			new RSTile(2878, 5288, 2) };

	// Positionables
	public static Positionable FIRST_ROCK_SLIDE = new RSTile(2879, 3668, 0);
	public static String FIRST_ROCK_SLIDE_ACTION = "Climb"; // object is Rocks
	public static Positionable BANK_TILE = new RSTile(3185, 3438, 0);
	public static Positionable GWD_CENTER = new RSTile(2901, 5283, 2);
	public static Positionable HOLE_TILE = new RSTile(2918, 3746, 0);
	public static Positionable RANDOM_EAST_TILE = new RSTile(2862, 5287, 2);
	public static Positionable RANDOM_WEST_TILE = new RSTile(2887, 5283, 2);

	// RSAreas
	public static RSTile[] TROLL_TELEPORT_POLYGON = { new RSTile(2879, 3667, 0),
			new RSTile(2880, 3667, 0), new RSTile(2886, 3662, 0),
			new RSTile(2890, 3662, 0), new RSTile(2894, 3665, 0),
			new RSTile(2898, 3668, 0), new RSTile(2903, 3674, 0),
			new RSTile(2903, 3683, 0), new RSTile(2902, 3685, 0),
			new RSTile(2897, 3690, 0), new RSTile(2895, 3691, 0),
			new RSTile(2891, 3691, 0), new RSTile(2889, 3690, 0),
			new RSTile(2885, 3691, 0), new RSTile(2879, 3685, 0),
			new RSTile(2878, 3679, 0), new RSTile(2877, 3678, 0),
			new RSTile(2877, 3671, 0), new RSTile(2878, 3668, 0) };
	public static RSTile[] FIRST_SLIDE_POLYGON = { new RSTile(2877, 3671, 0),
			new RSTile(2877, 3668, 0), new RSTile(2879, 3667, 0),
			new RSTile(2880, 3667, 0), new RSTile(2883, 3665, 0),
			new RSTile(2883, 3666, 0), new RSTile(2880, 3670, 0),
			new RSTile(2879, 3671, 0) };
	public static RSTile[] VARROCK_AREA_TILES = { new RSTile(3176, 3448, 0),
			new RSTile(3255, 3386, 0) };
	public static RSTile[] CWA = { new RSTile(2436, 3095, 0),
			new RSTile(2443, 3082, 0) };
	public static RSTile[] AVIES_AREA_TILES = { new RSTile(2847, 5307, 2),
			new RSTile(2901, 5281, 2) };

	public static RSArea TROLL_TELEPORT_AREA = new RSArea(TROLL_TELEPORT_POLYGON);
	public static RSArea ROCK_SLIDE_AREA = new RSArea(FIRST_SLIDE_POLYGON);
	public static RSArea VARROCK_AREA = new RSArea(VARROCK_AREA_TILES[0], VARROCK_AREA_TILES[1]);
	public static RSArea CW_AREA = new RSArea(CWA[0], CWA[1]);
	public static RSArea AVIES_Area = new RSArea(AVIES_AREA_TILES[0], AVIES_AREA_TILES[1]);

	// Variables
	public static final int[] PRAYER_POT = { 2434, 139, 141, 143 };
	public static final int[] RANGE_POT = { 169, 2444, 171, 173 };
	public static final int NAT = 561;
	public static final int FIRE = 554;
	public static final int LAW = 563;
	public static final int FTAB = 8009;
	public static final int VTAB = 8007;
	public static final int HTAB = 8013;
	public static final int ADDY = 2361;
	public static final int COIN = 995;
	public static final int RANARR = 207;
	
}
