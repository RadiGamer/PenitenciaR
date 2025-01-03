package org.imradigamer.chainPlugin.Books;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class DisplayManager {

    private final World world;
    private final Location corner1 = new Location(Bukkit.getWorld("world"), 174, 107, -16); // Set to your hardcoded coordinates
    private final Location corner2 = new Location(Bukkit.getWorld("world"), 176, 107, -16); // Set to your hardcoded coordinates
    private final String targetTag = "cartel_botas";

    public DisplayManager() {
        this.world = Bukkit.getWorld("world");

        // Check if the world exists
        if (this.world == null) {
            throw new IllegalArgumentException("World 'world' not found. Please check your configuration.");
        }
    }

    private ItemDisplay getTaggedItemDisplay() {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ItemDisplay && entity.getScoreboardTags().contains(targetTag)) {
                return (ItemDisplay) entity;
            }
        }
        return null;
    }

    public void removeDisplayAndBarriers() {
        ItemDisplay itemDisplay = getTaggedItemDisplay();

        if (itemDisplay != null) {
            itemDisplay.setItemStack(null);
        }

        // Remove barriers in the specified area
        for (int x = Math.min(corner1.getBlockX(), corner2.getBlockX()); x <= Math.max(corner1.getBlockX(), corner2.getBlockX()); x++) {
            for (int y = Math.min(corner1.getBlockY(), corner2.getBlockY()); y <= Math.max(corner1.getBlockY(), corner2.getBlockY()); y++) {
                for (int z = Math.min(corner1.getBlockZ(), corner2.getBlockZ()); z <= Math.max(corner1.getBlockZ(), corner2.getBlockZ()); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.BARRIER) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public void setDisplayAndBarriers() {
        // Find the ItemDisplay with the tag "cartel_botas"
        ItemDisplay itemDisplay = getTaggedItemDisplay();

        if (itemDisplay != null) {
            ItemStack cartel = new ItemStack(Material.RAW_GOLD);
            ItemMeta meta = cartel.getItemMeta();

            if (meta != null) {
                meta.setCustomModelData(139);
                cartel.setItemMeta(meta);
                itemDisplay.setItemStack(cartel);
            }
        }

        // Place barriers in the specified area
        for (int x = Math.min(corner1.getBlockX(), corner2.getBlockX()); x <= Math.max(corner1.getBlockX(), corner2.getBlockX()); x++) {
            for (int y = Math.min(corner1.getBlockY(), corner2.getBlockY()); y <= Math.max(corner1.getBlockY(), corner2.getBlockY()); y++) {
                for (int z = Math.min(corner1.getBlockZ(), corner2.getBlockZ()); z <= Math.max(corner1.getBlockZ(), corner2.getBlockZ()); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR) {
                        block.setType(Material.BARRIER);
                    }
                }
            }
        }
    }
}
