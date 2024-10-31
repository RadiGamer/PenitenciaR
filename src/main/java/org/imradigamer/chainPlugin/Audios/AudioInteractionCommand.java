package org.imradigamer.chainPlugin.Audios;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AudioInteractionCommand implements CommandExecutor {

    private final AudioInteractionListener audioListener;

    public AudioInteractionCommand(AudioInteractionListener audioListener) {
        this.audioListener = audioListener;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1 || !args[0].equalsIgnoreCase("restart")) {
            player.sendMessage("Uso: /audio restart");
            return true;
        }

        // Reset audio interactions
        audioListener.resetAllInteractions();
        player.sendMessage("Audios reiniciados.");

        audioListener.resetTripwire();

        return true;
    }
}
