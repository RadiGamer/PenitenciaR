package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DoorAnimator {

    private final Plugin plugin;

    public DoorAnimator(Plugin plugin) {
        this.plugin = plugin;
    }

    // Animate the door opening or closing
    public void animateDoor(Door door, boolean open, double slideDistance) {
        new BukkitRunnable() {
            int ticks = 0;
            final Location leftStartPos = door.getLeftDoor().getLocation();
            final Location rightStartPos = door.getRightDoor().getLocation();

            // When opening, left door moves to the left, right door moves to the right
            final Location leftEndPos = open
                    ? leftStartPos.clone().add(slideDistance, 0, 0)  // Move left door to the left
                    : door.getLeftOriginalPosition();  // Move left door back to its original position

            final Location rightEndPos = open
                    ? rightStartPos.clone().add(-slideDistance, 0, 0)  // Move right door to the right
                    : door.getRightOriginalPosition();  // Move right door back to its original position

            final int animationLength = 40; // 2 seconds for opening/closing

            @Override
            public void run() {
                ticks++;
                double progress = (double) ticks / animationLength;

                // Interpolate positions based on progress
                Location newLeftPos = leftStartPos.clone().add(leftEndPos.clone().subtract(leftStartPos).toVector().multiply(progress));
                Location newRightPos = rightStartPos.clone().add(rightEndPos.clone().subtract(rightStartPos).toVector().multiply(progress));

                // Move the doors smoothly
                door.getLeftDoor().teleport(newLeftPos);
                door.getRightDoor().teleport(newRightPos);

                // Stop animation after time is complete
                if (ticks >= animationLength) {
                    door.setOpen(open);  // Mark the door as open or closed after animation
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1); // Run every tick (20 ticks = 1 second)
    }
}
