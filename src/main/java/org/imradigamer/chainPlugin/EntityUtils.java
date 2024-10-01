package org.imradigamer.chainPlugin;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityUtils {

    // Get custom model data of the display entity if it's an ItemDisplay
    public static int getCustomModelData(ItemDisplay itemDisplay) {
        if (itemDisplay.getItemStack() != null) {
            ItemMeta meta = itemDisplay.getItemStack().getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                return meta.getCustomModelData();
            }
        }
        return -1;  // Return -1 if no custom model data is found
    }
}
