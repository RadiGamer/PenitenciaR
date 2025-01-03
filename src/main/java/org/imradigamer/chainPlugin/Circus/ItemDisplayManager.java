package org.imradigamer.chainPlugin.Circus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDisplayManager {

    public void updateItemDisplay(String tag, int modelData) {
        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof ItemDisplay && entity.getScoreboardTags().contains(tag)) {
                ItemDisplay itemDisplay = (ItemDisplay) entity;
                ItemStack itemStack = new ItemStack(Material.RAW_GOLD);
                ItemMeta meta = itemStack.getItemMeta();
                    meta.setCustomModelData(modelData);
                    itemStack.setItemMeta(meta);
                itemDisplay.setItemStack(itemStack);
            }
        }
    }
}
