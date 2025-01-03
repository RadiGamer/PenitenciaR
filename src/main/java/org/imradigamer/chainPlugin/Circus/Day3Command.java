package org.imradigamer.chainPlugin.Circus;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Day3Command implements CommandExecutor {

    private final JavaPlugin plugin;

    public Day3Command(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender is a player
        Player player = (Player) sender;
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        Bukkit.dispatchCommand(console, "execute as @a at @s run playsound 01_numeros_echo_cinta master @s ~ ~ ~ 100");

        scheduler.runTaskLater(plugin, () -> {
            Bukkit.dispatchCommand(console, "mediaplayerspigot:playaudio @a ~/assets/a/Pasillos.mp3");
            player.performCommand("mm t cast -s carruselBGIniciar");
            player.performCommand("timer create 38b80 7m WHITE COUNTDOWN");

            placeRedstoneBlock(-222, 81, -178);

            // Execute a single command to give all players the custom item
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @s minecraft:paper{CustomModelData:36,custom_data:{calculadora:1b,PublicBukkitValues:{\"oraxen:furniture\":1b,\"oraxen:id\":\"calculadora\"}},display:{Name:\"\\\"{\\\\\"text\\\\\":\\\\\"Calculadora\\\\\"}\\\"\"}}");
        }, 59 * 20L);

        scheduler.runTaskLater(plugin, () -> {
            player.performCommand("mm t cast -s carruselBGDetener");
        }, (59 + 600) * 20L);

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
