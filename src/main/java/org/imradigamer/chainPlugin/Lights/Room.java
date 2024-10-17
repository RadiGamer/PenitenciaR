package org.imradigamer.chainPlugin.Lights;

import org.bukkit.Location;

public class Room {

    private Location corner1;
    private Location corner2;
    private int maxLightLevel;

    public Room(Location corner1, Location corner2, int maxLightLevel) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.maxLightLevel = maxLightLevel;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public int getMaxLightLevel() {
        return maxLightLevel;
    }
}
