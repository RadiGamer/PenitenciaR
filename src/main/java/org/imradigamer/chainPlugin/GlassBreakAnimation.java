package org.imradigamer.chainPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlassBreakAnimation {

    private final World world;
    private final List<Block> glassBlocks = new ArrayList<>();
    private final ChainPlugin plugin;
    private int brokenBlocks = 0;

    public GlassBreakAnimation(World world, Location start, Location end, ChainPlugin plugin) {
        this.world = world;
        this.plugin = plugin;
        collectGlassPanes(start, end);
        Collections.shuffle(glassBlocks);
    }

    private void collectGlassPanes(Location start, Location end) {
        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        // Mensaje de depuración: Muestra el rango de coordenadas que se está revisando
        Bukkit.getLogger().info("Revisando paneles de cristal entre: (" + minX + ", " + minY + ", " + minZ + ") y (" + maxX + ", " + maxY + ", " + maxZ + ")");

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                        glassBlocks.add(block);
                }
            }
        }

        // Otro mensaje de depuración si no se encontraron paneles de cristal
        if (glassBlocks.isEmpty()) {
            Bukkit.getLogger().info("No se encontraron paneles de cristal en el rango seleccionado.");
        }
    }

    public void start() {
        if (glassBlocks.isEmpty()) {
            Bukkit.getLogger().info("No hay paneles de cristal para romper. Cancelando la animación.");
            return;
        }

        // Iniciar la animación
        breakGlassPanesSequentially();
    }

    private void breakGlassPanesSequentially() {
        new BukkitRunnable() {
            int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < glassBlocks.size()) {
                    if (brokenBlocks >= (glassBlocks.size() * 0.3)) {
                        explodeAllGlassPanes();
                        cancel();
                        return;
                    }

                    Block glassBlock = glassBlocks.get(currentIndex);

                    breakGlassPane(glassBlock);

                    currentIndex++;

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 4L);
    }
    private void breakGlassPane(Block glassBlock) {
        new BukkitRunnable() {
            int step = 0;

            @Override
            public void run() {
                if (step < 10) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        // p.sendBlockDamage(glassBlock.getLocation(), step / 9.0f);
                    }
                    step++;
                } else {
                    glassBlock.setType(Material.AIR);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        //p.sendBlockDamage(glassBlock.getLocation(), 0f);
                    }
                    glassBlock.getWorld().playSound(glassBlock.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
                    brokenBlocks++;
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    private void explodeAllGlassPanes() {
        for (Block glassBlock : glassBlocks) {
            if (glassBlock.getType() == Material.GLASS_PANE) {
                glassBlock.getWorld().spawnParticle(Particle.BLOCK, glassBlock.getLocation().add(0.5, 0.5, 0.5), 30, 0.3, 0.3, 0.3, glassBlock.getBlockData());

                glassBlock.setType(Material.AIR);

                glassBlock.getWorld().playSound(glassBlock.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            }
        }
        Location startbarrier = new Location(world, 139, 56, -49);
        Location endbarrier = new Location(world, 129, 59, -49);
        replaceBarriersWithAir(startbarrier, endbarrier);
    }
    public void replaceBarriersWithAir(Location start, Location end) {
        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.BARRIER) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

}
