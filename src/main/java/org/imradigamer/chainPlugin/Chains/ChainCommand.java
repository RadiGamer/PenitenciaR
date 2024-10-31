package org.imradigamer.chainPlugin.Chains;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.imradigamer.chainPlugin.ChainPlugin;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;


public class ChainCommand implements CommandExecutor, TabCompleter {

    private final ChainPlugin plugin;

    public ChainCommand(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Escriba bien el comando");
            return false;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            ChainManager.clearAllChains();
            ChainManager.resetKeyActive();
            ChainManager.setCommandActivated(false);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timer delete b6ea1");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=aj.trituradora.root] run function animated_java:trituradora/animations/on/pause");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopblink");
            for(Player player : Bukkit.getOnlinePlayers()) {
                ChainManager.removeHeadBand(player);
            }
            sender.sendMessage("Adios cadenas");
            return true;


        }
        if (args[0].equalsIgnoreCase("images")) {
            boolean toggle = true;
            removeTripwireHooksFromAllInventories();
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.hasPermission("chain.desgraciados") && players.getGameMode()== GameMode.ADVENTURE) {
                    ItemStack key = new ItemStack(Material.RAW_GOLD);
                    ItemMeta meta = key.getItemMeta();

                    if (meta != null) {
                        if (toggle) {
                            meta.setCustomModelData(15);
                            meta.setDisplayName(" ");
                        } else {
                            meta.setCustomModelData(16);
                            meta.setDisplayName(" ");
                        }
                        key.setItemMeta(meta);
                        players.getInventory().addItem(key);

                        toggle = !toggle;
                    }
                }
            }
            return true;

        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (args[0].toLowerCase()) {
                case "origin":
                    ChainManager.setChainOrigin(player.getLocation());
                    plugin.getConfig().set("chain.origin.world", player.getWorld().getName());
                    plugin.getConfig().set("chain.origin.x", player.getLocation().getX());
                    plugin.getConfig().set("chain.origin.y", player.getLocation().getY());
                    plugin.getConfig().set("chain.origin.z", player.getLocation().getZ());
                    plugin.saveConfig(); // Save the config after setting the values
                    player.sendMessage("Punto de origen colocado.");
                    break;

                case "start":
                    ChainManager.startChainingPlayers(player.getWorld(), plugin);
                    player.sendMessage("Encadenando a los jugadores en aventura.");
                    break;

                case "location":
                    saveLocation(player);
                    break;

                case "free":
                    ChainManager.setCommandActivated(true);
                    player.sendMessage("Ahora ya pueden estirar para liberarse");
                    break;

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

                case "roleplayer":
                    ChainManager.setKeyActive(true);
                    Bukkit.dispatchCommand(player, "timer create b6ea1 1m WHITE");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"startblink");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=aj.trituradora.root] run function animated_java:trituradora/animations/on/resume");
                    player.sendMessage("El roleplayer ya puede usar la llave");
                    break;

                default:
                    player.sendMessage("Comando no reconocido.");
            }
        } else {
            if (args[0].equalsIgnoreCase("key") || args[0].equalsIgnoreCase("roleplayer") || args[0].equalsIgnoreCase("free") || args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("origin")) {
            }
        }

        return true;
    }

    public void giveKey(Player player) {
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = key.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName(" ");
        key.setItemMeta(meta);

        player.getInventory().addItem(key);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("origin");
            completions.add("start");
            completions.add("stop");
            completions.add("key");
            completions.add("free");
            completions.add("images");
            completions.add("roleplayer");
        } else if (args.length >= 2 && "key".equals(args[0].toLowerCase())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
    private void saveLocation(Player player) {
        int locationCount = plugin.getConfig().getInt("chain.locationCount", 0);
        int nextLocationIndex = (locationCount % 8) + 1;

        String basePath = "chain.locations.location" + nextLocationIndex;
        plugin.getConfig().set(basePath + ".world", player.getWorld().getName());
        plugin.getConfig().set(basePath + ".x", player.getLocation().getX());
        plugin.getConfig().set(basePath + ".y", player.getLocation().getY());
        plugin.getConfig().set(basePath + ".z", player.getLocation().getZ());
        plugin.getConfig().set("chain.locationCount", nextLocationIndex);

        plugin.saveConfig();
        player.sendMessage("Location " + nextLocationIndex + " saved.");
    }
    public static void removeTripwireHooksFromAllInventories() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == Material.TRIPWIRE_HOOK) {
                    inventory.setItem(i, null);  // Removes the tripwire hook
                }
            }
        }
    }
}
