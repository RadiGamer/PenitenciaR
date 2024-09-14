package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class ChainManager {

    private static Location chainOrigin;
    private static final Map<Player, List<BlockDisplay>> chainedPlayers = new HashMap<>();
    private static final Map<Player, Location> initialChainingLocations = new HashMap<>();

    public static void setChainOrigin(Location location) {
        chainOrigin = location;
    }

    public static void startChainingPlayers(World world, ChainPlugin plugin) {
        List<Player> adventurePlayers = world.getPlayers().stream()
                .filter(p -> p.getGameMode() == GameMode.ADVENTURE)
                .collect(Collectors.toList());

        if (adventurePlayers.isEmpty()) {
            Bukkit.getLogger().info("No adventure mode players to teleport.");
            return;
        }

        for (int i = 0; i < Math.min(adventurePlayers.size(), 8); i++) {
            Player player = adventurePlayers.get(i);
            String path = "chain.locations.location" + (i + 1);
            if (plugin.getConfig().contains(path)) {
                World locWorld = Bukkit.getWorld(plugin.getConfig().getString(path + ".world"));
                double x = plugin.getConfig().getDouble(path + ".x");
                double y = plugin.getConfig().getDouble(path + ".y");
                double z = plugin.getConfig().getDouble(path + ".z");
                player.teleport(new Location(locWorld, x, y, z));
                chainPlayer(player);
                Bukkit.getLogger().info("Teleported " + player.getName() + " to location " + (i + 1));
            } else {
                Bukkit.getLogger().info("Location " + (i + 1) + " not set. Player " + player.getName() + " not teleported.");
            }
        }
    }

    private static void chainPlayer(Player player) {
        List<BlockDisplay> chainLinks = chainedPlayers.getOrDefault(player, new ArrayList<>());
        chainLinks.forEach(BlockDisplay::remove); // Remove old chain links first
        chainLinks.clear();

        Location playerLocation = player.getLocation().clone();
        double distance = chainOrigin.distance(playerLocation);
        Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();

        // Adjust interval dynamically based on distance
        double interval = Math.max(0.3, distance / 10);  // Ensures no more than 10 links, adjust as necessary

        int numberOfLinks = (int) (distance / interval);

        for (int i = 0; i <= numberOfLinks; i++) {
            Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i * interval));
            // Adjust the yaw of each link to align with the direction vector
            float yaw = calculateYaw(direction);
            chainLinkLocation.setYaw(yaw);
            // Spawning the BlockDisplay as before
            BlockDisplay chainLink = player.getWorld().spawn(chainLinkLocation, BlockDisplay.class);
            BlockData chainBlockData = Material.CHAIN.createBlockData();
            chainLink.setBlock(chainBlockData);
            chainLink.setPersistent(true);
            chainLinks.add(chainLink);
        }

        chainedPlayers.put(player, chainLinks);
        initialChainingLocations.put(player, playerLocation);
    }

    private static float calculateYaw(Vector direction) {
        double dx = direction.getX();
        double dz = direction.getZ();
        double yaw = Math.atan2(-dx, dz) * (180 / Math.PI);  // Convert radians to degrees
        return (float) yaw;
    }


    public static void clearAllChains() {
        for (Map.Entry<Player, List<BlockDisplay>> entry : chainedPlayers.entrySet()) {
            List<BlockDisplay> chainLinks = entry.getValue();
            chainLinks.forEach(BlockDisplay::remove);
        }
        chainedPlayers.clear();
        initialChainingLocations.clear();
    }

    public static boolean isPlayerChained(Player player) {
        return chainedPlayers.containsKey(player);
    }

    public static void clearPlayerChain(Player player) {
        List<BlockDisplay> chainLinks = chainedPlayers.get(player);
        if (chainLinks != null) {
            chainLinks.forEach(BlockDisplay::remove);
        }
        chainedPlayers.remove(player);
        initialChainingLocations.remove(player);
    }

    private static final double CHAIN_HEIGHT_OFFSET = -0.3;  // Adjust this value to make the chain spawn higher
    private static final double CHAIN_LEFT_OFFSET = -0.5;  // Negative value to move chain to the left (adjust as needed)



    // Update chains to follow players
    public static void updateChains() {

        for (Map.Entry<Player, List<BlockDisplay>> entry : chainedPlayers.entrySet()) {
            Player player = entry.getKey();
            List<BlockDisplay> chainLinks = entry.getValue();
            Location playerLocation = player.getLocation().clone();
            double distance = chainOrigin.distance(playerLocation);
            Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();

            Vector up = new Vector(0, 1, 0); // Upward direction
            Vector left = up.crossProduct(direction).normalize().multiply(-0.5);

            double interval = 0.5;  // Fixed interval for chain links
            int numberOfRequiredLinks = (int) Math.ceil(distance / interval) + 1;  // Use ceil to ensure we reach the player

            // Adjust the list size based on the distance
            if (chainLinks.size() > numberOfRequiredLinks) {
                // Remove extra links
                for (int i = chainLinks.size() - 1; i >= numberOfRequiredLinks; i--) {
                    chainLinks.get(i).remove();
                    chainLinks.remove(i);
                }
            } else if (chainLinks.size() < numberOfRequiredLinks) {
                // Add missing links
                for (int i = chainLinks.size(); i < numberOfRequiredLinks; i++) {
                    Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i * interval));
                    BlockDisplay chainLink = player.getWorld().spawn(chainLinkLocation, BlockDisplay.class);
                    BlockData chainBlockData = Material.CHAIN.createBlockData();
                    chainLink.setBlock(chainBlockData);
                    chainLink.setPersistent(true);
                    chainLinks.add(chainLink);
                }
            }

            // Update the position and orientation of each chain link
            for (int i = 0; i < chainLinks.size(); i++) {
                Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i * interval)).add(left);
                chainLinkLocation.setYaw(calculateYaw(direction));
                chainLinkLocation.setPitch(calculatePitch(direction));
                chainLinks.get(i).teleport(chainLinkLocation);
            }
        }
    }

    private static float calculatePitch(Vector direction) {
        double dy = direction.getY();
        double dx = Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ());
        double pitch = Math.atan2(dy, dx) * (180 / Math.PI) - 90; // Adjust for horizontal base
        return (float) pitch;
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