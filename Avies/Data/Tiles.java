/**
 * Tiles.java
 * 
 * Yaw hide
 */

package scripts.Avies.Data;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Tiles {
	// RSPaths
		public static RSTile[] TO_FIRST_SLIDE = { new RSTile(2889, 3683, 0),
				new RSTile(2884, 3679, 0), new RSTile(2881, 3673, 0),
				new RSTile(2887, 3668, 0), new RSTile(2885, 3665, 0),
				new RSTile(2880, 3668, 0) },

		BEFORE_PRAY_RANGE_SPOT = { new RSTile(2878, 3664, 0),
				new RSTile(2876, 3667, 0), new RSTile(2873, 3669, 0),
				new RSTile(2872, 3672, 0), new RSTile(2872, 3676, 0),
				new RSTile(2871, 3679, 0), new RSTile(2871, 3682, 0),
				new RSTile(2870, 3686, 0), new RSTile(2872, 3689, 0),
				new RSTile(2873, 3691, 0) },

		BEFORE_BOULDER = { new RSTile(2876, 3693, 0), new RSTile(2879, 3695, 0),
				new RSTile(2881, 3698, 0), new RSTile(2883, 3701, 0),
				new RSTile(2887, 3702, 0), new RSTile(2891, 3703, 0),
				new RSTile(2895, 3703, 0), new RSTile(2897, 3701, 0),
				new RSTile(2899, 3702, 0), new RSTile(2899, 3705, 0),
				new RSTile(2899, 3708, 0), new RSTile(2899, 3711, 0),
				new RSTile(2899, 3713, 0) }, TO_GWD = { new RSTile(2899, 3721, 0),
				new RSTile(2902, 3724, 0),

				new RSTile(2904, 3726, 0), new RSTile(2906, 3729, 0),
				new RSTile(2907, 3731, 0), new RSTile(2908, 3732, 0),
				new RSTile(2908, 3733, 0), new RSTile(2909, 3734, 0),
				new RSTile(2909, 3735, 0), new RSTile(2910, 3738, 0),
				new RSTile(2912, 3742, 0), new RSTile(2915, 3742, 0),
				new RSTile(2913, 3744, 0), new RSTile(2916, 3746, 0) },

		TO_AVIES = { new RSTile(2883, 5311, 2), new RSTile(2879, 5310, 2),
				new RSTile(2873, 5309, 2), new RSTile(2867, 5307, 2),
				new RSTile(2861, 5307, 2), new RSTile(2856, 5304, 2),
				new RSTile(2857, 5299, 2), new RSTile(2861, 5295, 2),
				new RSTile(2865, 5294, 2), new RSTile(2871, 5293, 2),
				new RSTile(2885, 5309, 2), new RSTile(2890, 5309, 2),
				new RSTile(2893, 5308, 2), new RSTile(2896, 5304, 2),
				new RSTile(2896, 5298, 2), new RSTile(2896, 5296, 2),
				new RSTile(2896, 5294, 2), new RSTile(2892, 5291, 2),
				new RSTile(2888, 5291, 2), new RSTile(2881, 5307, 2),
				new RSTile(2881, 5304, 2), new RSTile(2882, 5300, 2),
				new RSTile(2880, 5298, 2), new RSTile(2880, 5295, 2),
				new RSTile(2878, 5291, 2), new RSTile(2878, 5288, 2) };

		// Positionables
		public static Positionable FIRST_ROCK_SLIDE = new RSTile(2879, 3668, 0),
					BANK_TILE = new RSTile(3185, 3438, 0),
					GWD_CENTER = new RSTile(2901, 5283, 2),
					HOLE_TILE = new RSTile(2918, 3746, 0),
					RANDOM_EAST_TILE = new RSTile(2862, 5287, 2),
					RANDOM_WEST_TILE = new RSTile(2887, 5283, 2);

		// RSAreas
		public static RSTile[] TROLL_TELEPORT_POLYGON = {
				new RSTile(2879, 3667, 0), new RSTile(2880, 3667, 0),
				new RSTile(2886, 3662, 0), new RSTile(2890, 3662, 0),
				new RSTile(2894, 3665, 0), new RSTile(2898, 3668, 0),
				new RSTile(2903, 3674, 0), new RSTile(2903, 3683, 0),
				new RSTile(2902, 3685, 0), new RSTile(2897, 3690, 0),
				new RSTile(2895, 3691, 0), new RSTile(2891, 3691, 0),
				new RSTile(2889, 3690, 0), new RSTile(2885, 3691, 0),
				new RSTile(2879, 3685, 0), new RSTile(2878, 3679, 0),
				new RSTile(2877, 3678, 0), new RSTile(2877, 3671, 0),
				new RSTile(2878, 3668, 0) },

		FIRST_SLIDE_POLYGON = { new RSTile(2877, 3671, 0),
				new RSTile(2877, 3668, 0), new RSTile(2879, 3667, 0),
				new RSTile(2880, 3667, 0), new RSTile(2883, 3665, 0),
				new RSTile(2883, 3666, 0), new RSTile(2880, 3670, 0),
				new RSTile(2879, 3671, 0) },

				VARROCK_AREA_TILES = { new RSTile(3176, 3448, 0),
						new RSTile(3255, 3386, 0) },

				CWA = { new RSTile(2436, 3095, 0), new RSTile(2443, 3082, 0) },

				AVIES_AREA_TILES = { new RSTile(2847, 5307, 2),
						new RSTile(2901, 5281, 2) };

		public static RSArea TROLL_TELEPORT_AREA = new RSArea(
				TROLL_TELEPORT_POLYGON),

		ROCK_SLIDE_AREA = new RSArea(FIRST_SLIDE_POLYGON),

		VARROCK_AREA = new RSArea(VARROCK_AREA_TILES[0], VARROCK_AREA_TILES[1]),

		CW_AREA = new RSArea(CWA[0], CWA[1]),

		AVIES_AREA = new RSArea(AVIES_AREA_TILES[0], AVIES_AREA_TILES[1]);

}
