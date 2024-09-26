package org.imradigamer.chainPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoorCommandExecutor implements CommandExecutor {

    private final DoorManager doorManager;

    public DoorCommandExecutor(DoorManager doorManager) {
        this.doorManager = doorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) return false;

        if (args[0].equalsIgnoreCase("open")) {
            doorManager.openDoors();
            sender.sendMessage("Doors are opening...");
        } else if (args[0].equalsIgnoreCase("close")) {
            doorManager.closeDoors();
            sender.sendMessage("Doors are closing...");
        } else {
            sender.sendMessage("Unknown command.");
        }

        return true;
    }
}
