package org.imradigamer.chainPlugin.Elevator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoorCommandExecutor implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;

    public DoorCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            sender.sendMessage("Uso: /doors <open|close>");
            return false;
        }

        DoorAnimator animator = new DoorAnimator(plugin);

        if (args[0].equalsIgnoreCase("open")) {
            animator.animateDoors(true);  // Open doors
        } else if (args[0].equalsIgnoreCase("close")) {
            animator.animateDoors(false);  // Close doors
        }

        else if (args[0].equalsIgnoreCase("open2")) {
            animator.animateDoors2(true);  // Close doors
        }else if (args[0].equalsIgnoreCase("close2")) {
            animator.animateDoors2(false);  // Close doors
        }


        else if (args[0].equalsIgnoreCase("open3")) {
            animator.animateDoors3(true);  // Close doors
        }else if (args[0].equalsIgnoreCase("close3")) {
            animator.animateDoors3(false);  // Close doors
        }


        else {
            sender.sendMessage("Usage: /doors <open|close>");
            return false;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("open", "close", "open2", "close2", "open3", "close3");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        }

        return completions;
    }
}
