package org.imradigamer.chainPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DoorCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;

    public DoorCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /doors <open|close>");
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
}
