package org.imradigamer.chainPlugin.Audios;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class AudioInteractionListener implements Listener {

    private final HashMap<String, CommandSequence[]> commandSequences = new HashMap<>();
    private final HashMap<Entity, HashMap<String, Long>> entityInteractionCooldowns = new HashMap<>();
    private final HashMap<Entity, HashMap<String, Boolean>> entityInteractedStatus = new HashMap<>();
    private final HashMap<String, Long> cooldownTimes = new HashMap<>();
    private boolean activated = false;

    private final Location tripwireStart = new Location(Bukkit.getWorld("world"), -6, 89, -82);
    private final Location tripwireEnd = new Location(Bukkit.getWorld("world"), -5, 89, -82);

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
                    player.sendMessage(ChatColor.of("#018543") + "La cinta esta reproduciendo algo...");
                    return;
                }
                if (hasEntityBeenFullyInteracted(entity, tag)) {

                    return;
                }

                startEntityCooldown(entity, tag);
                scheduleCommands(player, audioNumber);
                markEntityAsFullyInteracted(entity, tag);

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
                if (commandSequence.isPlayerCommand()) {
                    // Execute as player with OP privileges
                    playerCommandAsOp(player, command);
                } else {
                    // Execute as console command
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }, delayInTicks);
        }
    }

    private void setupCommandSequences() {
        // Commands setup
        commandSequences.put("2", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:02_echo_cinta_bano master @s ~ ~ ~ 100", 0, false), // Instant
        });

        commandSequences.put("3", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:01_anuncio_tren_del_hype master @s ~ ~ ~ 100", 0, false), // Instant
        });
        commandSequences.put("4", new CommandSequence[]{
                new CommandSequence("execute as @a[gamemode=adventure] at @s run playsound minecraft:05_tren_del_hype_echo_cinta_sobrevivientes master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("execute as @a[gamemode=creative] at @s run playsound minecraft:05_tren_del_hype_echo_cinta_sobrevivientes master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("execute as @a[gamemode=survival] at @s run playsound minecraft:03_tren_del_hype_echo_cinta_perdedor master @s ~ ~ ~ 100", 0, false), // Instant
                new CommandSequence("main poison true", 33, false)
        });
        commandSequences.put("5", new CommandSequence[]{
                new CommandSequence("lights on hater", 0, false), // Instant
                new CommandSequence("execute as @a at @s run playsound minecraft:04_tren_del_hype_echo_cinta_secreta_hater master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("mediaplayerspigot:playaudio @a ~/assets/a/pasillos/I_m afraid... - Penitencia OST - ZairDeLuque Final Mix.mp3", 22, false),
                new CommandSequence("timer create b6ea1 30s WHITE", 50, true),
                new CommandSequence("hater kill", 120-50+12, true),
                new CommandSequence("lights off hater", 120-50+11, false),
                new CommandSequence("execute as @a at @s run fill 25 90 -85 25 92 -77 minecraft:black_concrete",90, true),
                new CommandSequence("execute as @a at @s run playsound minecraft:bass_impact master @s ~ ~ ~ 100", 90, false),



        });
        commandSequences.put("6", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:04_tren_del_hype_echo_cinta_final master @s ~ ~ ~ 100", 0, false), // Instant
        });

        //TODO -------------------DIA 2---------------------
        commandSequences.put("7", new CommandSequence[]{

                new CommandSequence("execute as @a at @s run playsound minecraft:02_extensible_echo_cinta_fin master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("playsubtitles @a[name=Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/2.2_Echo_Cinta_Fin_Prueba_Extensible_en.srt", 0, false),
                new CommandSequence("playsubtitles @a[name=!Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/2.2_Echo_Cinta_Fin_Prueba_Extensible_es.srt", 0, false),
                new CommandSequence("lights on interrogatorio", 0, false),
                new CommandSequence("stopaudio @a", 0, false),
                new CommandSequence("door two b false",0,false),
                new CommandSequence("execute as @e[tag=aj.pinchos.root] run function animated_java:pinchos/animations/start/resume",71,false),
                new CommandSequence("mediaplayerspigot:playaudio @a ~/assets/a/Trampas/Waiting and waiting and waiting - Penitencia OST - ZairDeLuque Final Mix.mp3",71,false),
        });
        commandSequences.put("8", new CommandSequence[]{ //ELEVADOR
                new CommandSequence("execute as @a at @s run playsound minecraft:03_competencia_echo_elevadores master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("playsubtitles @a[name=Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/3.1_Echo_Cinta_Competencia_V1_es.srt", 0, false),
                new CommandSequence("playsubtitles @a[name=!Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/3.1_Echo_Cinta_Competencia_V1_en.srt", 0, false),
                new CommandSequence("doors open", 95, false),

        });

        commandSequences.put("9", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:02_competencia_echo_cinta_fin master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("playsubtitles @a[name=Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/3.3_Echo_Cinta_Fin_Prueba_Competencia_es.srt", 0, false),
                new CommandSequence("playsubtitles @a[name=!Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/3.3_Echo_Cinta_Fin_Prueba_Competencia_en.srt", 0, false),
                new CommandSequence("mediaplayerspigot:playaudio @a ~/assets/a/trampa1.mp3", 55, false),
        });
        commandSequences.put("10", new CommandSequence[]{ //EXTENSIBLE
                new CommandSequence("execute as @a at @s run playsound minecraft:01_extensible_echo_computadora master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("playsubtitles @a[name=!Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/2.1_Echo_Computadora_Extensible_en.srt", 0, false),
                new CommandSequence("playsubtitles @a[name=Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/2.1_Echo_Computadora_Extensible_es.srt", 0, false),
                new CommandSequence("mediaplayerspigot:playaudio @a ~/assets/a/This is from modern times - Penitencia OST - ZairDeLuque Final Mix.mp3", 60+51, false), // Instant
        });
        commandSequences.put("11", new CommandSequence[]{ //ACIDO
                new CommandSequence("execute as @a at @s run playsound 3_2_echo_television_competencia master @s ~ ~ ~ 100", 0, false), // Instant
                new CommandSequence("playsubtitles @a[name=!Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/3.2_Echo_Television_Competencia_en.srt", 0, false),
                new CommandSequence("playsubtitles @a[name=Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/3.2_Echo_Television_Competencia_es.srt", 0, false),
                new CommandSequence("execute as @a at @s run playsound 2_bucket master @s ~ ~ ~ 1", 53, false), // Instant
                new CommandSequence("mutevoice @a", 40, false), // Instant
                new CommandSequence("mediaplayerspigot:playaudio @a ~/assets/a/Trampa acido.mp3", 22, false), // Instant
                new CommandSequence("timer create b6ea1 5m WHITE", 50, true),
                new CommandSequence("bucket drop", 51, true),
        });
        commandSequences.put("12", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:01_cooperacion_echo_cinta_television master @s ~ ~ ~ 100", 0, false),
                new CommandSequence("playsubtitles @a[name=!Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/4.1_Echo_Cinta_Television_en_el_techo_Cooperacion_en.srt", 0, false),
                new CommandSequence("playsubtitles @a[name=Sapnap] Arial 20 #fff798 https://media.mbpcreators.com/4.1_Echo_Cinta_Television_en_el_techo_Cooperacion_es.srt", 0, false),
                new CommandSequence("mediaplayerspigot:playaudio @a ~/assets/a/trampalibros.mp3", 0, false), // Instant
                new CommandSequence("door two h true", 50, false),
                new CommandSequence("door two f true", 50, false),
                new CommandSequence("door two e false", 50, false),

        });


        //TODO -------------------DIA 3---------------------
        //TODO AGREGAR SUBTITULOS

        commandSequences.put("13", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:01_impostor_echo_television master @s ~ ~ ~ 100", 0, false),
        });
        commandSequences.put("14", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:01_pieza_maestra_echo_cinta master @s ~ ~ ~ 100", 0, false),
        });
        commandSequences.put("15", new CommandSequence[]{
                new CommandSequence("execute as @a at @s run playsound minecraft:01_prueba_final_saw_echo_television master @s ~ ~ ~ 100", 0, false),
        });
    }

    private void setupCooldownTimes() {
        cooldownTimes.put("1", 10 * 1000L);
        cooldownTimes.put("audio_2", 104 * 1000L);
        cooldownTimes.put("audio_3", 89 * 1000L);
        cooldownTimes.put("audio_4", 38 * 1000L);
        cooldownTimes.put("audio_5", 49 * 1000L);
        cooldownTimes.put("audio_6", 45 * 1000L);
        cooldownTimes.put("audio_7", 111 * 1000L);
        cooldownTimes.put("audio_8", 45 * 1000L); //TODO POR DEFINIR
        cooldownTimes.put("audio_9", 54 * 1000L);
        cooldownTimes.put("audio_10", 151 * 1000L);
        cooldownTimes.put("audio_11", 10 * 1000L);
    }
    public void resetAllInteractions() {
        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
            for (String tag : entity.getScoreboardTags()) {
                if (tag.startsWith("audio_")) {
                    entityInteractionCooldowns.remove(entity);
                    entityInteractedStatus.remove(entity);
                }
            }
        }
    }
    public static void playerCommandAsOp(Player player,String actionLine){
        boolean isOp = player.isOp();
        player.setOp(true);
        player.performCommand(actionLine);
        if(!isOp) {
            player.setOp(false);
        }
    }
//    @EventHandler
//    public void onPlayerMove(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//
//        // Only check if the player is in Adventure mode and if the tripwire is not yet activated
//        if (player.getGameMode() == GameMode.ADVENTURE && !activated) {
//            Location playerLocation = player.getLocation();
//
//            if (isWithinTripwireArea(playerLocation)) {
//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mediaplayerspigot:playaudio @a ~/assets/a/trampalibros.mp3");
//                activated = true;
//            }
//        }
//    }

    public void resetTripwire() {
        activated = false;
    }
}

class CommandSequence {
    private final String command;
    private final int delay;
    private final boolean isPlayerCommand;

    public CommandSequence(String command, int delay, boolean isPlayerCommand) {
        this.command = command;
        this.delay = delay;
        this.isPlayerCommand = isPlayerCommand;  // Initialize the new field
    }

    public String getCommand() {
        return command;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isPlayerCommand() {
        return isPlayerCommand;
    }
}