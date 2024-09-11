package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class ChainCommand implements CommandExecutor {

    private final ChainPlugin plugin;  // Use a generic Plugin reference
    private static final Map<UUID, Long> cooldowns = new HashMap<>(); // Tracks cooldowns for players

    public ChainCommand(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "origin":
                ChainManager.setChainOrigin(player);
                player.sendMessage("Punto de origen colocado.");
                break;

            case "start":
                ChainManager.startChainingPlayers(player.getWorld());
                player.sendMessage("Encadenando a los jugadores en aventura.");
                break;

            case "stop":
                ChainManager.clearAllChains();
                player.sendMessage("Adios cadenas.");
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
        }
        return true;
    }

    private void giveKey(Player player) {
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = key.getItemMeta();
        meta.setDisplayName("Llave de Cadena");
        key.setItemMeta(meta);

        player.getInventory().addItem(key);
    }



}
