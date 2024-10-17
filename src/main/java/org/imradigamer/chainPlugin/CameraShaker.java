//package org.imradigamer.chainPlugin;
//
//import org.bukkit.entity.Player;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.HashMap;
//import java.util.UUID;
//import java.util.Random;
//
//public class CameraShaker {
//    private final JavaPlugin plugin;
//    private BukkitRunnable shakeTask;
//    private final HashMap<UUID, Float[]> originalAngles = new HashMap<>();
//    private final Random random = new Random();
//    private int shakeDuration;
//    private int restDuration;
//    private int currentTick = 0;
//
//    // Define the maximum allowable angle change for yaw and pitch
//    private final float maxYawChange = 3.0f; // Limit how much yaw can change
//    private final float maxPitchChange = 3.0f; // Limit how much pitch can change
//
//    public CameraShaker(JavaPlugin plugin) {
//        this.plugin = plugin;
//    }
//
//    public void startShaking() {
//        if (shakeTask == null || shakeTask.isCancelled()) {
//            // Initialize with random durations
//            assignRandomDurations();
//
//            shakeTask = new BukkitRunnable() {
//                @Override
//                public void run() {
//                    currentTick++;
//
//                    if (currentTick <= shakeDuration) {
//                        // Before shaking, update player's original angles to their current position
//                        for (Player player : plugin.getServer().getOnlinePlayers()) {
//                            UUID playerId = player.getUniqueId();
//
//                            // Update originalAngles with the player's current yaw and pitch before shaking
//                            originalAngles.put(playerId, new Float[]{player.getLocation().getYaw(), player.getLocation().getPitch()});
//
//                            Float[] angles = originalAngles.get(playerId);
//                            float originalYaw = angles[0];
//                            float originalPitch = angles[1];
//
//                            // Calculate new angles with limited shaking
//                            float newYaw = calculateNewAngle(originalYaw, 0.1f, maxYawChange);
//                            float newPitch = calculateNewAngle(originalPitch, 0.1f, maxPitchChange);
//
//                            // Apply shaking to player
//                            player.setRotation(newYaw, newPitch);
//                        }
//                    } else if (currentTick > shakeDuration + restDuration) {
//                        // Reset the tick counter and assign new random durations
//                        assignRandomDurations();
//                        currentTick = 0;
//                    }
//                }
//            };
//            shakeTask.runTaskTimer(plugin, 0L, 1L); // Runs every tick (1/20th of a second)
//        }
//    }
//
//    private void assignRandomDurations() {
//        // Shake duration between 3 seconds (60 ticks) and 8 seconds (160 ticks)
//        shakeDuration = 60 + random.nextInt(101); // Random between 60 and 160 ticks (3 to 8 seconds)
//
//        // Rest duration between 5 seconds (100 ticks) and 10 seconds (200 ticks)
//        restDuration = 100 + random.nextInt(101); // Random between 100 and 200 ticks (5 to 10 seconds)
//    }
//
//    // Calculate new angle with limits for yaw or pitch
//    private float calculateNewAngle(float originalAngle, float maxChange, float maxAllowedDeviation) {
//        float change = (float) (Math.random() * 2 * maxChange - maxChange);
//        float newAngle = originalAngle + change;
//
//        // Ensure the angle change doesn't exceed the maximum allowed deviation
//        if (Math.abs(newAngle - originalAngle) > maxAllowedDeviation) {
//            if (newAngle > originalAngle) {
//                newAngle = originalAngle + maxAllowedDeviation;
//            } else {
//                newAngle = originalAngle - maxAllowedDeviation;
//            }
//        }
//
//        // Normalize the angle to keep it within the range of 0-360 degrees
//        newAngle = (newAngle % 360 + 360) % 360;
//        return newAngle;
//    }
//
//    public void stopShaking() {
//        if (shakeTask != null) {
//            shakeTask.cancel();
//            shakeTask = null;
//            originalAngles.clear();
//        }
//    }
//
//    public boolean isShaking() {
//        return shakeTask != null && !shakeTask.isCancelled();
//    }
//}
package org.imradigamer.chainPlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.Random;

public class CameraShaker {
    private final JavaPlugin plugin;
    private BukkitRunnable shakeTask;
    private final HashMap<UUID, Float[]> originalAngles = new HashMap<>();
    private final Random random = new Random();
    private int shakeDuration;
    private int restDuration;
    private int currentTick = 0;

    // Define the maximum allowable angle change for pitch (yaw movement removed)
    private final float maxPitchChange = 3.0f; // Limit how much pitch can change

    public CameraShaker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startShaking() {
        if (shakeTask == null || shakeTask.isCancelled()) {
            // Initialize with random durations
            assignRandomDurations();

            shakeTask = new BukkitRunnable() {
                @Override
                public void run() {
                    currentTick++;

                    if (currentTick <= shakeDuration) {
                        // Before shaking, update player's original angles to their current position
                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            UUID playerId = player.getUniqueId();

                            // Update originalAngles with the player's current pitch (yaw removed)
                            originalAngles.put(playerId, new Float[]{player.getLocation().getPitch()});

                            Float[] angles = originalAngles.get(playerId);
                            float originalPitch = angles[0];

                            // Calculate new pitch with limited shaking (no yaw)
                            float newPitch = calculateNewAngle(originalPitch, 0.1f, maxPitchChange);

                            // Apply pitch shaking to player, yaw stays the same
                            player.setRotation(player.getLocation().getYaw(), newPitch); // Yaw unchanged
                        }
                    } else if (currentTick > shakeDuration + restDuration) {
                        // Reset the tick counter and assign new random durations
                        assignRandomDurations();
                        currentTick = 0;
                    }
                }
            };
            shakeTask.runTaskTimer(plugin, 0L, 1L); // Runs every tick (1/20th of a second)
        }
    }

    private void assignRandomDurations() {
        // Shake duration between 3 seconds (60 ticks) and 8 seconds (160 ticks)
        shakeDuration = 60 + random.nextInt(101); // Random between 60 and 160 ticks (3 to 8 seconds)

        // Rest duration between 5 seconds (100 ticks) and 10 seconds (200 ticks)
        restDuration = 100 + random.nextInt(101); // Random between 100 and 200 ticks (5 to 10 seconds)
    }

    // Calculate new angle with limits for pitch
    private float calculateNewAngle(float originalAngle, float maxChange, float maxAllowedDeviation) {
        float change = (float) (Math.random() * 2 * maxChange - maxChange);
        float newAngle = originalAngle + change;

        // Ensure the angle change doesn't exceed the maximum allowed deviation
        if (Math.abs(newAngle - originalAngle) > maxAllowedDeviation) {
            if (newAngle > originalAngle) {
                newAngle = originalAngle + maxAllowedDeviation;
            } else {
                newAngle = originalAngle - maxAllowedDeviation;
            }
        }

        // Normalize the angle to keep it within the range of 0-360 degrees
        newAngle = (newAngle % 360 + 360) % 360;
        return newAngle;
    }

    public void stopShaking() {
        if (shakeTask != null) {
            shakeTask.cancel();
            shakeTask = null;
            originalAngles.clear();
        }
    }

    public boolean isShaking() {
        return shakeTask != null && !shakeTask.isCancelled();
    }
}
