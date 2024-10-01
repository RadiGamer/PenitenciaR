package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class DoorAnimator {

    private final JavaPlugin plugin;

    public DoorAnimator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void animateDoors(boolean open) {
        // Define the bounds
        Location minBoundLat = new Location(plugin.getServer().getWorld("world"), 111, 52, 0);
        Location maxBoundLat = new Location(plugin.getServer().getWorld("world"), 136, 60, 4);

        Location cornerElevator1_1 = new Location(plugin.getServer().getWorld("world"), 132, 55, 1);
        Location cornerElevator1_2 = new Location(plugin.getServer().getWorld("world"), 130, 58, 1);

        Location cornerElevator2_1 = new Location(plugin.getServer().getWorld("world"), 124, 55, 1);
        Location cornerElevator2_2 = new Location(plugin.getServer().getWorld("world"), 122, 58, 1);

        Location cornerElevator3_1 = new Location(plugin.getServer().getWorld("world"), 116, 55, 1);
        Location cornerElevator3_2 = new Location(plugin.getServer().getWorld("world"), 114, 58, 1);

        if(open){
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator1_1, cornerElevator1_2);
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator2_1, cornerElevator2_2);
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator3_1, cornerElevator3_2);
        }

        if(!open) {
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator1_1, cornerElevator1_2);
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator2_1, cornerElevator2_2);
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator3_1, cornerElevator3_2);
        }


        // Loop through all entities in the specified bounds
        for (Entity entity : minBoundLat.getWorld().getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay itemDisplayEntity = (ItemDisplay) entity;

                // Check if entity is within bounds and has custom model data 57 or 58
                if (isEntityInBounds(itemDisplayEntity, minBoundLat, maxBoundLat)) {
                    int customModelData = EntityUtils.getCustomModelData(itemDisplayEntity);

                    if (customModelData == 57) {
                        // Move right if opening, left if closing
                        animateEntity(itemDisplayEntity, open ? new Vector(-2, 0, 0) : new Vector(2, 0, 0));
                    } else if (customModelData == 58) {
                        // Move left if opening, right if closing
                        animateEntity(itemDisplayEntity, open ? new Vector(2, 0, 0) : new Vector(-2, 0, 0));
                    } else if (customModelData == 53) {
                        animateEntity(itemDisplayEntity, open ? new Vector(-2, 0, 0) : new Vector(2, 0, 0));
                    }else if (customModelData == 54) {
                        animateEntity(itemDisplayEntity, open ? new Vector(2, 0, 0) : new Vector(-2, 0, 0));
                    }
                }
            }
        }
    }

    public void animateDoors2(boolean open) {
        // Define the bounds
        Location minBoundLat = new Location(plugin.getServer().getWorld("world"), 111, 72, 7);
        Location maxBoundLat = new Location(plugin.getServer().getWorld("world"), 136, 95, 14);

        Location cornerElevator1_1 = new Location(plugin.getServer().getWorld("world"), 132, 74, 10);
        Location cornerElevator1_2 = new Location(plugin.getServer().getWorld("world"), 130, 77, 10);

        Location cornerElevator2_1 = new Location(plugin.getServer().getWorld("world"), 124, 88, 10);
        Location cornerElevator2_2 = new Location(plugin.getServer().getWorld("world"), 122, 91, 10);

        Location cornerElevator3_1 = new Location(plugin.getServer().getWorld("world"), 116, 74, 10);
        Location cornerElevator3_2 = new Location(plugin.getServer().getWorld("world"), 114, 77, 10);

        if(open){
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator1_1, cornerElevator1_2);
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator2_1, cornerElevator2_2);
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator3_1, cornerElevator3_2);
        }

        if(!open) {
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator1_1, cornerElevator1_2);
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator2_1, cornerElevator2_2);
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator3_1, cornerElevator3_2);
        }


        for (Entity entity : minBoundLat.getWorld().getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay itemDisplayEntity = (ItemDisplay) entity;

                if (isEntityInBounds(itemDisplayEntity, minBoundLat, maxBoundLat)) {
                    int customModelData = EntityUtils.getCustomModelData(itemDisplayEntity);

                    if (customModelData == 57) {
                        animateEntity(itemDisplayEntity, open ? new Vector(2, 0, 0) : new Vector(-2, 0, 0));
                    } else if (customModelData == 58) {
                        animateEntity(itemDisplayEntity, open ? new Vector(-2, 0, 0) : new Vector(2, 0, 0));
                    } else if (customModelData == 53) {
                        animateEntity(itemDisplayEntity, open ? new Vector(2, 0, 0) : new Vector(-2, 0, 0));
                    }else if (customModelData == 54) {
                        animateEntity(itemDisplayEntity, open ? new Vector(-2, 0, 0) : new Vector(2, 0, 0));
                    }
                }
            }
        }
    }
    public void animateDoors3(boolean open) {
        // Define the bounds
        Location minBoundLat = new Location(plugin.getServer().getWorld("world"), 111, 104, 0);
        Location maxBoundLat = new Location(plugin.getServer().getWorld("world"), 136, 111, 4);

        Location cornerElevator1_1 = new Location(plugin.getServer().getWorld("world"), 132, 106, 1);
        Location cornerElevator1_2 = new Location(plugin.getServer().getWorld("world"), 130, 109, 1);

        Location cornerElevator2_1 = new Location(plugin.getServer().getWorld("world"), 124, 106, 1);
        Location cornerElevator2_2 = new Location(plugin.getServer().getWorld("world"), 122, 109, 1);

        Location cornerElevator3_1 = new Location(plugin.getServer().getWorld("world"), 116, 106, 1);
        Location cornerElevator3_2 = new Location(plugin.getServer().getWorld("world"), 114, 109, 1);

        if(open){
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator1_1, cornerElevator1_2);
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator2_1, cornerElevator2_2);
            removeBarrier(plugin.getServer().getWorld("world"), cornerElevator3_1, cornerElevator3_2);
        }

        if(!open) {
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator1_1, cornerElevator1_2);
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator2_1, cornerElevator2_2);
            placeBarrier(plugin.getServer().getWorld("world"), cornerElevator3_1, cornerElevator3_2);
        }

        for (Entity entity : minBoundLat.getWorld().getEntities()) {
            if (entity instanceof ItemDisplay) {
                ItemDisplay itemDisplayEntity = (ItemDisplay) entity;

                if (isEntityInBounds(itemDisplayEntity, minBoundLat, maxBoundLat)) {
                    int customModelData = EntityUtils.getCustomModelData(itemDisplayEntity);

                    if (customModelData == 57) {
                        animateEntity(itemDisplayEntity, open ? new Vector(-2, 0, 0) : new Vector(2, 0, 0));
                    } else if (customModelData == 58) {
                        animateEntity(itemDisplayEntity, open ? new Vector(2, 0, 0) : new Vector(-2, 0, 0));
                    } else if (customModelData == 53) {
                        animateEntity(itemDisplayEntity, open ? new Vector(-2, 0, 0) : new Vector(2, 0, 0));
                    }else if (customModelData == 54) {
                        animateEntity(itemDisplayEntity, open ? new Vector(2, 0, 0) : new Vector(-2, 0, 0));
                    }
                }
            }
        }
    }

    private boolean isEntityInBounds(Display entity, Location min, Location max) {
        Location loc = entity.getLocation();
        return loc.getX() >= min.getX() && loc.getX() <= max.getX()
                && loc.getY() >= min.getY() && loc.getY() <= max.getY()
                && loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ();
    }

    private void animateEntity(ItemDisplay entity, Vector direction) {
        // Increase the steps and decrease the movement for smoother animation
        int steps = 30; // Keep the number of steps
        double moveAmount = 0.04; // Keep the small movement amount for smoothness

        for (int i = 0; i < steps; i++) {
            final int step = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location currentLocation = entity.getLocation();
                entity.teleport(currentLocation.add(direction.clone().multiply(moveAmount)));
            }, step * 1L); // Reduce delay to speed up by 50%
        }
    }
    public void placeBarrier(World world, Location corner1, Location corner2) {
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.BARRIER);
                }
            }
        }
    }
    public void removeBarrier(World world, Location corner1, Location corner2) {
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }
    }
}
