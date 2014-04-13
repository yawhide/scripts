package scripts;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

public class RSUtil {
    /**
     * Gets the per hour value for the specified value.
     * @param value The value to evaluate.
     * @param startTime The time in ms when the script started.
     * @return The per hour value.
     */
    public static long getPerHour(final int value, final long startTime) {
        return (long)(value * 3600000D / (System.currentTimeMillis() - startTime));    
    }
    
    /**
     * Gets the xp gained for the specified skill based on the start xp.
     * @param skill The skill to get the xp gained.
     * @param startXp The starting xp of the skill.
     * @return The xp gained.
     */
    public static String getXpGainedAnHour(final Skills.SKILLS skill, final int startXp, final long startTime) {
        final int xp = skill.getXP() - startXp;
        return "" + xp + " (" + getPerHour(xp, startTime) + ")";
    }

    /**
     * Gets the players current hit points.
     * @return the players current hit points.
     */
    public static int getPlayerHPPercent() {
        return (int) (100.0 * ((double)Skills.SKILLS.HITPOINTS.getCurrentLevel() / (double)Skills.SKILLS.HITPOINTS.getActualLevel()));
    }

    /**
     * Gets the the Image from the specified url.
     * @param url The url to get the Image from.
     * @return The Image retrieved; null if no Image was found. 
     */
    public static Image getImageFromUrl(final String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            General.println("Failed to retreive image from: " + url);
            return null;
        }
    }

    /**
     * Gets the nearest RSObject with the specified name.
     * @param name The name of the RSObject to search for.
     * @param dist The distance away from the player.
     * @return the nearest RSObject with the specified name; null of no objects were found.
     */
    public static RSObject getObject(final String name, final int dist) {
        final RSObject[] objs = Objects.findNearest(dist, name);
        return objs.length > 0 ? objs[0] : null;
    }
    
    /**
     * Gets the nearest RSObject with the specified id.
     * @param id The id of the RSObject to search for.
     * @param dist The distance away from the player.
     * @return the nearest RSObject with the specified id; null of no objects were found.
     */
    public static RSObject getObject(final int id, final int dist) {
        final RSObject[] objs = Objects.findNearest(dist, id);
        return objs.length > 0 ? objs[0] : null;
    }
    
    /**
     * Gets the first item in the Inventory that matches the specified id.
     * @param id The id of the item to search for.
     * @return the first item in the Inventory that matches the specified id;
     * null of no items were found.
     */
    public static RSItem getItem(final int id) {
        final RSItem[] inv = Inventory.find(id);
        return (inv != null && inv.length > 0) ? inv[0] : null;
    }
    
    /**
     * Gets the first item in the Inventory that matches the specified name.
     * @param name The name of the item to search for.
     * @return the first item in the Inventory that matches the specified name;
     * null of no items were found.
     */
    public static RSItem getItem(final String name) {
        final RSItem[] inv = Inventory.find(name);
        return (inv != null && inv.length > 0) ? inv[0] : null;
    }
    
    /**
     * Checks to see if the inventory contains the specified item.
     * @param id The id of the item.
     * @return true if the inventory contains the item; false otherwise.
     */
    public static boolean hasItem(final int id) {
        final RSItem[] inv = Inventory.find(id);
        return inv != null && inv.length > 0;
    }
}