package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class DoorManager {

    private final Plugin plugin;
    private final DoorAnimator doorAnimator;

    // Coordinates defining the boundaries for door movements
    private final Location minBound = new Location(null, 111, 52, 0); // Minimum coordinate bounds
    private final Location maxBound = new Location(null, 136, 60, 4); // Maximum coordinate bounds

    // Custom Model Data constants for the left and right doors
    private static final int LEFT_DOOR_CUSTOM_MODEL_DATA = 58;
    private static final int RIGHT_DOOR_CUSTOM_MODEL_DATA = 57;

    // Configurable slide distance for door movement (you can set this dynamically)
    private final double slideDistance = 2.0;  // For example, 2 blocks

    public DoorManager(Plugin plugin) {
        this.plugin = plugin;
        this.doorAnimator = new DoorAnimator(plugin);
    }

    // Find and open the doors
    public void openDoors() {
        List<Door> doors = findDoors();
        for (Door door : doors) {
            if (!door.isOpen()) {
                doorAnimator.animateDoor(door, true, slideDistance);
            }
        }
    }

    // Find and close the doors
    public void closeDoors() {
        List<Door> doors = findDoors();
        for (Door door : doors) {
            if (door.isOpen()) {
                doorAnimator.animateDoor(door, false, slideDistance);
            }
        }
    }

    // Find all doors that match the criteria (within bounds and have custom model data)
    private List<Door> findDoors() {
        List<Door> doors = new ArrayList<>();
        World world = Bukkit.getWorld("world");  // Adjust if the world is named differently

        if (world == null) {
            return doors;
        }

        for (Entity entity : world.getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay itemDisplay = (ItemDisplay) entity;
                ItemMeta meta = itemDisplay.getItemStack().getItemMeta();

                // Check if entity has custom model data and is within the defined bounds
                if (meta != null && meta.hasCustomModelData()) {
                    int customModelData = meta.getCustomModelData();

                    if (isWithinBounds(itemDisplay.getLocation()) &&
                            (customModelData == LEFT_DOOR_CUSTOM_MODEL_DATA || customModelData == RIGHT_DOOR_CUSTOM_MODEL_DATA)) {

                        // If we find a valid left or right door, pair them together
                        if (customModelData == LEFT_DOOR_CUSTOM_MODEL_DATA) {
                            ItemDisplay rightDoor = findMatchingRightDoor(itemDisplay, world);
                            if (rightDoor != null) {
                                doors.add(new Door(itemDisplay, rightDoor));  // Add paired doors
                            }
                        }
                    }
                }
            }
        }
        return doors;
    }

    // Helper method to find the matching right door for a given left door
    private ItemDisplay findMatchingRightDoor(ItemDisplay leftDoor, World world) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay rightDoor = (ItemDisplay) entity;
                ItemMeta meta = rightDoor.getItemStack().getItemMeta();

                if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == RIGHT_DOOR_CUSTOM_MODEL_DATA) {
                    if (isWithinBounds(rightDoor.getLocation())) {
                        return rightDoor;  // Found matching right door
                    }
                }
            }
        }
        return null;
    }

    // Check if an entity is within the specified bounds
    private boolean isWithinBounds(Location location) {
        return location.getX() >= minBound.getX() && location.getX() <= maxBound.getX()
                && location.getY() >= minBound.getY() && location.getY() <= maxBound.getY()
                && location.getZ() >= minBound.getZ() && location.getZ() <= maxBound.getZ();
    }
}
