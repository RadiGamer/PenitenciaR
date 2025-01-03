package org.imradigamer.chainPlugin.Circus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class JigsawCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public JigsawCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender is a player
        Player player = (Player) sender;
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        // Immediately run the first command as console
        Bukkit.dispatchCommand(console, "execute as @a at @s run playsound minecraft:01_fisico_echo_television master @s ~ ~ ~ 100");

        scheduler.runTaskLater(plugin, () -> {
            Bukkit.dispatchCommand(console, "mediaplayerspigot:playaudio @a ~/assets/a/Anxiety and death what better - Penitencia OST - ZairDeLuque Final Mix.mp3");
            placeRedstoneBlock(-221, 81, -178);
        }, 65 * 20L);

        return true;
    }

    public void placeRedstoneBlock( int x, int y, int z) {
        World world = Bukkit.getWorld("world");

        if (world != null) {
            Block block = world.getBlockAt(x, y, z);
            block.setType(Material.REDSTONE_BLOCK);
        }
    }
}
