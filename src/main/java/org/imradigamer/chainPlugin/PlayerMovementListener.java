package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerMovementListener implements Listener {

    private final ChainManager chainManager;

    public PlayerMovementListener(ChainManager chainManager) {
        this.chainManager = chainManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (ChainManager.isPlayerChained(player)) {
            Location initialLocation = ChainManager.getInitialChainingLocation(player);
            if (initialLocation != null && event.getTo() != null) {
                if (initialLocation.distance(event.getTo()) > 2) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerUseKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        ChainManager.onPlayerUseKey(player, item);
    }
}
