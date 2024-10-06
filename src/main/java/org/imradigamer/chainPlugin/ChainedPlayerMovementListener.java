package org.imradigamer.chainPlugin;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                if (initialLocation.distance(to) > 4.0) {
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
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getSlot() == 39) {
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() == Material.CARVED_PUMPKIN) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
