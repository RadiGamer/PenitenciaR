package org.imradigamer.chainPlugin.Chains;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.imradigamer.chainPlugin.ChainPlugin;


import java.util.*;
import java.util.stream.Collectors;

public class ChainManager {

    private static Location chainOrigin;
    private static final Map<Player, List<BlockDisplay>> chainedPlayers = new HashMap<>();
    private static final Map<Player, Location> initialChainingLocations = new HashMap<>();
    private static boolean keyUsed = false;
    private static boolean commandActivated = false;
    private static boolean keyActive = false;
    private final ChainPlugin plugin;
    private boolean isTaskRunning = false;
    private BukkitRunnable repeatingTask;

    public ChainManager(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    public static void setChainOrigin(Location location) {
        chainOrigin = location;
    }

    public static void startChainingPlayers(World world, ChainPlugin plugin) {
        List<Player> adventurePlayers = world.getPlayers().stream()
                .filter(p -> p.getGameMode() == GameMode.ADVENTURE)
                .collect(Collectors.toList());

        if (adventurePlayers.isEmpty()) {
            Bukkit.getLogger().info("No se detectaron jugadores en modo aventura");
            return;
        }

        List<Player> specialPlayers = new ArrayList<>();
        List<Player> normalPlayers = new ArrayList<>();

        // Classify players by permission
        for (Player player : adventurePlayers) {
            if (player.hasPermission("chain.desgraciados")) {
                specialPlayers.add(player);
                //todo EQUIPAR CALABAZA AQUI
                equipHeadBand(player);
            } else {
                normalPlayers.add(player);
                //TODO AQUI VA LA LLAVE
                ChainCommand chainCommand = new ChainCommand(plugin);
                chainCommand.giveKey(player);
            }
        }

        int[] specialLocations = {3, 7};

        for (int index : specialLocations) {
            if (specialPlayers.isEmpty()) {
                break;
            }
            teleportPlayerToChainLocation(specialPlayers.remove(0), index + 1, plugin);
        }

        int currentLocation = 1;
        List<Player> remainingPlayers = new ArrayList<>(specialPlayers);
        remainingPlayers.addAll(normalPlayers);

        for (Player player : remainingPlayers) {
            if (currentLocation == 4 || currentLocation == 8) {
                currentLocation++;
            }
            if (currentLocation > 8) {
                break;
            }
            teleportPlayerToChainLocation(player, currentLocation, plugin);
            currentLocation++;
        }
    }

    private static void teleportPlayerToChainLocation(Player player, int locationNumber, ChainPlugin plugin) {
        String path = "chain.locations.location" + locationNumber;
        if (plugin.getConfig().contains(path)) {
            World locWorld = Bukkit.getWorld(plugin.getConfig().getString(path + ".world"));
            double x = plugin.getConfig().getDouble(path + ".x");
            double y = plugin.getConfig().getDouble(path + ".y");
            double z = plugin.getConfig().getDouble(path + ".z");
            player.teleport(new Location(locWorld, x, y, z));
            chainPlayer(player);
            Bukkit.getLogger().info("Teleported " + player.getName() + " to location " + locationNumber);
        } else {
            Bukkit.getLogger().info("Location " + locationNumber + " not set. Player " + player.getName() + " not teleported.");
        }
    }

    private static void chainPlayer(Player player) {
        List<BlockDisplay> chainLinks = chainedPlayers.getOrDefault(player, new ArrayList<>());
        chainLinks.forEach(BlockDisplay::remove);
        chainLinks.clear();

        Location playerLocation = player.getLocation().clone();
        double distance = chainOrigin.distance(playerLocation);
        Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();


        double interval = Math.max(0.3, distance / 10);

        int numberOfLinks = (int) (distance / interval);

        for (int i = 0; i <= numberOfLinks; i++) {
            Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i * interval));
            float yaw = calculateYaw(direction);
            chainLinkLocation.setYaw(yaw);
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
        double yaw = Math.atan2(-dx, dz) * (180 / Math.PI);
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
    
    // Update chains to follow players
    public static void updateChains() {

        for (Map.Entry<Player, List<BlockDisplay>> entry : chainedPlayers.entrySet()) {
            Player player = entry.getKey();
            List<BlockDisplay> chainLinks = entry.getValue();
            Location playerLocation = player.getLocation().clone();
            double distance = chainOrigin.distance(playerLocation);
            Vector direction = playerLocation.toVector().subtract(chainOrigin.toVector()).normalize();

            Vector up = new Vector(0, 1, 0);
            Vector left = up.crossProduct(direction).normalize().multiply(-0.5);

            double interval = 0.5;
            int numberOfRequiredLinks = (int) Math.ceil(distance / interval) + 1;

            if (chainLinks.size() > numberOfRequiredLinks) {

                for (int i = chainLinks.size() - 1; i >= numberOfRequiredLinks; i--) {
                    chainLinks.get(i).remove();
                    chainLinks.remove(i);
                }
            } else if (chainLinks.size() < numberOfRequiredLinks) {
                for (int i = chainLinks.size(); i < numberOfRequiredLinks; i++) {
                    Location chainLinkLocation = chainOrigin.clone().add(direction.clone().multiply(i * interval));
                    BlockDisplay chainLink = player.getWorld().spawn(chainLinkLocation, BlockDisplay.class);
                    BlockData chainBlockData = Material.CHAIN.createBlockData();
                    chainLink.setBlock(chainBlockData);
                    chainLink.setPersistent(true);
                    chainLinks.add(chainLink);
                }
            }

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
        double pitch = Math.atan2(dy, dx) * (180 / Math.PI) -90;
        return (float) pitch;
    }

    public static void freePlayersWithoutPermission(String permission) {
        List<Player> toRemove = new ArrayList<>();
        for (Player player : chainedPlayers.keySet()) {
            if (!player.hasPermission(permission)) {
                toRemove.add(player);
            }
        }
        for (Player player : toRemove) {
            clearPlayerChain(player);
        }
    }

    public static Location getInitialChainingLocation(Player player) {
        return initialChainingLocations.get(player);
    }

    private static final Map<UUID, Double> lastDistances = new HashMap<>();


    public static void updateLastDistance(Player player) {
        if (chainOrigin != null) {
            double distance = player.getLocation().distance(chainOrigin);
            lastDistances.put(player.getUniqueId(), distance);
        }
    }

    // Method to free players
    public static void freeDesgraciados() {
        List<Player> playersToFree = new ArrayList<>();
        boolean toggle = true;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopblink");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=aj.trituradora.root] run function animated_java:trituradora/animations/on/pause");

        // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timer delete b6ea1");
        Bukkit.broadcastMessage(ChatColor.GRAY+"timer delete b6ea1");

        for (Player player : chainedPlayers.keySet()) {
            removeHeadBand(player);
            if (player.hasPermission("chain.desgraciados")) {

                ItemStack key = new ItemStack(Material.RAW_GOLD);
                ItemMeta meta = key.getItemMeta();

                if (toggle) {
                    meta.setCustomModelData(15);
                    meta.setDisplayName(" ");
                } else {
                    meta.setCustomModelData(16);
                    meta.setDisplayName(" ");
                }
                key.setItemMeta(meta);
                player.getInventory().addItem(key);

                toggle = !toggle;

                playersToFree.add(player);
            }
        }

        for (Player player : playersToFree) {
            clearPlayerChain(player);
        }

        lastDistances.clear();
    }

    public static void setKeyUsed(boolean used) {
        keyUsed = used;
    }
    public static boolean isKeyUsed() {
        return keyUsed;
    }
    public static void resetKeyUsage() {
        keyUsed = false;
    }

    public static void setCommandActivated(boolean used) {
        commandActivated = used;
    }
    public static boolean isCommandActivated() {
        return commandActivated;
    }
    public static void resetCommandActivated() {
        commandActivated = false;
    }

    public static boolean checkAllMovedBackwards() {
        for (Player player : chainedPlayers.keySet()) {
            if (player.hasPermission("chain.desgraciados") && isCommandActivated()) {
                Location initialLocation = initialChainingLocations.get(player);
                double currentDistance = player.getLocation().distance(initialLocation);

                // If any player has not moved more than 2.0 blocks away from their initial location, return false
                if (currentDistance <= 2.0) {
                    return false;
                }
            }
        }
        return true;  // All players have moved more than 2.0 blocks away from their initial location
    }
    public static void equipHeadBand(Player player) {
        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN, 1);
        ItemMeta meta = pumpkin.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(1);
            meta.setDisplayName(ChatColor.GRAY + "Venda");
            pumpkin.setItemMeta(meta);
            player.getInventory().setHelmet(pumpkin);
        }
    }

    public static void removeHeadBand(Player player) {
        ItemStack headItem = player.getInventory().getHelmet();
        if (headItem != null && headItem.getType() == Material.CARVED_PUMPKIN) {
            ItemMeta meta = headItem.getItemMeta();
            if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                player.getInventory().setHelmet(null);
            }
        }
    }

    public static boolean isKeyActive() {
        return keyActive;
    }

    public static void setKeyActive(boolean keyActive) {
        ChainManager.keyActive = keyActive;
    }
    public static void resetKeyActive() {keyActive = false;}

}