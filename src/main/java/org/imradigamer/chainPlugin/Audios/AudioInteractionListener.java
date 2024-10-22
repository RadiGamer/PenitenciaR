package org.imradigamer.chainPlugin.Audios;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class AudioInteractionListener implements Listener {

    private final HashMap<String, CommandSequence[]> commandSequences = new HashMap<>();
    private final HashMap<Entity, HashMap<String, Long>> entityInteractionCooldowns = new HashMap<>();
    private final HashMap<Entity, HashMap<String, Boolean>> entityInteractedStatus = new HashMap<>();
    private final HashMap<String, Long> cooldownTimes = new HashMap<>();

    public AudioInteractionListener() {
        setupCommandSequences();
        setupCooldownTimes();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        for (String tag : entity.getScoreboardTags()) {
            if (tag.startsWith("audio_")) {
                String audioNumber = tag.split("_")[1];

                if (isEntityOnCooldown(entity, tag)) {
                    player.sendMessage("Tiene un cooldown");
                    return;
                }

                startEntityCooldown(entity, tag);

                if (!hasEntityBeenFullyInteracted(entity, tag)) {
                    scheduleCommands(player, audioNumber);
                    markEntityAsFullyInteracted(entity, tag);
                } else {
                    runInstantCommand(player, audioNumber);
                }

                break;
            }
        }
    }

    private boolean isEntityOnCooldown(Entity entity, String interactionTag) {
        long currentTime = System.currentTimeMillis();

        if (entityInteractionCooldowns.containsKey(entity)) {
            HashMap<String, Long> interactionCooldowns = entityInteractionCooldowns.get(entity);
            if (interactionCooldowns.containsKey(interactionTag)) {
                long lastInteractionTime = interactionCooldowns.get(interactionTag);
                return currentTime - lastInteractionTime < cooldownTimes.getOrDefault(interactionTag, 0L);
            }
        }
        return false;
    }

    private void startEntityCooldown(Entity entity, String interactionTag) {
        long currentTime = System.currentTimeMillis();

        entityInteractionCooldowns.putIfAbsent(entity, new HashMap<>());
        entityInteractionCooldowns.get(entity).put(interactionTag, currentTime);
    }

    private boolean hasEntityBeenFullyInteracted(Entity entity, String interactionTag) {
        if (entityInteractedStatus.containsKey(entity)) {
            HashMap<String, Boolean> interactionStatus = entityInteractedStatus.get(entity);
            return interactionStatus.getOrDefault(interactionTag, false);
        }
        return false;
    }

    private void markEntityAsFullyInteracted(Entity entity, String interactionTag) {
        entityInteractedStatus.putIfAbsent(entity, new HashMap<>());
        entityInteractedStatus.get(entity).put(interactionTag, true);
    }

    private void scheduleCommands(Player player, String audioNumber) {
        CommandSequence[] commands = commandSequences.get(audioNumber);
        if (commands == null) {
            return;
        }

        BukkitScheduler scheduler = Bukkit.getScheduler();

        for (CommandSequence commandSequence : commands) {
            final String command = commandSequence.getCommand();
            long delayInTicks = 20L * commandSequence.getDelay();

            scheduler.runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }, delayInTicks);
        }
    }

    private void runInstantCommand(Player player, String audioNumber) {
        CommandSequence[] commands = commandSequences.get(audioNumber);
        if (commands == null || commands.length == 0) {
            return;
        }

        String instantCommand = commands[0].getCommand();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), instantCommand);
    }

    private void setupCommandSequences() {
        //todo                                                ------- BANO-------

        commandSequences.put("2", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:02_echo_cinta_bano master @s ~ ~ ~ 1", 0), // Instant
        });

        //todo                                      ----------------- Tren Hype------------------------

        commandSequences.put("3", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:01_anuncio_tren_del_hype master @s ~ ~ ~ 1", 0), // Instant
        });
        commandSequences.put("4", new CommandSequence[]{
                new CommandSequence("execute as @a[gamemode=adventure] at @s run playsound minecraft:05_tren_del_hype_echo_cinta_sobrevivientes master @s ~ ~ ~ 1", 0), // Instant
        });
        commandSequences.put("5", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:04_tren_del_hype_echo_cinta_secreta_hater master @s ~ ~ ~ 1", 0), // Instant
        });
        commandSequences.put("6", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:04_tren_del_hype_echo_cinta_final master @s ~ ~ ~ 1", 0), // Instant
        });
    }

    private void setupCooldownTimes() {
        cooldownTimes.put("1", 10 * 1000L);
        cooldownTimes.put("audio_2", 104 * 1000L);
        cooldownTimes.put("audio_3", 89 * 1000L);
        cooldownTimes.put("audio_4", 38 * 1000L);
        cooldownTimes.put("audio_5", 49 * 1000L);
        cooldownTimes.put("audio_6", 45 * 1000L);

    }
}

class CommandSequence {
    private final String command;
    private final int delay;

    public CommandSequence(String command, int delay) {
        this.command = command;
        this.delay = delay;
    }

    public String getCommand() {
        return command;
    }

    public int getDelay() {
        return delay;
    }
}
