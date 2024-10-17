package org.imradigamer.chainPlugin.Chains.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.imradigamer.chainPlugin.Chains.ChainManager;

public class DesgraciadosMovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!ChainManager.isKeyUsed()) {
            return;
        }

        if (ChainManager.isPlayerChained(player) && player.hasPermission("chain.desgraciados") && ChainManager.isCommandActivated()) {
            Location initialLocation = ChainManager.getInitialChainingLocation(player);
            double currentDistance = player.getLocation().distance(initialLocation);

            if (currentDistance > 3.5) {
                ChainManager.updateLastDistance(player);


                if (ChainManager.checkAllMovedBackwards()) {
                    ChainManager.freeDesgraciados();
                    ChainManager.resetKeyUsage();
                    ChainManager.resetCommandActivated();
                    ChainManager.resetKeyActive();
                }
            }
        }
    }
}
