package org.imradigamer.chainPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ShakeCommandExecutor implements CommandExecutor {
    private final ChainPlugin plugin;
    private final CameraShaker shaker;

    public ShakeCommandExecutor(ChainPlugin plugin, CameraShaker shaker) {
        this.plugin = plugin;
        this.shaker = shaker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("shake")) {
            if (shaker.isShaking()) {
                shaker.stopShaking();
            } else {
                shaker.startShaking();
            }
            return true;
        }
        sender.sendMessage("Uso: /camerashake shake");
        return false;
    }
}
