package org.imradigamer.chainPlugin.Elevator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ElevatorCommand implements CommandExecutor, TabCompleter {
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
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as @a at @s run playsound minecraft:2_sonido_elevador master @s ~ ~ ~ 1");

        new BukkitRunnable() {
            @Override
            public void run() {
                DoorAnimator animator = new DoorAnimator(plugin);
                animator.animateDoors(false);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        Location corner1 = new Location(Bukkit.getWorld("world"), 133, 58, 8);
                        Location corner2 = new Location(Bukkit.getWorld("world"), 113, 58, 3);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                triggerFlickerSequence(corner1, corner2);
                            }
                        }.runTaskLater(plugin, 100L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                replaceBlocksInAreaWithLight(corner1, corner2, 15);

                                checkElevator1();
                                checkElevator2();
                                checkElevator3();

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        animator.animateDoors2(true);
                                    }
                                }.runTaskLater(plugin, 75L);
                            }
                        }.runTaskLater(plugin, 360L);
                    }
                }.runTaskLater(plugin, 100L);
            }
        }.runTaskLater(plugin, 40L);  // Delay the entire sequence by 2 seconds (40 ticks)
    }


//TODO CAMBIAR CADA MENSAJE A BUKKIT BROADCAST

    private void executeElevatorSequence2() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as @a at @s run playsound minecraft:2_sonido_elevador master @s ~ ~ ~ 1");
        new BukkitRunnable() {
            @Override
            public void run() {
                DoorAnimator animator = new DoorAnimator(plugin);
                animator.animateDoors2(false);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location corner2 = new Location(Bukkit.getWorld("world"), 111, 94, 2);
                        Location corner1 = new Location(Bukkit.getWorld("world"), 135, 75, 10);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                triggerFlickerSequence(corner1, corner2);
                            }
                        }.runTaskLater(plugin, 100L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                replaceBlocksInAreaWithLight(corner1, corner2, 15);
                                checkElevator12();
                                checkElevator22();
                                checkElevator32();

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        animator.animateDoors3(true);
                                    }
                                }.runTaskLater(plugin, 75L);
                            }
                        }.runTaskLater(plugin, 360L);
                    }
                }.runTaskLater(plugin, 100L);
            }
        }.runTaskLater(plugin, 40L);  // This delays the entire sequence by 2 seconds (40 ticks)
    }



    private void checkElevator1() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockX() >= 129 && loc.getBlockX() <= 133 &&
                    loc.getBlockY() >= 55 && loc.getBlockY() <= 60 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                loc.setY(loc.getY() + 19);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator2() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockX() <= 125 && loc.getBlockX() >= 121 &&
                    loc.getBlockY() >= 55 && loc.getBlockY() <= 60 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                loc.setY(loc.getY() + 33);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator3() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockX() <= 117 && loc.getBlockX() >= 113 &&
                    loc.getBlockY() >= 55 && loc.getBlockY() <= 60 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                loc.setY(loc.getY() + 19);
                player.teleport(loc);
            }
        }
    }
    private void checkElevator12() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockX() >= 129 && loc.getBlockX() <= 133 &&
                    loc.getBlockY() >= 74 && loc.getBlockY() <= 79 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                loc.setY(loc.getY() + 32);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator22() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockX() <= 125 && loc.getBlockX() >= 121 &&
                    loc.getBlockY() >= 88 && loc.getBlockY() <= 93 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                loc.setY(loc.getY() + 18);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator32() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            if (loc.getBlockX() <= 117 && loc.getBlockX() >= 113 &&
                    loc.getBlockY() >= 74 && loc.getBlockY() <= 79 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
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
                    if (block.getType() == Material.LIGHT) {
                        BlockData blockData = Bukkit.createBlockData(Material.LIGHT, "[level=" + lightLevel + "]");
                        block.setBlockData(blockData);
                    }
                }
            }
        }
    }
    private void triggerFlickerSequence(Location corner1, Location corner2) {
        triggerSingleFlicker(corner1, corner2, 5, 10);

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "puzzle shader SHAKE 1");
                triggerSingleFlicker(corner1, corner2, 5, 10);
            }
        }.runTaskLater(plugin, 5L);

        new BukkitRunnable() {
            @Override
            public void run() {
                triggerSingleFlicker(corner1, corner2, 5, 10);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "puzzle shader SHAKE 1");
            }
        }.runTaskLater(plugin, 20L);
    }

    private void triggerSingleFlicker(Location corner1, Location corner2, int minLight, int maxLight) {
        Random random = new Random();
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
                    if (block.getType() == Material.LIGHT) {
                        int randomLightLevel = minLight + random.nextInt(maxLight - minLight + 1);
                        BlockData blockData = Bukkit.createBlockData(Material.LIGHT, "[level=" + randomLightLevel + "]");
                        block.setBlockData(blockData);
                    }
                }
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                replaceBlocksInAreaWithLight(corner1, corner2, 15);
            }
        }.runTaskLater(plugin, 5L);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> subcommands = Arrays.asList("start1", "start2");

        if (args.length == 1) {
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        }

        return completions;
    }
}