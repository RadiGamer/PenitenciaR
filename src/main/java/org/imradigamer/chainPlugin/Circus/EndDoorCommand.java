package org.imradigamer.chainPlugin.Circus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EndDoorCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final String itemDisplayTag = "puerta_final";
    private final Location barrierStart = new Location(Bukkit.getWorld("world"), -80, 99, -234);
    private final Location barrierEnd = new Location(Bukkit.getWorld("world"), -80, 95, -237);

    public EndDoorCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usa /enddoor open o /enddoor close");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "open" -> openDoor(player);
            case "close" -> closeDoor(player);
            default -> player.sendMessage("Comando inválido. Usa /enddoor open o /enddoor close");
        }
        return true;
    }

    private void openDoor(Player player) {
        for (Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof Display display && display.getScoreboardTags().contains(itemDisplayTag)) {
                new RotateDoor(display, 90, 200).runTaskTimer(plugin, 0L, 1L);
            }
        }

        updateBarrier(Material.AIR);
    }

    private void closeDoor(Player player) {
        for (Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof Display display && display.getScoreboardTags().contains(itemDisplayTag)) {
                display.setRotation(90, 0);
            }
        }

        updateBarrier(Material.BARRIER);
        player.sendMessage("La puerta se está cerrando...");
    }

    private void updateBarrier(Material material) {
        for (int y = barrierStart.getBlockY(); y >= barrierEnd.getBlockY(); y--) {
            for (int z = barrierStart.getBlockZ(); z >= barrierEnd.getBlockZ(); z--) {
                Location loc = new Location(barrierStart.getWorld(), barrierStart.getBlockX(), y, z);
                loc.getBlock().setType(material);
            }
        }
    }

    private static class RotateDoor extends BukkitRunnable {
        private final Display display;
        private float currentYaw;
        private final float targetYaw;
        private final float yawIncrement = 2.5f; // Larger increment for faster animation while staying smooth

        public RotateDoor(Display display, float startYaw, float targetYaw) {
            this.display = display;
            this.currentYaw = startYaw;
            this.targetYaw = targetYaw;
        }

        @Override
        public void run() {
            if (currentYaw < targetYaw) {
                currentYaw += yawIncrement;
                if (currentYaw > targetYaw) currentYaw = targetYaw; // Prevent overshooting
                display.setRotation(currentYaw, 0);
            } else {
                display.setRotation(targetYaw, 0); // Ensure it ends exactly at the target yaw
                this.cancel();
            }
        }
    }
}
