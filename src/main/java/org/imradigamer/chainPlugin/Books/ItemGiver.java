package org.imradigamer.chainPlugin.Books;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGiver {

    public static void givePlayerItem(Player player, String itemName, int customModelData) {
        ItemStack item = createLeatherItem(itemName, customModelData);
        player.getInventory().addItem(item);
    }

    private static ItemStack createLeatherItem(String itemName, int customModelData) {
        ItemStack item = new ItemStack(Material.LEATHER);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");

        meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);
        return item;
    }
}
