package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.imradigamer.chainPlugin.Audios.AudioInteractionListener;
import org.imradigamer.chainPlugin.Books.BooksCommand;
import org.imradigamer.chainPlugin.Books.InteractionListener;
import org.imradigamer.chainPlugin.Boots.BootInteractListener;
import org.imradigamer.chainPlugin.Boots.RestartBootsCommand;
import org.imradigamer.chainPlugin.Buckets.BucketCommandExecutor;
import org.imradigamer.chainPlugin.Buckets.BucketListener;
import org.imradigamer.chainPlugin.Chains.*;
import org.imradigamer.chainPlugin.Elevator.DoorAnimator;
import org.imradigamer.chainPlugin.Elevator.DoorCommandExecutor;
import org.imradigamer.chainPlugin.Elevator.ElevatorCommand;
import org.imradigamer.chainPlugin.Glass.GlassBreakCommandExecutor;
import org.imradigamer.chainPlugin.Hater.HaterCommand;
import org.imradigamer.chainPlugin.Lights.LightCommand;
import org.imradigamer.chainPlugin.Lights.LightTabCompleter;
import org.imradigamer.chainPlugin.Chains.Listeners.ChainedPlayerMovementListener;
import org.imradigamer.chainPlugin.Chains.Listeners.DesgraciadosMovementListener;
import org.imradigamer.chainPlugin.Chains.Listeners.KeyUseListener;
import org.imradigamer.chainPlugin.Utils.StartShaderLoopCommand;
import org.imradigamer.chainPlugin.Utils.StopShaderLoopCommand;

public class ChainPlugin extends JavaPlugin {

    private CameraShaker cameraShaker;
    private DoorAnimator doorAnimator;
    private boolean isTaskRunning = false;
    private BukkitRunnable repeatingTask;
    private InteractionListener interactionListener;
    private StartShaderLoopCommand startShaderLoopCommand;

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
        startShaderLoopCommand = new StartShaderLoopCommand(this);
        interactionListener = new InteractionListener();
        LightCommand lightCommand = new LightCommand();
        //  doorManager = new DoorManager(this);

        this.getCommand("camerashake").setExecutor(new ShakeCommandExecutor(this, cameraShaker));
        this.getCommand("elevator").setExecutor(new ElevatorCommand(this));
        this.getCommand("glass").setExecutor(new GlassBreakCommandExecutor(this));
        this.getCommand("doors").setExecutor(new DoorCommandExecutor(this));
        this.getCommand("startblink").setExecutor(startShaderLoopCommand);
        this.getCommand("stopblink").setExecutor(new StopShaderLoopCommand(this, startShaderLoopCommand));
        this.getCommand("books").setExecutor(new BooksCommand(interactionListener.getEstanteBooks()));
        this.getCommand("hater").setExecutor(new HaterCommand(this));
        this.getCommand("lights").setExecutor(new LightCommand());
        this.getCommand("chain").setExecutor(new ChainCommand(this));
        this.getCommand("lights").setTabCompleter(new LightTabCompleter(lightCommand.getRooms()));
        this.getCommand("boots").setExecutor(new RestartBootsCommand(this));
        this.getCommand("bucket").setExecutor(new BucketCommandExecutor());

        ChainManager chainManager = new ChainManager(this);
        getServer().getScheduler().runTaskTimer(this, ChainManager::updateChains, 0L, 1L);
        getServer().getPluginManager().registerEvents(new KeyUseListener(this, chainManager), this);
        getServer().getPluginManager().registerEvents(new ChainedPlayerMovementListener(this), this);
        getServer().getPluginManager().registerEvents(new DesgraciadosMovementListener(), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);
        getServer().getPluginManager().registerEvents(new AudioInteractionListener(),this);
        getServer().getPluginManager().registerEvents(new BootInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BucketListener(), this);



    }

    @Override
    public void onDisable() {
        ChainManager.clearAllChains();
    }


}

