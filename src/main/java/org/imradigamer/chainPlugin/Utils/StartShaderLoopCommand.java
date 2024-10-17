package org.imradigamer.chainPlugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.imradigamer.chainPlugin.ChainPlugin;

public class StartShaderLoopCommand implements CommandExecutor {

    private final ChainPlugin plugin;
    private int taskID = -1; // Store task ID to manage the running task

    public StartShaderLoopCommand(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the shader loop is already running
        if (taskID != -1) {
            return true;
        }

        // Start the repeating task
        taskID = new BukkitRunnable() {
            @Override
            public void run() {
                // Execute the puzzle shader command
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "puzzle shader RED 1");
            }
        }.runTaskTimer(plugin, 0L, 60L).getTaskId(); // 60 ticks = 3 seconds (20 ticks = 1 second)

        return true;
    }

    public int getTaskID() {
        return taskID;
    }

    public void resetTaskID() {
        this.taskID = -1;
    }
}
