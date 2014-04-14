/**
 * Tiles.java
 * 
 * Yaw hide
 */

package scripts.BDK.Data;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Tiles {
	// RSTile for paths
	public static RSTile[] TO_TAVERNLY_DUNGEON_PATH = {
			new RSTile(2928, 3357, 0), new RSTile(2925, 3363, 0),
			new RSTile(2921, 3370, 0), new RSTile(2914, 3373, 0),
			new RSTile(2907, 3377, 0), new RSTile(2901, 3381, 0),
			new RSTile(2895, 3386, 0), new RSTile(2892, 3391, 0),
			new RSTile(2885, 3392, 0), new RSTile(2884, 3396, 0) };

	// RSTiles
	public static RSTile[] TO_TAV_LADDER_AREA_TILES = {
			new RSTile(2877, 3403, 0), new RSTile(2935, 3346, 0) },

			FALLY_AREA_TILES = { new RSTile(2936, 3390, 0),
					new RSTile(3030, 3323, 0) }, BANK_AREA_TILES = {
					new RSTile(2949, 3368, 0), new RSTile(2943, 3368, 0),
					new RSTile(2943, 3371, 0), new RSTile(2949, 3371, 0) },

			LOW_WALL_AREA_TILES = { new RSTile(2936, 3358, 0),
					new RSTile(2940, 3358, 0), new RSTile(2940, 3352, 0),
					new RSTile(2936, 3352, 0) }, TAV_LADDER_AREA_TILES = {
					new RSTile(2881, 3400, 0), new RSTile(2888, 3393, 0) },

			TAV_DUNGEON_LADDER_AREA_TILES = { new RSTile(2881, 9801, 0),
					new RSTile(2887, 9794, 0) }, BLUE_DRAG_AREA_TILES = {
					new RSTile(2889, 9787, 0), new RSTile(2889, 9813, 0),
					new RSTile(2924, 9812, 0), new RSTile(2924, 9787, 0) },

			SOUTH_SAFE_SPOT_AREA_TILES = { new RSTile(2893, 9792, 0),
					new RSTile(2894, 9791, 0) },

			NORTHWEST_SAFESPOT_AREA_TILES = { new RSTile(2900, 9809, 0),
					new RSTile(2902, 9810, 0) },

			NORTHEAST_SAFESPOT_AREA_TILES = { new RSTile(2903, 9808, 0),
					new RSTile(2904, 9808, 0), new RSTile(2904, 9810, 0),
					new RSTile(2900, 9810, 0), new RSTile(2900, 9809, 0),
					new RSTile(2903, 9809, 0) };

	// Positionables
	public static Positionable FALLY_BANK_TILE = new RSTile(2946, 3369, 0),
			FALLY_TELEPORT_TILE = new RSTile(2965, 3376, 0),
			LOWWALL_TILE = new RSTile(2935, 3355, 0),
			LOWWALL_TILE2 = new RSTile(2938, 3355, 0),
			AFTER_LOWWALL_TILE = new RSTile(2934, 3355, 0),
			TAV_LADDER_TILE = new RSTile(2884, 3397, 0),
			TAV_DUNG_LADDER_TILE = new RSTile(2884, 9798, 0),
			SAFESPOT_TILE = new RSTile(2903, 9808, 0);

	// RSAreas
	public static RSArea FALLY_AREA = new RSArea(FALLY_AREA_TILES[0],
			FALLY_AREA_TILES[1]),

			TO_TAV_LADDER_AREA = new RSArea(TO_TAV_LADDER_AREA_TILES[0],
					TO_TAV_LADDER_AREA_TILES[1]),

			FALLY_BANK_AREA = new RSArea(BANK_AREA_TILES),

			LOWWALL_AREA = new RSArea(LOW_WALL_AREA_TILES),
			TAV_LADDER_AREA = new RSArea(TAV_LADDER_AREA_TILES[0],
					TAV_LADDER_AREA_TILES[1]),

			TAV_DUNG_LADDER_AREA = new RSArea(TAV_DUNGEON_LADDER_AREA_TILES[0],
					TAV_DUNGEON_LADDER_AREA_TILES[1]),

			BLUE_DRAG_AREA = new RSArea(BLUE_DRAG_AREA_TILES),

			NORTHWEST_SAFESPOT_AREA = new RSArea(
					NORTHWEST_SAFESPOT_AREA_TILES[0],
					NORTHWEST_SAFESPOT_AREA_TILES[1]),

			NORTHEAST_SAFESPOT_AREA = new RSArea(NORTHEAST_SAFESPOT_AREA_TILES);
}
