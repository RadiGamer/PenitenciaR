package org.imradigamer.chainPlugin.Buckets;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BucketCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command has at least one argument
        if (args.length > 0) {
            // Handle the "drop" subcommand
            if (args[0].equalsIgnoreCase("drop")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "execute as @e[tag=aj.cubeta.root] run function animated_java:cubeta/animations/fall/play");

                for (int i = 1; i <= 4; i++) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "tag @e[tag=cubeta_" + i + "] add pickable");
                }

                return true;
            }

            else if (args[0].equalsIgnoreCase("start")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "bucket drop");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "mutevoice @a");
                Bukkit.broadcastMessage("Aqui va un timer");
                Bukkit.broadcastMessage("Aqui va un audio");

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
}
