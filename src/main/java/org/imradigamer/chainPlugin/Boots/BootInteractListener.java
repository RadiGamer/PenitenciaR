package org.imradigamer.chainPlugin.Boots;

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

public class BootInteractListener implements Listener {

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        ItemStack currentBoots = player.getInventory().getBoots();
        if (currentBoots != null && currentBoots.getType() != Material.AIR) {
            return;
        }

        if (event.getRightClicked() instanceof Interaction) {
            Interaction interactionEntity = (Interaction) event.getRightClicked();

            if (interactionEntity.getScoreboardTags().contains("botas_1")) {
                interactionEntity.getNearbyEntities(2, 2, 2).stream()
                        .filter(entity -> entity instanceof ItemDisplay)
                        .map(entity -> (ItemDisplay) entity)
                        .filter(display -> display.getScoreboardTags().contains("visual_botas_1"))
                        .forEach(display -> {
                            ItemStack boots = display.getItemStack();
                            if (boots != null && boots.getType() == Material.CHAINMAIL_BOOTS) {

                                ItemMeta meta = boots.getItemMeta();
                                if (meta != null && meta.hasDisplayName()) {
                                    meta.setDisplayName(" ");
                                    boots.setItemMeta(meta);
                                }

                                player.getInventory().setBoots(boots);
                                display.setItemStack(null);

                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "clientoptions set " + player.getName() + " SHADERS FLIP";
                                Bukkit.dispatchCommand(console, command);
                            }
                        });
            }


            if (interactionEntity.getScoreboardTags().contains("botas_2")) {
                interactionEntity.getNearbyEntities(2, 2, 2).stream()
                        .filter(entity -> entity instanceof ItemDisplay)
                        .map(entity -> (ItemDisplay) entity)
                        .filter(display -> display.getScoreboardTags().contains("visual_botas_2"))
                        .forEach(display -> {
                            ItemStack boots = display.getItemStack();
                            if (boots != null && boots.getType() == Material.CHAINMAIL_BOOTS) {

                                ItemMeta meta = boots.getItemMeta();
                                if (meta != null && meta.hasDisplayName()) {
                                    meta.setDisplayName(" ");
                                    boots.setItemMeta(meta);
                                }

                                player.getInventory().setBoots(boots);
                                display.setItemStack(null);

                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "clientoptions set " + player.getName() + " SHADERS FLIP";
                                Bukkit.dispatchCommand(console, command);
                            }
                        });
            }
            if (interactionEntity.getScoreboardTags().contains("botas_3")) {
                interactionEntity.getNearbyEntities(2, 2, 2).stream()
                        .filter(entity -> entity instanceof ItemDisplay)
                        .map(entity -> (ItemDisplay) entity)
                        .filter(display -> display.getScoreboardTags().contains("visual_botas_3"))
                        .forEach(display -> {
                            ItemStack boots = display.getItemStack();
                            if (boots != null && boots.getType() == Material.CHAINMAIL_BOOTS) {

                                ItemMeta meta = boots.getItemMeta();
                                if (meta != null && meta.hasDisplayName()) {
                                    meta.setDisplayName(" ");
                                    boots.setItemMeta(meta);
                                }

                                player.getInventory().setBoots(boots);
                                display.setItemStack(null);

                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "clientoptions set " + player.getName() + " SHADERS FLIP";
                                Bukkit.dispatchCommand(console, command);
                            }
                        });
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack boots = player.getInventory().getBoots();

            if (boots != null && boots.getType() == Material.CHAINMAIL_BOOTS) {
                if (event.getSlot() == 36) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
