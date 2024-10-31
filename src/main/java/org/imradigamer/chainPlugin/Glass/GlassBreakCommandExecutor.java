package org.imradigamer.chainPlugin.Glass;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.imradigamer.chainPlugin.ChainPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlassBreakCommandExecutor implements CommandExecutor, TabCompleter {

    private final ChainPlugin plugin;

    public GlassBreakCommandExecutor(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("glass") && args.length > 0 && args[0].equalsIgnoreCase("break")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                World world = player.getWorld();

                Location start = new Location(world, 139, 56, -50);
                Location end = new Location(world, 129, 59, -50);

                GlassBreakAnimation animation = new GlassBreakAnimation(world, start, end, plugin);
                animation.start();

                player.sendMessage("Comenzando animacion de cristales");
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> subcommands = Arrays.asList("break");

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
