package org.imradigamer.chainPlugin.Statue;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;

public class StatueListener implements Listener {

    private int globalProgress = 0;
    private final Map<String, Location> entityLampLocations = new HashMap<>();

    public StatueListener() {
        // Define the lamp floor locations for each estatua
        entityLampLocations.put("estatua_1", new Location(Bukkit.getWorld("world"), -217, 80, -148));
        entityLampLocations.put("estatua_2", new Location(Bukkit.getWorld("world"), -201, 80, -140));
        entityLampLocations.put("estatua_3", new Location(Bukkit.getWorld("world"), -217, 80, -140));
        entityLampLocations.put("estatua_4", new Location(Bukkit.getWorld("world"), -201, 80, -133));
        entityLampLocations.put("estatua_5", new Location(Bukkit.getWorld("world"), -201, 80, -148));
        entityLampLocations.put("estatua_6", new Location(Bukkit.getWorld("world"), -217, 80, -133));
        entityLampLocations.put("estatua_7", new Location(Bukkit.getWorld("world"), -209, 80, -140));
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity.getScoreboardTags().stream().anyMatch(tag -> tag.startsWith("estatua_"))) {
            handleEntityInteraction(player, entity);
        }
    }

    private void handleEntityInteraction(Player player, Entity entity) {
        String entityTag = entity.getScoreboardTags().stream()
                .filter(tag -> tag.startsWith("estatua_"))
                .findFirst().orElse(null);

        if (entityTag != null) {
            int entityNumber = Integer.parseInt(entityTag.split("_")[1]);

            if (entityNumber == globalProgress + 1) {
                globalProgress++;


                activateLamps(entityTag);
                for (Player p: Bukkit.getOnlinePlayers()) {
                    playProgressiveNoteSound(p, globalProgress);
                }


                if (globalProgress == 7) {
                    for(Player p: Bukkit.getOnlinePlayers()) {
                        playCompletionMelody(p);
                    }


                    Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                        resetPuzzle();
                        resetAllLamps();

                    }, 120+40L);

                    Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), "minecraft:1_es_leaking_gas_no_fire", 10F, 1F);
                        }
                    }, 60+40L);

                    Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "puzzle shader BLINK 300");
                    }, 100+40L);

                    Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "puzzle shader BLINK -1");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutevoice @a");
                    }, 300+40L);
                    Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:playaudio @a ~/assets/a/JigSaw Laugh - Penitencia SFX - ZairDeLuque Final Mix.mp3");

                    }, 320+40L);
                }
            } else {
                resetPuzzle();
            }
        }
    }

    private void playProgressiveNoteSound(Player player, int progress) {
        float pitch = 0.5f + (progress - 1) * 0.2f;

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, pitch);
    }

    private void playCompletionMelody(Player player) {
        Sound[] melodyNotes = {
                Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_CHIME
        };

        float[] pitches = {1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f, 2.2f}; // Define pitch for each note

        new BukkitRunnable() {
            int noteIndex = 0;

            @Override
            public void run() {
                if (noteIndex < melodyNotes.length) {
                    player.playSound(player.getLocation(), melodyNotes[noteIndex], 1.0f, pitches[noteIndex]);
                    noteIndex++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("PenitenciaR"), 0L, 20L); // 10 ticks delay between notes for 3.5 seconds duration
    }


    private void activateLamps(String entityTag) {
        Location center = entityLampLocations.get(entityTag);

        if (center != null) {
            int minX = center.getBlockX() - 1;
            int maxX = center.getBlockX() + 1;
            int minZ = center.getBlockZ() - 1;
            int maxZ = center.getBlockZ() + 1;
            int y = center.getBlockY() - 1;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "fill " + minX + " " + y + " " + minZ + " " + maxX + " " + y + " " + maxZ + " minecraft:redstone_block");

            Bukkit.getLogger().info("Activated lamps around: " + entityTag);
        } else {
            Bukkit.getLogger().warning("Lamp location for " + entityTag + " is null. Check if world is loaded.");
        }
    }

    private void resetAllLamps() {
        for (Location center : entityLampLocations.values()) {
            int minX = center.getBlockX() - 1;
            int maxX = center.getBlockX() + 1;
            int minZ = center.getBlockZ() - 1;
            int maxZ = center.getBlockZ() + 1;
            int y = center.getBlockY() - 1;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "fill " + minX + " " + y + " " + minZ + " " + maxX + " " + y + " " + maxZ + " minecraft:air");

        }
        Bukkit.getLogger().info("All lamps reset.");
    }

    private void resetPuzzle() {
        globalProgress = 0;

        resetAllLamps();
    }
}
