package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.block.data.type.Chain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DesgraciadosMovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Only proceed if the key has been used
        if (!ChainManager.isKeyUsed()) {
            return;  // Key has not been used, so we ignore this movement
        }

        // Check if the player is chained and has the 'chain.desgraciados' permission
        if (ChainManager.isPlayerChained(player) && player.hasPermission("chain.desgraciados") && ChainManager.isCommandActivated()) {
            Location initialLocation = ChainManager.getInitialChainingLocation(player);
            double currentDistance = player.getLocation().distance(initialLocation);

            // We are interested in players moving more than 2.0 blocks away from their initial location
            if (currentDistance > 3.5) {
                // Update the player's last known distance in case you need it
                ChainManager.updateLastDistance(player);


                if (ChainManager.checkAllMovedBackwards()) {
                    ChainManager.freeDesgraciados();
                    ChainManager.resetKeyUsage();
                    ChainManager.resetCommandActivated();// Reset the key usage flag
                }
            }
        }
    }
}
