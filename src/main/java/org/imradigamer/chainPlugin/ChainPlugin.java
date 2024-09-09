package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChainPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        ChainManager chainManager = new ChainManager();
        getCommand("chain").setExecutor(new ChainCommand(this));
        getServer().getScheduler().runTaskTimer(this, ChainManager::updateChains, 0L, 1L);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(chainManager), this);
        // Initialize other plugin features if needed
    }

    @Override
    public void onDisable() {
        ChainManager.clearAllChains();
    }
    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (ChainManager.isPlayerChained(player)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (to != null && from != null) {
                if (to.getY() > from.getY()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}

