package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class KeyUseListener implements Listener {
    private final ChainPlugin plugin;

    public KeyUseListener(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUseKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.TRIPWIRE_HOOK && item.hasItemMeta() && "Llave de Cadena".equals(item.getItemMeta().getDisplayName())) {
                // Remove one key from the player's inventory
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().remove(item);
                }

                // Check if the player has the 'chain.roleplayer' permission
                if (player.hasPermission("chain.roleplayer")) {
                    if (ChainManager.isPlayerChained(player)) {
                        ChainManager.clearPlayerChain(player);
                    }

                    // Free other players without 'chain.desgraciados' after 30 seconds
                    Bukkit.getScheduler().runTaskLater(plugin, () -> ChainManager.freePlayersWithoutPermission("chain.desgraciados"), 600L);  // 600 ticks = 30 seconds

                    player.sendMessage("Has utilizado tu llave. Otros jugadores sin el permiso 'chain.desgraciados' serán liberados en 30 segundos.");
                } else {
                    player.sendMessage("No tienes los permisos necesarios para usar esta llave.");
                }
        }
    }
}
