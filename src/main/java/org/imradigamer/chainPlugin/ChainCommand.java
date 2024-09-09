package org.imradigamer.chainPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChainCommand implements CommandExecutor {

    private final ChainPlugin plugin;

    public ChainCommand(ChainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo jugadores pa.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Uso: /chain <origin|start|stop>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "origin":
                ChainManager.setChainOrigin(player);
                player.sendMessage("Se ha definido el punto de origen.");
                break;
            case "start":
                ChainManager.startChainingPlayers(player.getWorld());
                player.sendMessage("Encadenando a los jugadores en aventura.");
                break;
            case "stop":
                ChainManager.clearAllChains();
                player.sendMessage("Adios cadenas.");
                break;
            default:
                player.sendMessage("Invalido. Usa /chain <origin|start|stop>");
                break;
        }
        return true;
    }
}
