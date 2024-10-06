package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class ElevatorCommand implements CommandExecutor {
    private final JavaPlugin plugin;


    public ElevatorCommand(JavaPlugin plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        Player player = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start1")) {
                executeElevatorSequence1();
                return true;
            } else if (args[0].equalsIgnoreCase("start2")) {
                executeElevatorSequence2();
                return true;
            } else if (args[0].equalsIgnoreCase("teleport")) {
                checkElevator12();
                checkElevator22();
                checkElevator32();
                return true;
            }
        }

        sender.sendMessage("Uso: /elevator [start|teleport]");
        return false;
    }


    private void executeElevatorSequence1() {
        DoorAnimator animator = new DoorAnimator(plugin);
        animator.animateDoors(false);
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage("Puertas cerrandose");
        }
            new BukkitRunnable() {

                @Override
                public void run() {
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        player.sendMessage("Elevador Andando");
                    }

                    Location corner1 = new Location(Bukkit.getWorld("world"), 133, 58, 8); // example coordinates
                    Location corner2 = new Location(Bukkit.getWorld("world"), 113, 58, 3); // example coordinates

                    // Schedule a flicker sequence in the middle of the "Elevador andando" phase
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // Trigger 3 flickers (2 fast, 1 slow)
                            triggerFlickerSequence(corner1, corner2);
                        }
                    }.runTaskLater(plugin, 200L); // Schedule the flicker sequence after 10 seconds (halfway)

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // After the elevator sequence is done, ensure the lights are fully on
                            replaceBlocksInAreaWithLight(corner1, corner2, 15); // Set light to full brightness

                            for (Player player : plugin.getServer().getOnlinePlayers()) {
                                player.sendMessage("Sonido Marcando Piso");
                            }
                            checkElevator1();
                            checkElevator2();
                            checkElevator3();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                                        player.sendMessage("Puertas se abren");
                                    }
                                    animator.animateDoors2(true);
                                }
                            }.runTaskLater(plugin, 150L); // 7.5 seconds later
                        }
                    }.runTaskLater(plugin, 400L); // End of elevator sequence after 20 seconds
                }
            }.runTaskLater(plugin, 100L); // 5 seconds after doors close
    }

//TODO CAMBIAR CADA MENSAJE A BUKKIT BROADCAST

    private void executeElevatorSequence2() {
        DoorAnimator animator = new DoorAnimator(plugin);
        animator.animateDoors2(false);

        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage("Puertas cerrandose");
        }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                        player.sendMessage("Elevador Andando");
                    }


                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            for(Player player : plugin.getServer().getOnlinePlayers()) {
                                player.sendMessage("Sonido Marcando piso");
                            }
                            checkElevator12();
                            checkElevator22();
                            checkElevator32();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                                        player.sendMessage("Puertas se abren");
                                    }
                                    animator.animateDoors3(true);
                                }
                            }.runTaskLater(plugin, 150L); // 7.5 seconds later
                        }
                    }.runTaskLater(plugin, 400L); // 20 segundos seconds later
                }
            }.runTaskLater(plugin, 100L); // 5 seconds after doors close
    }


    private void checkElevator1() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 1
            if (loc.getBlockX() >= 129 && loc.getBlockX() <= 133 &&
                    loc.getBlockY() == 55 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 19);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator2() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 2 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 125 && loc.getBlockX() >= 121 &&
                    loc.getBlockY() == 55 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 33 blocks
                loc.setY(loc.getY() + 33);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator3() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 3 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 117 && loc.getBlockX() >= 113 &&
                    loc.getBlockY() == 55 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 19);
                player.teleport(loc);
            }
        }
    }
    private void checkElevator12() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 1
            if (loc.getBlockX() >= 129 && loc.getBlockX() <= 133 &&
                    loc.getBlockY() == 74 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 32);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator22() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 2 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 125 && loc.getBlockX() >= 121 &&
                    loc.getBlockY() == 88 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 33 blocks
                loc.setY(loc.getY() + 18);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator32() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 3 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 117 && loc.getBlockX() >= 113 &&
                    loc.getBlockY() == 74 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 32);
                player.teleport(loc);
            }
        }
    }

    private void replaceBlocksInAreaWithLight(Location corner1, Location corner2, int lightLevel) {
        World world = corner1.getWorld();
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    // Check if the block is already a light block
                    if (block.getType() == Material.LIGHT) {
                        // Create the light block data with the desired light level
                        BlockData blockData = Bukkit.createBlockData(Material.LIGHT, "[level=" + lightLevel + "]");
                        block.setBlockData(blockData);
                    }
                }
            }
        }
    }
    private void triggerFlickerSequence(Location corner1, Location corner2) {
        // First fast flicker
        triggerSingleFlicker(corner1, corner2, 5, 10); // Flicker with random light level between 5 and 10

        // Schedule the second fast flicker
        new BukkitRunnable() {
            @Override
            public void run() {
                triggerSingleFlicker(corner1, corner2, 5, 10);
            }
        }.runTaskLater(plugin, 5L); // 5 ticks (0.25 seconds) delay

        // Schedule the third slow flicker
        new BukkitRunnable() {
            @Override
            public void run() {
                triggerSingleFlicker(corner1, corner2, 5, 10);
            }
        }.runTaskLater(plugin, 15L); // 15 ticks (0.75 seconds) delay from the start
    }

    // Method to trigger a single flicker (down and back up)
    private void triggerSingleFlicker(Location corner1, Location corner2, int minLight, int maxLight) {
        Random random = new Random();
        World world = corner1.getWorld();
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        // Reduce the light level momentarily (like a voltage drop)
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.LIGHT) {
                        // Set light to a random low value between minLight and maxLight
                        int randomLightLevel = minLight + random.nextInt(maxLight - minLight + 1); // Random between minLight and maxLight
                        BlockData blockData = Bukkit.createBlockData(Material.LIGHT, "[level=" + randomLightLevel + "]");
                        block.setBlockData(blockData);
                    }
                }
            }
        }

        // Restore the light level to full brightness after a short delay (depending on flicker speed)
        new BukkitRunnable() {
            @Override
            public void run() {
                replaceBlocksInAreaWithLight(corner1, corner2, 15); // Restore light to full brightness
            }
        }.runTaskLater(plugin, 5L); // 5 ticks = 0.25 seconds delay
    }

}