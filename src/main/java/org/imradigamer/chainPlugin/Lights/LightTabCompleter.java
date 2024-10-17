package org.imradigamer.chainPlugin.Lights;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LightTabCompleter implements TabCompleter {

    private final Map<String, Room> rooms;

    public LightTabCompleter(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("on", "off", "flick", "loop");
            return subCommands.stream()
                    .filter(subCommand -> subCommand.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return rooms.keySet().stream()
                    .filter(roomName -> roomName.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
