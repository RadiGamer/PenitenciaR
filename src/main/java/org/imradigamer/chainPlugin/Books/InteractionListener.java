package org.imradigamer.chainPlugin.Books;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class InteractionListener implements Listener {

    private static final long COOLDOWN_TIME = 5000;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof Interaction) {
            Interaction interaction = (Interaction) event.getRightClicked();

            if (interaction.getScoreboardTags().contains("libro_1")) {
                ItemGiver.givePlayerItem(player, "Libro 1", 26);
                removeNearbyDisplay(player, Material.LEATHER, 26);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_2")) {
                ItemGiver.givePlayerItem(player, "Libro 2", 43);
                removeNearbyDisplay(player, Material.LEATHER, 43);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_3")) {
                ItemGiver.givePlayerItem(player, "Libro 3", 27);
                removeNearbyDisplay(player, Material.LEATHER, 27);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_4")) {
                ItemGiver.givePlayerItem(player, "Libro 4", 28);
                removeNearbyDisplay(player, Material.LEATHER, 28);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_5")) {
                ItemGiver.givePlayerItem(player, "Libro 5", 29);
                removeNearbyDisplay(player, Material.LEATHER, 29);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_6")) {
                ItemGiver.givePlayerItem(player, "Libro 6", 30);
                removeNearbyDisplay(player, Material.LEATHER, 30);
                interaction.remove();
            }else if (interaction.getScoreboardTags().contains("libro_7")) {
                ItemGiver.givePlayerItem(player, "Libro 7", 36);
                removeNearbyDisplay(player, Material.LEATHER, 36);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_8")) {
                ItemGiver.givePlayerItem(player, "Libro 8", 31);
                removeNearbyDisplay(player, Material.LEATHER, 31);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_9")) {
                ItemGiver.givePlayerItem(player, "Libro 9", 32);
                removeNearbyDisplay(player, Material.LEATHER, 32);
                interaction.remove();
            } else if (interaction.getScoreboardTags().contains("libro_10")) {
                ItemGiver.givePlayerItem(player, "Libro 10", 33);
                removeNearbyDisplay(player, Material.LEATHER, 33);
                interaction.remove();
            }
        }
    }
    private void removeNearbyDisplay(Player player, Material material, int customModelData) {
        player.getNearbyEntities(3, 3, 3).stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .forEach(itemDisplay -> {
                    ItemStack displayedItem = itemDisplay.getItemStack();

                    if (displayedItem.getType() == material) {
                        ItemMeta meta = displayedItem.getItemMeta();
                        if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == customModelData) {
                            itemDisplay.remove();
                        }
                    }
                });
    }
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand != null && itemInHand.getType() == Material.LEATHER) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta != null && meta.hasCustomModelData()) {
                    int customModelData = meta.getCustomModelData();

                    if (isInCooldown(player)) {
                        return;
                    }

                    switch (customModelData) {
                        case 26:
                            broadcastLibroMessage(1);
                            break;
                        case 43:
                            broadcastLibroMessage(2);
                            break;
                        case 27:
                            broadcastLibroMessage(3);
                            break;
                        case 28:
                            broadcastLibroMessage(4);
                            break;
                        case 29:
                            broadcastLibroMessage(5);
                            break;
                        case 30:
                            broadcastLibroMessage(6);
                            break;
                        case 31:
                            broadcastLibroMessage(8);
                            break;
                        case 32:
                            broadcastLibroMessage(9);
                            break;
                        case 33:
                            broadcastLibroMessage(10);
                            break;
                        default:
                            break;
                    }
                    setCooldown(player);
                }
            }
        }
    }

    private void broadcastLibroMessage(int libroNumber) {
        Bukkit.broadcastMessage("Aquí se interactuó con el libro " + libroNumber);
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private boolean isInCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long lastInteractionTime = cooldowns.get(playerId);
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastInteractionTime < COOLDOWN_TIME) {
                return true;
            }
        }
        return false;
    }
}

