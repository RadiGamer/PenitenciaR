package org.imradigamer.chainPlugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.imradigamer.chainPlugin.ChainPlugin;

public class StopShaderLoopCommand implements CommandExecutor {

    private final ChainPlugin plugin;
    private final StartShaderLoopCommand startCommand;

    public StopShaderLoopCommand(ChainPlugin plugin, StartShaderLoopCommand startCommand) {
        this.plugin = plugin;
        this.startCommand = startCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int taskID = startCommand.getTaskID();

        if (taskID == -1) {
            return true;
        }

        Bukkit.getScheduler().cancelTask(taskID);
        startCommand.resetTaskID();

        return true;
    }
}
