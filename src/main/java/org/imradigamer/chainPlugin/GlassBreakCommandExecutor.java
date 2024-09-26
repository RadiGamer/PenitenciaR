package org.imradigamer.chainPlugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlassBreakCommandExecutor implements CommandExecutor {

    private final ChainPlugin plugin;

    public GlassBreakCommandExecutor(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("glass") && args.length > 0 && args[0].equalsIgnoreCase("break")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                World world = player.getWorld();

                Location start = new Location(world, 139, 56, -50);
                Location end = new Location(world, 129, 59, -50);

                GlassBreakAnimation animation = new GlassBreakAnimation(world, start, end, plugin);
                animation.start();

                player.sendMessage("Comenzando animacion de cristales");
                return true;
            }
        }
        return false;
    }
}
