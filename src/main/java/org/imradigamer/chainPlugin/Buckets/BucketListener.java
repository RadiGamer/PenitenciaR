package org.imradigamer.chainPlugin.Buckets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class BucketListener implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Interaction interactionEntity) {
            Player player = event.getPlayer();

            if (interactionEntity.getScoreboardTags().stream().anyMatch(tag -> tag.startsWith("cubeta_")) &&
                    interactionEntity.getScoreboardTags().contains("pickable")) {

                interactionEntity.removeScoreboardTag("pickable");

                ItemStack bucket = new ItemStack(Material.BUCKET);
                ItemMeta meta = bucket.getItemMeta();
                meta.setDisplayName(" ");
                bucket.setItemMeta(meta);
                player.getInventory().addItem(bucket);

                Location entityLocation = interactionEntity.getLocation();
                double x = entityLocation.getX();
                double y = entityLocation.getY();
                double z = entityLocation.getZ();

                String command = String.format(
                        "execute positioned %.2f %.2f %.2f run execute as @n[limit=1,tag=aj.cubeta.root] run function animated_java:cubeta/animations/fall/stop",
                        x, y, z
                );

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                Bukkit.getLogger().info(command);

                event.setCancelled(true);
            }
        }
    }
}

