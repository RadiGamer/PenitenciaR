package org.imradigamer.chainPlugin.Utils;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityUtils {

    public static int getCustomModelData(ItemDisplay itemDisplay) {
        if (itemDisplay.getItemStack() != null) {
            ItemMeta meta = itemDisplay.getItemStack().getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                return meta.getCustomModelData();
            }
        }
        return -1;
    }
}
