package org.imradigamer.chainPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.imradigamer.chainPlugin.ChainPlugin;
import org.imradigamer.chainPlugin.Chains.ChainManager;

public class KeyUseListener implements Listener {
    private final ChainPlugin plugin;
    private ChainManager chainManager;

    public KeyUseListener(ChainPlugin plugin, ChainManager chainManager) {
        this.plugin = plugin;
        this.chainManager = chainManager;
    }

    @EventHandler
    public void onPlayerUseKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.TRIPWIRE_HOOK && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {

            if (player.hasPermission("chain.roleplayer")) {
                if (ChainManager.isKeyActive()) {
                    if (ChainManager.isPlayerChained(player)) {
                        ChainManager.clearPlayerChain(player);
                        ChainManager.setKeyUsed(true);
                        if (item.getAmount() > 1) {
                            item.setAmount(item.getAmount() - 1);
                        } else {
                            player.getInventory().remove(item);
                        }
                    }
                    Bukkit.getScheduler().runTaskLater(plugin, () -> ChainManager.freePlayersWithoutPermission("chain.desgraciados"), 300L);  // 600 ticks = 30 seconds //TODO TIEMPO DE ESTA MADREEEEEEEE
                }
            }
        }
    }
}
