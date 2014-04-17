package scripts.MDK.Utilities;

import java.util.HashMap;

public class Looting {

	public static int[] loot = { 11335, 11286, diamondEBolt, rubyEBolt,
			addyBolt, 11338, 1615, 1631, 1149, 1249, 2366, 1319, 1201, 985,
			987, 1373, 1163, 1147, 566, 565, 561, 1601, 1617, 2363, 1432, 1247,
			868, 9144 },

	badLoot = { 443, 1462, 892, 811, 817, },

	conditionalLoot = { 385, 11497, 11499, 11465, 11467, 11465, 11467 },

	clue = { 2722, 2723, 2725, 2727, 2729, 2731, 2733, 2725, 2737, 2739, 2741,
			2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785,
			2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 3520, 3522,
			3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542,
			3544, 3546, 3548, 3560, 3562, 3564, 3566, 3568, 3570, 3272, 3573,
			3574, 3575, 3577, 3579, 3580, 7239, 7241, 7243, 7245, 7247, 7248,
			7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262,
			7264, 7266, 7268, 7270, 7272, 10234, 10236, 10238, 10240, 10242,
			10244, 10426, 10428, 40250, 10252, 13010, 13012, 13014, 13016,
			13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034,
			13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049 },

	junk = { 886, 1539, 9003, 229, 1623, 1355, 440, 7767, 117, 6963, 554, 556,
			829, 1971, 687, 464, 1973, 1917, 808, 1454, 6180, 6965, 1969, 6183,
			6181, 6962, 865, 41 };

	public static String[] names = { "Dragon full helm", "Draconic visage",
			"Diamond bolts (e)", "Ruby bolts (e)", "Adamant bolts",
			"Chewed bones", "Dragon med helm", "Dragon spear",
			"Shield left half", "Rune 2h sword", "Rune kiteshield",
			"Dragonstone", "Uncut dragonstone", "Half of a key",
			"Half of a key", "Rune battleaxe", "Rune full helm",
			"Rune med helm", "Soul rune", "Blood rune", "Nature rune",
			"Diamond", "Uncut diamond", "Runite bar", "Rune mace",
			"Rune spear", "Rune knife", "Runite bolts" },

	badNames = { "Silver ore", "Nature talisman", "Rune arrow", "Rune dart",
			"Rune dart(p)" },

	conditionNames = { "Shark", "Super defence mix(2)", "Super defence mix(1)",
			"Prayer mix(2)", "Prayer mix(1)", "Restore prayer mix(2)",
			"Restore prayer mix(1)" },

	clueStr = { "Clue scroll" },

	priorityLoot = { "Dragon full helm", "Draconic visage" };

	public static HashMap<Integer, String> map = new HashMap<Integer, String>(
			loot.length),
			mapBad = new HashMap<Integer, String>(badLoot.length),
			mapConditional = new HashMap<Integer, String>(
					conditionalLoot.length),
			mapClue = new HashMap<Integer, String>(clue.length);
}
