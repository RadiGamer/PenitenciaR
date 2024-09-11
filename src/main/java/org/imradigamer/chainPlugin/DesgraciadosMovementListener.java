package org.imradigamer.chainPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class DesgraciadosMovementListener implements Listener {
    private final Plugin plugin;

    public DesgraciadosMovementListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (ChainManager.isPlayerChained(player) && player.hasPermission("chain.desgraciados")) {
            double currentDistance = player.getLocation().distance(ChainManager.getChainOrigin());

            if (currentDistance > ChainManager.getLastDistance(player.getUniqueId())) {
                ChainManager.updateLastDistance(player); // Update last distance from the origin

                // Check if all required players have moved away from the origin
                if (ChainManager.checkAllMovedAwayFromOrigin()) {
                    ChainManager.freeDesgraciados(); // Free all relevant players
                }
            }
        }
    }
}
