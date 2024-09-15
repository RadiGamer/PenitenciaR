package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChainPlugin extends JavaPlugin {

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

        ChainManager chainManager = new ChainManager();
        getCommand("chain").setExecutor(new ChainCommand(this));
        getServer().getScheduler().runTaskTimer(this, ChainManager::updateChains, 0L, 1L);
        getServer().getPluginManager().registerEvents(new KeyUseListener(this), this);
        getServer().getPluginManager().registerEvents(new ChainedPlayerMovementListener(this), this);
        getServer().getPluginManager().registerEvents(new DesgraciadosMovementListener(), this);
    }

    @Override
    public void onDisable() {
        ChainManager.clearAllChains();
    }
}

