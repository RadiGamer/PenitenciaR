package org.imradigamer.chainPlugin.Hater;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HaterCommand implements CommandExecutor {

    private final Plugin plugin;

    public HaterCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hater")) {
            if (args.length > 0) {
                String subcommand = args[0];

                if (subcommand.equalsIgnoreCase("idle")) {
                    return handleIdleCommand(sender);
                } else if (subcommand.equalsIgnoreCase("kill")) {
                    return handleKillCommand(sender);
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean handleIdleCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String commandToExecute = "execute as @e[tag=aj.hater.root] run function animated_java:hater/animations/idle/tween {to_frame: 0, duration: 5}";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);

            return true;
        } else {
            return true;
        }
    }
    private boolean handleKillCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String commandToExecute = "execute as @e[tag=aj.hater.root] run function animated_java:hater/animations/death/tween {to_frame: 0, duration: 5}";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);

            new BukkitRunnable() {
                int photosTaken = 0;
                final Location flashLocation1 = new Location(player.getWorld(), 27, 90, -83); // First flash light block
                final Location flashLocation2 = new Location(player.getWorld(), 27, 90, -79); // Second flash light block
                final Location particleLocation1 = new Location(player.getWorld(), 27.02, 90.56, -83.00); // First flash particle
                final Location particleLocation2 = new Location(player.getWorld(), 26.82, 90.58, -78.00); // Second flash particle

                @Override
                public void run() {
                    if (photosTaken < 5) {
                        simulateFlash(flashLocation1, particleLocation1);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                simulateFlash(flashLocation2, particleLocation2);
                            }
                        }.runTaskLater(plugin, 5L);

                        photosTaken++;
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 180L, 10L);

            return true;
        } else {
            return true;
        }
    }

    private void simulateFlash(Location lightBlockLocation, Location particleLocation) {
        if (lightBlockLocation.getBlock().getType() == Material.LIGHT) {
            BlockData blockData = lightBlockLocation.getBlock().getBlockData();
            if (blockData instanceof Levelled) {
                Levelled levelledBlock = (Levelled) blockData;
                levelledBlock.setLevel(15);
                lightBlockLocation.getBlock().setBlockData(levelledBlock);

                particleLocation.getWorld().spawnParticle(Particle.FLASH, particleLocation, 1);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        levelledBlock.setLevel(3);
                        lightBlockLocation.getBlock().setBlockData(levelledBlock);
                    }
                }.runTaskLater(plugin, 10L);
            }
        }
    }
}
