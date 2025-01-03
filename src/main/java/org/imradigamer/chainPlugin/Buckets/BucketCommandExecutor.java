package org.imradigamer.chainPlugin.Buckets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BucketCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        // Ensure the command has at least one argument
        if (args.length > 0) {
            // Handle the "drop" subcommand
            if (args[0].equalsIgnoreCase("drop")) {
                bucketDrop();

                return true;
            }

            else if (args[0].equalsIgnoreCase("start")) {

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @a at @s run playsound 3_2_echo_television_competencia master @s ~ ~ ~ 1");
                Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as @a at @s run playsound 2_bucket master @s ~ ~ ~ 1");
                }, 1060L);
                Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutevoice @a");
                }, 800L);
                Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                    playerCommandAsOp(player,"timer create b6ea1 10m WHITE");
                }, 1000L);

                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("start");
            completions.add("drop");
        }
        return completions;
    }
    public static void playerCommandAsOp(Player player,String actionLine){
        boolean isOp = player.isOp();
        player.setOp(true);
        player.performCommand(actionLine);
        if(!isOp) {
            player.setOp(false);
        }
    }
    public static void bucketDrop(){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "execute as @e[tag=aj.cubeta.root] run function animated_java:cubeta/animations/fall/play");

        for (int i = 1; i <= 4; i++) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "tag @e[tag=cubeta_" + i + "] add pickable");
        }
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
            spawnHappyVillagerParticlesForTaggedEntities();
        }, 20L);
    }
    public static void spawnHappyVillagerParticlesForTaggedEntities() {
        // Run a task every 10 ticks (0.5 seconds) for a total of 10 seconds (200 ticks)
        new BukkitRunnable() {
            int duration = 200; // Total duration in ticks (10 seconds)

            @Override
            public void run() {
                if (duration <= 0) {
                    // Stop the task when the duration is up
                    cancel();
                    return;
                }

                // Get all entities with the "visual" tag
                for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {  // Assumes main world, can loop through all worlds if needed
                    if (entity.getScoreboardTags().contains("visual")) {
                        // Spawn happy villager particles at the entity's location
                        Location location = entity.getLocation();
                        location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 10, 0.5, 0.5, 0.5, 0.1);
                    }
                }

                duration -= 10; // Decrease duration by 10 ticks each iteration
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("PenitenciaR"), 0L, 10L); // Run task immediately and repeat every 10 ticks
    }
}
