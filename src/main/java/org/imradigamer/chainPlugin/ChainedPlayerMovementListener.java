package org.imradigamer.chainPlugin;

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
                if (initialLocation.distance(to) > 2.5) {
                    player.teleport(initialLocation);
                    event.setCancelled(true);
                    return;
                }

                // Prevent jumping by checking Y difference
                if (to.getY() > from.getY()) {
                    to.setY(from.getY());
                    player.teleport(to);
                    event.setCancelled(true);
                }
            }
        }
    }
}
