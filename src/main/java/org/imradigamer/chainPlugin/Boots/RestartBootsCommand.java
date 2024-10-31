package org.imradigamer.chainPlugin.Boots;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestartBootsCommand implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;

    public RestartBootsCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .filter(display -> display.getScoreboardTags().contains("visual_botas_1"))
                .forEach(display -> {
                    ItemStack ironBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
                    ItemMeta meta = ironBoots.getItemMeta();
                    if (meta != null) {
                        ironBoots.setItemMeta(meta);
                    }

                    display.setItemStack(ironBoots);

                });
        player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .filter(display -> display.getScoreboardTags().contains("visual_botas_2"))
                .forEach(display -> {
                    ItemStack ironBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
                    ItemMeta meta = ironBoots.getItemMeta();
                    if (meta != null) {
                        ironBoots.setItemMeta(meta);
                    }

                    display.setItemStack(ironBoots);

                });
        player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .filter(display -> display.getScoreboardTags().contains("visual_botas_3"))
                .forEach(display -> {
                    ItemStack ironBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
                    ItemMeta meta = ironBoots.getItemMeta();
                    if (meta != null) {
                        ironBoots.setItemMeta(meta);
                    }

                    display.setItemStack(ironBoots);

                });

        return true;

    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("restart");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        }

        return completions;
    }
}
