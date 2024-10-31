package org.imradigamer.chainPlugin.Safe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestartSafeCommand implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;

    public RestartSafeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("door")) {
                if (sender instanceof Player) {
                    sender.sendMessage("Abriendo caja fuerte...");
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill 122 107 -50 123 107 -50 minecraft:air");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:clientoptions set @a SHADERS NONE");
                rotateEntitySmoothly();
                teleportInteractionUp(5); // Teleport entity with "panel_caja_fuerte" tag up by 5 blocks
            } else if (args[0].equalsIgnoreCase("reset")) {
                if (sender instanceof Player) {
                    sender.sendMessage("Cerrando caja fuerte...");
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill 122 107 -50 123 107 -50 minecraft:barrier");

                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.getInventory().setBoots(null);
                }
                resetEntities();
                if (sender instanceof Player) {
                    teleportInteractionToFixedLocation((Player) sender);
                }
            }
        } else {
            sender.sendMessage("Uso: /safe <door|reset>");
        }
        return true;
    }

    private void rotateEntitySmoothly() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .filter(display -> display.getScoreboardTags().contains("puerta_caja"))
                .forEach(display -> new BukkitRunnable() {
                    final double targetYaw = -90;
                    final double yawStep = 1.5;

                    @Override
                    public void run() {
                        double currentYaw = display.getLocation().getYaw();
                        if (currentYaw > targetYaw) {
                            currentYaw -= yawStep;
                            Location loc = display.getLocation();
                            loc.setYaw((float) currentYaw);
                            display.teleport(loc);

                            if (currentYaw <= targetYaw) {
                                this.cancel();
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L)));
    }

    private void resetEntities() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .forEach(display -> {
                    if (display.getScoreboardTags().contains("tarjeta_1") || display.getScoreboardTags().contains("tarjeta_2")) {
                        ItemStack raw_gold = new ItemStack(Material.RAW_GOLD);
                        ItemMeta meta = raw_gold.getItemMeta();
                        if (meta != null) {
                            meta.setCustomModelData(111);
                            raw_gold.setItemMeta(meta);
                        }
                        display.setItemStack(raw_gold);
                    }

                    if (display.getScoreboardTags().contains("puerta_caja")) {
                        Location loc = display.getLocation();
                        loc.setYaw(0);
                        display.teleport(loc);
                    }
                }));
    }

    private void teleportInteractionUp(int blocksUp) {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream()
                .filter(entity -> entity instanceof Interaction)
                .filter(entity -> entity.getScoreboardTags().contains("panel_caja_fuerte"))
                .forEach(interaction -> {
                    Location currentLocation = interaction.getLocation();
                    currentLocation.add(0, blocksUp, 0);
                    interaction.teleport(currentLocation);
                }));
    }

    private void teleportInteractionToFixedLocation(Player player) {
        player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof Interaction)
                .filter(entity -> entity.getScoreboardTags().contains("panel_caja_fuerte"))
                .forEach(interaction -> {
                    Location fixedLocation = new Location(player.getWorld(), 123.269, 107, -48.426);
                    interaction.teleport(fixedLocation);
                });
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("door", "reset");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        }

        return completions;
    }
}
