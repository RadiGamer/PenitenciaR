package org.imradigamer.chainPlugin.Circus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

public class BarrierManager {

    public void modifyBarrierBlocks(int area, boolean open) {

        // Retrieve the world instance
        World world = Bukkit.getWorld("world");
        if (world == null) {
            return;
        }

        // Define bounding box for each area
        BoundingBox areaBounds;
        switch (area) {
            case 1:
                areaBounds = new BoundingBox(-181, 80, -237, -181, 82, -234);
                break;
            case 2:
                areaBounds = new BoundingBox(-130, 80, -236, -130, 82, -235);
                break;
            case 3:
                areaBounds = new BoundingBox(-210, 81, -217, -208, 84, -217);
                break;
            default:
                return;
        }

        // Iterate over each block in the bounding box
        for (int x = (int) areaBounds.getMinX(); x <= areaBounds.getMaxX(); x++) {
            for (int y = (int) areaBounds.getMinY(); y <= areaBounds.getMaxY(); y++) {
                for (int z = (int) areaBounds.getMinZ(); z <= areaBounds.getMaxZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);

                    if (open) {
                        // Open: Replace barriers with air
                        if (block.getType() == Material.BARRIER) {
                            block.setType(Material.AIR);
                        }
                    } else {
                        // Close: Replace air with barriers
                        if (block.getType() == Material.AIR) {
                            block.setType(Material.BARRIER);
                        }
                    }
                }
            }
        }
    }
}
