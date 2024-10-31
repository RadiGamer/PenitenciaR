package org.imradigamer.chainPlugin.Hater;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HaterCommand implements CommandExecutor, TabCompleter {

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
                final Location flashLocation1 = new Location(player.getWorld(), 27, 90, -83);
                final Location flashLocation2 = new Location(player.getWorld(), 27, 90, -79);
                final Location particleLocation1 = new Location(player.getWorld(), 27.02, 90.56, -83.00);
                final Location particleLocation2 = new Location(player.getWorld(), 26.82, 90.58, -78.00);

                @Override
                public void run() {
                    if (photosTaken < 15) {
                        simulateFlash(flashLocation1, particleLocation1);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                simulateFlash(flashLocation2, particleLocation2);
                            }
                        }.runTaskLater(plugin, 7L);

                        photosTaken++;
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 10L, 10L);

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

                for(Player player: Bukkit.getOnlinePlayers()){
                    player.playSound(particleLocation, "minecraft:2_rafaga_de_flash_solo", SoundCategory.BLOCKS, 0.2F, 1F);
                }


                new BukkitRunnable() {
                    @Override
                    public void run() {
                        levelledBlock.setLevel(3);
                        lightBlockLocation.getBlock().setBlockData(levelledBlock);
                    }
                }.runTaskLater(plugin, 22L);
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> subcommands = Arrays.asList("idle", "kill");

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
