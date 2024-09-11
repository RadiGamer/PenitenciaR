package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class ChainedPlayerMovementListener implements Listener {
    private final Plugin plugin;

    public ChainedPlayerMovementListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player is currently chained
        if (ChainManager.isPlayerChained(player)) {
            Location initialLocation = ChainManager.getInitialChainingLocation(player);
            if (initialLocation != null) {
                Location to = event.getTo(); // The location the player is attempting to move to
                if (to != null && initialLocation.distance(to) > 1.5) {
                    // If the player attempts to move more than 1.5 blocks away, cancel the event
                    event.setCancelled(true);
                }
            }
        }
    }
}
