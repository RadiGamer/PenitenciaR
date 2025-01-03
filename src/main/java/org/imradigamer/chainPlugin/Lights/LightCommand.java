package org.imradigamer.chainPlugin.Lights;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class LightCommand implements CommandExecutor {

    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, BukkitRunnable> activeLoops = new HashMap<>();

    public LightCommand() {
        rooms.put("hypetrain", new Room(
                new Location(Bukkit.getWorld("world"), 5, 89, 60),
                new Location(Bukkit.getWorld("world"), -13, 97, -81),
                8
        ));
        rooms.put("money", new Room(
                new Location(Bukkit.getWorld("world"), -12, 89, -100),
                new Location(Bukkit.getWorld("world"), 3, 94, -82),
                10
        ));
        rooms.put("maze", new Room(
                new Location(Bukkit.getWorld("world"), 4, 68, -63),
                new Location(Bukkit.getWorld("world"), -64, 76, -131),
                8
        ));
        rooms.put("echo", new Room(
                new Location(Bukkit.getWorld("world"), 6, 88, -58),
                new Location(Bukkit.getWorld("world"), 11, 95, -91),
                8
        ));
        rooms.put("hater", new Room(
                new Location(Bukkit.getWorld("world"), 26, 94, -85),
                new Location(Bukkit.getWorld("world"), 33, 94, -77),
                15
        ));
        rooms.put("interrogatorio", new Room(
                new Location(Bukkit.getWorld("world"), 134, 56, -55),
                new Location(Bukkit.getWorld("world"), 134, 56, -55),
                15
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Uso: /lights <on|off|flick|loop> <cuarto>");
            return false;
        }

        String action = args[0];
        String roomName = args[1].toLowerCase();
        Room room = rooms.get(roomName);

        if (room == null) {
            sender.sendMessage(ChatColor.RED+"No se encontro el cuarto: " +ChatColor.GOLD + roomName);
            return false;
        }

        switch (action.toLowerCase()) {
            case "on":
                replaceBlocksInAreaWithLight(room.getCorner1(), room.getCorner2(), room.getMaxLightLevel());
                sender.sendMessage(ChatColor.BLUE+"Se han"+ChatColor.GREEN+" prendido "+ChatColor.BLUE+"las luces en "+ChatColor.GOLD + roomName);
                break;
            case "off":
                replaceBlocksInAreaWithLight(room.getCorner1(), room.getCorner2(), 3);
                sender.sendMessage(ChatColor.BLUE+"Se han"+ChatColor.RED+" apagado"+ChatColor.BLUE+" las luces en "+ChatColor.GOLD + roomName);
                break;
            case "flick":
                flickerLights(room.getCorner1(), room.getCorner2(), room.getMaxLightLevel());
                sender.sendMessage(ChatColor.BLUE+"Las luces parpadearan en "+ChatColor.GOLD + roomName);
                break;
            case "loop":
                handleLoopCommand(roomName, room, sender);
                break;
            default:
                return false;
        }

        return true;
    }

    private void handleLoopCommand(String roomName, Room room, CommandSender sender) {
        if (activeLoops.containsKey(roomName)) {
            activeLoops.get(roomName).cancel();
            activeLoops.remove(roomName);
        } else {
            BukkitRunnable flickerTask = createFlickerLoop(room.getCorner1(), room.getCorner2(), room.getMaxLightLevel());
            flickerTask.runTaskTimer(Bukkit.getPluginManager().getPlugin("PenitenciaR"), 0L, 4L);
            activeLoops.put(roomName, flickerTask);
        }
    }

    private void replaceBlocksInAreaWithLight(Location corner1, Location corner2, int lightLevel) {
        World world = corner1.getWorld();

        lightLevel = Math.max(0, Math.min(lightLevel, 15));

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
                    if (block.getType() == Material.LIGHT) {
                        BlockData blockData = Bukkit.createBlockData(Material.LIGHT, "[level=" + lightLevel + "]");
                        block.setBlockData(blockData);
                    }
                }
            }
        }
    }

    private void flickerLights(Location corner1, Location corner2, int maxLightLevel) {
        new BukkitRunnable() {
            int flickerCount = 0;
            Random random = new Random();

            @Override
            public void run() {
                if (flickerCount >= 10) {
                    this.cancel();
                    return;
                }

                int randomLightLevel = random.nextInt(maxLightLevel + 1);
                replaceBlocksInAreaWithLight(corner1, corner2, randomLightLevel);

                flickerCount++;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("PenitenciaR"), 0L, 4L);
    }

    private BukkitRunnable createFlickerLoop(Location corner1, Location corner2, int maxLightLevel) {
        return new BukkitRunnable() {
            Random random = new Random();

            @Override
            public void run() {
                int randomLightLevel = random.nextInt(maxLightLevel + 1);
                replaceBlocksInAreaWithLight(corner1, corner2, randomLightLevel);

                long delay = random.nextInt(25) + 10;
                this.runTaskLater(Bukkit.getPluginManager().getPlugin("PenitenciaR"), delay);
            }
        };
    }
    public Map<String, Room> getRooms() {
        return rooms;
    }
}
