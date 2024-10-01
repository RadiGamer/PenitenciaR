package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class ChainPlugin extends JavaPlugin {

    private CameraShaker cameraShaker;
    private DoorAnimator doorAnimator;
    // private DoorManager doorManager;
    public static NamespacedKey customTagKey;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

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

        ChainManager chainManager = new ChainManager();
        getCommand("chain").setExecutor(new ChainCommand(this));
        getServer().getScheduler().runTaskTimer(this, ChainManager::updateChains, 0L, 1L);
        getServer().getPluginManager().registerEvents(new KeyUseListener(this), this);
        getServer().getPluginManager().registerEvents(new ChainedPlayerMovementListener(this), this);
        getServer().getPluginManager().registerEvents(new DesgraciadosMovementListener(), this);

        customTagKey = new NamespacedKey(this, "custom_tag");
        getCommand("tagitems").setExecutor(new ItemTaggerCommand(this));
    }

    @Override
    public void onDisable() {
        ChainManager.clearAllChains();
        cameraShaker.stopShaking();
    }
}

