package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class ChainManager {

    private static Location chainOrigin;
    private static final Map<Player, List<ItemDisplay>> chainedPlayers = new HashMap<>();
    private static final Map<Player, Location> initialChainingLocations = new HashMap<>();

    public static void setChainOrigin(Player player) {
        chainOrigin = player.getLocation();
    }

    public static void startChainingPlayers(World world) {
        if (chainOrigin == null) {
            return;
        }

        for (Player player : world.getPlayers()) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                chainPlayer(player);
            }
        }
        // Optionally add keys to players if needed
    }

    private static void chainPlayer(Player player) {
        List<ItemDisplay> chainLinks = new ArrayList<>();
        Location playerLocation = player.getLocation().clone();
        playerLocation.setY(playerLocation.getY() + 1.5);

        double distance = chainOrigin.distance(playerLocation);
        Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();

        for (double i = 0; i < distance; i += 0.5) {
            Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i));
            ItemDisplay chainLink = player.getWorld().spawn(chainLinkLocation, ItemDisplay.class);
            chainLink.setItemStack(new ItemStack(Material.CHAIN));
            chainLink.setPersistent(true);
            chainLinks.add(chainLink);
        }

        chainedPlayers.put(player, chainLinks);
        initialChainingLocations.put(player, player.getLocation().clone());
    }

    public static void clearAllChains() {
        for (Map.Entry<Player, List<ItemDisplay>> entry : chainedPlayers.entrySet()) {
            List<ItemDisplay> chainLinks = entry.getValue();
            chainLinks.forEach(ItemDisplay::remove);
        }
        chainedPlayers.clear();
        initialChainingLocations.clear();
    }

    public static boolean isPlayerChained(Player player) {
        return chainedPlayers.containsKey(player);
    }

    public static void clearPlayerChain(Player player) {
        List<ItemDisplay> chainLinks = chainedPlayers.get(player);
        if (chainLinks != null) {
            chainLinks.forEach(ItemDisplay::remove);
        }
        chainedPlayers.remove(player);
        initialChainingLocations.remove(player);
        player.sendMessage("You have been freed from your chains!");
    }

    // Update chains to follow players
    public static void updateChains() {
        for (Map.Entry<Player, List<ItemDisplay>> entry : chainedPlayers.entrySet()) {
            Player player = entry.getKey();
            List<ItemDisplay> chainLinks = entry.getValue();
            Location playerLocation = player.getLocation().clone();
            playerLocation.setY(playerLocation.getY() + 1.2);

            double distance = chainOrigin.distance(playerLocation);
            Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();

            double sagFactor = calculateSagFactor(distance);

            Location previousLinkLocation = chainOrigin.clone();
            double totalLinks = chainLinks.size();

            for (int i = 0; i < chainLinks.size(); i++) {
                ItemDisplay chainLink = chainLinks.get(i);

                double t = i / totalLinks;
                Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(t * distance));

                double sag = Math.sin(Math.PI * t) * sagFactor; // Simulate the sag with a sine wave
                chainLinkLocation.setY(chainLinkLocation.getY() - sag);

                chainLink.teleport(chainLinkLocation);

                previousLinkLocation = chainLinkLocation;
            }
        }
    }

    // Calculate sag factor based on the distance
    private static double calculateSagFactor(double distance) {
        double maxSag = 2.0;  // Maximum sag in blocks
        double minSag = 0.2;  // Minimum sag in blocks
        double sagFactor = maxSag / (distance + 1);  // Adjusting factor
        return Math.max(minSag, sagFactor);  // Ensure sag is not less than the minimum
    }
    public static void freePlayersWithoutPermission(String permission) {
        for (Player player : chainedPlayers.keySet()) {
            if (!player.hasPermission(permission)) {
                clearPlayerChain(player);
            }
        }
    }
    public static Location getInitialChainingLocation(Player player) {
        return initialChainingLocations.get(player);
    }

    private static final Map<UUID, Double> lastDistances = new HashMap<>();

    public static Location getChainOrigin() {
        return chainOrigin;
    }
    public static double getLastDistance(UUID playerUuid) {
        return lastDistances.getOrDefault(playerUuid, -1.0);
    }


    public static void updateLastDistance(Player player) {
        if (chainOrigin != null) {
            double distance = player.getLocation().distance(chainOrigin);
            lastDistances.put(player.getUniqueId(), distance);
        }
    }
    public static boolean checkAllMovedAwayFromOrigin() {
        for (Player player : chainedPlayers.keySet()) {
            if (player.hasPermission("chain.desgraciados")) {
                double currentDistance = player.getLocation().distance(chainOrigin);
                Double lastDistance = lastDistances.get(player.getUniqueId());
                if (lastDistance == null || currentDistance <= lastDistance) {
                    return false; // Not all players have moved away from the origin
                }
            }
        }
        return true; // All required players have moved away from the origin
    }

    // Method to free players
    public static void freeDesgraciados() {
        for (Player player : chainedPlayers.keySet()) {
            if (player.hasPermission("chain.desgraciados")) {
                clearPlayerChain(player);
            }
        }
        lastDistances.clear(); // Clear distances post-release
    }
}
