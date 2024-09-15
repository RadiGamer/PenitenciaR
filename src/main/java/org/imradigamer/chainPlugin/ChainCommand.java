package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class ChainCommand implements CommandExecutor, TabCompleter {

    private final ChainPlugin plugin;
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public ChainCommand(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "origin":
                ChainManager.setChainOrigin(player.getLocation());
                // Save the location to config immediately after setting
                plugin.getConfig().set("chain.origin.world", player.getWorld().getName());
                plugin.getConfig().set("chain.origin.x", player.getLocation().getX());
                plugin.getConfig().set("chain.origin.y", player.getLocation().getY());
                plugin.getConfig().set("chain.origin.z", player.getLocation().getZ());
                plugin.saveConfig(); // Make sure to save the config after setting the values
                player.sendMessage("Punto de origen colocado.");
                break;

            case "start":
                ChainManager.startChainingPlayers(player.getWorld(), plugin);
                player.sendMessage("Encadenando a los jugadores en aventura.");
                break;

            case "location":
                saveLocation(player);
                break;

            case "stop":
                ChainManager.clearAllChains();
                player.sendMessage("Adios cadenas.");
                break;
            case "free":
                ChainManager.setCommandActivated(true);
                player.sendMessage("Ahora ya pueden estirar para liberarse");

            case "key":
                if (args.length > 1) {
                    List<String> nicknames = Arrays.asList(args).subList(1, args.length);
                    for (String nickname : nicknames) {
                        Player targetPlayer = Bukkit.getPlayerExact(nickname);
                        if (targetPlayer != null) {
                            giveKey(targetPlayer);
                            player.sendMessage("Se le dio la llave a " + targetPlayer.getName());
                        } else {
                            player.sendMessage("Jugador " + nickname + " no encontrado.");
                        }
                    }
                } else {
                    player.sendMessage("Uso: /chain key <nicknames>");
                }
                break;
        }
        return true;
    }

    private void giveKey(Player player) {
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = key.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("Llave");
        key.setItemMeta(meta);

        player.getInventory().addItem(key);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // First argument completions
            completions.add("origin");
            completions.add("start");
            completions.add("stop");
            completions.add("key");
        } else if (args.length >= 2 && "key".equals(args[0].toLowerCase())) {
            // Add player names for the 'key' command
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
    private void saveLocation(Player player) {
        // Get the current location count from config and increment
        int locationCount = plugin.getConfig().getInt("chain.locationCount", 0);
        int nextLocationIndex = (locationCount % 8) + 1;  // Ensure it cycles between 1 and 8

        String basePath = "chain.locations.location" + nextLocationIndex;
        plugin.getConfig().set(basePath + ".world", player.getWorld().getName());
        plugin.getConfig().set(basePath + ".x", player.getLocation().getX());
        plugin.getConfig().set(basePath + ".y", player.getLocation().getY());
        plugin.getConfig().set(basePath + ".z", player.getLocation().getZ());
        plugin.getConfig().set("chain.locationCount", nextLocationIndex);  // Save the new index

        plugin.saveConfig();
        player.sendMessage("Location " + nextLocationIndex + " saved.");
    }
}
