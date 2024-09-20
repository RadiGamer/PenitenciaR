package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ElevatorCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final CameraShaker cameraShaker;

    public ElevatorCommand(JavaPlugin plugin, CameraShaker cameraShaker) {
        this.plugin = plugin;
        this.cameraShaker = cameraShaker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start1")) {
                executeElevatorSequence1();
                return true;
            } else if (args[0].equalsIgnoreCase("start2")) {
                executeElevatorSequence2();
                return true;
            } else if (args[0].equalsIgnoreCase("teleport")) {
                checkElevator12();
                checkElevator22();
                checkElevator32();
                return true;
            }
        }

        sender.sendMessage("Uso: /elevator [start|teleport]");
        return false;
    }


    private void executeElevatorSequence1() {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage("Puertas cerrandose");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("Elevador andando");
                    cameraShaker.startShaking();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cameraShaker.stopShaking();
                            player.sendMessage("Sonido Marcando el piso*");
                            checkElevator1();
                            checkElevator2();
                            checkElevator3();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("Puertas se abren");
                                }
                            }.runTaskLater(plugin, 150L); // 7.5 seconds later
                        }
                    }.runTaskLater(plugin, 400L); // 20 segundos seconds later
                }
            }.runTaskLater(plugin, 100L); // 5 seconds after doors close
        }
    }
    private void executeElevatorSequence2() {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage("Puertas cerrandose");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("Elevador andando");
                    cameraShaker.startShaking();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cameraShaker.stopShaking();
                            player.sendMessage("Sonido Marcando el piso*");
                            checkElevator12();
                            checkElevator22();
                            checkElevator32();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("Puertas se abren");
                                }
                            }.runTaskLater(plugin, 150L); // 7.5 seconds later
                        }
                    }.runTaskLater(plugin, 400L); // 20 segundos seconds later
                }
            }.runTaskLater(plugin, 100L); // 5 seconds after doors close
        }
    }


    private void checkElevator1() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 1
            if (loc.getBlockX() >= 129 && loc.getBlockX() <= 133 &&
                    loc.getBlockY() == 55 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 19);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator2() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 2 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 125 && loc.getBlockX() >= 121 &&
                    loc.getBlockY() == 55 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 33 blocks
                loc.setY(loc.getY() + 33);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator3() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 3 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 117 && loc.getBlockX() >= 113 &&
                    loc.getBlockY() == 55 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 19);
                player.teleport(loc);
            }
        }
    }
    private void checkElevator12() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 1
            if (loc.getBlockX() >= 129 && loc.getBlockX() <= 133 &&
                    loc.getBlockY() == 74 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 32);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator22() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 2 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 125 && loc.getBlockX() >= 121 &&
                    loc.getBlockY() == 88 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 33 blocks
                loc.setY(loc.getY() + 18);
                player.teleport(loc);
            }
        }
    }

    private void checkElevator32() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Location loc = player.getLocation();
            // Adjusted to correct coordinates for Elevator 3 and fixed the X and Z comparisons
            if (loc.getBlockX() <= 117 && loc.getBlockX() >= 113 &&
                    loc.getBlockY() == 74 &&
                    loc.getBlockZ() >= 3 && loc.getBlockZ() <= 10) {
                // Teleport the player vertically by 19 blocks
                loc.setY(loc.getY() + 32);
                player.teleport(loc);
            }
        }
    }
}
