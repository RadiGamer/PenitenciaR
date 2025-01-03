package org.imradigamer.chainPlugin.Safe;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SafeInteractListener implements Listener {

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        ItemStack currentBoots = player.getInventory().getBoots();
        if (currentBoots != null && currentBoots.getType() != Material.AIR) {
            return;
        }

        if (event.getRightClicked() instanceof Interaction) {
            Interaction interactionEntity = (Interaction) event.getRightClicked();
            if(!player.getScoreboardTags().contains("tarjeta_recogida")) {
                if (interactionEntity.getScoreboardTags().contains("int_tarjeta_1")) {
                    interactionEntity.getNearbyEntities(5, 5, 5).stream()
                            .filter(entity -> entity instanceof ItemDisplay)
                            .map(entity -> (ItemDisplay) entity)
                            .filter(display -> display.getScoreboardTags().contains("tarjeta_1"))
                            .forEach(display -> {
                                ItemStack raw_gold = display.getItemStack();
                                if (raw_gold != null && raw_gold.getType() == Material.RAW_GOLD) {

                                    display.setItemStack(null);
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm items give " + player.getName() + " elevadorKey");
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:clientoptions set " + player.getName() + " CHATVISIBILITY false");
                                    for (int i = 0; i < 200; i++) {
                                        player.sendMessage(" ");
                                    }
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:clientoptions set " + player.getName() + " CHATVISIBILITY true");
                                    player.getScoreboardTags().add("tarjeta");
                                }
                            });
                }
            }

        if(!player.getScoreboardTags().contains("tarjeta_recogida")) {
            if (interactionEntity.getScoreboardTags().contains("int_tarjeta_2")) {
                interactionEntity.getNearbyEntities(5, 5, 5).stream()
                        .filter(entity -> entity instanceof ItemDisplay)
                        .map(entity -> (ItemDisplay) entity)
                        .filter(display -> display.getScoreboardTags().contains("tarjeta_2"))
                        .forEach(display -> {
                            ItemStack raw_gold = display.getItemStack();
                            if (raw_gold != null && raw_gold.getType() == Material.RAW_GOLD) {

                                display.setItemStack(null);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm items give " + player.getName() + " elevadorKey");
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:clientoptions set " + player.getName() + " CHATVISIBILITY false");
                                for (int i = 0; i < 200; i++) {
                                    player.sendMessage(" ");
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:clientoptions set " + player.getName() + " CHATVISIBILITY true");
                                player.getScoreboardTags().add("tarjeta_recogida");

                            }
                        });
                }
            }
        }
    }
}
