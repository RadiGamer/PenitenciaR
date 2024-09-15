package org.imradigamer.chainPlugin;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChainedPlayerMovementListener implements Listener {
    private final ChainPlugin plugin;

    public ChainedPlayerMovementListener(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (ChainManager.isPlayerChained(player)) {
            Location initialLocation = ChainManager.getInitialChainingLocation(player);
            Location to = event.getTo();
            Location from = event.getFrom();

            if (initialLocation != null && to != null) {
                // Check horizontal distance
                if (initialLocation.distance(to) > 3.0) {
                    player.teleport(initialLocation);
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event){
        Player player = event.getPlayer();

        if (ChainManager.isPlayerChained(player)) {
            event.setCancelled(true);
        }
    }
}
