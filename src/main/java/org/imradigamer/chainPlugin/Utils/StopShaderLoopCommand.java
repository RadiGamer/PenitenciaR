package org.imradigamer.chainPlugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.imradigamer.chainPlugin.ChainPlugin;


public class StopShaderLoopCommand implements CommandExecutor {

    private final ChainPlugin plugin;
    private final StartShaderLoopCommand startCommand; // Reference to start command to get task ID

    public StopShaderLoopCommand(ChainPlugin plugin) {
        this.plugin = plugin;
        this.startCommand = new StartShaderLoopCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int taskID = startCommand.getTaskID();

        // Check if there is a running task
        if (taskID == -1) {
            return true;
        }

        // Stop the repeating task
        Bukkit.getScheduler().cancelTask(taskID);
        startCommand.resetTaskID(); // Reset the task ID to -1 after stopping

        return true;
    }
}
