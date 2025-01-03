package org.imradigamer.chainPlugin.Credits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.imradigamer.chainPlugin.ChainPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CreditsCommandExecutor implements CommandExecutor, TabCompleter {

    private final ChainPlugin plugin;

    public CreditsCommandExecutor(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player p = Bukkit.getPlayer(strings[0]);
        if (p == null) {
            commandSender.sendMessage("No se encontro ese jugador!");
            return true;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopvideo @a");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                p.playSound(p.getLocation(), "minecraft:1_sonido_tinnitus", 30F, 1F);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutevoice " + p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playglow "+ p.getName() +" RED 10 10 10");
        }, 20L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"playvideo " +p.getName()+ " BLACK 1 1 ~/assets/v/CREDITOS_PENITENCIA_2.mp4");
        }, 100L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"stopsound " +p.getName());
        }, 110L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getWhitelistedPlayers().remove(p);
            p.kickPlayer(ChatColor.DARK_RED+"Gracias por participar");
        }, 1520L);


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // Return online player names
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(player.getName());
                }
            }
        }

        return suggestions;
    }
}
