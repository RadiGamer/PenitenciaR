package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.imradigamer.chainPlugin.Books.InteractionListener;
import org.imradigamer.chainPlugin.Chains.*;
import org.imradigamer.chainPlugin.Elevator.DoorAnimator;
import org.imradigamer.chainPlugin.Elevator.DoorCommandExecutor;
import org.imradigamer.chainPlugin.Elevator.ElevatorCommand;
import org.imradigamer.chainPlugin.Glass.GlassBreakCommandExecutor;
import org.imradigamer.chainPlugin.Listeners.ChainedPlayerMovementListener;
import org.imradigamer.chainPlugin.Listeners.DesgraciadosMovementListener;
import org.imradigamer.chainPlugin.Listeners.KeyUseListener;
import org.imradigamer.chainPlugin.Utils.StartShaderLoopCommand;
import org.imradigamer.chainPlugin.Utils.StopShaderLoopCommand;

public class ChainPlugin extends JavaPlugin {

    private CameraShaker cameraShaker;
    private DoorAnimator doorAnimator;
    private boolean isTaskRunning = false;
    private BukkitRunnable repeatingTask;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.getLogger().info(ChatColor.GOLD + "Activando PenitenciaR." + ChatColor.WHITE +" by "+ ChatColor.AQUA + "Radi ;)");

        if (getConfig().contains("chain.origin")) {
            World world = Bukkit.getWorld(getConfig().getString("chain.origin.world"));
            double x = getConfig().getDouble("chain.origin.x");
            double y = getConfig().getDouble("chain.origin.y");
            double z = getConfig().getDouble("chain.origin.z");
            Location loadedOrigin = new Location(world, x, y, z);
            ChainManager.setChainOrigin(loadedOrigin);
        }

        cameraShaker = new CameraShaker(this);
        doorAnimator = new DoorAnimator(this);
        //  doorManager = new DoorManager(this);
        this.getCommand("camerashake").setExecutor(new ShakeCommandExecutor(this, cameraShaker));
        this.getCommand("elevator").setExecutor(new ElevatorCommand(this));
        this.getCommand("glass").setExecutor(new GlassBreakCommandExecutor(this));
        this.getCommand("doors").setExecutor(new DoorCommandExecutor(this));

        this.getCommand("startblink").setExecutor(new StartShaderLoopCommand(this));
        this.getCommand("stopblink").setExecutor(new StopShaderLoopCommand(this));

        ChainManager chainManager = new ChainManager(this);
        getCommand("chain").setExecutor(new ChainCommand(this));
        getServer().getScheduler().runTaskTimer(this, ChainManager::updateChains, 0L, 1L);
        getServer().getPluginManager().registerEvents(new KeyUseListener(this, chainManager), this);
        getServer().getPluginManager().registerEvents(new ChainedPlayerMovementListener(this), this);
        getServer().getPluginManager().registerEvents(new DesgraciadosMovementListener(), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);



    }

    @Override
    public void onDisable() {
        ChainManager.clearAllChains();
    }

    public void startPuzzleShaderTask() {
        if (isTaskRunning) {
            return; // Don't start a new task if it's already running
        }

        repeatingTask = new BukkitRunnable() {
            @Override
            public void run() {
                // This is where the command is executed every 3 seconds
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "puzzle shader RED 1");
            }
        };
        repeatingTask.runTaskTimer(this, 0, 60); // 60 ticks = 3 seconds
        isTaskRunning = true;
    }

    // Method to stop the repeating task
    public void stopPuzzleShaderTask() {
        if (repeatingTask != null && !repeatingTask.isCancelled()) {
            repeatingTask.cancel();
        }
        isTaskRunning = false;
    }

    // Optional: Method to toggle the task (start if stopped, stop if running)
    public void togglePuzzleShaderTask() {
        if (isTaskRunning) {
            stopPuzzleShaderTask();
        } else {
            startPuzzleShaderTask();
        }
    }
}

