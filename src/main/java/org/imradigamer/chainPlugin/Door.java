package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;

public class Door {

    private final ItemDisplay leftDoor;
    private final ItemDisplay rightDoor;
    private boolean isOpen;

    // Store the original position to revert when closing
    private Location leftOriginalPosition;
    private Location rightOriginalPosition;

    public Door(ItemDisplay leftDoor, ItemDisplay rightDoor) {
        this.leftDoor = leftDoor;
        this.rightDoor = rightDoor;
        this.isOpen = false;  // Initially closed
        this.leftOriginalPosition = leftDoor.getLocation().clone();  // Save initial position
        this.rightOriginalPosition = rightDoor.getLocation().clone();  // Save initial position
    }

    public ItemDisplay getLeftDoor() {
        return leftDoor;
    }

    public ItemDisplay getRightDoor() {
        return rightDoor;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Location getLeftOriginalPosition() {
        return leftOriginalPosition;
    }

    public Location getRightOriginalPosition() {
        return rightOriginalPosition;
    }
}
